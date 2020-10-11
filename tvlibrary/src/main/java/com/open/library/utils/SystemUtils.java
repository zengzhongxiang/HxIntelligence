package com.open.library.utils;

import com.open.library.network.ShellUtils;

/**
 * 系统常用工具类:比如静默安装
 *
 */
public class SystemUtils {

    /**
     * 静默安装
     *
     * @param apkPath apk路径,/mnt/sdcard/test.apk
     * @return true 执行成功, false 反之
     */
    public static boolean installApp(String apkPath) {
        return ShellUtils.execCommand("pm install -r " + apkPath, true).result == 0 ? true : false;
    }

    /**
     * 静默卸载
     *
     * @param packName apk包名，com.qiyi.video
     * @return true 执行成功, false 反之
     */
    public static boolean unInstallApk(String packName) {
        return ShellUtils.execCommand("pm uninstall " + packName, true).result == 0 ? true : false;
    }

}
