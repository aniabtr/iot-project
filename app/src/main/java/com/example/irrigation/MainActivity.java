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

    AutoCompleteTextView autoCompleteTextViewCrops;

    ArrayAdapter<String> adapterCrops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextViewCrops = findViewById(R.id.auto_complete_txt);
        adapterCrops = new ArrayAdapter<String>(this, R.layout.list_crops, crops);

        autoCompleteTextViewCrops.setAdapter(adapterCrops);

        autoCompleteTextViewCrops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }
}