package com.app.ybiptv.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.utils.ViewUtils;
import com.open.library.network.NetWorkTools;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 *
 */

public class AboutSettingsFragment extends BaseFragment {

    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_macAddress)
    TextView tv_macAddress;
    @BindView(R.id.tv_authorization)
    TextView tv_authorization;
    @BindView(R.id.authorization_btn)
    Button authorization_btn;

    @BindView(R.id.rel_authorization)
    AutoRelativeLayout rel_authorization;

    @BindView(R.id.img_authorization)
    ImageView img_authorization;

    Unbinder unbinder;

    private NetWorkTools mNetWorkTools;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_settings_about, null);
        unbinder = ButterKnife.bind (this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        authorization_btn.requestFocus ();
        setAboutInfoShow ();
    }

    private void setAboutInfoShow() {
        IptvApplication application = IptvApplication.getApplication ();
        tv_version.setText (application.getVersionName ());
        tv_macAddress.setText (application.getManAddr ());
        tv_authorization.setText ("2020-10-01至2021-10-10");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        unbinder.unbind ();
    }

    @OnFocusChange({R.id.tv_version, R.id.tv_macAddress, R.id.tv_authorization})
    public void onViewFocusChange(View view, boolean isFocus) {
        ViewUtils.scaleView (view, isFocus);
    }

    @OnClick({R.id.authorization_btn})
    public void onViewClick(View view) {
        int visbiliy = rel_authorization.getVisibility ();
        if (visbiliy == View.GONE) {
            rel_authorization.setVisibility (View.VISIBLE);
            rel_authorization.requestFocus ();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction () == KeyEvent.ACTION_DOWN) {
//            int visbiliy = rel_authorization.getVisibility ();
//            if (visbiliy == View.VISIBLE) {
//                rel_authorization.setVisibility (View.GONE);
//                return true;
//            }
//        }
//
//        return  super.onKeyDown (keyCode, event);
//    }
}
