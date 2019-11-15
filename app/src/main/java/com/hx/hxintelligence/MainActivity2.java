package com.hx.hxintelligence;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.hx.hxintelligence.response.DeviceResponse;
import com.hx.hxintelligence.response.HomeResponse;
import com.hx.hxintelligence.response.RoomResponse;
import com.hx.hxintelligence.response.SessionResponse;
import com.hx.hxintelligence.utils.HttpUtil;
import com.hx.hxintelligence.utils.ResultCallback;
import com.hx.hxintelligence.utils.UnicodeUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ContentView(R.layout.activity_main2)
public class MainActivity2 extends FragmentActivity {

    @ViewInject(R.id.txt_1)
    private TextView txt_1;

    @ViewInject(R.id.txt_2)
    private TextView txt_2;

    @ViewInject(R.id.txt_3)
    private TextView txt_3;

    @ViewInject(R.id.txt_4)
    private TextView txt_4;

    private String session;
    private String  home_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view ().inject (this);

    }

    @Event(value = {R.id.btn_1,R.id.btn_2,R.id.btn_3,R.id.btn_4}, type = View.OnClickListener.class)
    private void relativeClick(View view) {
        switch (view.getId ()) {
            case R.id.btn_1:
//                Toast.makeText (this,"我要请求网络",Toast.LENGTH_LONG).show ();
//                getDetailImages("70","");
                getTestData();
                break;
            case R.id.btn_2:
                getTestData2();
                break;
            case R.id.btn_3:
                getTestData3();
                break;
            case R.id.btn_4:
                getTestData4();
                break;
        }
    }

    public  void getTestData()
    {
        int random = new Random ().nextInt(65536);
        String time = getTimeStame();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getSessionRequest");//根据实际需求添加相应键值对
            js_request.put("username", "15217466339");
            js_request.put("password", "123456");
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
                    txt_1.setText ("session:" + response.getSession ());
                }else {
                    Toast.makeText (MainActivity2.this,UnicodeUtil.decode (response.getMsg()),Toast.LENGTH_LONG).show ();
                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
            }
        });

    }

    public  void getTestData2()
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"请先登录获取session",Toast.LENGTH_LONG).show ();
            return;
        }


        int random = new Random ().nextInt(65536);
        String time = getTimeStame();

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

                Gson gson = new Gson();
                HomeResponse response = gson.fromJson(s, HomeResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    List<HomeResponse.HomeBean> homes = response.getHomes ();
                    StringBuffer stringBuffer = new StringBuffer ();
                    for (HomeResponse.HomeBean home:homes) {
                        stringBuffer.append (home.getHome_id ());
                        stringBuffer.append (":");
                        stringBuffer.append (UnicodeUtil.decode (home.getName ()));
                        stringBuffer.append ("   ");


                    }

                    home_id = homes.get (0).getHome_id ();
                    txt_2.setText (stringBuffer.toString ());
                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
            }
        });

    }

    public  void getTestData3()
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"请先登录获取session",Toast.LENGTH_LONG).show ();
            return;
        }
        if(TextUtils.isEmpty (home_id)){
            Toast.makeText (this,"请先获取家庭ID",Toast.LENGTH_LONG).show ();
            return;
        }

        int random = new Random ().nextInt(65536);
        String time = getTimeStame();

        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("method", "getRoomRequest");//根据实际需求添加相应键值对
            js_request.put("home_id", home_id);
            js_request.put("seq", time+random);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.hxpost (js_request.toString (),session,random,time, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
                txt_3.setText (s);

                Gson gson = new Gson();
                RoomResponse response = gson.fromJson(s, RoomResponse.class);

                if("ok".equals (response.getAck ())) {   //成功
                    List<RoomResponse.RoomBean> rooms = response.getRooms ();
                    StringBuffer stringBuffer = new StringBuffer ();
                    for (RoomResponse.RoomBean room:rooms) {
                        stringBuffer.append (room.getRoom_id ());
                        stringBuffer.append (":");
                        stringBuffer.append (UnicodeUtil.decode (room.getName ()));
                        stringBuffer.append ("   ");
                    }

                    txt_3.setText (stringBuffer.toString ());
                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
            }
        });

    }

    public  void getTestData4()
    {
        if(TextUtils.isEmpty (session)){
            Toast.makeText (this,"请先登录获取session",Toast.LENGTH_LONG).show ();
            return;
        }
        if(TextUtils.isEmpty (home_id)){
            Toast.makeText (this,"请先获取家庭ID",Toast.LENGTH_LONG).show ();
            return;
        }

        int random = new Random ().nextInt(65536);
        String time = getTimeStame();

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
                txt_4.setText (s);

                Gson gson = new Gson();
                DeviceResponse response = gson.fromJson(s, DeviceResponse.class);

                if("ok".equals (response.getAck ())) {   //成功

                }
            }

            @Override
            public void fail(String str) {
                System.out.println("str=="+str);
            }
        });

    }

    /**
     * 获取当前的时间戳
     * @return
     */
    public String getTimeStame() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis()/1000;
        //将毫秒值转换为String类型数据
        String time_stamp = String.valueOf(time);
        //返回出去
        return time_stamp;
    }

    public  void getDetailImages(final String typeId, String server)
    {
        String serverStri;
        if(!TextUtils.isEmpty (server)){
            serverStri = server;
        }else {
            serverStri = "http://app.woosyun.com";
        }
        System.out.println("typeId=="+typeId);
        if(TextUtils.isEmpty(typeId)) return;
        String url = serverStri+ "/api/ppt/picture";
        System.out.println("url=="+url);
        Map paramMap = new HashMap ();
        paramMap.put("id", typeId);
        HttpUtil.post(url,paramMap, new ResultCallback<String> () {
            @Override
            public void success(String s) {
                System.out.println("success=="+s);
//                txt_context.setText (s);
            }

            @Override
            public void fail(String str) {

//                AlertDialog.Builder ab = new AlertDialog.Builder(PPTMainActivity.this);
//                ab.setTitle("出错了");
//                ab.setMessage("找不到服务器，请联系管理员");
//                ab.show();
            }
        });

    }
}
