package com.hx.hxintelligence.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hx.hxintelligence.BaseActivity;
import com.hx.hxintelligence.R;
import com.hx.hxintelligence.response.AirResponse;
import com.hx.hxintelligence.response.BaseResponse;
import com.hx.hxintelligence.response.DengResponse;
import com.hx.hxintelligence.response.DeviceResponse;
import com.hx.hxintelligence.response.MacroResponse;
import com.hx.hxintelligence.response.QRcodeResponse;
import com.hx.hxintelligence.response.SessionResponse;
import com.hx.hxintelligence.utils.HttpUtil;
import com.hx.hxintelligence.utils.ImageUtil;
import com.hx.hxintelligence.utils.KkUtil;
import com.hx.hxintelligence.utils.ResultCallback;
import com.hx.hxintelligence.utils.SpApplyTools;
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

import java.util.ArrayList;
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
    @ViewInject(R.id.linear_qr)
    private LinearLayout linear_qr;  //客控二维码

    @ViewInject(R.id.deng_all_linear)
    private LinearLayout deng_all_linear;   //灯光布局
    @ViewInject(R.id.mode_all_linear)
    private LinearLayout mode_all_linear;   //模式布局
    @ViewInject(R.id.but_mode)
    private Button but_mode;

    @ViewInject(R.id.qr_img)   //二维码图片
    private ImageView qr_img;

//    @ViewInject(R.id.but_mode1)
//    private Button but_mode1;
//    @ViewInject(R.id.but_mode2)
//    private Button but_mode2;
//    @ViewInject(R.id.but_mode3)
//    private Button but_mode3;

    private int viewId = -1;
    private final static int DENG_ON = 2001;    //灯光打开
    private final static int DENG_OFF = 2002;   //灯光关闭
    private final static int MACRO_INFO = 2003;   //情景模式

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
//    private DeviceResponse.DeviceBean lamp_Device;  //卧室开关
//    private DeviceResponse.DeviceBean lamp_second_Device;  //进门灯光

    private DeviceResponse.DeviceBean air_Device;  //空调开关

    private List<DeviceResponse.DeviceBean> deviceBeans = new ArrayList<> ();   //所有的灯光
    List<DengResponse> listDengs = new ArrayList<> ();   //所有的灯光个数列表

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
        getQRcode();   //获取二维码

        getMacro("正在获取所有模式，请稍后...",false);  //获取所有情景模式

        String allMacro = SpApplyTools.getString (SpApplyTools.HX_ALL_MACRO,"");
        if(!TextUtils.isEmpty (allMacro)) {
            initMacro (allMacro);
        }

//        String imgRes =  SpApplyTools.getString (SpApplyTools.HX_QR_IMAGE,"");
//        if(!TextUtils.isEmpty (imgRes)){
//            Bitmap bitmap = base64ToBitmap(imgRes);
//            System.out.println ("bitmap=="+bitmap);
//            if(bitmap!=null) {
//                SpApplyTools.putString (SpApplyTools.HX_QR_IMAGE,imgRes);
//                linear_qr.setVisibility (View.VISIBLE);
//                qr_img.setImageBitmap (bitmap);
//            }
//        }
    }

    private void initDevice() {
        String allDevice = SpApplyTools.getString (SpApplyTools.HX_ALL_DEVICE,"");
        Gson gson = new Gson();
        DeviceResponse response = gson.fromJson(allDevice, DeviceResponse.class);
        if(response!=null){
            List<DeviceResponse.DeviceBean> devices = response.getDevices ();
            if(devices!=null){
                deviceBeans.clear ();
                listDengs.clear ();
                for (DeviceResponse.DeviceBean device:devices) {
                    if(device.getSub_type () == 97){   //反馈窗帘
                        window_Device = device;
                    }else if(device.getSub_type () == 57 || device.getSub_type () == 51){  //轻触开关 3路   反馈开关
                        deviceBeans.add (device);
//                        if("0".equals (device.getIn_room_order ())) {   //设备在房间中排列的序号
//                            lamp_Device = device;
//                        }else{
//                            lamp_second_Device = device;
//                        }
                    }else if(device.getSub_type () == 145){  //温控面板
                        air_Device = device;
                    }
                }
                addDeng (deviceBeans);
            }
        }
    }

    int speed = 0;
    @Event(value = {R.id.but_off_lamp,R.id.but_on_lamp,R.id.but_stop_window,R.id.but_off_window,R.id.but_on_window,R.id.but_speed_air,R.id.but_temperature_add,R.id.but_temperature_reduce,
            R.id.but_off_air,R.id.but_on_air, R.id.but_air_refrigeration,R.id.but_air_heat, R.id.but_mode/*,R.id.but_mode1,R.id.but_mode2,R.id.but_mode3*/}, type = View.OnClickListener.class)
    private void relativeClick(View view) {
        viewId = view.getId ();
        onClickBtn (viewId);
    }

    private void onClickBtn(int viewId) {
        System.out.println ("viewId=="+viewId);
        switch (viewId) {
            //灯光相关
            case R.id.but_off_lamp:   //全开
                if(deviceBeans!=null && deviceBeans.size ()>0){
                    for (int i = 0; i < deviceBeans.size (); i++) {
                        DeviceResponse.DeviceBean deviceBean = deviceBeans.get (i);
                        getDengDevice (deviceBean.getMd5 (),deviceBean.getSub_id (),"all",0,1,"正在打开全部灯光，请稍后...",i == 0 ? true:false);
                    }
                }
                break;
            case DENG_ON:   //灯光打开
                getDengDevice (dengInfo.getMd5 (),dengInfo.getSub_id (),"one",dengInfo.getRoad (),1,"正在打开"+dengInfo.getName ()+"，请稍后...",true);
                break;
            case DENG_OFF:  //灯光关闭
                getDengDevice (dengInfo.getMd5 (),dengInfo.getSub_id (),"one",dengInfo.getRoad (),0,"正在关闭"+dengInfo.getName ()+"，请稍后...",true);
                break;
            case R.id.but_on_lamp:  //全关
                if(deviceBeans!=null && deviceBeans.size ()>0){
                    for (int i = 0; i < deviceBeans.size (); i++) {
                        DeviceResponse.DeviceBean deviceBean = deviceBeans.get (i);
                        getDengDevice (deviceBean.getMd5 (),deviceBean.getSub_id (),"all",0,0,"正在关闭全部灯光，请稍后...",i == 0 ? true:false);
                    }
                }
                break;
            //情景模式相关
            case  R.id.but_mode:
                getMacro("正在获取所有模式，请稍后...",true);
                break;
            case MACRO_INFO:  //情景模式开关
                getCtrlMacro (macro_id, "请稍后...");
                break;
//            case  R.id.but_mode1:
//                Object id = but_mode1.getTag ();
//                if(id!=null) {
//                    getCtrlMacro (id.toString (), "请稍后...");
//                }
//                break;
//            case  R.id.but_mode2:
//                id = but_mode2.getTag ();
//                if(id!=null) {
//                    getCtrlMacro (id.toString (), "请稍后...");
//                }
//                break;
//            case  R.id.but_mode3:
//                id = but_mode3.getTag ();
//                if(id!=null) {
//                    getCtrlMacro (id.toString (), "请稍后...");
//                }
//                break;
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
    private void getDengDevice(String Md5,int sub_id,String opt,int which,int switch_s ,String lodingTex,boolean bl)
    {
        if(Md5 == null ){
            return;
        }
        if(bl) {
            loadingDialog = new LoadingDialog (MainActivity.this, lodingTex, R.mipmap.ic_dialog_loading);
            loadingDialog.show ();
        }

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "ctrlDeviceRequest");//根据实际需求添加相应键值对
            js_request.put("seq", time+random);
            js_request.put("home_id", home_id);
            js_request.put("md5", Md5);
            js_request.put("sub_id", sub_id);
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
    private void getMacro(String lodingTex,boolean bl)
    {

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
                LoginSession (s);

                initMacro (s);
            }

            @Override
            public void fail(String str) {

            }
        });
    }

    private void initMacro(String s) {
        Gson gson = new Gson();
        MacroResponse response = gson.fromJson(s, MacroResponse.class);
        if("ok".equals (response.getAck ())) {   //成功
            SpApplyTools.putString (SpApplyTools.HX_ALL_MACRO,s);

            List<MacroResponse.MacroInfo> datas = response.getMacros ();
            if(datas!=null && datas.size ()>0) {
                mode_all_linear.removeAllViews ();
                for (int i = 0; i <datas.size () ; i++) {
                    MacroResponse.MacroInfo  macroInfo = datas.get (i);
                    addButton(macroInfo,i,datas.size ());
                }
            }
        }
    }

    private Button firstBtn;
    private String macro_id;
    private void addButton(MacroResponse.MacroInfo  macroInfo,int i,int maxleng){
        Button btnAdd = new Button(MainActivity.this);
        LinearLayout.LayoutParams btnAddParam = new LinearLayout.LayoutParams(
                getResources ().getDimensionPixelSize(R.dimen.w_300),
                ViewGroup.LayoutParams.MATCH_PARENT);
        btnAdd.setLayoutParams(btnAddParam);
        // 靠右放置
        btnAddParam.setMargins (getResources ().getDimensionPixelSize(R.dimen.w_30),getResources ().getDimensionPixelSize(R.dimen.h_20),getResources ().getDimensionPixelSize(R.dimen.w_30),0);
        // 设置属性
        btnAdd.setBackgroundResource(R.drawable.selecor_btn_kk);
        btnAdd.setText (macroInfo.getName ());
        btnAdd.setTextColor (getResources ().getColor (R.color.white));
        btnAdd.setTextSize (18);
        btnAdd.setFocusable (true);
        btnAdd.setId (10101010+i);
        btnAdd.setNextFocusUpId (R.id.but_mode);
        btnAdd.setTag (macroInfo.getMacro_id ());
//        btnAdd.setId();
        // 设置点击操作
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Object id = v.getTag ();
                if(id!=null) {
                    viewId = MACRO_INFO;
                    macro_id = id.toString();
                    getCtrlMacro (id.toString (), "请稍后...");
                }
            }
        });
        mode_all_linear.addView(btnAdd);

        if(i == 0){
            but_mode.setNextFocusDownId (10101010);
            firstBtn = btnAdd;
        }

        if(i == maxleng-1){
            firstBtn.setNextFocusLeftId (10101010+i);
            btnAdd.setNextFocusRightId (10101010);
        }
    }

    private DengResponse dengInfo = new DengResponse ();
    public void addDeng(List<DeviceResponse.DeviceBean> deviceBeans){
        deng_all_linear.removeAllViews ();
        if(deviceBeans!=null && deviceBeans.size ()>0) {
            for (int i = 0; i < deviceBeans.size (); i++) {
                DeviceResponse.DeviceBean deviceBean = deviceBeans.get (i);
                List<DeviceResponse.DeviceBean.FbsInfo> fbs = deviceBeans.get (i).getFbs ();
                System.out.println ("fbs=="+fbs);
                if(fbs!=null && fbs.size ()>0) {
                    System.out.println ("fbs.size ()=="+fbs.size ());
                    for (int j = 0; j < fbs.size (); j++) {
                        DeviceResponse.DeviceBean.FbsInfo fbsInfo = fbs.get (j);
                        DengResponse dengResponse = new DengResponse ();
                        dengResponse.setMd5 (deviceBean.getMd5 ());
                        dengResponse.setSub_id (deviceBean.getSub_id ());
                        if(fbsInfo!=null) {
                            dengResponse.setName (fbsInfo.getName ());
                            dengResponse.setRoad (fbsInfo.getRoad ());
                        }
                        listDengs.add (dengResponse);
                    }
                }
            }
        }
        if(listDengs!=null && listDengs.size ()>0){
            for (int i = 0; i < listDengs.size (); i++) {
                final DengResponse dengResponse = listDengs.get (i);
                RelativeLayout relativeLayout =  (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.lamp_info, null);
                TextView deng_name_txt = relativeLayout.findViewById (R.id.deng_name_txt);
                deng_name_txt.setText (dengResponse.getName ());
                deng_all_linear.addView (relativeLayout);

                Button but_on_deng = relativeLayout.findViewById (R.id.but_on_deng);
                but_on_deng.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        dengInfo.setName (dengResponse.getName());
                        dengInfo.setSub_id (dengResponse.getSub_id());
                        dengInfo.setMd5 (dengResponse.getMd5());
                        dengInfo.setRoad (dengResponse.getRoad());
                        viewId = DENG_ON;
                        getDengDevice (dengResponse.getMd5 (),dengResponse.getSub_id (),"one",dengResponse.getRoad (),1,"正在打开"+dengResponse.getName ()+"，请稍后...",true);
                    }
                });

                Button but_off_deng = relativeLayout.findViewById (R.id.but_off_deng);
                but_off_deng.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        dengInfo.setName (dengResponse.getName());
                        dengInfo.setSub_id (dengResponse.getSub_id());
                        dengInfo.setMd5 (dengResponse.getMd5());
                        dengInfo.setRoad (dengResponse.getRoad());
                        viewId = DENG_OFF;
                        getDengDevice (dengResponse.getMd5 (),dengResponse.getSub_id (),"one",dengResponse.getRoad (),0,"正在关闭"+dengResponse.getName ()+"，请稍后...",true);
                    }
                });
            }
        }
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

                System.out.println("success=="+s);
                Gson gson = new Gson();
                SessionResponse response = gson.fromJson(s, SessionResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    session = response.getSession ();
                    SpApplyTools.putString (SpApplyTools.HX_SESSION,session);
                    onClickBtn (viewId);
                    getQRcode();
                }
            }

            @Override
            public void fail(String str) {
//                Toast.makeText (MainActivity.this, str, Toast.LENGTH_LONG).show ();
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
                }else{
                    if("no devices".equals (response.getMsg ())) {
                        Toast.makeText (MainActivity.this, "当前房间没有找到设备", Toast.LENGTH_LONG).show ();
                    }
                }
            }

            @Override
            public void fail(String str) {

            }
        });

    }

    /**
     * 获取酒店当前房间的二维码
     */
    private void getQRcode()
    {

        int random = KkUtil.getRandom();
        String time = KkUtil.getTimeStame ();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getQRcode");//根据实际需求添加相应键值对
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
//                LoginSession (s);
                Gson gson = new Gson();
                QRcodeResponse response = gson.fromJson(s, QRcodeResponse.class);
                if("ok".equals (response.getAck ())) {   //成功
                    Bitmap bitmap = ImageUtil.base64ToBitmap(response.getRes ());
                    if(bitmap!=null) {
//                        SpApplyTools.putString (SpApplyTools.HX_QR_IMAGE,response.getRes ());
                        linear_qr.setVisibility (View.VISIBLE);
                        qr_img.setImageBitmap (bitmap);
                    }
                }


            }

            @Override
            public void fail(String str) {

            }
        });
    }



}
