package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class dashboard extends AppCompatActivity {

    private Irrigation irrigationData;
    private Button cancel;
    private TextView dataField;
    String filename = "irrigationinfo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Retrieve the values from the Intent
        //Intent intent = getIntent();
        //String selectedCrop = intent.getStringExtra("SELECTED_CROP");
        //String waterFlowRate = intent.getStringExtra("WATER_FLOW_RATE");
        //String selectedCoverageAreaType = intent.getStringExtra("SELECTED_COVERAGE_AREA_TYPE");
        //String coverageAreaValue = intent.getStringExtra("COVERAGE_AREA_VALUE");
        //String numberOfIrrigationWeeks = intent.getStringExtra("NUMBER_OF_IRRIGATION_WEEKS");

        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String selectedCrop = bufferedReader.readLine();
            String waterFlowRateStr = bufferedReader.readLine();
            String selectedCoverageAreaType = bufferedReader.readLine();
            String coverageAreaValueStr = bufferedReader.readLine();
            String numberOfIrrigationWeeksStr = bufferedReader.readLine();

            // Create Irrigation instance and set parsed values
            irrigationData = new Irrigation();
            irrigationData.setSelectedCrop(selectedCrop);
            irrigationData.setWaterFlowRate(Float.parseFloat(waterFlowRateStr));
            irrigationData.setSelectedCoverageAreaType(selectedCoverageAreaType);
            irrigationData.setCoverageAreaValue(Float.parseFloat(coverageAreaValueStr));
            irrigationData.setNumberOfIrrigationWeeks(Integer.parseInt(numberOfIrrigationWeeksStr));

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Use the values as needed (e.g., display in TextViews, perform calculations, etc.)
        dataField = findViewById(R.id.textViewData);
        String displayText = "Values received from previous activity:" +
                "\n" +
                "\nSelected Crop: " + irrigationData.getSelectedCrop() +
                "\nWater Flow Rate: " + irrigationData.getWaterFlowRate() +
                "\nCoverage Area Type: " + irrigationData.getSelectedCoverageAreaType() +
                "\nCoverage Area Value: " + irrigationData.getCoverageAreaValue() +
                "\nNumber of Irrigation Weeks: " + irrigationData.getNumberOfIrrigationWeeks();
        dataField.setText(displayText);

        cancel = findViewById(R.id.cancelIrrigation);
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
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

    }
}