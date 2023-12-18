package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {


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
                    String waterFlowRate = waterFlow.getText().toString();
                    String selectedCoverageAreaType = autoCompleteTextViewCoverageAreaType.getText().toString();
                    String coverageAreaValue = coverageArea.getText().toString();
                    String numberOfIrrigationWeeks = weeks.getText().toString();

                    // Create an Intent to start the second activity
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                    // Put the user input as extras in the Intent
                    intent.putExtra("SELECTED_CROP", selectedCrop);
                    intent.putExtra("WATER_FLOW_RATE", waterFlowRate);
                    intent.putExtra("SELECTED_COVERAGE_AREA_TYPE", selectedCoverageAreaType);
                    intent.putExtra("COVERAGE_AREA_VALUE", coverageAreaValue);
                    intent.putExtra("NUMBER_OF_IRRIGATION_WEEKS", numberOfIrrigationWeeks);

                    // Start the second activity
                    startActivity(intent);
                }
            } );



        } else {
            // File is not empty, open the dashboard related to that irrigation
            Intent intent = new Intent(MainActivity.this, dashboard.class);
            startActivity(intent);
            finish(); // Finish MainActivity so the user can't go back to it
        }
    }


    }
