package com.app.ybiptv.fragment;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 *
 */

public class BaseFragment extends Fragment {

    public void showToast(int strId) {
        Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

}
