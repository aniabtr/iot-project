package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class dashboard extends AppCompatActivity {

    private Irrigation irrigationData;
    private Button cancel;
    private TextView dataField;
    String filename = "irrigationinfo.txt";
    private SimpleDateFormat dateFormat;
    private Switch switchManualControl;
    private TextView selectedCrop, waterFlowRate, coverageAreaType, coverageAreaValue, weeks, timestampCreation;

    private boolean piFailure = false;
    private boolean isInternalChange = false;

    private static String outputValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Retrieve the values from the Intent
        // Intent intent = getIntent();
        // String selectedCrop = intent.getStringExtra("SELECTED_CROP");
        // String waterFlowRate = intent.getStringExtra("WATER_FLOW_RATE");
        // String selectedCoverageAreaType = intent.getStringExtra("SELECTED_COVERAGE_AREA_TYPE");
        // String coverageAreaValue = intent.getStringExtra("COVERAGE_AREA_VALUE");
        // String numberOfIrrigationWeeks = intent.getStringExtra("NUMBER_OF_IRRIGATION_WEEKS");

        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String selectedCrop = bufferedReader.readLine();
            String waterFlowRateStr = bufferedReader.readLine();
            String selectedCoverageAreaType = bufferedReader.readLine();
            String coverageAreaValueStr = bufferedReader.readLine();
            String numberOfIrrigationWeeksStr = bufferedReader.readLine();
            String timestampStr = bufferedReader.readLine();
            String endDateStr = bufferedReader.readLine();
            String triggerTimeMillisStr = bufferedReader.readLine();
            String intervalMillisStr = bufferedReader.readLine();

            // Create Irrigation instance and set parsed values
            irrigationData = new Irrigation();
            irrigationData.setSelectedCrop(selectedCrop);
            irrigationData.setWaterFlowRate(Float.parseFloat(waterFlowRateStr));
            irrigationData.setSelectedCoverageAreaType(selectedCoverageAreaType);
            irrigationData.setCoverageAreaValue(Float.parseFloat(coverageAreaValueStr));
            irrigationData.setNumberOfIrrigationWeeks(Integer.parseInt(numberOfIrrigationWeeksStr));
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                irrigationData.setTimestamp(dateFormat.parse(timestampStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            try {
                irrigationData.setEndDate(dateFormat.parse(endDateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            irrigationData.setTriggerTimeMillis(Long.parseLong(triggerTimeMillisStr));
            irrigationData.setIntervalMillis(Long.parseLong(intervalMillisStr));

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // new dashboard design
        dataField = findViewById(R.id.textViewData);
        selectedCrop = findViewById(R.id.textViewSelectedCrop);
        waterFlowRate = findViewById(R.id.textViewWaterFlow);
        coverageAreaType = findViewById(R.id.textViewCoverageAreaType);
        coverageAreaValue = findViewById(R.id.textViewCoverageAreaValue);
        weeks = findViewById(R.id.textViewWeeks);
        timestampCreation = findViewById(R.id.textViewTimestampCreation);

        String displayText;
        displayText = "Ongoing Irrigation:";
        dataField.setText(displayText);
        displayText = "Selected Crop:\n" + irrigationData.getSelectedCrop();
        selectedCrop.setText(displayText);
        displayText = "Water Flow Rate:\n" + irrigationData.getWaterFlowRate();
        waterFlowRate.setText(displayText);
        displayText = "Coverage Area Type:\n" + irrigationData.getSelectedCoverageAreaType();
        coverageAreaType.setText(displayText);
        displayText = "Coverage Area Value:\n" + irrigationData.getCoverageAreaValue();
        coverageAreaValue.setText(displayText);
        displayText = "Time of Irrigation:\n" + irrigationData.getNumberOfIrrigationWeeks() + " weeks";
        weeks.setText(displayText);
        displayText = "Created:\n" + irrigationData.getTimestamp();
        timestampCreation.setText(displayText);

        // recreate scheduler
        IrrigationScheduler.loadSavedIrrigation(dashboard.this, irrigationData, irrigationData.getTriggerTimeMillis(), irrigationData.getIntervalMillis());

        cancel = findViewById(R.id.cancelIrrigation);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // erase content of saved file
                try {
                    FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();

                }
                // transition back to MainActivity
                Intent intent = new Intent(dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        switchManualControl = findViewById(R.id.switchManualControl);

        // controls for switch
        // switchManualControl.setTextOn("On");
        // switchManualControl.setTextOff("Off");
        // switchManualControl.setChecked(true);
        // switchManualControl.setChecked(false);
        // switchManualControl.setText("Pump is on");
        // switchManualControl.setText("Pump is off");

        // the status of switch
        if (switchManualControl.isChecked()) {
            switchManualControl.setText("Pump is on");
        } else {
            switchManualControl.setText("Pump is off");
        }

        switchManualControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isInternalChange) {
                    // Display confirmation dialog
                    showConfirmationDialog(isChecked);
                }
            }
        });
    }

    private void showConfirmationDialog(final boolean isChecked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isChecked ? "Turn Pump On" : "Turn Pump Off")
                .setMessage(isChecked ? "Are you sure you want to turn the pump on?" : "Are you sure you want to turn the pump off?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, perform the action
                        performSwitchAction(isChecked);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, change switch back without triggering listener
                        isInternalChange = true;
                        switchManualControl.setChecked(!isChecked);
                        isInternalChange = false;
                    }
                })
                .show();
    }

    @SuppressLint("StaticFieldLeak")
    private void performSwitchAction(final boolean isChecked) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    if (isChecked) {
                        run("tdtool --on 1");
                    } else {
                        run("tdtool --off 1");
                    }
                } catch (IOException e) {
                    piFailure = true;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                if (piFailure) {
                    Toast.makeText(dashboard.this, "Connection to Raspberry Pi failed", Toast.LENGTH_SHORT).show();
                    // Change switch back without triggering listener
                    isInternalChange = true;
                    switchManualControl.setChecked(!isChecked);
                    isInternalChange = false;
                    piFailure = false;
                } else {
                    switchManualControl.setText(isChecked ? "Pump is on" : "Pump is off");
                }
            }
        }.execute(1);
    }


    public static void run(String command) throws IOException {
        String hostname = "raspberrypi23.local";
        String username = "IOT";
        String password = "1234";
        StringBuilder output = new StringBuilder();
        try
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection conn = new Connection(hostname); //init connection
            conn.connect(); //start connection to the hostname
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (!isAuthenticated)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            //reads text
            while (true){
                String line = br.readLine(); // read line
                if (line == null)
                    break;
                System.out.println(line);
                output.append(line);
            }
            outputValue = output.toString();
            /* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e)
        { e.printStackTrace(System.err);
            //System.exit(2);
            throw new IOException("Error: Connection to Raspberry Pi failed.");
        }
    }

    public static String getOutputValue() {
        return outputValue;
    }
}