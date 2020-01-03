package com.hxsoft.laucher.utils;

import android.content.Intent;

import com.hxsoft.laucher.activity.App;
import com.jmgo.android.projector.JmgoCommonManager;

import java.io.File;

/**
 * JMGO工具类
 */
public class JmgoUtil {

    /**
     * 是否是jmgo投影仪
     *
     * @return
     */
    public static boolean isJmgo() {
        boolean f = ApkUtils.checkPackage (App.getApp (), "com.jmgo.setting");
        System.out.println ("jmgo:" + f);
        return f;

    }


    /**
     * 安装apk
     *
     * @param path
     */
    public static void installAPK(final String path) {
        ApkUtils.chmodDir777 (SdCardUtil.getSDCardPath ().getPath () + "/woss");
        if (isJmgo ()) {
            Intent i = new Intent ("jmgo.intent.action.HOLATEK_SILENT_INSTALL");//静默安装
            i.putExtra ("install_file_patch", path);
            i.putExtra ("install_file_path", path);
            App.getApp ().sendBroadcast (i);
        }
    }

    public static void installAPK(File path) {
        installAPK (path.getAbsolutePath ());
    }


    /**
     * 卸载apk
     *
     * @param packageName 要删除的包名
     */
    public static void unInstallAPK(String packageName) {
        if (isJmgo ()) {
            System.out.println ("packageName==" + packageName);
            Intent i = new Intent ("jmgo.intent.action.HOLATEK_SILENT_UNINSTALL");//静默安装
            i.putExtra ("uninstall_packagename", packageName);
            App.getApp ().sendBroadcast (i);

        } else {
//            boolean f = RootManager.uninstall (packageName);
//            if (f == false) {
//                ApkUtils.uninstall(App.getApp(),packageName); //注意这里不能用App.getAPP,需要传入当前Activity
//            }


        }


    }


    //切换hdmi
    public static void changeHDMI() {
        if (isJmgo ()) {
            JmgoCommonManager.getInstance ("unitedview").set (1, 0x4D882, 1);
        }
    }
}
