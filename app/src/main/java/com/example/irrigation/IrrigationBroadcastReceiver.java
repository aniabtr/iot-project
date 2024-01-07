package com.example.irrigation;

import static com.example.irrigation.dashboard.run;

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
import java.util.Objects;

public class IrrigationBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(Context context, Intent intent) {
        // called when the alarm fires
        Toast.makeText(context, "Performing irrigation task...", Toast.LENGTH_SHORT).show();

        // Create an array to store the evaporation values for each month in Hobart
        float[] evaporationPerMonth = {6.2f, 5.4f, 4.2f, 2.8f, 1.9f, 1.3f, 1.4f, 2f, 3f, 4.1f, 4.9f, 5.9f};

        // Get the current month
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        //retrieve data
        String cropType = intent.getStringExtra("CROP_TYPE");
        float waterFlowRate = intent.getFloatExtra("WATER_FLOW_RATE", 0.0f);
        float coverageAreaValue = intent.getFloatExtra("COVERAGE_AREA_VALUE", 0.0f);
        String endDateString = intent.getStringExtra("END_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        int numberOfWeeks = intent.getIntExtra("NUMBER_WEEKS", 0);
        Date endDate = null;
        try {
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = calendar.getTime();

        if (today.compareTo(endDate) <= 0) {
            new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        run("python3 runFiles/precipitation.py");
                        // uncomment for testing purposes with precipitation value 0
                        // run("python3 runFiles/test.py");
                    } catch (IOException | RuntimeException e) {
                        Log.d("IrrigationException", "could not run python3 runFiles/precipitation.py");
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void v) {
                    if (dashboard.getOutputValue() == null) {
                        Log.d("D", "IrrigationBroadcastReceiver: Output value from dashboard is null");
                        Toast.makeText(context, "Couldn't get output from Pi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    float precipitationValue = Float.parseFloat(dashboard.getOutputValue());
                    float irrigationPerSeason;
                    float irrigationPerDay;
                    float realIrrigation;
                    float irrigationTime;
                    if (cropType == null) {
                        Log.d("D", "cropType == null");
                    }
                    switch (Objects.requireNonNull(cropType)) {
                        case "Potatoes":
                        case "Carrots":
                            irrigationPerSeason = coverageAreaValue * 350000000 / 107639;
                            break;
                        case "Onions":
                            irrigationPerSeason = coverageAreaValue * 375000000 / 107639;
                            break;
                        case "Peas":
                            irrigationPerSeason = coverageAreaValue * 150000000 / 107639;
                            break;
                        default:
                            irrigationPerSeason = 0.0f;
                    }
                    int numberOfDays=numberOfWeeks*7;
                    irrigationPerDay = irrigationPerSeason /numberOfDays;
                    realIrrigation = irrigationPerDay + evaporationPerMonth[currentMonth] - precipitationValue;
                    irrigationTime = realIrrigation / waterFlowRate;

                    // Log the calculated values
                    Log.d("Irrigation", "Crop Type: " + cropType);
                    Log.d("Irrigation", "Water Flow Rate: " + waterFlowRate+ " Liter per minute");
                    Log.d("Irrigation", "Coverage Area Value: " + coverageAreaValue+ " ft2");
                    Log.d("Irrigation", "Current Month: " + currentMonth+1);
                    Log.d("Irrigation", "Number of Weeks: " + numberOfWeeks);
                    Log.d("Irrigation", "Irrigation Per Season: " + irrigationPerSeason+ " Liter");
                    Log.d("Irrigation", "Irrigation Per Day: " + irrigationPerDay + " Liter");
                    Log.d("Irrigation", "Real Irrigation: " + realIrrigation+ " Liter");
                    Log.d("Irrigation", "Irrigation Time: " + irrigationTime+ " Minutes");
                    Log.d("Irrigation", "Logic executed ");

                    //Toast.makeText(context, "Expected precipitation in Hobart: " + dashboard.getOutputValue(), Toast.LENGTH_SHORT).show(); // dashboard.getOutputValue() gets the value from python script
                    if (realIrrigation>0) {
                        //turn on the irrigation
                        new AsyncTask<Integer, Void, Void>() {
                            @Override
                            protected Void doInBackground(Integer... params) {
                                try {
                                    //start irrigation
                                    run("tdtool --on 1");
                                } catch (IOException | RuntimeException e) {
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }
                            protected void onPostExecute(Void v) {
                                //wait for the irrigation time to end
                                try {
                                    Toast.makeText(context, "Automatic irrigation turned on!", Toast.LENGTH_SHORT).show();
                                    // displayCurrentlyIrrigatingOn();
                                    Thread.sleep((long) (irrigationTime * 60 * 1000));
                                    //stop irrigation
                                    run("tdtool --off 1");
                                    Toast.makeText(context, "Automatic irrigation turned off!", Toast.LENGTH_SHORT).show();
                                    // displayCurrentlyIrrigatingOff();
                                    Toast.makeText(context, "Irrigation finished for today", Toast.LENGTH_SHORT).show();
                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                        }.execute(1);
                    } else{        Toast.makeText(context, "No irrigation today due to precipitation", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(1);
        }
    }
}

