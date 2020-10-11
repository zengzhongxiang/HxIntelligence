package com.app.ybiptv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.app.ybiptv.R;
import com.app.ybiptv.fragment.SearchFragment;

/**
 * 影视搜索
 *
 */
public class SearchMoviceActivity extends BaseActivity {

    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movice);
        mSearchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentlay, mSearchFragment).commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mSearchFragment.dispatchKeyEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
