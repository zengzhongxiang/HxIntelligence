package com.app.ybiptv.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.utils.ViewUtils;
import com.open.library.network.NetWorkTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 *
 */

public class NetSettingsFragment extends BaseFragment {

    @BindView(R.id.tv_netIpAddress)
    TextView tvNetIpAddress;
    @BindView(R.id.tv_subNetMask)
    TextView tvSubNetMask;
    @BindView(R.id.tv_gateway)
    TextView tvGateway;
    @BindView(R.id.tv_dns)
    TextView tvDns;

    Unbinder unbinder;

    private NetWorkTools mNetWorkTools;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_net, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNetIpAddress.requestFocus();
        setNetInfoShow();
    }

    private void setNetInfoShow() {
        mNetWorkTools = new NetWorkTools (getActivity());
        String ipAddr = mNetWorkTools.getIpAddr();
        if (!TextUtils.isEmpty(ipAddr)) {
            tvNetIpAddress.setText(ipAddr);
        }else {
            tvNetIpAddress.setText("暂未获取到有线网络信息");
        }
        String netMask = mNetWorkTools.getNetMask();
        if (!TextUtils.isEmpty(netMask)) {
            tvSubNetMask.setText(netMask);
        }else {
            tvSubNetMask.setText("暂未获取到有线网络信息");

        }
        String gateWay = mNetWorkTools.getDefaultWay();
        if (!TextUtils.isEmpty(gateWay)) {
            tvGateway.setText(gateWay);
        }else {
            tvGateway.setText("暂未获取到有线网络信息");
        }
        String dns = mNetWorkTools.getFirstDns();
        if (!TextUtils.isEmpty(dns)) {
            tvDns.setText(dns);
        }else {
            tvDns.setText("暂未获取到有线网络信息");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnFocusChange({R.id.tv_netIpAddress,R.id.tv_subNetMask,R.id.tv_gateway,R.id.tv_dns})
    public void onViewFocusChange(View view, boolean isFocus){
        ViewUtils.scaleView(view, isFocus);
    }

}
