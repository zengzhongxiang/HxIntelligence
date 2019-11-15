package com.hx.hxintelligence.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class KkUtil {

    /**
     * 获取当前的时间戳
     * @return
     */
    public static String getTimeStame() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis()/1000;
        //将毫秒值转换为String类型数据
        String time_stamp = String.valueOf(time);
        //返回出去


        return time_stamp;
    }

    /**
     * 获取 0 到65536的随机数
     * @return
     */
    public static int getRandom(){
        int random = new Random ().nextInt(65536);
        return  random;
    }
}
