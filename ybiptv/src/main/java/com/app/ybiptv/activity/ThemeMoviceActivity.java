package com.app.ybiptv.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.bean.MoviceMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.utils.ViewUtils;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.bumptech.glide.Glide;
import com.open.leanback.widget.HorizontalGridView;
import com.orhanobut.logger.Logger;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeMoviceActivity extends BaseActivity {

    private static final int GRIDVIEW_LEFT_P = 80;
    private static final int GRIDVIEW_RIGHT_P = 50;
    private static final int GRIDVIEW_TOP_P = 20;
    private static final int GRIDVIEW_BOTTOM_P = 50;

    private static final int ITEM_TOP_PADDING = 35;
    private static final int ITEM_PADDING = 30;


    @BindView(R.id.theme_vgridview)
    HorizontalGridView theme_vgridview;

    @BindView(R.id.loading)
    ProgressBar loadingProgBar;

    ThemeMoviceActivity.MoviceAdapter mMoviceAdapter;

    MyOkHttp mMyOkHttp;
    List<MoviceMode> mMoviceList = new ArrayList<> ();
    double mItemWidth = 0;
    double mItemHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_theme_movice);
        ButterKnife.bind(this);

        initAllDatas ();
        initSearchGridView ();
        refshRequest ();

    }

    private void initAllDatas() {
        mMyOkHttp = ((IptvApplication) this.getApplicationContext ()).getOkHttp ();
        // test data
        for (int i = 0; i < 20; i++) {
            mMoviceList.add (new MoviceMode ());
        }
    }

    private void initSearchGridView() {
        theme_vgridview.setPadding (GRIDVIEW_LEFT_P, GRIDVIEW_TOP_P, GRIDVIEW_RIGHT_P, GRIDVIEW_BOTTOM_P);
        int top = AutoUtils.getPercentHeightSizeBigger (ITEM_TOP_PADDING);
        int right = AutoUtils.getPercentWidthSizeBigger (ITEM_PADDING);
        theme_vgridview.addItemDecoration (new SpaceItemDecoration (right, top));
        AutoUtils.autoSize (theme_vgridview);
        mMoviceAdapter = new ThemeMoviceActivity.MoviceAdapter ();
        theme_vgridview.setAdapter (mMoviceAdapter);

    }

    ///////////////////// Adapter ////////////////////////////////////

    public class MoviceAdapter extends RecyclerView.Adapter<ThemeMoviceActivity.MoviceAdapter.ViewHolder> {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ThemeMoviceActivity.MoviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate (parent.getContext (), R.layout.item_movice_layout, null);
            // 保持影视比例.
//            mItemWidth = (AutoUtils.getPercentWidthSizeBigger(searchVgridview.getWidth()) - GRIDVIEW_LEFT_P - GRIDVIEW_RIGHT_P - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
//            mItemWidth = 180; //(searchVgridview.getWidth() - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemWidth = (AutoLayoutConifg.getInstance().getDesignWidth() - GRIDVIEW_LEFT_P - GRIDVIEW_RIGHT_P - (ITEM_PADDING * 6.5)) / 6.5;

            mItemHeight = mItemWidth / 3 * 4 + 45; // 保持影视item比例
            System.out.println ("mItemHeight=="+mItemHeight);
//            mItemWidth = AutoUtils.getPercentWidthSizeBigger(mItemWidth);
//            mItemHeight = AutoUtils.getPercentHeightSizeBigger(mItemHeight);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams ((int) mItemWidth,(int) mItemHeight);
            view.setLayoutParams (lp);
            //
            view.setFocusable (true);
            view.setFocusableInTouchMode (true);
            AutoUtils.auto (view);
            ThemeMoviceActivity.MoviceAdapter.ViewHolder holder = new ThemeMoviceActivity.MoviceAdapter.ViewHolder (view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ThemeMoviceActivity.MoviceAdapter.ViewHolder holder, final int position) {
            if (null != mMoviceList) {
                final MoviceMode moviceMode = mMoviceList.get (position);
                Glide.with (ThemeMoviceActivity.this)
                        .load (Consts.ROOT_ADDR + moviceMode.getPath ()).into (((ImageView) holder.bgIv));
                ((TextView) holder.nameTv).setText (mMoviceList.get (position).getProgram_name ());
                holder.itemView.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        holder.boardView.setVisibility (b ? View.VISIBLE : View.INVISIBLE);
                        ViewUtils.scaleView (view, b);
                    }
                });
                holder.itemView.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (ThemeMoviceActivity.this, TvDetailsActivity.class);
                        intent.putExtra ("movice_mode", moviceMode);
                        startActivity (intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mMoviceList ? mMoviceList.size () : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bgIv;
            TextView nameTv;
            View boardView;

            public ViewHolder(View view) {
                super (view);
                bgIv = view.findViewById (R.id.bg_iv);
                nameTv = view.findViewById (R.id.name_tv);
                boardView = view.findViewById (R.id.board_view);
            }
        }

    }

    private void refshRequest() {
        loadingProgBar.setVisibility (View.VISIBLE);
        Map<String, String> params = new HashMap<> ();
        mMyOkHttp.post ().url (Consts.SEARCH_MOVICE).params (params)
                .enqueue (new GsonResponseHandler<ResultBean<List<MoviceMode>>> () {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.d ("statusCode:" + statusCode + " error_msg:" + error_msg);
                        showToast (error_msg + "(" + statusCode + ")");
                        loadingProgBar.setVisibility (View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, ResultBean<List<MoviceMode>> response) {
                        List<MoviceMode> searchInfoBeanList = response.getData ();

                        mMoviceList.addAll (searchInfoBeanList);
                        mMoviceAdapter.notifyDataSetChanged ();

                        loadingProgBar.setVisibility (View.GONE);
                    }
                });
    }


}
