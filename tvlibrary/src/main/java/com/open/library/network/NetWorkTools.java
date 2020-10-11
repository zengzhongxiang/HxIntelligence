package com.open.library.network;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * 网络模块(包括热点，有线 自动/手动 切换等等)
 *
 */
public class NetWorkTools {

    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_DISABLING = 10;

    public static final int WIFI_AP_STATE_FAILED = 14;

    public static class KeyMgmt {
        public static final int WPA2_PSK = 4;
    }

    private Context mContext;
    private WifiManager mWifiManager;
    private WifiManagerRef mWifiManagerRef;
    private EthernetManagerRef mEthernetManagerRef; // 有线信息.

    public NetWorkTools(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiManagerRef = new WifiManagerRef(mContext);
        mEthernetManagerRef = new EthernetManagerRef(mContext);
    }

    /**
     * WIFI是否開啟
     * @return
     */
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 打开热点
     */
    public void openWifiAp() {
        mWifiManager.setWifiEnabled(false);
        setWifiApEnabled(null, true);
    }

    /**
     * 关闭热点
     */
    public void closeWifiAp() {
        setWifiApEnabled(null, false);
    }

    /**
     * 获取热点名称
     *
     * @return
     */
    public String getWifiApSSID() {
        WifiConfiguration config = mWifiManagerRef.getWifiApConfiguration();
        return config.SSID;
    }

    /**
     * 获取热点密码
     *
     * @return
     */
    public String getWifiApPass() {
        WifiConfiguration config = mWifiManagerRef.getWifiApConfiguration();
        return config.preSharedKey;
    }

    /**
     * 设置 WIFI热点配置.
     */
    public void setWifiApConfig(String ssid, String pwd) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedKeyManagement.set(KeyMgmt.WPA2_PSK);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.SSID = ssid;
        config.preSharedKey = pwd;

        int apState = getWifiApState(); // 获取热点状态
        if (WIFI_AP_STATE_ENABLED == apState) {
            setWifiApEnabled(null, false);
            setWifiApEnabled(config, true);
        } else {
            setWifiApEnabled(null, true);
            setWifiApConfiguration(config); /* mWifiManager.setWifiApConfiguration(config); */
        }
    }

    /**
     * 设置热点配置
     */
    private void setWifiApConfiguration(WifiConfiguration config) {
        mWifiManagerRef.setWifiApConfiguration(config);
    }

    /**
     * 获取热点状态
     */
    private int getWifiApState() {
        return mWifiManagerRef.getWifiApState();
    }

    /**
     * 设置WIFI热点.
     */
    private void setWifiApEnabled(final WifiConfiguration wifiConfiguration, final boolean enable) {
        mWifiManagerRef.setWifiApEnabled(wifiConfiguration, enable);
    }

    /**
     * 获取有线信息(IP地址，MAC等等)
     */

    /**
     * 设置有线自动连接.
     * 手动 则为可以编辑.
     * 自动 不可以编辑.
     */
    public void setWiredAutoConnect() {
        Object ipConObject = mEthernetManagerRef.getConfiguration();
        IpConfigurationRef ipConfiguration;
//            ipConfiguration = new IpConfigurationRef();
        ipConfiguration = new IpConfigurationRef(ipConObject);
        ipConfiguration.setIpAssignment(ipConfiguration.getIpAssignmentType("DHCP"));
        ipConfiguration.setStaticIpConfiguration(null);
        mEthernetManagerRef.setConfiguration(ipConfiguration.getObject());
    }

    /**
     * 获取有线的类(主要用此返回类来获取 IP地址，掩码等等信息，还有操作函数)
     *
     * @return EthernetManagerRef
     */
    public EthernetManagerRef getEthernetManagerRef() {
        return mEthernetManagerRef;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public String getIpAddr() {
        return mEthernetManagerRef.getIp();
    }

    /**
     * 获取掩码
     *
     * @return
     */
    public String getNetMask() {
        return mEthernetManagerRef.getNetmask();
    }

    /**
     * 获取网关
     *
     * @return
     */
    public String getDefaultWay() {
        return mEthernetManagerRef.getDefaultWay();
    }

    /**
     * 获取DNS
     *
     * @return
     */
    public String getFirstDns() {
        return mEthernetManagerRef.getFirstDns();
    }

    /**
     * 获取 其它 DNS
     *
     * @return
     */
    public String getSecondDns() {
        return mEthernetManagerRef.getSecondDns();
    }

    /**
     * 获取MAC地址
     */
    public String getMacAddr() {
        return mEthernetManagerRef.getMacAddress();
    }

//    /**
//     * 得到TV_机顶盒网卡的IP地址
//     *	参阅：http://blog.csdn.net/harryweasley/article/details/50039503
//     * @return
//     */
//    public  String getEthernetIP() {
//        try {
//            // 获取本地设备的所有网络接口
//            Enumeration<NetworkInterface> enumerationNi = NetworkInterface
//                    .getNetworkInterfaces();
//            while (enumerationNi.hasMoreElements()) {
//                NetworkInterface networkInterface = enumerationNi.nextElement();
//                String interfaceName = networkInterface.getDisplayName();
//                //				Log.i("ghq", "网络名字" + interfaceName);
//
//                // 如果是有限网卡
//                if (interfaceName.equals("eth0")) {
//                    Enumeration<InetAddress> enumIpAddr = networkInterface
//                            .getInetAddresses();
//
//                    while (enumIpAddr.hasMoreElements()) {
//                        // 返回枚举集合中的下一个IP地址信息
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                        // 不是回环地址，并且是ipv4的地址
//                        if (!inetAddress.isLoopbackAddress()
//                                && inetAddress instanceof Inet4Address) {
//                            //							Log.i("ghq", inetAddress.getHostAddress() + "   ");
//
//                            return inetAddress.getHostAddress();
//                        }
//                    }
//                }
//            }
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

}
