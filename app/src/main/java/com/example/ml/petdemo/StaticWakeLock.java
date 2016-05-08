package com.example.ml.petdemo;

import android.content.Context;
import android.os.PowerManager;


public class StaticWakeLock {
    private static PowerManager.WakeLock wl = null;
    /**闹钟提醒时阻止系统休眠*/
    public static void lockOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (wl == null)
            wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MATH_ALARM");
        wl.acquire();//获取锁并阻止系统进入休眠
    }
    /**恢复系统休眠*/
    public static void lockOff(Context context) {

        try {
            if (wl != null)
                wl.release();
        } catch (Exception e) {

        }
    }
}
