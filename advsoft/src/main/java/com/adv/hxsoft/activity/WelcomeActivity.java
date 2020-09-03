package com.adv.hxsoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.adv.hxsoft.R;
import com.adv.hxsoft.util.SpApplyTools;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends Activity  {


    private  Handler  h = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            int player = SpApplyTools.getInt (SpApplyTools.MODE_IMG_VIDEO, 1);
            Intent i;
            System.out.println ("player======"+player);
            if(player == 1){
                i  = new Intent(WelcomeActivity.this,VideoActivity.class);
            }else if(player == 2){
                i  = new Intent(WelcomeActivity.this,ImgVideoActivity.class);
            }else{
                i  = new Intent(WelcomeActivity.this, ImageActivity.class);
            }
            startActivity(i);
            finish();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        h.sendEmptyMessageDelayed(200,1000);
    }
}
