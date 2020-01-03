package com.hxsoft.laucher.receiver;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxsoft.laucher.service.StartActivityService;
import com.hxsoft.laucher.utils.SystemInfo;

import java.util.List;

public class BootCompletedReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private boolean isOpen = false;

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            List runningProgress = SystemInfo.getRunningProgress(context);
            for (int i = 0; i < runningProgress.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("运行中进程：");
                stringBuilder.append(((RunningAppProcessInfo) runningProgress.get(i)).processName);
                if ("com.hxsoft.laucher".equals(((RunningAppProcessInfo) runningProgress.get(i)).processName)) {
                    this.isOpen = true;
                    break;
                }
            }
            if (!this.isOpen) {
                Intent i = new Intent(context, StartActivityService.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(i);
            }
        }
    }
}
