package com.app.ybiptv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.app.ybiptv.R;
import com.app.ybiptv.fragment.MoviceMainFragment;
import com.app.ybiptv.fragment.SearchFragment;

/**
 * 分类
 *
 */
public class ThemeMoviceActivity extends BaseActivity {

    private MoviceMainFragment themeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_movice);
        themeFragment = new MoviceMainFragment();
        initView();
    }

    private void initView() {
        if(!themeFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().replace(R.id.contentlay, themeFragment).commit();
        }
    }
}
