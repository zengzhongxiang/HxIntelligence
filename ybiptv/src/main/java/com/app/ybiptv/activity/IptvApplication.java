package com.app.ybiptv.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.ybiptv.service.WebSocketService;
import com.app.ybiptv.utils.BasicParamsInterceptor;
import com.app.ybiptv.utils.Consts;
import com.open.library.network.NetWorkTools;
import com.open.library.utils.PreferencesUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.smtt.sdk.QbSdk;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**

 */
public class IptvApplication extends Application {

    public static final String ETHERNET_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.ETHERNET_STATE_CHANGED";

    public static final String TAG = IptvApplication.class.getSimpleName();

    NetWorkTools mNetWorkTools;
    private boolean mIsEthernet = false;

    public static IptvApplication application;

    public static IptvApplication getApplication() {
        return application;
    }

    public static void setApplication(IptvApplication application) {
        IptvApplication.application = application;
    }

    /**
     * 版本号
     **/
    public String versionName;
    public int versionCode;
    public BasicParamsInterceptor basicParamsInterceptor;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
        PackageManager manager = getPackageManager();
        try {
            setVersionName(manager.getPackageInfo(getPackageName(), 0).versionName);
            setVersionCode(manager.getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//添加post公共请求参数  Constants.PACKAGE 和 Constants.CFROM
        String roomNo = PreferencesUtils.getString(getApplication (), Consts.IP_ROOM_NO_KEY);

        mNetWorkTools = new NetWorkTools(this);
        System.out.println ("mac1111=="+getManAddr ());
        Map<String, String> params = new HashMap<> ();
        params.put("mac",getManAddr ());
        params.put ("room",roomNo);
        params.put ("ver",getVersionName ());
        params.put ("type",android.os.Build.MODEL);
        System.out.println ("mac=="+getManAddr ());
        basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addHeaderParamsMap(params)  //  添加公共消息头
                .addParam("from", "androidTV") //添加公共参数到 post 请求体
                .addQueryParam("version",getVersionName ())  // 添加公共版本号，加在 URL 后面
//                .addHeaderLine("mac:"+getManAddr ())  // 示例： 添加公共消息头
//                .addParamsMap(params) // 可以添加 Map 格式的参数
                .build();

        startService(new Intent(getApplicationContext(), WebSocketService.class)); // 启动websocket服务
//        initLogger();
//        initTencentX5WebView();
        initOkHttp();
        initNetworkChangedReceiver();
//        initWifiAp();
    }

    private void initNetworkChangedReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ETHERNET_STATE_CHANGED_ACTION);
        registerReceiver(mNetworkChangedReceiver, filter);
    }
//        File sdcache = new File(Environment.getExternalStorageDirectory(), "cache");

    MyOkHttp mMyOkhttp;

    private void initOkHttp() {
        //okhttp可以缓存数据....指定缓存路径
//        //指定缓存大小
//        int cacheSize = 10 * 1024 * 1024;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(basicParamsInterceptor) // 添加公共参数拦截器
//                .cache(new Cache (sdcache.getAbsoluteFile(), cacheSize))//设置缓存
                //其他配置
                .build();
        mMyOkhttp = new MyOkHttp(okHttpClient);
    }

    public MyOkHttp getOkHttp() {
        return mMyOkhttp;
    }

    /**
     * 初始化log
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("yb_iptv")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    /**
     * 初始化腾讯X5webview
     */
    private void initTencentX5WebView() {
        String ipAddr = PreferencesUtils.getString(getApplicationContext(), Consts.IP_ADDR_KEY);
        Consts.ROOT_ADDR = "http://" + ipAddr;
        //
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Logger.d("：Tentent x5 core is inited finished");
            }

            //-isX5Core:isX5Core 为 true 表示 x5 内核加载成功；
            // false 表示加载失败，此时会自动切换到系统内核。如果在此回调前创建 webview 会导致使用系统内核 -return:void
            @Override
            public void onViewInitFinished(boolean isX5Core) {
                Logger.d("x5webview isX5Core " + isX5Core);
            }
        });
        QbSdk.preinstallStaticTbs(this);
    }

    public String getManAddr() {
        String macAddr = mNetWorkTools.getMacAddr();
        if (TextUtils.isEmpty(macAddr)) {
            macAddr = "aa:bb:cc:ee:ff:dd";
        }
        Logger.d("macAddr:" + macAddr);
        return macAddr;
//        return "1111111111";
    }

    private BroadcastReceiver mNetworkChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ETHERNET_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                mIsEthernet = intent.getBooleanExtra("ETHERNET_state", false);
                if (mIsEthernet) {
                    startService(new Intent(getApplicationContext(), WebSocketService.class)); // 启动websocket服务
                } else {
                    Toast.makeText(getApplicationContext(), "有线网络未连接，请检查", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                }
                Logger.d("mIsEthernet:" + mIsEthernet);
            }
        }
    };

    /**
     * 获取IP地址.
     * @return
     */
    public String getIpAddr() {
        return PreferencesUtils.getString(getApplicationContext(), Consts.IP_ROOM_NO_KEY);
    }

    /**
     * 判断有线网络是否存在.
     */
    public boolean isEthernet() {
        return mIsEthernet;
    }

    /**
     * 初始化热点
     */
//    public void initWifiAp() {
//        Map<String, String> params = new HashMap<>();
//        params.put("mac_addr", ((IptvApplication)getApplicationContext()).getManAddr());
////        params.put("mac_addr", "aa:bb:cc:ee:ff:dd");
//        mMyOkhttp.post()
//                .url(Consts.GET_LIVE_LIST_ADDR)
//                .params(params)
//                .enqueue(new GsonResponseHandler<ResultBean<WifiApMode>>() {
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        Logger.d("statusCode:" + statusCode + "error_msg:" + error_msg);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, ResultBean<WifiApMode> response) {
//                        Logger.d("WifiApMode:" + response);
//                        if (null != response && null != response.getData()) {
//                            WifiApMode mode = response.getData();
//                            String name = mode.getWifi_name();
//                            String pass = mode.getWifi_pswd();
//                            // 如果WIFI開啟，則關閉WIFI，開啟熱點.
//                            if (mNetWorkTools.isWifiEnabled()) {
//                                mNetWorkTools.openWifiAp();
//                            }
//                            mNetWorkTools.setWifiApConfig(name, pass);
//                        }
//                    }
//                });
//    }

}
