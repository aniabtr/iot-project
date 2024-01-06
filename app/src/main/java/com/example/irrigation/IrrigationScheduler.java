package com.example.irrigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class IrrigationScheduler {

    public static long[] scheduleDailyIrrigation(Context context, Irrigation irrigationData) {
        // get AlarmManager instance
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // PendingIntent for the BroadcastReceiver
        Intent intent = new Intent(context, IrrigationBroadcastReceiver.class);
        intent.putExtra("END_DATE", irrigationData.getEndDate().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // time for the alarm to trigger
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 10); // currently 8:00 am
        //calendar.set(Calendar.MINUTE, 57);
        //calendar.set(Calendar.HOUR_OF_DAY, 8); // currently 8:00 am
        //calendar.set(Calendar.MINUTE, 0);
        long triggerTimeMillis = System.currentTimeMillis();
        long intervalMillis = 60 * 1000; // ever 60 seconds

        // schedule to repeat every day
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTimeMillis, intervalMillis, pendingIntent);

        long[] timeArray = new long[2];
        timeArray[0] = triggerTimeMillis;
        timeArray[1] = intervalMillis;

        return timeArray;
    }

    public static void loadSavedIrrigation(Context context, Irrigation irrigationData, long triggerTimeMillis, long intervalMillis) {
        // get AlarmManager instance
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // PendingIntent for the BroadcastReceiver
        Intent intent = new Intent(context, IrrigationBroadcastReceiver.class);
        intent.putExtra("END_DATE", irrigationData.getEndDate().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // schedule to repeat every day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTimeMillis, intervalMillis, pendingIntent);
    }
}
