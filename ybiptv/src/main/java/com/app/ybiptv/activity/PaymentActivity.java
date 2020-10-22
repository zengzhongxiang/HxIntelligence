package com.app.ybiptv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.app.ybiptv.R;
import com.app.ybiptv.fragment.MoviceMainFragment;

import butterknife.ButterKnife;

public class PaymentActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
    }
}
