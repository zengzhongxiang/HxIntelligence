package com.app.ybiptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.app.ybiptv.activity.BaseActivity;
import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.bean.HeartMode;
import com.app.ybiptv.bean.MoviceTitlerMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.bean.WebSocketGetBean;
import com.app.ybiptv.bean.WebSocketPostBean;
import com.app.ybiptv.fragment.FiltrateMoviceFragment;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.view.TextLoopWindow;
import com.google.gson.Gson;
import com.open.library.utils.PreferencesUtils;
import com.orhanobut.logger.Logger;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * WebSocket 服务
 *
 */
public class WebSocketService extends Service {
    private static final long HEART_BEAT_RATE = 15 * 1000;//每隔15秒进行一次对长连接的心跳检测
    TextLoopWindow mTextLoopWindow = null;
    MyOkHttp mMyOkhttp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWebSocket();
        initTextLoopWindow();

    }

    private void initTextLoopWindow() {
        if (null == mTextLoopWindow) {
            mTextLoopWindow = new TextLoopWindow(getApplicationContext());
            //mTextLoopWindow.setLoopText("云犇影院");
//            mScrollTextList.add ("云犇影院正式开业，消费满一千送美女一个");
            mScrollTextHandler.sendEmptyMessage(250);
        }
    }

    private void initWebSocket() {
        mMyOkhttp = ((IptvApplication)getApplicationContext()).getOkHttp();
        new Timer ().schedule(new TimerTask () {
            @Override
            public void run() {
                new MessgeThread().start();
            }
        },0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (null != mTextLoopWindow) {
                mTextLoopWindow.hidePopupWindow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LinkedList mScrollTextList= new LinkedList(); // 滚屏字幕队列.

    String xxMsg = "云犇影院正式开业，消费满一千送美女一个";

    Handler mScrollTextHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTextLoopWindow.setLoopText(xxMsg);
            mScrollTextHandler.sendEmptyMessageDelayed(250, 60 * 1000); // 20秒

//            System.out.println ("mScrollTextList.size()=="+mScrollTextList.size());
//            if (mScrollTextList.size() > 0) {
//                mTextLoopWindow.setLoopText((String) mScrollTextList.pop());
//                mScrollTextHandler.sendEmptyMessageDelayed(250, 20 * 1000); // 20秒
//            } else {
//                mTextLoopWindow.stopLoopText();
//                mScrollTextHandler.removeCallbacksAndMessages(null);
//            }
        }
    };

    class MessgeThread extends  Thread
    {
        @Override
        public void run() {
            super.run();
            try {
                Map<String, String> params = new HashMap<> ();
                params.put("mac",((IptvApplication)getApplicationContext()).getManAddr ());
                while (true)
                {
                    mMyOkhttp.get ().url(Consts.GET_HEART).params(params).enqueue(new GsonResponseHandler<ResultBean<List<HeartMode>>> () {
                        @Override
                        public void onFailure(int statusCode, String error_msg) {
//                            System.out.println ("statusCode=="+statusCode+"   error_msg=="+error_msg);
                        }

                        @Override
                        public void onSuccess(int statusCode, ResultBean<List<HeartMode>> response) {
//                            System.out.println ("response=="+response);

                        }
                    });
                    Thread.sleep(HEART_BEAT_RATE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
