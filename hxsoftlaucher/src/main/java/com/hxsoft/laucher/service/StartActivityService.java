package com.hxsoft.laucher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hxsoft.laucher.activity.foot.FootBathMainActivity;


public class StartActivityService extends Service {

    public static final int MAX_MSG_SIZE = 268435456;
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Intent intent2 = new Intent(this, FootBathMainActivity.class);
        intent2.addFlags(MAX_MSG_SIZE);
        startActivity(intent2);
        return super.onStartCommand(intent, i, i2);
    }
}
