package com.example.ml.petdemo;

import android.app.Application;
import android.service.notification.StatusBarNotification;

/**
 * Created by ML on 2016/5/18.
 */
public class petApplication extends Application {
    private StatusBarNotification msbn1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public StatusBarNotification getMsbn() {
        return msbn1;
    }

    public void setMsbn1(StatusBarNotification msbn1) {
        this.msbn1 = msbn1;
    }
}
