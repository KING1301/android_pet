package com.example.ml.petdemo;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;


public class NotificationListener extends NotificationListenerService {
    //private Notification mNotification;
    private StatusBarNotification msbn;
    private PendingIntentReceiver pendingIntentReceiver = new PendingIntentReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("服务Oncreat", "服务启动");
        //android.os.Debug.waitForDebugger();
        registerReceiver(pendingIntentReceiver, new IntentFilter("WECHAT_CLICK"));
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // if(sbn.getPackageName().toString().equals("com.tencent.mm"))
        //  {
        //
        //  }
        msbn = sbn;
        Notification mNotification = msbn.getNotification();
        if (mNotification!=null){
            Bundle extras = mNotification.extras;
            Log.d("信息",mNotification.tickerText+"");
            Intent intent = new Intent("WECHAT_NOTICE");
            intent.putExtras(mNotification.extras);
            sendBroadcast(intent);
            // PendingIntent pendingIntent = mNotification.contentIntent;
            // try {
            //     pendingIntent.send();
            // } catch (PendingIntent.CanceledException e) {
            //      e.printStackTrace();
            //  }



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

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("服务连接", "连接成功");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pendingIntentReceiver);

    }

    public class PendingIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Log.d("启动应用", "微信");
                try {
                    if (msbn.getNotification() != null) {
                        PendingIntent pendingIntent = msbn.getNotification().contentIntent;
                        pendingIntent.send();
                    }

                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }


            }

        }
    }


}

