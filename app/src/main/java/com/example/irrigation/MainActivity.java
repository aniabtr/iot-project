package com.example.irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String [] crops = {"Potatoes", "Carrots", "Onions", "Peas"};
    //String [] waterFlowRate = {"6", "8", "10"};
    String [] coverageAreaTypes = {"Rectangle", "L-Shape", "Maximum Coverage"};

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
    }
}