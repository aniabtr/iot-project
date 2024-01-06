package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    private Button yes, cancel;
    private String filename = "irrigationinfo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Retrieve the values from the Intent
        Intent intent = getIntent();
        String selectedCrop = intent.getStringExtra("SELECTED_CROP");
        String waterFlowRateStr = intent.getStringExtra("WATER_FLOW_RATE");
        String selectedCoverageAreaType = intent.getStringExtra("SELECTED_COVERAGE_AREA_TYPE");
        String coverageAreaValueStr = intent.getStringExtra("COVERAGE_AREA_VALUE");
        String numberOfIrrigationWeeksStr = intent.getStringExtra("NUMBER_OF_IRRIGATION_WEEKS");

        // parsing of numeric data
        Float waterFlowRate = Float.parseFloat(waterFlowRateStr);
        Float coverageAreaValue = Float.parseFloat(coverageAreaValueStr);
        int numberOfIrrigationWeeks = Integer.parseInt(numberOfIrrigationWeeksStr);

        // Use the values as needed (e.g., display in TextViews, perform calculations, etc.)
        TextView textViewData = findViewById(R.id.textViewData);
        String displayText = "Do you want to start the irrigation with the following parameters?" +
                "\n" +
                "\nSelected Crop: " + selectedCrop +
                "\nWater Flow Rate: " + waterFlowRateStr +
                "\nCoverage Area Type: " + selectedCoverageAreaType +
                "\nCoverage Area Value: " + coverageAreaValueStr +
                "\nNumber of Irrigation Weeks: " + numberOfIrrigationWeeksStr;
        textViewData.setText(displayText);

        yes = findViewById(R.id.yesButton);
        cancel = findViewById(R.id.cancelButton);

        yes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // create irrigation instance
                Irrigation irrigationData = new Irrigation();
                irrigationData.setSelectedCrop(selectedCrop);
                irrigationData.setWaterFlowRate(waterFlowRate);
                irrigationData.setSelectedCoverageAreaType(selectedCoverageAreaType);
                irrigationData.setCoverageAreaValue(coverageAreaValue);
                irrigationData.setNumberOfIrrigationWeeks(numberOfIrrigationWeeks);
                irrigationData.setTimestamp(new Timestamp(System.currentTimeMillis()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(irrigationData.getTimestamp());
                calendar.add(Calendar.WEEK_OF_YEAR, numberOfIrrigationWeeks);
                Date endDate = calendar.getTime();
                irrigationData.setEndDate(endDate);

                // schedule daily irrigation using scheduler
                long[] timeArray = IrrigationScheduler.scheduleDailyIrrigation(MainActivity2.this, irrigationData);

                irrigationData.setTriggerTimeMillis(timeArray[0]);
                irrigationData.setIntervalMillis(timeArray[1]);

                // create data string
                String dataToSave =
                        irrigationData.getSelectedCrop() + "\n" +
                        irrigationData.getWaterFlowRate() + "\n" +
                        irrigationData.getSelectedCoverageAreaType() + "\n" +
                        irrigationData.getCoverageAreaValue() + "\n" +
                        irrigationData.getNumberOfIrrigationWeeks() + "\n" +
                        irrigationData.getTimestamp() + "\n" +
                        irrigationData.getEndDate() + "\n" +
                        irrigationData.getTriggerTimeMillis() + "\n" +
                        irrigationData.getIntervalMillis();

                try {

                    // save data to file
                    FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    fos.write(dataToSave.getBytes());
                    fos.close();

                    // transition to dashboard after saving
                    Intent intent = new Intent(MainActivity2.this, dashboard.class);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // transition back to MainActivity
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}