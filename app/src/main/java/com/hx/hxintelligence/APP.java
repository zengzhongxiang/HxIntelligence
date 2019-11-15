package com.hx.hxintelligence;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

public class APP extends Application  {

    public static DbManager.DaoConfig daoConfig;

    private static APP app;

    public static APP getApp() {
        return app;
    }

    public static void setApp(APP app) {
        APP.app = app;
    }

    public static File CardPath;

    public static File getCardPath() {
        return CardPath;
    }

    public static void setCardPath(File cardPath) {
        APP.CardPath = cardPath;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        APP.setApp(this);
    }


}
