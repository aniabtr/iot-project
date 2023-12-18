package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String [] crops = {"Potatoes", "Carrots", "Onions", "Peas"};
    //String [] waterFlowRate = {"6", "8", "10"};
    String [] coverageAreaTypes = {"Rectangle", "L-Shape", "Maximum Coverage"};
    private Button create;
    private EditText waterFlow, coverageArea,weeks;
    AutoCompleteTextView autoCompleteTextViewCrops;
    //AutoCompleteTextView autoCompleteTextViewWaterFlowRate;
    AutoCompleteTextView autoCompleteTextViewCoverageAreaType;

    ArrayAdapter<String> adapterCrops;
    //ArrayAdapter<String> adapterWaterFlow;
    ArrayAdapter<String> adapterCoverageAreaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //autoCompleteTextViewWaterFlowRate = findViewById(R.id.dropdown_waterFlow);
        //adapterWaterFlow = new ArrayAdapter<String>(this, R.layout.list_waterflow, waterFlowRate);

        //autoCompleteTextViewWaterFlowRate.setAdapter(adapterWaterFlow);

        /*autoCompleteTextViewWaterFlowRate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });*/

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
                Intent intent = new Intent(MainActivity.this, dashboard.class);

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
    }
}