package com.hxsoft.laucher.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DataUtil {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");// HH:mm:ss
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");// HH:mm:

    public static String getData(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        return dateFormat.format(date);
    }

    public static String getTime(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        return timeFormat.format(date);
    }

    public static String getWeek(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return "星期"+mWay;
    }
}
