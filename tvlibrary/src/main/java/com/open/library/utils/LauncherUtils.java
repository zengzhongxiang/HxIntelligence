package com.open.library.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import com.open.library.R;
import com.orhanobut.logger.Logger;

import java.util.Locale;

/**
 * Launcher 常用函数封装.
 *
 */
public class LauncherUtils {

    /**
     * 安全启动应用
     *
     * @param context
     * @param packageName com.open.activity
     * @param className com.open.activity.HomeActivity
     * @return true 启动成功 false 反之
     */
    public static boolean startApp(Context context, String packageName, String className) {
        try {
            if (context == null) {
                return false;
            }
            // 启动app的activity.
            startActivityForProfile(context, packageName, className);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void startActivityForProfile(Context context, String packageName, String className) {
        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.setComponent(new ComponentName(packageName, className));
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 设置转场动画.
            ActivityOptions optsBundle = ActivityOptions.makeCustomAnimation(context, R.anim.task_open_enter, R.anim.no_anim);
            // 启动
            context.startActivity(launchIntent, null != optsBundle ? optsBundle.toBundle() : null);
        } else {
            // 启动
            context.startActivity(launchIntent);
            // 设置转场动画.
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.task_open_enter, R.anim.no_anim);
            }
        }
    }

    /**
     * 设置语言(中，英 等等语言切换)
     */
    public static void setLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);
    }

}
