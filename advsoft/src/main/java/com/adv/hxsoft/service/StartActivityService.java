package com.adv.hxsoft.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.adv.hxsoft.activity.WelcomeActivity;


public class StartActivityService extends Service {

    public static final int MAX_MSG_SIZE = 268435456;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Intent intent2 = new Intent(this, WelcomeActivity.class);
        intent2.addFlags(MAX_MSG_SIZE);
        startActivity(intent2);
        return super.onStartCommand(intent, i, i2);
    }
}
