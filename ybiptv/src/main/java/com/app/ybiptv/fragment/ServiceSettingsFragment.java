package com.app.ybiptv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.service.WebSocketService;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.utils.ViewUtils;

import com.open.library.utils.PreferencesUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class ServiceSettingsFragment extends BaseFragment {

    @BindView(R.id.et_ipaddr)
    EditText etIpAddr;

    Unbinder unbinder;

    @BindView(R.id.et_room_no)
    EditText etRoomNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_service, null);
        unbinder = ButterKnife.bind(this, view);
        etIpAddr.requestFocus();
        //
        String ipAddr = PreferencesUtils.getString(getActivity(), Consts.IP_ADDR_KEY);
        if (!TextUtils.isEmpty(ipAddr)) {
            etIpAddr.setText(ipAddr);
        }
        //
        String roomNo = PreferencesUtils.getString(getActivity(), Consts.IP_ROOM_NO_KEY);
        etRoomNo.setText(roomNo);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnFocusChange({R.id.et_ipaddr, R.id.save_btn})
    public void onViewFocusChange(View view, boolean isfocus) {
        ViewUtils.scaleView(view, isfocus);
    }

    @OnClick({R.id.save_btn})
    public void onViewClick(View view) {
        if (TextUtils.isEmpty(etIpAddr.getText().toString())) {
            showToast(R.string.empty_ipaddr_tip);
            return;
        }
        if (TextUtils.isEmpty(etRoomNo.getText().toString())) {
            showToast(R.string.empty_roomno_tip);
            return;
        }
        // 保存房间号
        PreferencesUtils.putString(getActivity(), Consts.IP_ROOM_NO_KEY, etRoomNo.getText().toString());
        final String ipaddr = etIpAddr.getText().toString();
        // TODO: 点击确认 测试后台链接
        if (!TextUtils.isEmpty(ipaddr)) {

            // 發送IP地址測試.
            Request request=new Request.Builder()
                    .url("http://" + ipaddr).build();
            Call call = ((IptvApplication) getActivity().getApplication()).getOkHttp().getOkHttpClient().newCall(request);
            call.enqueue(new Callback () {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "服务器地址请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 保存IP地址.
                            PreferencesUtils.putString(getActivity(), Consts.IP_ADDR_KEY, etIpAddr.getText().toString());

                            Toast.makeText(getActivity(), "服务器地址请求成功", Toast.LENGTH_SHORT).show();
                            Consts.ROOT_ADDR = "http://" + ipaddr;
                        }
                    });
                }
            });
        }
        // 发送一次websocket.
//        getActivity().startService(new Intent(getActivity(), WebSocketService.class));
    }

}
