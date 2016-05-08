package com.example.ml.petdemo;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }


}
