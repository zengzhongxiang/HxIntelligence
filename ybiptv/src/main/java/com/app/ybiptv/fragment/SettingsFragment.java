package com.app.ybiptv.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.service.NetWordReceiverCallBack;
import com.app.ybiptv.service.NetWorkReceiver;
import com.app.ybiptv.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 *
 */

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.servicesetting)
    RelativeLayout servicesetting;

    @BindView(R.id.netsetting)
    RelativeLayout netsetting;

    @BindView(R.id.aboutsetting)
    RelativeLayout aboutsetting;

    @BindView (R.id.net_txt)
    TextView net_txt;

    Unbinder unbinder;

    ServiceSettingsFragment mServiceSettingFragment;

    NetSettingsFragment mNetSettingsFragment;

    AboutSettingsFragment mAboutSettingsFragment;

    private static final String TAG_WIFI = "WIFI";
    private static final String TAG_NET = "NET";
    private static final String TAG_ABOUT = "ABOUT";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        mNetSettingsFragment = new NetSettingsFragment ();
        mServiceSettingFragment = new ServiceSettingsFragment();
        mAboutSettingsFragment = new AboutSettingsFragment ();

        netsetting.requestFocusFromTouch();

        initNetWork();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        if(netWorkReceiver!=null){
            getActivity ().unregisterReceiver(netWorkReceiver);
        }
    }

    @OnClick({R.id.servicesetting, R.id.netsetting,R.id.aboutsetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.servicesetting: // 服务端设置
                if(!mServiceSettingFragment.isAdded())
                    getFragmentManager().beginTransaction().replace(R.id.contentlay, mServiceSettingFragment).addToBackStack(TAG_WIFI).commit();
                break;
            case R.id.netsetting: // 有线设置
                if(!mNetSettingsFragment.isAdded())
                    getFragmentManager().beginTransaction().replace(R.id.contentlay, mNetSettingsFragment).addToBackStack(TAG_NET).commit();
                break;
            case R.id.aboutsetting:
                if(!mAboutSettingsFragment.isAdded ()){
                    getFragmentManager ().beginTransaction ().replace (R.id.contentlay,mAboutSettingsFragment).addToBackStack (TAG_ABOUT).commit ();
                }
                break;
        }
    }



    @OnFocusChange({R.id.servicesetting, R.id.netsetting,R.id.aboutsetting})
    public void onViewFocusChanged(View view ,boolean isFocus){
        ViewUtils.scaleView(view, isFocus);
    }

    private NetWorkReceiver netWorkReceiver;
    private void initNetWork(){
        /** 注册监听网络改变的广播*/
        netWorkReceiver = new NetWorkReceiver(new NetWordReceiverCallBack () {
            @Override
            public void result(int code) {
                switch (code) {
                    case NetWorkReceiver.NONETWORK://盒子没有接上网络
                        net_txt.setText ("无网络");
                        break;
                    case NetWorkReceiver.NETWORK://盒子接上有线网络
                        net_txt.setText ("有线网络");
                        break;
                    case NetWorkReceiver.WIFI://盒子接上无线网络
                        //显示wifi强弱
                        net_txt.setText ("无线WIFI");
                        break;
                    default:

                        break;

                }

            }

        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity ().registerReceiver(netWorkReceiver, filter);
    }


}


