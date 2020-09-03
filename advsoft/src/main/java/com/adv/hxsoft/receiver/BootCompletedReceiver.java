package com.adv.hxsoft.receiver;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adv.hxsoft.service.StartActivityService;
import com.adv.hxsoft.util.SystemInfo;

import java.util.List;

public class BootCompletedReceiver extends BroadcastReceiver {
    private boolean isOpen = false;

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            List runningProgress = SystemInfo.getRunningProgress(context);
            for (int i = 0; i < runningProgress.size(); i++) {
                if ("com.adv.hxsoft".equals(((RunningAppProcessInfo) runningProgress.get(i)).processName)) {
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
