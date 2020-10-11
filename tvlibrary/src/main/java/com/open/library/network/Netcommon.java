package com.open.library.network;

import android.content.Context;

import com.open.library.R;
import com.open.library.utils.StringUtils;

import java.util.Arrays;

/**
 *
 */
public class Netcommon {

    /**
     * 通过busybox获取WiFi的子网掩码(通过wlan指定为Wifi)
     *
     * @return
     */
    public static String getWifiMask() {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("busybox ifconfig", false, true);
        String res = commandResult.successMsg.split("wlan")[1];
        if (res.contains("Mask:") && res.contains("inet6")) {
            res = res.substring(res.indexOf("Mask:") + "Mask:".length(), res.indexOf("inet6")).trim();
        } else {
            res = "255.255.255.0";
        }
        return res;
    }

    // int型ip地址转化为字符串
    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
    }

    // 检查int型子网掩码是否匹配
    public static boolean matchMask(Context context, String subnetMask) {
        return Arrays.asList(context.getResources().getStringArray(R.array.legalMask)).contains(subnetMask);
    }

    /**
     * 子网掩码转化为Int型
     *
     * @param sIP
     * @return
     */
    public static int MaskStringToInt(String sIP) {
        if (StringUtils.isEmpty(sIP)) {
            return 0;
        }
        int ret = 0;
        String[] aIP = sIP.split("\\.");
        if (aIP.length == 4) {
            for (int i = 0; i < aIP.length; i++) {
                int iIP = Integer.parseInt(aIP[i]);
                while (iIP != 0) {
                    if ((iIP & 0x1) == 0x1)
                        ret++;
                    iIP >>= 1;
                }
            }
        }
        return ret;
    }

    /**
     * 子网掩码转化为String型
     *
     * @param iIP
     * @return
     */
    public static String MaskIntToString(int iIP) {
        int cIP = iIP / 8;
        int tIP = iIP % 8;
        String sIP = "";
        for (int i = 0; i < cIP; i++)
            sIP = sIP + "255.";
        int t = 0;
        for (int i = 0; i < 7; i++) {
            if (i < tIP)
                t += 0x1;
            t <<= 1;
        }

        if (sIP.length() > 0 && cIP != 4)
            sIP = sIP + "." + t;
        else if (cIP == 4) {
            sIP = sIP.substring(0, sIP.length() - 1);
        } else {
            sIP = "" + t;
        }
        int r = (cIP == 0 && tIP == 0) ? (4 - cIP) : (4 - cIP - 1);

        if (r < 0)
            r = 0;
        int i;
        for (i = 0; i < r; i++)
            sIP = sIP + ".0";
        return sIP.replace("..", ".");
    }

}
