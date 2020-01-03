package com.hxsoft.laucher.activity;

import android.app.Application;

import org.xutils.x;

public class App extends Application{

    private static App app;

    public static App getApp() {
        return app;
    }

    public static void setApp(App app) {
        App.app = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler.getInstance().init(this);
        App.setApp(this);
        x.Ext.init(this);
    }


}
