package com.hxsoft.laucher.utils;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 天气api
 */
public class WeatherUtil {

    public static void getWeather(final Activity activity, final TextView temperature_txt, final TextView region_txt, final ImageView tq_img)
    {
        final String reg = "<div class=\"cityname\">([^<]+)</div>\\s*<div class=\"divCurrentWeather\">\\s*<img class='pngtqico' align='absmiddle' src='([^\']+)' style='border:0;width:20px;height:20px'/>([^<]+)<span class=\"cc30 f1\">([^<]+)</span>～<span class=\"c390 f1\">([^<]+)</span>";

        //String reg = "";
        String ipServer = "http://i.tianqi.com/index.php?c=code&id=1";
        HttpUtil.get(ipServer, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                Matcher matcher = Pattern.compile(reg).matcher(s);
                if (matcher.find())
                {
                    String city = matcher.group(1);
                    String image = matcher.group(2);
                    String tianqi = matcher.group(3);
                    String maxWendu = matcher.group(4);
                    String minWendu = matcher.group(5);

                    System.out.println(city);
                    System.out.println(image);
                    System.out.println(maxWendu);
                    System.out.println(minWendu);

                    temperature_txt.setText(minWendu+"～"+maxWendu+"");
                    region_txt.setText(city+"  "+tianqi);
                    System.out.println("image=="+image);
                    Glide.with(activity.getApplicationContext()).load(image).into(tq_img);
                }

            }

            @Override
            public void fail(String str) {

            }
        });
    }
}
