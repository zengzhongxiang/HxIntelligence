package com.open.library.network;

import com.open.library.joor.Reflect;
import com.orhanobut.logger.Logger;

/**
 * IpConfiguration 为 @Hide 类，需要反射调用.
 *
 */
public class IpConfigurationRef {

    public enum ProxySettings {
        /* No proxy is to be used. Any existing proxy settings
        * should be cleared. */
        NONE,
        /* Use statically configured proxy. Configuration can be accessed
         * with httpProxy. */
        STATIC,
        /* no proxy details are assigned, this is used to indicate
         * that any existing proxy settings should be retained */
        UNASSIGNED,
        /* Use a Pac based proxy.
         */
        PAC
    }

    ;

    public enum IpAssignment {
        /* Use statically configured IP settings. Configuration can be accessed
         * with staticIpConfiguration */
        STATIC,
        /* Use dynamically configured IP settigns */
        DHCP,
        /* no IP details are assigned, this is used to indicate
         * that any existing IP settings should be retained */
        UNASSIGNED
    }

    Object mIpConfigurationObj;
    Reflect mReflect;

    public Object getObject() {
        return mIpConfigurationObj;
    }

    public IpConfigurationRef() {
        try {
            mReflect = Reflect.on("android.net.IpConfiguration").create();
            mIpConfigurationObj = mReflect.get();
            Logger.d("IpConfiguration:" + mIpConfigurationObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IpConfigurationRef(Object ipConfigurationObj) {
        this.mIpConfigurationObj = ipConfigurationObj;
        mReflect = Reflect.on(mIpConfigurationObj);
    }

    public void setProxySettings(Object proxySettings) {
        try {
            mReflect.call("setProxySettings", proxySettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getProxySettingsType(String name) {
        try {
            return mReflect.field("proxySettings").get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setHttpProxy(Object obj) {
        try {
            mReflect.call("setHttpProxy", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIpAssignment(Object type) {
        try {
            mReflect.call("setIpAssignment", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getIpAssignmentType(String name) {
        try {
            return mReflect.field("ipAssignment").get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStaticIpConfiguration(Object object) {
        try {
            mReflect.call("setStaticIpConfiguration", object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
