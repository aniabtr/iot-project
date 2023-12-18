package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Retrieve the values from the Intent
        Intent intent = getIntent();
        String selectedCrop = intent.getStringExtra("SELECTED_CROP");
        String waterFlowRate = intent.getStringExtra("WATER_FLOW_RATE");
        String selectedCoverageAreaType = intent.getStringExtra("SELECTED_COVERAGE_AREA_TYPE");
        String coverageAreaValue = intent.getStringExtra("COVERAGE_AREA_VALUE");
        String numberOfIrrigationWeeks = intent.getStringExtra("NUMBER_OF_IRRIGATION_WEEKS");

        // Use the values as needed (e.g., display in TextViews, perform calculations, etc.)
        TextView textViewData = findViewById(R.id.textViewData);
        String displayText = "Do you want to start the irrigation with the following parameters?" +
                "\n" +
                "\nSelected Crop: " + selectedCrop +
                "\nWater Flow Rate: " + waterFlowRate +
                "\nCoverage Area Type: " + selectedCoverageAreaType +
                "\nCoverage Area Value: " + coverageAreaValue +
                "\nNumber of Irrigation Weeks: " + numberOfIrrigationWeeks;
        textViewData.setText(displayText);
    }
}