package com.example.ml.petdemo;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


public class NotificationListener extends NotificationListenerService {
    //private Notification mNotification;
    //private StatusBarNotification msbn;
    private PendingIntentReceiver pendingIntentReceiver = new PendingIntentReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("服务Oncreat", "服务启动");
        registerReceiver(pendingIntentReceiver, new IntentFilter("WECHAT_CLICK"));
    }

    //需要考虑系统休眠情况下的处理
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().toString().equals("com.tencent.mm") || (sbn.getPackageName().toString().equals("com.tencent.mobileqq"))) {
            // msbn = sbn;
            ((petApplication) getApplication()).setMsbn1(sbn);
            //Notification mNotification = msbn.getNotification();
            Notification mNotification = sbn.getNotification();
        if (mNotification!=null){
            Bundle extras = mNotification.extras;
            Log.d("信息",mNotification.tickerText+"");
            Intent intent = new Intent("WECHAT_NOTICE");
            intent.putExtras(mNotification.extras);
            sendBroadcast(intent);
        }
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
        Log.i("OnSestory", "");
        unregisterReceiver(pendingIntentReceiver);

    }

    public class PendingIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("点击广播", "微信");

            if (intent != null && ((petApplication) getApplication()).getMsbn() != null) {
                Log.d("启动应用", "微信");
                try {
                    if (((petApplication) getApplication()).getMsbn().getNotification() != null) {
                        PendingIntent pendingIntent = ((petApplication) getApplication()).getMsbn().getNotification().contentIntent;
                        pendingIntent.send();
                        //msbn=null;
                        ((petApplication) getApplication()).setMsbn1(null);
                        Log.i("Msbn1", ((petApplication) getApplication()).getMsbn() + "");
                    }

                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }


            }

        }
    }


}

