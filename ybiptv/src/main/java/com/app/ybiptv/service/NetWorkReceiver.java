package com.app.ybiptv.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkReceiver extends BroadcastReceiver {

    /** 没有网络 */
    public static final int NONETWORK = 1;

    /** wifi */
    public static final int WIFI = 2;

    /** 有网络 */
    public static final int NETWORK = 3;

    //接口回调
    private NetWordReceiverCallBack mNetWordReceiverCallBack;

    public NetWorkReceiver(NetWordReceiverCallBack netWordReceiverCallBack){
        this.mNetWordReceiverCallBack=netWordReceiverCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 默认返回状态为无网络
        int result = NONETWORK;
        if (NetworkInfo.State.CONNECTED == cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState()) {
            result = NETWORK;
        } else if (NetworkInfo.State.CONNECTED == cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()) {
            result = WIFI;
        } else{}

        // 调用回调接口
        if (mNetWordReceiverCallBack != null) {
            mNetWordReceiverCallBack.result(result);
        }
    }

}
