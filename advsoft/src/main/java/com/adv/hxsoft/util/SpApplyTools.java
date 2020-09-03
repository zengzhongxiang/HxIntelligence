package com.adv.hxsoft.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.adv.hxsoft.APP;

import java.util.Set;

public class SpApplyTools {

    private static SharedPreferences sp = APP.getApp().getSharedPreferences("gg_boot.xml", 0);

    public static String USB_PATH = "usb_path"; //USB路径
    public static String MODE_IMG_VIDEO = "mode_img_video"; //视频图片模式
    public static String VIDEO_POSITION = "video_position"; //视频位置 1左上 2 左下 3右上 1右下
    public static String IMAGE_ANIM = "image_anim"; //图片动画
    public static String IMAGE_ANIM_TIME = "image_anim_time"; //动画时间切换

    public static String ISUSBSDCARD = "isusbSdcard"; //是否有SD卡
    public static String PATHSTRING = "pathString"; //SD卡路径

    public static void putString(String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String value) {
        return sp.getString(key, value);
    }

    public static void putBoolean(String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static void putInt(String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static Set<String> getSetString(String key, Set<String> sets) {
        return sp.getStringSet(key, sets);
    }

    public static void putSetString(String key, Set<String> set) {
        Editor editor = sp.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }
}
