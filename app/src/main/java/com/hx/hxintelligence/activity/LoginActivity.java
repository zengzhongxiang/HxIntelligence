package com.hx.hxintelligence.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hx.hxintelligence.BaseActivity;
import com.hx.hxintelligence.MainActivity2;
import com.hx.hxintelligence.R;
import com.hx.hxintelligence.response.DeviceResponse;
import com.hx.hxintelligence.response.HomeResponse;
import com.hx.hxintelligence.response.SessionResponse;
import com.hx.hxintelligence.utils.HttpUtil;
import com.hx.hxintelligence.utils.KkUtil;
import com.hx.hxintelligence.utils.ResultCallback;
import com.hx.hxintelligence.utils.SpApplyTools;
import com.hx.hxintelligence.utils.UnicodeUtil;
import com.hx.hxintelligence.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {

    @ViewInject(R.id.edit_phone)
    private EditText edit_phone;

    @ViewInject(R.id.edit_pwd)
    private EditText edit_pwd;

    @ViewInject(R.id.edit_room)
    private EditText edit_room;


    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view ().inject (this);

        edit_phone.setText ( SpApplyTools.getString (SpApplyTools.HX_PHONE,""));
        edit_pwd.setText ( SpApplyTools.getString (SpApplyTools.HX_PWD,""));
        edit_room.setText ( SpApplyTools.getString (SpApplyTools.HX_ROOM,""));
    }

    @Event(value = {R.id.but_login}, type = View.OnClickListener.class)
    private void relativeClick(View view) {
        switch (view.getId ()) {
            case R.id.but_login:
                final String phone = edit_phone.getText ().toString ();
                final String pwd = edit_pwd.getText ().toString ();
                final String room = edit_room.getText ().toString ();

                if (TextUtils.isEmpty (phone)) {
                    Toast.makeText (LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show ();
                    edit_phone.requestFocus ();
                    return;
                }

                if (TextUtils.isEmpty (pwd)) {
                    Toast.makeText (LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show ();
                    edit_pwd.requestFocus ();
                    return;
                }

                if (TextUtils.isEmpty (room)) {
                    Toast.makeText (LoginActivity.this, "请输入房间号", Toast.LENGTH_LONG).show ();
                    edit_room.requestFocus ();
                    return;
                }

                getLogin(phone,pwd,room);
                break;
        }
    }

    private  void getLogin(final String phone,final String pwd,final String room)
    {
        loadingDialog = new LoadingDialog (this, getResources ().getString (R.string.loading_login), R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getSessionRequest");//根据实际需求添加相应键值对
            js_request.put("username", phone);
            js_request.put("password", pwd);
            js_request.put("seq", time+random);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.hxpost (js_request.toString (),"",random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }

                System.out.println("success=="+s);
                Gson gson = new Gson();
                SessionResponse response = gson.fromJson(s, SessionResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    String session = response.getSession ();
                    SpApplyTools.putString (SpApplyTools.HX_SESSION,session);
                    SpApplyTools.putString (SpApplyTools.HX_PHONE,phone);
                    SpApplyTools.putString (SpApplyTools.HX_PWD,pwd);
                    System.out.println ("kkkkkkkkkkk-111=="+session);
                    getHome(room,session);
//                    Toast.makeText (LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show ();
//                    Intent intent = new Intent (LoginActivity.this, MainActivity.class);
//                    startActivity (intent);
                }else {
                    Toast.makeText (LoginActivity.this, UnicodeUtil.decode (response.getMsg ()), Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {
                Toast.makeText (LoginActivity.this, str, Toast.LENGTH_LONG).show ();
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
            }
        });

    }


    /**
     * 获取所有家庭
     * @param room
     */
    private void getHome(final String room, final String session)
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"获取session失败",Toast.LENGTH_LONG).show ();
            return;
        }
        loadingDialog = new LoadingDialog (LoginActivity.this, getResources ().getString (R.string.loading_home), R.mipmap.ic_dialog_loading);
        loadingDialog.show ();
        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getHomeRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }

                Gson gson = new Gson();
                HomeResponse response = gson.fromJson(s, HomeResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    List<HomeResponse.HomeBean> homes = response.getHomes ();
                    String homeId = "";
                    String centerId = "";
                    for (HomeResponse.HomeBean home:homes) {
                        if(room.equals (home.getName ())){
                            homeId = home.getHome_id ();
                            centerId = home.getCenter_id ();
                            break;
                        }

                    }

                    if(!TextUtils.isEmpty (homeId)){
                        SpApplyTools.putString (SpApplyTools.HX_ROOM,room);
                        SpApplyTools.putString (SpApplyTools.HX_HOME_ID,homeId);
                        SpApplyTools.putString (SpApplyTools.HX_HOME_CREATE,centerId);

                        getDevice(homeId,session);
                    }else {
                        Toast.makeText (LoginActivity.this,"没有找到房间号，请添加后再重新登录。",Toast.LENGTH_LONG).show ();
                    }
                }else {
                    Toast.makeText (LoginActivity.this, UnicodeUtil.decode (response.getMsg ()), Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
            }
        });

    }

    /**
     * 获取家庭下面所有设备
     * @param home_id
     * @param session
     */
    private void getDevice(String home_id,String session)
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"获取session失败",Toast.LENGTH_LONG).show ();
            return;
        }

        loadingDialog = new LoadingDialog (LoginActivity.this, getResources ().getString (R.string.loading_device), R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getDeviceRequest");//根据实际需求添加相应键值对
            js_request.put("home_id", home_id);
            js_request.put("seq", time+random);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }

                Gson gson = new Gson();
                DeviceResponse response = gson.fromJson(s, DeviceResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    SpApplyTools.putString (SpApplyTools.HX_ALL_DEVICE,s);
                    Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                    startActivity (intent);
                    finish ();
                }else {
                    Toast.makeText (LoginActivity.this, UnicodeUtil.decode (response.getMsg ()), Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
            }
        });

    }
}
