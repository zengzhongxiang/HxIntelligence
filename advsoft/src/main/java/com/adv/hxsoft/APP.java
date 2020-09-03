package com.adv.hxsoft;

import android.app.Application;
import android.util.DisplayMetrics;

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

//    private static boolean isusbSdcard;
//    private static String usbFile;
//
//    public static boolean isIsusbSdcard() {
//        return isusbSdcard;
//    }
//
//    public static void setIsusbSdcard(boolean isusbSdcard) {
//        APP.isusbSdcard = isusbSdcard;
//    }
//
//    public static String getUsbFile() {
//        return usbFile;
//    }

//    public static void setUsbFile(String usbFile) {
//        APP.usbFile = usbFile;
//    }

    private static float mScreenDensity;
    private static int mScreenHeight;
    private static int mScreenWidth;

    public static int getmScreenWidth() {
        return mScreenWidth;
    }

    public static void setmScreenWidth(int screenWidth) {
        mScreenWidth = screenWidth;
    }

    public static int getmScreenHeight() {
        return mScreenHeight;
    }

    public static void setmScreenHeight(int screenHeight) {
        mScreenHeight = screenHeight;
    }

    public static float getmScreenDensity() {
        return mScreenDensity;
    }

    public static void setmScreenDensity(float screenDensity) {
        mScreenDensity = screenDensity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initScreenInfo();
        x.Ext.init(this);
        APP.setApp(this);
    }

    public void initScreenInfo() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        APP.setmScreenDensity(density);
        APP.setmScreenWidth(screenWidth);
        APP.setmScreenHeight(screenHeight);
    }

}
