package com.hx.hxintelligence;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hx.hxintelligence.activity.LoginActivity;

public class BaseActivity extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String KEYCODE_any = "";
    public boolean dispatchKeyEvent(KeyEvent event) {
//        System.out.println ("event.getAction ()=="+event.getAction ());
//        System.out.println ("event.getKeyCode ()=="+event.getKeyCode ());

        if (event.getAction () == 1) {
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

            String pwd = "ududud";//SpApplyTools.getString ("SettingPassword", "uudd");
            if (this.KEYCODE_any.endsWith (pwd)) {
                this.KEYCODE_any = "";
                    Intent i = new Intent (this, LoginActivity.class);
                    startActivity (i);
                    return true;
            }

        }
        return super.dispatchKeyEvent (event);
    }

}
