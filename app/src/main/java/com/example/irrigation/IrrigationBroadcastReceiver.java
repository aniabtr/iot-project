package com.example.irrigation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IrrigationBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(Context context, Intent intent) {
        // irrigation logic here

        // called when the alarm fires
        Toast.makeText(context, "Performing irrigation task...", Toast.LENGTH_SHORT).show();
        String endDateString = intent.getStringExtra("END_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        Date endDate = null;
        try {
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        if (today.compareTo(endDate) <= 0) {
            new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        dashboard.run("python3 runFiles/precipitation.py");
                    } catch (IOException | RuntimeException e) {

                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void v) {
                    Toast.makeText(context, "Expected precipitation in Hobart: " + dashboard.getOutputValue(), Toast.LENGTH_SHORT).show(); // dashboard.getOutputValue() gets the value from python script
                }
            }.execute(1);
        }
        //TODO Add irrigation logic here

        //TODO remove
    }
}

