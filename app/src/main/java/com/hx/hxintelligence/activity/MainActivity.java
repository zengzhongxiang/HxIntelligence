package com.hx.hxintelligence.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hx.hxintelligence.BaseActivity;
import com.hx.hxintelligence.R;
import com.hx.hxintelligence.response.AirResponse;
import com.hx.hxintelligence.response.BaseResponse;
import com.hx.hxintelligence.response.DeviceResponse;
import com.hx.hxintelligence.response.MacroResponse;
import com.hx.hxintelligence.response.SessionResponse;
import com.hx.hxintelligence.utils.HttpUtil;
import com.hx.hxintelligence.utils.KkUtil;
import com.hx.hxintelligence.utils.ResultCallback;
import com.hx.hxintelligence.utils.SpApplyTools;
import com.hx.hxintelligence.utils.UnicodeUtil;
import com.hx.hxintelligence.widget.AirTempView;
import com.hx.hxintelligence.widget.LoadingDialog;
import com.hx.hxintelligence.widget.WeighingMeterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.lamp_layout)
    private RelativeLayout lamp_layout;  //灯光布局
    @ViewInject(R.id.mode_layout)
    private RelativeLayout mode_layout;  //情景模式布局
    @ViewInject(R.id.window_layout)
    private RelativeLayout window_layout;  //窗帘布局
    @ViewInject(R.id.air_layout)
    private RelativeLayout air_layout;   //空调布局

    @ViewInject(R.id.but_mode1)
    private Button but_mode1;
    @ViewInject(R.id.but_mode2)
    private Button but_mode2;
    @ViewInject(R.id.but_mode3)
    private Button but_mode3;

    private int viewId = -1;


    //灯光相关
    @ViewInject(R.id.but_off_lamp)
    private Button but_off_lamp;

    //温度相关
    @ViewInject(R.id.weighing)
    private WeighingMeterView weighing;

    @ViewInject(R.id.customVolume)
    private AirTempView customVolume;

    @ViewInject(R.id.temperature_txt)
    private TextView temperature_txt;  //当前温度

    private LoadingDialog loadingDialog;

    private DeviceResponse.DeviceBean window_Device;//反馈窗帘
    private DeviceResponse.DeviceBean lamp_Device;  //灯光
    private DeviceResponse.DeviceBean air_Device;  //空调开关

    private String session;
    private String home_id;
    private String home_create_id;   //中控ID

    private int default_temperature = 26;  //默认温度

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view ().inject (this);
        initView();
        initData ();
    }

    private void initView(){
        but_off_lamp.requestFocus ();

        default_temperature = SpApplyTools.getInt (SpApplyTools.HX_AIR_TEMPERATURE,26);
        customVolume.setProcess(default_temperature - 16);
        temperature_txt.setText (default_temperature+"℃");
//        customVolume.setOnProcessListener(listener);

        //默认风速
        speed = SpApplyTools.getInt (SpApplyTools.HX_AIR_SPEED,0);
        weighing.setPercentData(speed,new DecelerateInterpolator ());
    }


    private void initData(){
        session = SpApplyTools.getString (SpApplyTools.HX_SESSION,"");
        home_id = SpApplyTools.getString (SpApplyTools.HX_HOME_ID,"");
        home_create_id =  SpApplyTools.getString (SpApplyTools.HX_HOME_CREATE,"");

        if(TextUtils.isEmpty (session) || TextUtils.isEmpty (home_id)){  //如果数据为空就重新登录
            Intent intent = new Intent (MainActivity.this, LoginActivity.class);
            startActivity (intent);
            finish ();
            return;
        }

        getDevice();  //每次启动进来重新获取当前家庭里面的设备
        initDevice ();
    }

    private void initDevice() {
        String allDevice = SpApplyTools.getString (SpApplyTools.HX_ALL_DEVICE,"");
        Gson gson = new Gson();
        DeviceResponse response = gson.fromJson(allDevice, DeviceResponse.class);
        if(response!=null){
            List<DeviceResponse.DeviceBean> devices = response.getDevices ();
            if(devices!=null){
                for (DeviceResponse.DeviceBean device:devices) {
                    if(device.getSub_type () == 97){   //反馈窗帘
                        window_Device = device;
                    }else if(device.getSub_type () == 57){  //轻触开关 3路
                        lamp_Device = device;
                    }else if(device.getSub_type () == 162){  //人体感应器

                    }else if(device.getSub_type () == 51){  //反馈开关 3路
                        lamp_Device = device;
                    }else if(device.getSub_type () == 145){  //温控面板
                        air_Device = device;
                    }
                }
            }
        }
    }

    int speed = 0;
    @Event(value = {R.id.but_off_lamp,R.id.but_on_lamp,R.id.but_stop_window,R.id.but_off_window,R.id.but_on_window,R.id.but_speed_air,R.id.but_temperature_add,R.id.but_temperature_reduce,
            R.id.but_off_deng1,R.id.but_on_deng1,R.id.but_off_deng2,R.id.but_on_deng2,R.id.but_off_deng3,R.id.but_on_deng3,R.id.but_off_air,R.id.but_on_air,
            R.id.but_air_refrigeration,R.id.but_air_heat,
            R.id.but_mode,R.id.but_mode1,R.id.but_mode2,R.id.but_mode3}, type = View.OnClickListener.class)
    private void relativeClick(View view) {
        viewId = view.getId ();
        onClickBtn (viewId);
    }

    private void onClickBtn(int viewId) {
        switch (viewId) {
            //灯光相关
            case R.id.but_off_lamp:   //全开
                getDengDevice ("all",1,1,"正在开灯，请稍后...");
                break;
            case R.id.but_on_lamp:  //全关
                getDengDevice ("all",1,0,"正在关灯，请稍后...");
                break;
            case R.id.but_off_deng1:  //灯光1开
                getDengDevice ("one",1,1,"正在开灯，请稍后...");
                break;
            case R.id.but_on_deng1:   //灯光1关
                getDengDevice ("one",1,0,"正在关灯，请稍后...");
                break;
            case R.id.but_off_deng2:  //灯光2开
                getDengDevice ("one",2,1,"正在开灯，请稍后...");
                break;
            case R.id.but_on_deng2:   //灯光2关
                getDengDevice ("one",2,0,"正在关灯，请稍后...");
                break;
            case R.id.but_off_deng3:  //灯光3开
                getDengDevice ("one",3,1,"正在开灯，请稍后...");
                break;
            case R.id.but_on_deng3:   //灯光3关
                getDengDevice ("one",3,0,"正在关灯，请稍后...");
                break;

            //情景模式相关
            case  R.id.but_mode:
                getMacro("正在获取所有模式，请稍后...");
                break;
            case  R.id.but_mode1:
                Object id = but_mode1.getTag ();
                if(id!=null) {
                    getCtrlMacro (id.toString (), "请稍后...");
                }
                break;
            case  R.id.but_mode2:
                id = but_mode2.getTag ();
                if(id!=null) {
                    getCtrlMacro (id.toString (), "请稍后...");
                }
                break;
            case  R.id.but_mode3:
                id = but_mode3.getTag ();
                if(id!=null) {
                    getCtrlMacro (id.toString (), "请稍后...");
                }
                break;
            //窗帘相关
            case R.id.but_stop_window:  //停止
                getCtrlDevice("stop",0,"正在停止，请稍后...");
                break;
            case R.id.but_off_window:  //打开窗帘
                getCtrlDevice("start",0,"正在打开，请稍后...");
                break;
            case R.id.but_on_window:  //关闭窗帘
                getCtrlDevice("start",100,"正在关闭，请稍后...");
                break;

            //空调相关
            case R.id.but_off_air:  //空调开
                getAirDevice("switch",1,"正在打开空调，请稍后...");
                break;
            case R.id.but_on_air:  //空调关
                getAirDevice("switch",0,"正在关闭空调，请稍后...");
                break;
            case R.id.but_air_refrigeration:  //制冷
                getAirDevice("model",0,"正在切换制冷模式，请稍后...");
                break;
            case R.id.but_air_heat:  //制热
                getAirDevice("model",1,"正在切换制热模式，请稍后...");
                break;
            case R.id.but_temperature_add:  //空调温度加
                default_temperature ++;
                if(default_temperature >30){
                    default_temperature = 30;
                    Toast.makeText (MainActivity.this, "最大温度只能设置30℃", Toast.LENGTH_LONG).show ();
                    return;
                }

//                customVolume.setProcess(default_temperature - 16);
//                temperature_txt.setText (default_temperature+"℃");

                getAirDevice("temp",default_temperature,"请稍后...");
                break;
            case R.id.but_temperature_reduce:  //空调温度减
                default_temperature --;
                if(default_temperature <16){
                    default_temperature = 16;
                    Toast.makeText (MainActivity.this, "最小温度只能设置16℃", Toast.LENGTH_LONG).show ();
                    return;
                }
//                customVolume.setProcess(default_temperature - 16);
//                temperature_txt.setText (default_temperature+"℃");

                getAirDevice("temp",default_temperature,"请稍后...");
                break;
            case R.id.but_speed_air:  //空调风速
                int fs;
                if(speed == 0){
                    fs = 2;
                }else if(speed == 150){
                    fs = 3;
                }else{
                    fs = 1;
                }
                getAirDevice("speed",fs,"请稍后...");
                break;
        }
    }

    @Event(value = {R.id.but_off_lamp,R.id.but_on_lamp,R.id.but_stop_window,R.id.but_off_window,R.id.but_on_window,R.id.but_off_air,R.id.but_on_air,R.id.but_mode}, type = View.OnFocusChangeListener.class)
    private void relativeFocusChange(View view, boolean hasFocus) {
        switch (view.getId ()) {
            case R.id.but_off_lamp:
            case R.id.but_on_lamp:
                lamp_layout.setVisibility (View.VISIBLE);
                mode_layout.setVisibility (View.GONE);
                window_layout.setVisibility (View.GONE);
                air_layout.setVisibility (View.GONE);
                break;
            case R.id.but_mode:
                lamp_layout.setVisibility (View.GONE);
                mode_layout.setVisibility (View.VISIBLE);
                window_layout.setVisibility (View.GONE);
                air_layout.setVisibility (View.GONE);
                break;
            case R.id.but_stop_window:
            case R.id.but_off_window:
            case R.id.but_on_window:
                lamp_layout.setVisibility (View.GONE);
                mode_layout.setVisibility (View.GONE);
                window_layout.setVisibility (View.VISIBLE);
                air_layout.setVisibility (View.GONE);
                break;
            case R.id.but_off_air:
            case R.id.but_on_air:
                lamp_layout.setVisibility (View.GONE);
                mode_layout.setVisibility (View.GONE);
                window_layout.setVisibility (View.GONE);
                air_layout.setVisibility (View.VISIBLE);
                break;
        }
    }

    /**
     * 控制反馈窗帘
     */
    private void getCtrlDevice(String opt,int value,String lodingTex)
    {
        if(window_Device == null){
            Toast.makeText (MainActivity.this, "没有找到反馈窗帘，请重新登录再试", Toast.LENGTH_LONG).show ();
            return;
        }
        loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "ctrlDeviceRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
            js_request.put("md5", window_Device.getMd5 ());
            js_request.put("sub_id", window_Device.getSub_id ());
            js_request.put("type", "curtain");

            JSONObject js_data = new JSONObject();
            js_data.put ("opt",opt);
            js_data.put ("value",value);
            js_request.put("data", js_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println ("js_request=="+js_request);
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }

                LoginSession (s);
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
     * 控制反馈开关
     *
     * opt   one:一次只控制一路 all:一次控制所有路
     * which 表示要控制哪一路  1-4
     * switch 0:关  1:开
     *
     */
    private void getDengDevice(String opt,int which,int switch_s ,String lodingTex)
    {
        if(lamp_Device == null){
            Toast.makeText (MainActivity.this, "没有找到反馈开关，请重新登录再试", Toast.LENGTH_LONG).show ();
            return;
        }
        loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "ctrlDeviceRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
            js_request.put("md5", lamp_Device.getMd5 ());
            js_request.put("sub_id", lamp_Device.getSub_id ());
            js_request.put("type", "fb");

            JSONObject js_data = new JSONObject();
            js_data.put ("opt",opt);
            js_data.put ("which",which);
            js_data.put ("switch",switch_s);
            js_request.put("data", js_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println ("js_request=="+js_request);
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
                LoginSession (s);
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
     * 空调
     * @param attr
     * @param value
     * @param lodingTex
     */
    private void getAirDevice(String attr,int value,String lodingTex)
    {
        if(air_Device == null){
            Toast.makeText (MainActivity.this, "没有找到空调设备，请重新登录再试", Toast.LENGTH_LONG).show ();
            return;
        }
        loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "ctrlDeviceRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
            js_request.put("md5", air_Device.getMd5 ());
            js_request.put("sub_id", air_Device.getSub_id ());
            js_request.put("type", "thermostats");

            JSONObject js_data = new JSONObject();
            js_data.put ("attr",attr);
            js_data.put ("value",value);
            JSONArray array_data = new JSONArray();
            array_data.put (js_data);
            js_request.put ("data",array_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println ("js_request=="+js_request);
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
                LoginSession (s);

                Gson gson = new Gson();
                AirResponse response = gson.fromJson(s, AirResponse.class);
                if("ok".equals (response.getAck ())) {   //成功
                    List<AirResponse.AirInfo> datas = response.getData ();
                    for (AirResponse.AirInfo data:datas) {
                        String attr = data.getAttr ();
                        String value = data.getValue ();
                        if("speed".equals (attr)){  //当前风速
                            //显示风速相关信息
                            if("1".equals (value)){
                                speed = 0;
                            }else if("2".equals (value)){
                                speed = 150;
                            }else{
                                speed = 300;
                            }
                            weighing.setPercentData(speed,new DecelerateInterpolator ());
                            SpApplyTools.putInt (SpApplyTools.HX_AIR_SPEED,speed);
                        }else if("temp".equals (attr)){  //当前温度
                            if(!TextUtils.isEmpty (value)) {
                                int wd = Integer.parseInt (value);
                                customVolume.setProcess (wd - 16);
                                temperature_txt.setText (wd + "℃");
                                SpApplyTools.putInt (SpApplyTools.HX_AIR_TEMPERATURE,wd);  //保存当前温度
                            }
                        }

                    }
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
     * 获取家庭下面所有的情景模式
     * @param lodingTex
     */
    private void getMacro(String lodingTex)
    {
//        if(lamp_Device == null){
//            Toast.makeText (MainActivity.this, "没有找到反馈开关，请重新登录再试", Toast.LENGTH_LONG).show ();
//            return;
//        }
        loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getMacroRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println ("js_request=="+js_request);
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
                LoginSession (s);

                Gson gson = new Gson();
                MacroResponse response = gson.fromJson(s, MacroResponse.class);
                if("ok".equals (response.getAck ())) {   //成功
                    List<MacroResponse.MacroInfo> datas = response.getMacros ();
                    if(datas!=null && datas.size ()>0) {
                        for (int i = 0; i <datas.size () ; i++) {
                            MacroResponse.MacroInfo  macroInfo = datas.get (i);
                            if(i == 0){
                                but_mode1.setTag (macroInfo.getMacro_id ());
                                but_mode1.setText (macroInfo.getName ());
                            }else if(i == 1){
                                but_mode2.setTag (macroInfo.getMacro_id ());
                                but_mode2.setText (macroInfo.getName ());
                            }else if(i == 2){
                                but_mode3.setTag (macroInfo.getMacro_id ());
                                but_mode3.setText (macroInfo.getName ());
                            }
                        }
                    }
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

    private void getCtrlMacro(String macro_id,String lodingTex)
    {
        loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
        loadingDialog.show ();

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "ctrlMacroRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
            js_request.put("center_id", home_create_id);
            js_request.put("macro_id", macro_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println ("js_request=="+js_request);
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
                LoginSession (s);
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
     * 重新登录
     * @param s
     */
    private void LoginSession(String s) {
        Gson gson = new Gson();
        BaseResponse response = gson.fromJson(s, BaseResponse.class);
        if("error".equals (response.getAck ())) {  //说明接口报错了
            if("wrong session".equals (response.getMsg ())){  //session 被占用了  重新登录
                String phone =  SpApplyTools.getString (SpApplyTools.HX_PHONE,"");
                String pwd = SpApplyTools.getString (SpApplyTools.HX_PWD,"");

                if(!TextUtils.isEmpty (phone) && !TextUtils.isEmpty (pwd)){
                    getLogin(phone,pwd);
                }else {
                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                    startActivity (intent);
                    finish ();
                }
            }
        }
    }

    /**
     * 登录接口
     * @param phone
     * @param pwd
     */
    private  void getLogin(final String phone,final String pwd)
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
                    session = response.getSession ();
                    SpApplyTools.putString (SpApplyTools.HX_SESSION,session);
                    onClickBtn (viewId);
                }else {
                    Toast.makeText (MainActivity.this, UnicodeUtil.decode (response.getMsg ()), Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {
                Toast.makeText (MainActivity.this, str, Toast.LENGTH_LONG).show ();
                if (loadingDialog != null && loadingDialog.isShowing ()) {
                    loadingDialog.dismiss ();
                }
            }
        });
    }

    /**
     * 获取当前家庭所有设备
     */
    private void getDevice()
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"获取session失败",Toast.LENGTH_LONG).show ();
            return;
        }

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

                LoginSession (s);

                System.out.println("success=="+s);
                Gson gson = new Gson();
                DeviceResponse response = gson.fromJson(s, DeviceResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    SpApplyTools.putString (SpApplyTools.HX_ALL_DEVICE,s);
                    initDevice();
                }else {
                    Toast.makeText (MainActivity.this, UnicodeUtil.decode (response.getMsg ()), Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {

            }
        });

    }
}
