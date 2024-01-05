package com.example.irrigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class IrrigationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // irrigation logic here

        // called when the alarm fires
        Toast.makeText(context, "Performing irrigation task...", Toast.LENGTH_SHORT).show();

        // TODO Add irrigation logic here
        Log.d("Irrigation", "Logic executed"); // TODO remove
    }
}

