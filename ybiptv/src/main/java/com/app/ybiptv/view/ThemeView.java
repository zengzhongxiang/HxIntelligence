package com.app.ybiptv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.utils.ViewUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.autolayout.utils.AutoUtils;

/**
 */

public class ThemeView extends LinearLayout {

    private Context mContext;
    private ImageView img_cover;
    private TextView tv_name;
    private View boardView;

    double mItemWidth = 0;
    double mItemHeight = 0;

    public ThemeView(Context context) {
        this(context, null);
        setClipChildren(false);
        setClipToPadding(false);
    }

    public ThemeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = getContext().getApplicationContext();
//        View view = View.inflate(mContext, R.layout.item_movice_layout, null);
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_list, this, true);

        // 保持影视比例.
        mItemWidth = (AutoLayoutConifg.getInstance().getDesignWidth() )/ 4;

        mItemHeight = mItemWidth / 4 * 3; // 保持影视item比例
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int)mItemWidth,(int)mItemHeight);
        view.setLayoutParams(lp);
        //
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        AutoUtils.autoSize(view);

        img_cover = (ImageView) findViewById(R.id.bg_iv);
        tv_name = (TextView) findViewById(R.id.name_tv);
        boardView = findViewById (R.id.board_view);

        view.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText (getContext (),"22",Toast.LENGTH_LONG).show ();
                boardView.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                ViewUtils.scaleView(view, b);
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText (getContext (),"11",Toast.LENGTH_LONG).show ();
//                        Intent intent = new Intent(getActivity(), TvDetailsActivity.class);
//                        intent.putExtra("movice_mode", moviceMode);
//                        startActivity(intent);
            }
        });
    }

}
