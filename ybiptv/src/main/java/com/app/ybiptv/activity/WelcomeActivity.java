package com.app.ybiptv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.app.ybiptv.R;
import com.app.ybiptv.utils.Consts;
import com.open.library.utils.PreferencesUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import butterknife.ButterKnife;

public class WelcomeActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        String serverUrl = getIntent().getStringExtra("serverUrl");   //地址
        String roomNo = getIntent().getStringExtra("roomNo");   //房间号

        if(!TextUtils.isEmpty (roomNo)){
            PreferencesUtils.putString(this, Consts.IP_ROOM_NO_KEY, roomNo);
        }

        if(!TextUtils.isEmpty (serverUrl)){
            PreferencesUtils.putString(this, Consts.IP_ADDR_KEY, serverUrl);
            Consts.ROOT_ADDR = "http://" + serverUrl;
            Consts.initUrl();
        }else{
            String url = PreferencesUtils.getString (this,Consts.IP_ADDR_KEY);
            Consts.ROOT_ADDR = "http://" + url;
            Consts.initUrl();
        }
        new Thread(runnable).start();
    }

    private  Handler  handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Intent i  = new Intent(WelcomeActivity.this,FiltrateMoviceActivity.class);
            startActivity(i);
            finish();
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {

            handler.sendEmptyMessageDelayed(200,2000);
                        initHost();
        }
    };

    private void initHost(){
        int port = 9999;//开启监听的端口
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        byte[] buf = new byte[1024];//存储发来的消息
        StringBuffer sbuf = new StringBuffer();
        try {
            //绑定端口的
            ds = new DatagramSocket (port);
            dp = new DatagramPacket (buf, buf.length);
            System.out.println("监听广播端口打开：");
            ds.receive(dp);
            ds.close();
            int i;
            for(i=0;i<1024;i++){
                if(buf[i] == 0){
                    break;
                }
                sbuf.append((char) buf[i]);
            }
            String url = sbuf.toString();
            if(!TextUtils.isEmpty (url)){
                PreferencesUtils.putString(this, Consts.IP_ADDR_KEY, url);
                Consts.ROOT_ADDR = "http://" + url;
                Consts.initUrl();
            }
            System.out.println("收到广播消息：" + sbuf.toString());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
