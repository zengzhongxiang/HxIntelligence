package com.hxsoft.laucher.activity.foot;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsoft.laucher.R;
import com.hxsoft.laucher.activity.BaseActivity;
import com.hxsoft.laucher.activity.activitycommon.DetailActivity;
import com.hxsoft.laucher.utils.ApkDefault;
import com.hxsoft.laucher.utils.ApkUtils;
import com.hxsoft.laucher.utils.DataUtil;
import com.hxsoft.laucher.utils.FileUtils;
import com.hxsoft.laucher.utils.JmgoUtil;
import com.hxsoft.laucher.utils.SdCardUtil;
import com.hxsoft.laucher.utils.WeatherUtil;
import com.hxsoft.laucher.view.MarqueeTextviewNofocus;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.RelativeMainLayout;
import com.open.androidtvwidget.view.SmoothHorizontalScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_zy_main)
public class FootBathMainActivity extends BaseActivity {
    MainUpView mainUpView1;
    OpenEffectBridge mOpenEffectBridge;
    View mOldFocus; // 4.3以下版本需要自己保存.

    @ViewInject(R.id.message_txt)
    private MarqueeTextviewNofocus message_txt;  //消息
    @ViewInject(R.id.message_txt2)
    private MarqueeTextviewNofocus message_txt2;  //消息
    @ViewInject(R.id.time_txt)
    private TextView time_txt;  //时间
    @ViewInject(R.id.date_txt)
    private TextView date_txt;  //日期
    @ViewInject(R.id.week_txt)
    private TextView week_txt;  //星期
    @ViewInject(R.id.temperature_txt)
    private TextView temperature_txt;  //气温
    @ViewInject(R.id.region_txt)
    private TextView region_txt;  //地区
    @ViewInject(R.id.tq_img)
    private ImageView tq_img;  //星期

    private Timer timer;
    private TimerTask timerTask;
    private Timer timer2;
    private TimerTask timerTask2;
    private List<String> apksList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view().inject(this);
        startTimer();
        SmoothHorizontalScrollView hscroll_view = (SmoothHorizontalScrollView) findViewById(R.id.hscroll_view);
        hscroll_view.setFadingEdge((int)getDimension(R.dimen.w_100)); // 滚动窗口也需要适配.
        //
        /* MainUpView 设置. */
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        // mainUpView1 = new MainUpView(this); // 手动添加(test)
        // mainUpView1.attach2Window(this); // 手动添加(test)
        mOpenEffectBridge = (OpenEffectBridge) mainUpView1.getEffectBridge();
        // 4.2 绘制有问题，所以不使用绘制边框.
        // 也不支持倒影效果，绘制有问题.
        // 请大家不要按照我这样写.
        // 如果你不想放大小人超出边框(demo，张靓颖的小人)，可以不使用OpenEffectBridge.
        // 我只是测试----DEMO.(建议大家使用 NoDrawBridge)
        if (Utils.getSDKVersion() == 17) { // 测试 android 4.2版本.
            switchNoDrawBridgeVersion();
        } else { // 其它版本（android 4.3以上）.
            mainUpView1.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片.
            mainUpView1.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影.
        }
        // mainUpView1.setUpRectResource(R.drawable.item_highlight); //
        // 设置移动边框的图片.(test)
        // mainUpView1.setDrawUpRectPadding(new Rect(0, 0, 0, -26)); //
        // 设置移动边框的距离.
        // mainUpView1.setDrawShadowPadding(0); // 阴影图片设置距离.
        // mOpenEffectBridge.setTranDurAnimTime(500); // 动画时间.

        RelativeMainLayout main_lay11 = (RelativeMainLayout) findViewById(R.id.main_lay);
        main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener () {
            @Override
            public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                if (newFocus != null)
                    newFocus.bringToFront(); // 防止放大的view被压在下面. (建议使用MainLayout)
                float scale = 1.1f;
                mainUpView1.setFocusView(newFocus, mOldFocus, scale);
                mOldFocus = newFocus; // 4.3以下需要自己保存.
                // 测试是否让边框绘制在下面，还是上面. (建议不要使用此函数)
                if (newFocus != null) {
                    testTopDemo(newFocus, scale);
                }
            }
        });


        /**
         * 尽量不要使用鼠标. !!!! 如果使用鼠标，自己要处理好焦点问题.(警告)
         */
//		main_lay11.setOnHoverListener(new OnHoverListener() {
//			@Override
//			public boolean onHover(View v, MotionEvent event) {
//				mainUpView1.setVisibility(View.INVISIBLE);
//				return true;
//			}
//		});
        //
        for (int i = 0; i < main_lay11.getChildCount(); i++) {
            main_lay11.getChildAt(i).setOnTouchListener(new View.OnTouchListener () {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
//						v.performClick();
                        v.requestFocus();
                    }
                    return false;
                }
            });
        }
        initData();
    }

    private void initData(){
//        message_txt.setText (welcomeWord);
        // 初始化
        message_txt.init (getWindowManager ());
        // 设置滚动方向
        message_txt.setScrollDirection (MarqueeTextviewNofocus.RIGHT_TO_LEFT);
        // 设置滚动速度
        message_txt.setScrollMode (MarqueeTextviewNofocus.SCROLL_FAST);

        message_txt2.init (getWindowManager ());
        // 设置滚动方向
        message_txt2.setScrollDirection (MarqueeTextviewNofocus.RIGHT_TO_LEFT);
        // 设置滚动速度
        message_txt2.setScrollMode (MarqueeTextviewNofocus.SCROLL_FAST);

        time_txt.setText(DataUtil.getTime());
        date_txt.setText(DataUtil.getData());
        week_txt.setText(DataUtil.getWeek());

        timer.schedule(timerTask,10000,3600000);//延时0s，每隔一小时执行一次run方法
        timer2.schedule(timerTask2,0,60000);//延时0s，每隔一分钟执行一次run方法

//        UdiskUtil udiskUtil = new UdiskUtil ();
//        udiskUtil.setBootLogo();

        FileUtils.getInstance(this).copyAssetsToSD("apks", SdCardUtil.getSDCardPath ().getPath ()+"/woss/apk").setFileOperateCallback(new FileUtils.FileOperateCallback() {
            @Override
            public void onSuccess() {
                // TODO: 文件复制成功时，主线程回调
                System.out.println ("文件复制成功");
                apksList = ApkDefault.FootDefautApk ();

                for (int i = 0; i < apksList.size (); i++) {
                    if (!ApkUtils.isAvailable (FootBathMainActivity.this, apksList.get (i))) {
                        String apkUri = SdCardUtil.getSDCardPath ().getPath ()+"/woss/apk/"+apksList.get (i)+".apk";
                        System.out.println ("apkUri ==" + apkUri);
                        File file = new File (apkUri);
                        System.out.println ("file.isFile () ==" + file.isFile ());
                        if (file.isFile ()) {
                            JmgoUtil.installAPK (file);
                        }
                    }
                }
            }

            @Override
            public void onFailed(String error) {
                // TODO: 文件复制失败时，主线程回调
                System.out.println ("文件复制失败");
            }
        });

    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    @Event(value = {R.id.recyclerview_rlayt_1,R.id.recyclerview_rlayt_2,R.id.recyclerview_rlayt_3,R.id.recyclerview_rlayt_4,R.id.recyclerview_rlayt_5,R.id.recyclerview_rlayt_6,R.id.recyclerview_rlayt_7,R.id.recyclerview_rlayt_8}, type = View.OnClickListener.class)
    private void relativeClick(View view) {
        String packageName = "";
        Intent intent;
        switch (view.getId ()) {
            case R.id.recyclerview_rlayt_1:
//                packageName = "com.cibn.iptv";   //CIBN
                packageName = apksList.get (0);
                ApkUtils.insertApp (this, packageName, "");
                break;
            case R.id.recyclerview_rlayt_2:
//                packageName = "com.elinkway.tvlive2";   //网络电视
                packageName = apksList.get (1);
                ApkUtils.insertApp (this, packageName, "");
                break;
            case R.id.recyclerview_rlayt_3:
//                packageName = "com.etv.live";   //呼叫服务
//                ApkUtils.insertApp (this, packageName, "");
                Toast.makeText(this, "呼叫成功，请稍后...", Toast.LENGTH_LONG).show();
                break;
            case R.id.recyclerview_rlayt_4:
//                packageName = "com.gitvdemo.video";   //爱奇艺
                packageName = apksList.get (2);
                ApkUtils.insertApp (this, packageName, "");
                break;
            case R.id.recyclerview_rlayt_5:
                //技师风采
                intent = new Intent (this, DetailActivity.class);
                intent.putExtra ("type",1);
                startActivity (intent);
                break;
            case R.id.recyclerview_rlayt_6:
//                packageName = "com.xiami.tv";
                packageName = apksList.get (3);
                ApkUtils.insertApp (this, packageName, "");
                break;
            case R.id.recyclerview_rlayt_7:
                //会所介绍
                intent = new Intent (this,DetailActivity.class);
                intent.putExtra ("type",2);
                startActivity (intent);
                break;
            case R.id.recyclerview_rlayt_8:
                //手机投屏
//                packageName = "com.hpplay.happyplay.aw";
                packageName = apksList.get (4);
                ApkUtils.insertApp (this, packageName, "");
//                intent = new Intent (this,DetailActivity.class);
//                intent.putExtra ("type",3);
//                startActivity (intent);
                break;
        }
    }

    public void testTopDemo(View newView, float scale) {
        mOpenEffectBridge.setDrawUpRectPadding(0);
        mOpenEffectBridge.setDrawShadowPadding(0);
        mOpenEffectBridge.setDrawUpRectEnabled(true);
    }

    private void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void switchNoDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_9) * density, getDimension(R.dimen.h_9) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge ();
        effectNoDrawBridge.setTranDurAnimTime(200);
//        effectNoDrawBridge.setDrawUpRectPadding(rectf);
        mainUpView1.setEffectBridge(effectNoDrawBridge); // 4.3以下版本边框移动.
        mainUpView1.setUpRectResource(R.mipmap.white_light_10); // 设置移动边框的图片.
        mainUpView1.setDrawUpRectPadding(rectf); // 边框图片设置间距.
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                //do something
                WeatherUtil.getWeather(FootBathMainActivity.this,temperature_txt,region_txt,tq_img);
            }else if(msg.what == 2){
                time_txt.setText(DataUtil.getTime());
                date_txt.setText(DataUtil.getData());
                week_txt.setText(DataUtil.getWeek());
            }
            super.handleMessage(msg);
        }
    };

    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer2 = new Timer();
        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        };
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (timerTask2 != null) {
            timerTask2.cancel();
            timerTask2 = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        stopTimer();
    }

}
