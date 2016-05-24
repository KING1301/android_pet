package com.example.ml.petdemo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接受开机广播，开机启动服务
 */
public class AlarmServiceBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("AlarmServiceBroadcastReciever", "onReceive()");
        /**启动ALARM服务*/
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }

}
