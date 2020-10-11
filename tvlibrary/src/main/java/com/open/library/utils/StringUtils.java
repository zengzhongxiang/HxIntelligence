package com.open.library.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @描述: 一些常用字符串操作函数.
 */
public class StringUtils {

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public static boolean matchIP(String ip) {
        String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 连接字符串.
     */
    public static String join(String str1, String str2, String joinStr) {
        if (str1 == null || str2 == null || joinStr == null) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        b.append(str1);
        if (!str1.endsWith(joinStr) && !str2.startsWith(joinStr)) {
            b.append(joinStr);
        }
        b.append(str2);
        return b.toString();
    }

    /**
     * 连接字符串.
     */
    public static String joinPath(String str1, String str2) {
        return join(str1, str2, "/");
    }

    /**
     * 将类的ID注入字典中去. <br>
     * 注意，类的字段必须和你POST,GET的字段是一致的.
     */
    public static Map<String, Object> setMapValues(Object object,
                                                   StringBuilder filterBuf) {
        Map<String, Object> params = new HashMap<String, Object>();
        Class avatarClass = object.getClass();
        Field[] fs = avatarClass.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            Object val;
            try {
                val = f.get(object);
                if ((filterBuf == null)
                        || filterBuf.lastIndexOf(f.getName()) == -1) {
                    params.put(f.getName(), val);
                }
                System.out.println("name:" + f.getName() + "\t value = " + val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    public static Map<String, Object> setMapValues(Object object) {
        return setMapValues(object, null);
    }

    /**
     * 获取重复的字符串.
     */
    public static String getResetStr(int num, String str) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < num; i++) {
            b.append(str);
        }
        return b.toString();
    }

    /**
     * 获取 unix时间戳转换成正常的时间!!.
     */
    public static String getUnixTimeToDate(String beginDate, String fromat) {
        if (beginDate != null && beginDate.length() > 0) {
            int len = beginDate.length();
            int resetStrNum = 13 - len;
            beginDate += StringUtils.getResetStr(resetStrNum, "0");
            if (fromat == null)
                fromat = "dd/MM/yyyy HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(fromat);
            String sd = sdf.format(new Date(Long.parseLong(beginDate)));
            return sd;
        } else {
            return "";
        }
    }
}
