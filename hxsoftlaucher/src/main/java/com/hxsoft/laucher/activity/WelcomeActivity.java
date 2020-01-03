package com.hxsoft.laucher.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class WelcomeActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }


    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        Log.d("zzx", "屏幕宽度（像素）：" + width);
        Log.d("zzx", "屏幕高度（像素）：" + height);
        Log.d("zzx", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("zzx", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("zzx", "屏幕宽度（dp）：" + screenWidth);
        Log.d("zzx", "屏幕高度（dp）：" + screenHeight);
    }

}
