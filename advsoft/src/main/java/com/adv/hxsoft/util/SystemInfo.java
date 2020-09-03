package com.adv.hxsoft.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class SystemInfo {

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

    public static void rebootApp(final Context context){
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntentForPackage);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 300);//500秒后执行Runnable中的run方法
    }
}
