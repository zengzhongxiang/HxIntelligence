package com.open.library.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.open.library.joor.Reflect;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * ethernetManager 反射类.
 *
 */
@SuppressWarnings("WrongConstant")
public class EthernetManagerRef {

    public static final String ETHERNET_SERVICE = "ethernet";
    Object mEthernetManagerObject;
    Object mEthernetDeviceInfoRef;
    Class mEthernetManagerClass;
    Context mContext;

    private String mIp; // ip地址
    private String mNetmask; // 掩码
    private String mDefaultWay; // 网关
    private String mFirstDns; // dns
    private String mSecondDns;

    public EthernetManagerRef(Context context) {
        this.mContext = context;
        mEthernetManagerObject = context.getSystemService(ETHERNET_SERVICE); // ethernet
        if (null != mEthernetManagerObject) {
            mEthernetManagerClass = mEthernetManagerObject.getClass();
        }
        getEthernetDevInfo();

        getWifiDevInfo();
    }

    public void updateDevInfo() {
    }

    /**
     * 获取MAC地址.
     *
     * @return
     */
    public String getMacAddress() {
        String macAddress = null;
        try {
            macAddress = getLocalEthernetMacAddress(); //Reflect.on(mEthernetManagerObject).call("getMacAddress").get();
            if (!TextUtils.isEmpty(macAddress)) {
                return macAddress.toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocalEthernetMacAddress() {
        String mac=null;
        try {
            Enumeration localEnumeration=NetworkInterface.getNetworkInterfaces();

            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface=(NetworkInterface) localEnumeration.nextElement();
                String interfaceName=localNetworkInterface.getDisplayName();

                if (interfaceName==null) {
                    continue;
                }

                if (interfaceName.equals("eth0")) {
                    // MACAddr = convertMac(localNetworkInterface
                    // .getHardwareAddress());
                    mac=convertToMac(localNetworkInterface.getHardwareAddress());
                    if (mac!=null&&mac.startsWith("0:")) {
                        mac="0"+mac;
                    }
                    break;
                }

                // byte[] address =
                // localNetworkInterface.getHardwareAddress();
                // Log.i(TAG, "mac=" + address.toString());
                // for (int i = 0; (address != null && i < address.length);
                // i++)
                // {
                // Log.i("Debug", String.format("  : %x", address[i]));
                // }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }

    private static String convertToMac(byte[] mac) {
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<mac.length; i++) {
            byte b=mac[i];
            int value=0;
            if (b>=0&&b<=16) {
                value=b;
                sb.append("0"+Integer.toHexString(value));
            } else if (b>16) {
                value=b;
                sb.append(Integer.toHexString(value));
            } else {
                value=256+b;
                sb.append(Integer.toHexString(value));
            }
            if (i!=mac.length-1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }


    public void setConfiguration(Object ipConfiguration) {
        try {
            Reflect.on(mEthernetManagerObject).call("setConfiguration", ipConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getConfiguration() {
        try {
            Method getConfiguration = mEthernetManagerClass.getMethod("getConfiguration");
            return getConfiguration.invoke(mEthernetManagerObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEnabled() {
        try {
            return Reflect.on(mEthernetManagerObject).call("isEnabled").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开有线，用于有线网络的IP修复
     */
    public boolean setIprepair() {
        try {
            setEnabled(false);
            setEnabled(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setEnabled(boolean enabled) {
        try {
            Reflect.on(mEthernetManagerObject).call("setEnabled", enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于有线网络配置界面.
     * 获取 IP地址，默认网关，DNS地址等等信息.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getEthernetDevInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        LinkProperties linkProperties = null;
        try {
            linkProperties = Reflect.on(connectivityManager).call("getLinkProperties", ConnectivityManager.TYPE_ETHERNET).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int index = 0;
            List<LinkAddress> linkAddressList = Reflect.on(linkProperties).call("getAllLinkAddresses").get();
            for (LinkAddress linkAddr : linkAddressList) {
                if (index == 0) {
                    setIp(linkAddr.getAddress().getHostAddress()); // 设置IP地址.
                    int prefixLen = Reflect.on(linkAddr).call("getNetworkPrefixLength").get();
                    setNetmask(Netcommon.MaskIntToString(prefixLen));
                }
                index++;
            }
            index = 0;
            List<RouteInfo> routeInfoList = Reflect.on(linkProperties).call("getAllRoutes").get();
            for (RouteInfo route : routeInfoList) {
                if (index == 1 && route.getGateway() != null) {
                    setDefaultWay(route.getGateway().getHostAddress());
                }
                index++;
            }
            index = 0;
            List<InetAddress> dnsList = linkProperties.getDnsServers();
            if (null != dnsList) {
                for (InetAddress inetAddr : dnsList) {
                    if (index == 0) {
                        setFirstDns(inetAddr.getHostAddress());
                    }
                    if (index == 1) {
                        setSecondDns(inetAddr.getHostAddress());
                    }
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setIp("");
            setNetmask("");
            setDefaultWay("");
            setFirstDns("");
            setSecondDns("");
           // setIprepair();
        }
    }

    public void getWifiDevInfo(){
        WifiManager my_wifiManager = ((WifiManager) mContext.getSystemService("wifi"));
        DhcpInfo dhcpInfo = my_wifiManager.getDhcpInfo();
//        WifiInfo wifiInfo = my_wifiManager.getConnectionInfo();
        if(TextUtils.isEmpty (getIp ())){
            setIp(intToIp(dhcpInfo.ipAddress));
        }
        if(TextUtils.isEmpty (getNetmask ())){
            setNetmask(intToIp(dhcpInfo.netmask));
        }
        if(TextUtils.isEmpty (getDefaultWay ())){
            setDefaultWay(intToIp(dhcpInfo.gateway));
        }

        if(TextUtils.isEmpty (getFirstDns ())){
            setFirstDns (intToIp(dhcpInfo.dns1));
        }

        if(TextUtils.isEmpty (getSecondDns ())){
            setSecondDns (intToIp(dhcpInfo.dns2));
        }
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }
    /**
     * 获取IP地址
     * @return
     */
    public String getIp() {
        return mIp;
    }

    public void setIp(String ip) {
        this.mIp = ip;
    }

    /**
     * 获取掩码
     * @return
     */
    public String getNetmask() {
        return mNetmask;
    }

    public void setNetmask(String netmask) {
        this.mNetmask = netmask;
    }

    /**
     * 获取网关
     * @return
     */
    public String getDefaultWay() {
        return mDefaultWay;
    }

    public void setDefaultWay(String defaultWay) {
        this.mDefaultWay = defaultWay;
    }

    /**
     * 获取DNS
     * @return
     */
    public String getFirstDns() {
        return mFirstDns;
    }

    public void setFirstDns(String firstDns) {
        this.mFirstDns = firstDns;
    }

    /**
     * 获取 其它 DNS
     * @return
     */
    public String getSecondDns() {
        return mSecondDns;
    }

    public void setSecondDns(String secondDns) {
        this.mSecondDns = secondDns;
    }

}
