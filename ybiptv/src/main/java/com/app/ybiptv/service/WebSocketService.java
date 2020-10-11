package com.app.ybiptv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.bean.WebSocketGetBean;
import com.app.ybiptv.bean.WebSocketPostBean;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.view.TextLoopWindow;
import com.google.gson.Gson;
import com.open.library.utils.PreferencesUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * WebSocket 服务
 *
 */
public class WebSocketService extends Service {

    public static final int TEXT_LOOP = 1; // 文字轮播
    public static final int IMAGE_LOOP = 2; // 图片轮播
//    public static final int VIDEO_LOOP = 3; // 视频轮播
    public static final int QUIT_FULL_LOOP = 3; // 退出强制全屏轮播
//    public static final int QUIT_TEXT_LOOP = 5;

    WebSocketConnection mConnection = new WebSocketConnection ();
    TextLoopWindow mTextLoopWindow = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initWebSocket();
        initTextLoopWindow();

    }

    private void initTextLoopWindow() {
        if (null == mTextLoopWindow) {
            mTextLoopWindow = new TextLoopWindow(getApplicationContext());
            //mTextLoopWindow.setLoopText("成都天国酒店酒店停电，停水");
//            mScrollTextList.add ("云犇影院正式开业，消费满一千送美女一个");
            mScrollTextHandler.sendEmptyMessage(250);
        }
    }

    private void initWebSocket() {
        try {
            mConnection.connect(Consts.ROOT_ADDR, mWebSocketHandler);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (null != mConnection) {
//            initWebSocket();
//            return START_STICKY;
//        }
        // 如果触发再发一次.(这次出发一般在联网的情况下 mNetworkChangedReceiver)
        sendIptvInfoToServer();
        return START_STICKY;
    }

    private void sendIptvInfoToServer() {
        // 发送房间信息.
        try {
            Logger.d("websocket sendIptvInfoToServer");
            String roomNo = PreferencesUtils.getString(getApplicationContext(), Consts.IP_ROOM_NO_KEY); // 获取房间号
            String macAddr = ((IptvApplication)getApplicationContext()).getManAddr(); // 获取 mac地址
            WebSocketPostBean webSocketPostBean = new WebSocketPostBean();
            webSocketPostBean.setType("1");
            webSocketPostBean.setMac(macAddr);
            webSocketPostBean.setRoom_number(roomNo);
            Gson gson = new Gson();
            String postStr = gson.toJson(webSocketPostBean);
            Logger.d("websocket postStr:" + postStr);
            mConnection.sendTextMessage(postStr);
        } catch (Exception e) {
            e.printStackTrace();
//            mConnection.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Logger.d("websocket disconnect");
            mConnection.disconnect();
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

    WebSocketHandler mWebSocketHandler = new WebSocketHandler () {
        @Override
        public void onOpen() {
            super.onOpen();
            Logger.d("websocket connect open");
            // 第一次链接发送信息.
            sendIptvInfoToServer();
        }

        @Override
        public void onTextMessage(String payload) {
            super.onTextMessage(payload);
            Logger.d("websocket payload:" + payload);
            // way 发布方式 1.文字轮播 2.图片插播 3.视频插播
            // data 发布信息内容 (文字信息/图片地址/视频地址)
//            sendIptvInfoToServer();
            try {
                Gson gson = new Gson();
                WebSocketGetBean webSocketGetBean = gson.fromJson(payload, WebSocketGetBean.class);
                switch (webSocketGetBean.getWay()) {
                    case TEXT_LOOP: // 文字轮播
                        mScrollTextList.addLast(webSocketGetBean.getData());
                        if (mScrollTextList.size() == 1) {
                            mScrollTextHandler.sendEmptyMessage(250);
                        }
//                        mTextLoopWindow.setLoopText(webSocketGetBean.getData());
//                        EventBus.getDefault().post(webSocketGetBean); // 发送
                        // 监听端.
                        // EventBus.getDefault().register(this);
                        // @Subscribe(threadMode = ThreadMode.MAIN)
                        // public void onTextLoopUpdate(WebSocketGetBean webSocketGetBean) {
                        break;
                    case IMAGE_LOOP: //图片轮播 视频轮播
//                    case VIDEO_LOOP: // 视频轮播
//                        Intent intent = new Intent(getApplicationContext(), FullPlayerActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        String playUrl = webSocketGetBean.getData();
//                        Logger.d("playUrl:" + playUrl);
//                        intent.putExtra("play_url", playUrl);
//                        startActivity(intent);
                        break;
//                    case QUIT_TEXT_LOOP: // 退出字幕滚动
//                        mTextLoopWindow.stopLoopText();
//                        break;
                    case QUIT_FULL_LOOP: // 退出全屏强制插播
                        EventBus.getDefault().post(webSocketGetBean); // 发送
                        break;
                }
                // code:1 type参数错误 code:2 房间号为空或没有 code:3 mac地址为空或没有 code:4 mac地址重复 code:5 房间号重复 code:6 ip地址重复
                switch (webSocketGetBean.getCode()) {
                    case 0: // 註冊成功
                        Toast.makeText(getApplicationContext(), webSocketGetBean.getMessage(), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(webSocketGetBean);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), webSocketGetBean.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        Toast.makeText(getApplicationContext(), webSocketGetBean.getMessage(), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(getApplicationContext(), SettingActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.d(e.getMessage());
            }
        }

        @Override
        public void onClose(int code, String reason) {
            super.onClose(code, reason);
            Logger.d("websocket error code:" + code + " reason:" + reason);
            initWebSocket();
        }

    };

}
