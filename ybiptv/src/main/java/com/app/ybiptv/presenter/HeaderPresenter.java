package com.app.ybiptv.presenter;

import android.graphics.Color;
import android.view.Gravity;

import com.app.ybiptv.R;
import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.RowHeaderPresenter;
import com.open.leanback.widget.RowHeaderView;

/**
 *  关于横向item的头样式.
 */
public class HeaderPresenter extends RowHeaderPresenter {

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        super.onBindViewHolder(viewHolder, item);
        RowHeaderView headerView = (RowHeaderView) viewHolder.view;
        headerView.setTextSize(25);
        headerView.setTextColor(Color.WHITE);
//        headerView.setBackgroundResource (R.drawable.trailer_btn_normal);
//        headerView.setGravity (Gravity.CENTER);
        headerView.setPadding(10, 10, 0, 30);
    }

}
