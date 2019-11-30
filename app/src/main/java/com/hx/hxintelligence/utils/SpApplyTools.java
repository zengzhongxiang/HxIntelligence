package com.hx.hxintelligence.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hx.hxintelligence.APP;

import java.util.Set;

public class SpApplyTools {

    private static SharedPreferences sp = APP.getApp().getSharedPreferences("hx_boot.xml", 0);

    public static String HX_PHONE = "hx_phone"; //当前登录的用户名
    public static String HX_PWD = "hx_pwd"; //当前登录的密码
    public static String HX_SESSION = "hx_session"; //保存session
    public static String HX_ROOM = "hx_room"; //房间号  相当于家庭
    public static String HX_HOME_ID = "hx_home_id"; //家庭ID
    public static String HX_HOME_CREATE = "hx_home_create"; //家庭中控的id，控制情景和控制分机都需要用到。注：情景是保存在中控中的，所以控制情景需要用到该id
    public static String HX_ALL_DEVICE = "hx_all_device";   //当前家庭下所有设备的JSON
    public static String HX_ALL_MACRO = "hx_all_macro";   //当前家庭下所有的情景模式
    public static String HX_AIR_TEMPERATURE = "hx_air_temperature";   //保存空调当前温度
    public static String HX_AIR_SPEED = "hx_air_speed";   //保存空调当前风速

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
