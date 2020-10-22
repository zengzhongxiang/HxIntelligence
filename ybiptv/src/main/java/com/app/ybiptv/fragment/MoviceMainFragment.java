package com.app.ybiptv.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.bean.MoviceMode;
import com.app.ybiptv.bean.MoviceTitlerMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.presenter.CardPresenter;
import com.app.ybiptv.presenter.NewPresenterSelector;
import com.app.ybiptv.presenter.ThemePresenter;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.HeaderItem;
import com.open.leanback.widget.ItemBridgeAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.OnChildSelectedListener;
import com.open.leanback.widget.OnChildViewHolderSelectedListener;
import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.VerticalGridView;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 专题节目
 *
 */
public class MoviceMainFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    VerticalGridView recyclerView;
    Unbinder unbinder;

    List<MoviceMode> mMoviceList = new ArrayList<>();

    ArrayObjectAdapter mRowsAdapter;
    ItemBridgeAdapter mItemBridgeAdapter;

    MyOkHttp mMyOkhttp;

    public MoviceMainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vertical, container, false);
        AutoUtils.autoSize(view);
        unbinder = ButterKnife.bind(this, view);

        initAllDatas();

        // test data
        for (int i = 0; i < 50; i++) {
            mMoviceList.add(new MoviceMode());
        }

        final NewPresenterSelector newPresenterSelector = new NewPresenterSelector();
        mRowsAdapter = new ArrayObjectAdapter (newPresenterSelector); // 填入Presenter选择器.
        CardPresenter cardPresenter = new CardPresenter(getActivity ());
        ThemePresenter themePresenter = new ThemePresenter(getActivity ());

        int i;

        // 主题电影
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter (cardPresenter);
        HeaderItem header;
        for (int j = 0; j < 20; j++) {
            listRowAdapter.add(mMoviceList.get(j));
        }
//        header = new HeaderItem (0, "");
        ListRow listRow = new ListRow (null, listRowAdapter);
        mRowsAdapter.add(listRow);
        ////////////////////////////////////

        // 专题
        ArrayObjectAdapter themeAdapter = new ArrayObjectAdapter (themePresenter);
        for (int j = 0; j < 20; j++) {
            themeAdapter.add(mMoviceList.get(j));
        }
        header = new HeaderItem (1, "更多专题");
        ListRow themelistRow = new ListRow (header, themeAdapter);
        mRowsAdapter.add(themelistRow);
        ////////////////////////////////////


        mItemBridgeAdapter = new ItemBridgeAdapter (mRowsAdapter);
        recyclerView.setAdapter(mItemBridgeAdapter);

        // 不然有一些item放大被挡住了. (注意)
        recyclerView.setClipChildren(false);
        recyclerView.setClipToPadding(false);
        // 设置间隔.
        recyclerView.setPadding(30, 30, 30, 30);
        // 设置垂直item的间隔.
        recyclerView.setVerticalMargin(30);
        // 设置缓存.
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 100);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initAllDatas() {
        mMyOkhttp = ((IptvApplication) getActivity ().getApplicationContext ()).getOkHttp ();

    }

    private void refshRequest() {
        mMyOkhttp.get ().url(Consts.GET_MOVICE_TITLE).enqueue(new GsonResponseHandler<ResultBean<List<MoviceTitlerMode>>> () {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                System.out.println ("statusCode=="+statusCode+"   error_msg=="+error_msg);
            }

            @Override
            public void onSuccess(int statusCode, ResultBean<List<MoviceTitlerMode>> response) {
                System.out.println ("response=="+response);
                if(response.getCode () == 200 || response.getCode () == 0) {

                }else{
                    Toast.makeText(getActivity ().getApplicationContext(), response.getMsg (), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
