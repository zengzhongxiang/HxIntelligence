package com.app.ybiptv.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.IptvApplication;

/**
 *
 */

public class TextLoopWindow {

    Context mContext;
    WindowManager mWindowManager = null;
    View mView = null;
    ScrollTextView mScrollTextView;

    public TextLoopWindow(Context context) {
        try { // todo android 6.0 申请权限
            mContext = context.getApplicationContext();
            // 获取WindowManager
            mWindowManager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            mView = LayoutInflater.from(context).inflate(R.layout.popupwindow,
                    null);
            //
            mScrollTextView = mView.findViewById(R.id.scroll_tv);
            //
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();


//这里针对不同版本进行判断
            System.out.println ("Build.VERSION.SDK_INT =="+Build.VERSION.SDK_INT );
            if (Build.VERSION.SDK_INT >= 26)//8.0
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            else if(Build.VERSION.SDK_INT<26 && Build.VERSION.SDK_INT>=23){//6.0
                params.type = WindowManager.LayoutParams.TYPE_TOAST;
            }else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
//            TYPE_PHONE
//                    TYPE_PRIORITY_PHONE
//            TYPE_SYSTEM_ALERT
//                    TYPE_SYSTEM_OVERLAY
//            TYPE_SYSTEM_ERROR
//                    TYPE_TOAST

//            if(Build.VERSION.SDK_INT >=26 && mContext.getApplicationContext ().getApplicationInfo ().targetSdkVersion>22){
//                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            }else {
//                PackageManager pm = context.getPackageManager ();
//                boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission ("android.permission.SYSTEM_ALERT_WINDOW",context.getPackageName ()));
//                if(permission || "Xiaomi".equals (Build.MANUFACTURER) || "vivo".equals (Build.MANUFACTURER)){
//                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
//                }else {
//                    params.type = WindowManager.LayoutParams.TYPE_TOAST;
//                }
//            }

            // 类型
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;  //TYPE_SYSTEM_ALERT   TYPE_TOAST
            // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            // 设置flag
            int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

            //FLAG_ALT_FOCUSABLE_IM;
            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
            params.flags = flags;
            // 不设置这个弹出框的透明遮罩显示为黑色
            params.format = PixelFormat.TRANSLUCENT;
            // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
            // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
            // 不设置这个flag的话，home页的划屏会有问题
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            mWindowManager.addView(mView, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoopText(String text) {
        mScrollTextView.setText(text, 2);
        mScrollTextView.starScroll();
    }

    public void stopLoopText() {
        mScrollTextView.stopScroll();
    }

    public void hidePopupWindow() {
        if (null != mView) {
            mScrollTextView.stopScroll();
            mWindowManager.removeView(mView);
        }
    }

}
