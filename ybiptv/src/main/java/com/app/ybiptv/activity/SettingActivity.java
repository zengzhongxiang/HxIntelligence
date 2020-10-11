package com.app.ybiptv.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.app.ybiptv.R;
import com.app.ybiptv.fragment.SettingsFragment;

import butterknife.ButterKnife;

/**
 * 系统设置 主界面
 *
 */
public class SettingActivity extends BaseActivity {

    private static final String TAG_SETTINGS =  "SETTINGS";

    private SettingsFragment mSettingsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mSettingsFragment = new SettingsFragment();
        initView();
    }

    private void initView() {
        if(!mSettingsFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().replace(R.id.contentlay, mSettingsFragment).commit();
        }
    }


}
