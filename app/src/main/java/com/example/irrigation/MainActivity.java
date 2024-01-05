package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class MainActivity extends AppCompatActivity {

//test
    private boolean isFileEmpty() {
        try {
            FileInputStream fis = openFileInput("irrigationinfo.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            return line == null || line.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Function to display a message when the form is empty
    private void displayEmptyFormMessage() {
        TextView emptyFormMessage = findViewById(R.id.message);
        emptyFormMessage.setText("Please fill out all fields.");
    }
    private void displayRectangleAreaMessage() {
        TextView emptyFormMessage = findViewById(R.id.message);
        emptyFormMessage.setText("Cannot irrigate an area this large with Rectangle coverage and this amount of water flow.");
    }
    private void displayLshapeAreaMessage () {
        TextView emptyFormMessage = findViewById(R.id.message);
        emptyFormMessage.setText("Cannot irrigate an area this large with L-Shape coverage and this amount of water flow.");
    }
    private void displayMaximumCoverageAreaMessage() {
        TextView emptyFormMessage = findViewById(R.id.message);
        emptyFormMessage.setText("Cannot irrigate an area this large with Maximum coverage and this amount of water flow.");
    }


    String [] crops = {"Potatoes", "Carrots", "Onions", "Peas"};
    String [] coverageAreaTypes = {"Rectangle", "L-Shape", "Maximum Coverage"};
    private Button create;
    private EditText waterFlow, coverageArea,weeks;
    AutoCompleteTextView autoCompleteTextViewCrops;
    AutoCompleteTextView autoCompleteTextViewCoverageAreaType;
    ArrayAdapter<String> adapterCrops;
    ArrayAdapter<String> adapterCoverageAreaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the file is empty
        boolean isFileEmpty = isFileEmpty();
        boolean value= true;
        if (isFileEmpty) {
            // File is empty, log to MainActivity where there is the form to create a new irrigation
            setContentView(R.layout.activity_main);
            autoCompleteTextViewCrops = findViewById(R.id.dropdown_crop);
            adapterCrops = new ArrayAdapter<String>(this, R.layout.list_crops, crops);

            autoCompleteTextViewCrops.setAdapter(adapterCrops);

            autoCompleteTextViewCrops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                }
            });

            autoCompleteTextViewCoverageAreaType = findViewById(R.id.dropdown_coverageAreaType);
            adapterCoverageAreaType = new ArrayAdapter<String>(this, R.layout.list_coverageareatype, coverageAreaTypes);

            autoCompleteTextViewCoverageAreaType.setAdapter(adapterCoverageAreaType);

            autoCompleteTextViewCoverageAreaType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                }
            });
            waterFlow = findViewById(R.id.waterFlow);
            coverageArea = findViewById(R.id.coverageArea);
            weeks = findViewById(R.id.numberOfIrrigationWeeks);
            create = findViewById(R.id.button);
            create.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v) {
                    // Get the user input from the form

                    String selectedCrop = autoCompleteTextViewCrops.getText().toString();
                    String waterFlowRateStr = waterFlow.getText().toString();
                    String selectedCoverageAreaType = autoCompleteTextViewCoverageAreaType.getText().toString();
                    String coverageAreaValueStr = coverageArea.getText().toString();
                    String numberOfIrrigationWeeksStr = weeks.getText().toString();
                    // check whether the form is empty or not
                    if (selectedCrop.isEmpty() || waterFlowRateStr.isEmpty() || selectedCoverageAreaType.isEmpty() || coverageAreaValueStr.isEmpty() || numberOfIrrigationWeeksStr.isEmpty()) {
                        // Display a message to the user to fill out the form
                        displayEmptyFormMessage();
                    } else {
                        //if the selected area is rectangle check if its irrigation is possible
                            if ("Rectangle".equals(selectedCoverageAreaType)) {
                            float waterFlowRate = Float.parseFloat(waterFlowRateStr);
                            float coverageAreaValue = Float.parseFloat(coverageAreaValueStr);
                            float maxPossibleAreaCoverage = (float) (0.0021 * Math.pow(waterFlowRate, 2) - 0.0422 * waterFlowRate + 0.8986);
                            if (maxPossibleAreaCoverage < coverageAreaValue) {
                                // Display a message to the user
                                displayRectangleAreaMessage();
                                return; // Stop further processing
                            }
                        }
                        //if the selected area is L-Shape check if its irrigation is possible
                        if ("L-Shape".equals(selectedCoverageAreaType))
                        {
                            float waterFlowRate = Float.parseFloat(waterFlowRateStr);
                            float coverageAreaValue = Float.parseFloat(coverageAreaValueStr);
                            float maxPossibleAreaCoverage = (float) (0.022895 * Math.pow(waterFlowRate, 2) - 1.28448 * waterFlowRate + 18.6402);
                            if (maxPossibleAreaCoverage < coverageAreaValue) {
                                // Display a message to the user
                                displayLshapeAreaMessage();
                                return; // Stop further processing
                            }

                        }
                        //if the selected area is Maximum Coverage check if its irrigation is possible
                        if ("Maximum Coverage".equals(selectedCoverageAreaType)) {
                            float waterFlowRate = Float.parseFloat(waterFlowRateStr);
                            float coverageAreaValue = Float.parseFloat(coverageAreaValueStr);
                            float maxPossibleAreaCoverage = (float) (0.0571679 * Math.pow(waterFlowRate, 2) - 3.20197 * waterFlowRate + 46.2867);
                            if (maxPossibleAreaCoverage < coverageAreaValue) {
                                // Display a message to the user
                                displayMaximumCoverageAreaMessage();
                                return; // Stop further processing
                            }
                        }


                        // Create an Intent to start the second activity
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                        // Put the user input as extras in the Intent
                        intent.putExtra("SELECTED_CROP", selectedCrop);
                        intent.putExtra("WATER_FLOW_RATE", waterFlowRateStr);
                        intent.putExtra("SELECTED_COVERAGE_AREA_TYPE", selectedCoverageAreaType);
                        intent.putExtra("COVERAGE_AREA_VALUE", coverageAreaValueStr);
                        intent.putExtra("NUMBER_OF_IRRIGATION_WEEKS", numberOfIrrigationWeeksStr);

                        // Start the second activity
                        startActivity(intent);
                    } }
                }
             );



        } else {
            // File is not empty, open the dashboard related to that irrigation
            Intent intent = new Intent(MainActivity.this, dashboard.class);
            startActivity(intent);
            finish(); // Finish MainActivity so the user can't go back to it
        }
    }

    }
