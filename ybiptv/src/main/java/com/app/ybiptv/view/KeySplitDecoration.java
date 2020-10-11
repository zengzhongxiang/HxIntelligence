package com.app.ybiptv.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * 键盘分割线
 */
public class KeySplitDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int padding = AutoUtils.getPercentWidthSizeBigger(5);
//        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = 0;
            outRect.right = padding;
            outRect.bottom = 0;
            outRect.top = 0;
//        }else if(parent.getChildLayoutPosition(view) % 2 == 1){
//            outRect.left = padding;
//            outRect.right = 0;
//            outRect.bottom = 0;
//            outRect.top = 0;
//        }
    }

}
