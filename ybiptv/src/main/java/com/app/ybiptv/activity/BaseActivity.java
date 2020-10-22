package com.app.ybiptv.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Activity基类
 */
public class BaseActivity extends FragmentActivity {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private IptvApplication application;
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout (context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundResource(R.drawable.main_bg);
        application = IptvApplication.getApplication ();

    }

    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public void showToast(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    private String KEYCODE_any = "";
    @SuppressLint("RestrictedApi")
    public boolean dispatchKeyEvent(KeyEvent event) {
//        System.out.println ("event.getAction ()==" + event.getAction ());
        if (event.getAction () == 1) {
//            System.out.println ("event.getKeyCode ()==" + event.getKeyCode ());
            //这里实现频道切换

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
                    this.KEYCODE_any = "";
                    break;
            }

            String pwd = "ccccc";
            if (this.KEYCODE_any.endsWith (pwd)) {
                this.KEYCODE_any = "";
                Intent i = new Intent (this, SettingActivity.class);
                startActivity (i);
                return true;
            }

        }
        return super.dispatchKeyEvent (event);
    }

    public String getMac(){
        return application.getManAddr ();
    }
}
