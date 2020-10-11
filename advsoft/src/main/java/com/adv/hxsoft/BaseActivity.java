package com.adv.hxsoft;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioGroup;


import com.adv.hxsoft.util.Display;
import com.adv.hxsoft.util.SystemInfo;
import com.adv.hxsoft.widget.MenuDialog;

public class BaseActivity extends FragmentActivity implements  MenuDialog.DialogButtonBack{

    private MenuDialog menuDialog;
    private Display display;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        InitDisplay();
        menuDialog = new MenuDialog(this,display,R.style.CustomDialog);
        menuDialog.setDialogCancelBack(this);

    }

    /**
     * 获取屏幕分辨率参数
     */
    private void InitDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int densityDpi = displayMetrics.densityDpi;
        float scaledDensity = displayMetrics.scaledDensity;
        int heightPixels = displayMetrics.heightPixels;
        int widthPixels = displayMetrics.widthPixels;
        display = new Display (densityDpi, scaledDensity,  heightPixels, widthPixels);
    }

    @SuppressLint("RestrictedApi")
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction () == 1) {
            System.out.println ("event.getKeyCode ()=="+event.getKeyCode ());
            switch (event.getKeyCode ()) {
                case 82:
                    if (!menuDialog.isShowing()){
                        menuDialog.show();

                        WindowManager.LayoutParams params = menuDialog.getWindow().getAttributes();
                        params.gravity = Gravity.CENTER;
                        params.width = display.getWidth();
                        params.height = display.getHeight();
                        menuDialog.getWindow().setAttributes(params);
                    }
                    break;
            }

        }
        return super.dispatchKeyEvent (event);
    }

    @Override
    public void buttonPlayerBalck(RadioGroup group, int checkedId) {
        ActivityManager am = (ActivityManager) getSystemService (ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks (1).get (0).topActivity;
        finish ();
        if(checkedId == 0){  //图片轮播
            if (!cn.getClassName ().contains ("com.adv.hxsoft.activity.ImageActivity")) {
                SystemInfo.rebootApp(this);
            }
        }else if(checkedId == 1){    //视频轮播
            if (!cn.getClassName ().contains ("com.adv.hxsoft.activity.VideoActivity")) {
                SystemInfo.rebootApp(this);
            }
        }else if(checkedId == 2){   //视频图片
            if (!cn.getClassName ().contains ("com.adv.hxsoft.activity.ImgVideoActivity")) {
                SystemInfo.rebootApp(this);
            }
        }
    }




}
