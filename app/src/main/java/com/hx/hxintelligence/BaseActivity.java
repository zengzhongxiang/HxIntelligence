package com.hx.hxintelligence;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hx.hxintelligence.activity.LoginActivity;

public class BaseActivity extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        strBuffer = new StringBuffer();
//        strBuffer.setLength(0);
    }

    private String KEYCODE_any = "";

    @SuppressLint("RestrictedApi")
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction () == 1) {
            System.out.println ("event.getKeyCode ()==" + event.getKeyCode ());
            //这里实现频道切换
//            boolean bl = handlerMultKey(event.getKeyCode ());
//            if(bl){
//                return true;
//            }

            switch (event.getKeyCode ()) {
                case 19:
                    this.KEYCODE_any += "u";
                    break;
                case 20:
                    this.KEYCODE_any += "d";
                    break;
                case 21:
                    this.KEYCODE_any += "l";
                    break;
                case 22:
                    this.KEYCODE_any += "r";
                    break;
                case 82:
                    this.KEYCODE_any += "c";
                    break;
                case 23:
                case 66:
                    //this.KEYCODE_any += "o";
//                    String pwd = SpApplyTools.getString("SettingPassword", "uuuuu");
//                    if (this.KEYCODE_any.endsWith(pwd)) {
//                        this.KEYCODE_any = "";
//                        Intent i = new Intent(this, SettingActivity.class);
//                        startActivity(i);
//                        return true;
//                    }
                    this.KEYCODE_any = "";
                    break;
            }

            String pwd = "ccccc";//SpApplyTools.getString ("SettingPassword", "uudd");
            if (this.KEYCODE_any.endsWith (pwd)) {
                this.KEYCODE_any = "";
                Intent i = new Intent (this, LoginActivity.class);
                startActivity (i);
                return true;
            }

        }
        return super.dispatchKeyEvent (event);
    }

//    private StringBuffer strBuffer;
//    private boolean handlerMultKey(int keyCode) {
//        boolean bl = false;
//        if (keyCode >= KeyEvent.KEYCODE_0
//                && keyCode <= KeyEvent.KEYCODE_9 ) {
//            bl =true;
//            int iDigital = keyCode - KeyEvent.KEYCODE_0;
//            strBuffer.append(String.valueOf(iDigital));
//
//            handler.sendEmptyMessageDelayed(100, 500);
//        }
//        return bl;
//    }
//
//    public Handler handler = new Handler () {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 100:
//                   if(strBuffer.toString ().length ()>0) {
//                       String keyCode = strBuffer.toString ();
//                       for (int i = 0; i < ChannelConstants.channels.length; i++) {
//                           if(keyCode.equals (String.valueOf(ChannelConstants.channels[i][0]))){
//                               System.out.println ("strBuffer.toString ()==" + strBuffer.toString ()+"    channels[i][0]"+ChannelConstants.channels[i][0]+"    channels[i][1]"+ChannelConstants.channels[i][1]);
//                               Toast.makeText (BaseActivity.this, String.valueOf(ChannelConstants.channels[i][1]), Toast.LENGTH_LONG).show ();
//                               break;
//                           }
//                       }
//                       strBuffer.setLength (0);
//                   }
//                    break;
//            }
//        }
//
//    };
}
