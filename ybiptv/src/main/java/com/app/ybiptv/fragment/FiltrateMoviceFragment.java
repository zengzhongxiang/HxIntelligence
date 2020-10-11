package com.app.ybiptv.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.TvDetailsActivity;
import com.app.ybiptv.bean.MoviceMode;
import com.app.ybiptv.bean.MoviceTitlerMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.utils.ViewUtils;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.bumptech.glide.Glide;
import com.open.leanback.widget.OnChildSelectedListener;
import com.open.leanback.widget.VerticalGridView;
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
import butterknife.Unbinder;

/**
 * 影视筛选 内容界面
 *
 */
@SuppressLint("ValidFragment")
public class FiltrateMoviceFragment extends BaseFragment {

    private static final int ITEM_NUM_ROW = 5; // 一行多少个row item.
    private static final int GRIDVIEW_LEFT_P = 80;
    private static final int GRIDVIEW_RIGHT_P = 50;
    private static final int GRIDVIEW_TOP_P = 20;
    private static final int GRIDVIEW_BOTTOM_P = 50;

    private static final int ITEM_TOP_PADDING = 35;
    private static final int ITEM_PADDING = 30;

    @BindView(R.id.movice_vgridview)
    VerticalGridView moviceVgridview;
    Unbinder unbinder;

    MyOkHttp mMyOkhttp;
    MoviceAdapter mMoviceAdapter;
    List<MoviceMode> mMoviceList = new ArrayList<>();
    MoviceTitlerMode mMoviceTitlerMode;

    private boolean blToast;

    int mItemWidth = 0;
    int mItemHeight = 0;
    int mPageNum = 0;

    @SuppressLint("ValidFragment")
    public FiltrateMoviceFragment(MyOkHttp myOkHttp, MoviceTitlerMode titleMode) {
        mMyOkhttp = myOkHttp;
        mMoviceTitlerMode = titleMode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtrate_movice, container, false);
        AutoUtils.autoSize(view);
        unbinder = ButterKnife.bind(this, view);
        mMoviceAdapter = new MoviceAdapter();
        // test data
        for (int i = 0; i < 50; i++) {
            mMoviceList.add(new MoviceMode());
        }
        // 初始化影视垂直布局.
        moviceVgridview.setPadding(GRIDVIEW_LEFT_P, GRIDVIEW_TOP_P, GRIDVIEW_RIGHT_P, GRIDVIEW_BOTTOM_P);
        moviceVgridview.setNumColumns(ITEM_NUM_ROW);
        int top = AutoUtils.getPercentHeightSizeBigger(ITEM_TOP_PADDING);
        int right = AutoUtils.getPercentWidthSizeBigger(ITEM_PADDING);
        moviceVgridview.addItemDecoration(new SpaceItemDecoration (right, top));
//        moviceVgridview.setSelectedPosition(0); // 设置默认选中位置
        moviceVgridview.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        moviceVgridview.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);
//        moviceVgridview.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        moviceVgridview.getBaseGridViewLayoutManager().setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                System.out.println ("当前选中=="+position);
                if(!blToast) {
                    if (position >= 15) {
                        showToast ("按返回键回到标题");
                        blToast = true;
                    }
                }
            }
        });

        AutoUtils.autoSize(moviceVgridview);
        moviceVgridview.setAdapter(mMoviceAdapter);
        moviceVgridview.setOnLoadMoreListener(new VerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int count = mMoviceList.size();
                for (int i = count; i < count + 50; i++) {
                    mMoviceList.add(new MoviceMode());
                }
                moviceVgridview.endMoreRefreshComplete();
                mMoviceAdapter.notifyDataSetChanged();
                if (mMoviceList.size() > 200) {
                    showToast("没有更多数据");
                    moviceVgridview.endRefreshingWithNoMoreData();
                }
                mPageNum += 1;
                refshRequest();
            }
        });
        // 刷新界面.
//        refshRequest();
        return view;
    }

    private void refshRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("typeid", "" + mMoviceTitlerMode.getId());
        params.put("page", mPageNum + "");
        params.put("size", Consts.PAGE_COUNT + "");
        mMyOkhttp.post().url(Consts.GET_SERIE_INFO).params(params).enqueue(new GsonResponseHandler<ResultBean<List<MoviceMode>>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                showToast(error_msg + "(" + statusCode + ")");
            }

            @Override
            public void onSuccess(int statusCode, ResultBean<List<MoviceMode>> response) {
                List<MoviceMode> moviceModes = response.getData();
                Logger.d("" + moviceModes);
                if (mPageNum == Consts.DEFUALT_PAGE) {
                    if (null != moviceModes) {
                        mMoviceList = moviceModes;
                    }
                } else {
                    if (moviceModes != null && moviceModes.size() > 0) {
                        mMoviceList.addAll(moviceModes);
                    }
                }
                mMoviceAdapter.notifyDataSetChanged();
                moviceVgridview.endMoreRefreshComplete();
                // 没有更多的数据.
                if (mPageNum > Consts.DEFUALT_PAGE && (moviceModes == null || moviceModes.size() <= 0)) {
                    moviceVgridview.endRefreshingWithNoMoreData();
                }
//                loadingProgBar.setVisibility(View.GONE);
//                showTipView();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class MoviceAdapter extends RecyclerView.Adapter<MoviceAdapter.ViewHolder> {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_movice_layout, null);
            // 保持影视比例.
            mItemWidth = (AutoLayoutConifg.getInstance().getDesignWidth() - GRIDVIEW_LEFT_P - GRIDVIEW_RIGHT_P - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemHeight = mItemWidth / 3 * 4 + 45; // 保持影视item比例
//            mItemWidth = AutoUtils.getPercentWidthSizeBigger(mItemWidth);
//            mItemHeight = AutoUtils.getPercentHeightSizeBigger(mItemHeight);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
            view.setLayoutParams(lp);
            //
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            AutoUtils.autoSize(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (null != mMoviceList) {
                final MoviceMode moviceMode = mMoviceList.get(position);
                Glide.with(getActivity())
                        .load(Consts.ROOT_ADDR + moviceMode.getPath ()).into(((ImageView) holder.bgIv));
                ((TextView) holder.nameTv).setText(moviceMode.getProgram_name ());
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        holder.boardView.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                        ViewUtils.scaleView(view, b);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (getActivity(), TvDetailsActivity.class);
                        Logger.d("传递数据:moviceMode:" + moviceMode);
                        intent.putExtra("movice_mode", moviceMode);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mMoviceList ? mMoviceList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bgIv;
            TextView nameTv;
            View boardView;

            public ViewHolder(View view) {
                super(view);
                bgIv = view.findViewById(R.id.bg_iv);
                nameTv = view.findViewById(R.id.name_tv);
                boardView = view.findViewById(R.id.board_view);
            }
        }

    }

}
