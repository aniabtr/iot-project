package com.example.irrigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IrrigationBroadcastReceiver extends BroadcastReceiver {

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
            Log.d("Irrigation", "Logic executed ");
        }
        //TODO Add irrigation logic here

        //TODO remove
    }
}

