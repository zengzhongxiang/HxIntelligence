package com.hxsoft.laucher.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.usage.UsageEvents;
import android.app.usage.UsageEvents.Event;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SystemInfo {
    public static String getWifiMacAddr(Activity activity) {
        String macAddress = ((WifiManager) activity.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getMacAddress();
        return macAddress != null ? macAddress.replace(":", "") : macAddress;
    }

    private static String parseByte(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("00");
        stringBuilder.append(Integer.toHexString(b));
        String stringBuilder2 = stringBuilder.toString();
        return stringBuilder2.substring(stringBuilder2.length() - 2);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                return activeNetworkInfo.getType();
            }
        }
        return -1;
    }

    public static String getAPPVersionCodeFromAPP(Context context) {
        String str = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static void modifyFile(File file) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("chmod -R 777 ");
            stringBuilder.append(file.getAbsolutePath());
            Runtime.getRuntime().exec(stringBuilder.toString()).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static String getRunningActivityName(Context context) {
        String className = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static String getAppPackageName(Context context) {
        ComponentName componentName = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("当前应用:");
        stringBuilder.append(componentName.getPackageName());
        return componentName.getPackageName();
    }

    public static String getTopAppPackageName(Context context) {
        String str = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 21) {
            long currentTimeMillis = System.currentTimeMillis();
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
            if (usageStatsManager == null) {
                return str;
            }
            UsageEvents queryEvents = usageStatsManager.queryEvents(currentTimeMillis - 60000, currentTimeMillis);
            if (queryEvents == null) {
                return str;
            }
            Event event = new Event();
            Event event2 = null;
            while (queryEvents.hasNextEvent()) {
                queryEvents.getNextEvent(event);
                if (event.getEventType() == 1) {
                    event2 = event;
                }
            }
            if (event2 != null) {
                str = event2.getPackageName();
            }
        } else {
            str = ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        return str;
    }

    public static List<RunningAppProcessInfo> getRunningProgress(Context context) {
        List<RunningAppProcessInfo> arrayList = new ArrayList();
        context.getPackageManager();
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            String str = runningServiceInfo.process.split(":")[0];
            RunningAppProcessInfo runningAppProcessInfo = new RunningAppProcessInfo();
            runningAppProcessInfo.pkgList = new String[]{str};
            runningAppProcessInfo.pid = runningServiceInfo.pid;
            runningAppProcessInfo.processName = runningServiceInfo.process;
            runningAppProcessInfo.uid = runningServiceInfo.uid;
            arrayList.add(runningAppProcessInfo);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("后台运行进程数：");
        stringBuilder.append(arrayList.size());
        return arrayList;
    }

    public static void killBackgroundProcesses(Context context, String str) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<RunningServiceInfo> runningServices = activityManager.getRunningServices(50);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("killBackgroundProcesses: runServiceList=");
        stringBuilder.append(runningServices.size());
        activityManager.killBackgroundProcesses(str);
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName componentName = runningServiceInfo.service;
            componentName.getShortClassName();
            String packageName = componentName.getPackageName();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("###########packageName=[");
            stringBuilder2.append(str);
            stringBuilder2.append("]pkgName=[");
            stringBuilder2.append(packageName);
            stringBuilder2.append("]");
            if (packageName.equals(str)) {
                activityManager.killBackgroundProcesses(str);
                stringBuilder = new StringBuilder();
                stringBuilder.append("###########killBackgroundProcesses");
                stringBuilder.append(str);
            }
        }
    }

    public static String getLocalMacAddr() {
        String str = "";
        String str2 = "";
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ").getInputStream()));
            while (str2 != null) {
                str2 = lineNumberReader.readLine();
                if (str2 != null) {
                    str = str2.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str != null ? str.replace(":", "") : str;
    }

    public static String getHostIp() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (inetAddress != null && (inetAddress instanceof Inet4Address) && !inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("本地内网IP:");
                        stringBuilder.append(inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String GetNetIp() {
        return null;
    }
}
