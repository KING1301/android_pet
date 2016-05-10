package com.example.ml.petdemo;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


public class NotificationListener extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        //android.os.Debug.waitForDebugger();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification=sbn.getNotification();
        if (mNotification!=null){
            Bundle extras = mNotification.extras;
            Log.d("信息",mNotification.tickerText+"");
            Intent intent = new Intent("WECHAT_NOTICE");
            intent.putExtras(mNotification.extras);
            sendBroadcast(intent);
            PendingIntent pendingIntent = mNotification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }



           /* Notification.Action[] mActions=mNotification.actions;
            if (mActions!=null){
                for (Notification.Action mAction:mActions){
                    int icon=mAction.icon;
                    CharSequence actionTitle=mAction.title;
                    PendingIntent pendingIntent=mAction.actionIntent;
                }
            }*/
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}

