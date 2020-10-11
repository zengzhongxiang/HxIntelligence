package com.open.library.network;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.open.library.joor.Reflect;

/**
 * WifiManger反射使用类.
 *
 */
public class WifiManagerRef {

    WifiManager mWifiManager;
    Class mWifiManagerClass;
    Context mContext;

    public WifiManagerRef(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiManagerClass = mWifiManager.getClass();
    }

    public void save(WifiConfiguration wifiConfiguration) {
        try {
            Reflect.on(mWifiManager).call("save", wifiConfiguration, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean removeNetwork(int netId) {
        try {
            Reflect reflect = Reflect.on(mWifiManager).call("removeNetwork", netId);
            return reflect.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean disableNetwork(int netId) {
        try {
            Reflect reflect = Reflect.on(mWifiManager).call("disableNetwork", netId);
            return reflect.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    public void forget(int netID) {
        try {
            Reflect.on(mWifiManager).call("forget", netID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forget(WifiConfiguration wifiConfiguration) {
        try {
            Reflect.on(mWifiManager).call("forget", wifiConfiguration, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(WifiConfiguration wifiConfiguration) {
        try {
            Reflect.on(mWifiManager).call("connect", wifiConfiguration, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(int netId) {
        try {
            Reflect.on(mWifiManager).call("connect", netId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWifiApEnabled(WifiConfiguration wifiConfiguration, boolean enable) {
        try {
            Reflect.on(mWifiManager).call("setWifiApEnabled", wifiConfiguration, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWifiApEnabled() {
        try {
            return Reflect.on(mWifiManager).call("isWifiApEnabled").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getWifiApState() {
        int apState = 0;
        try {
            apState = Reflect.on(mWifiManager).call("getWifiApState").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apState;
    }

    public void setWifiApConfiguration(WifiConfiguration config) {
        try {
            Reflect.on(mWifiManager).call("setWifiApConfiguration", config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DhcpInfo getDhcpInfo() {
        return mWifiManager.getDhcpInfo();
    }

    public WifiConfiguration getWifiApConfiguration() {
        WifiConfiguration config = Reflect.on(mWifiManager).call("getWifiApConfiguration").get();
        return config;
    }

}
