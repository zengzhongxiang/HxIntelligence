package com.app.ybiptv.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.app.ybiptv.activity.IptvApplication;
import com.app.ybiptv.activity.TvDetailsActivity;
import com.app.ybiptv.bean.MoviceMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.utils.ViewUtils;
import com.app.ybiptv.view.KeySplitDecoration;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.bumptech.glide.Glide;
import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.OnChildSelectedListener;
import com.open.leanback.widget.VerticalGridView;
import com.orhanobut.logger.Logger;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 影视搜索 界面
 *
 */
public class SearchFragment extends BaseFragment {

    private static final int ITEM_NUM_ROW = 6; // 一行多少个row item.

    private static final int GRIDVIEW_LEFT_P = 80;
    private static final int GRIDVIEW_RIGHT_P = 50;
    private static final int GRIDVIEW_TOP_P = 20;
    private static final int GRIDVIEW_BOTTOM_P = 50;

    private static final int ITEM_TOP_PADDING = 35;
    private static final int ITEM_PADDING = 30;

    private int mPageNum = Consts.DEFUALT_PAGE;
    MyOkHttp mMyOkHttp;
    List<MoviceMode> mMoviceList = new ArrayList<>();
    int mItemWidth = 0;
    int mItemHeight = 0;
    boolean isAllKey = true; // true 全键盘 false T9

    @BindView(R.id.all_key_vgridview)
    VerticalGridView allKeyVgridview; // 全键盘布局.
    @BindView(R.id.t9_key_vgridview)
    VerticalGridView t9KeyVgridview; // T9键盘.
    @BindView(R.id.search_vgridview)
    VerticalGridView searchVgridview;
    Unbinder unbinder;

    @BindView(R.id.loading)
    ProgressBar loadingProgBar;
    @BindView(R.id.tip_tv)
    TextView tipTextView;

    @BindView(R.id.search_show_tv)
    TextView searchShowTv;
    @BindView(R.id.clear_btn)
    Button clearBtn;
    @BindView(R.id.del_btn)
    Button delBtn;
    @BindView(R.id.key_select_vgridview)
    HorizontalGridView keySelectHgridview;

    /* T9键盘 */
    @BindView(R.id.one_btn)
    Button oneBtn;
    @BindView(R.id.two_btn)
    Button twoBtn;
    @BindView(R.id.three_btn)
    Button threeBtn;
    @BindView(R.id.four_btn)
    Button fourBtn;
    @BindView(R.id.five_btn)
    Button fiveBtn;
    @BindView(R.id.t9_popup_flyt)
    AutoLinearLayout t9PopupFlyt;
    @BindView(R.id.t9_bg_flyt)
    AutoFrameLayout t9BgFlyt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_movice, container, false);
        AutoUtils.autoSize(view);
        unbinder = ButterKnife.bind(this, view);
        //
        initAllDatas();
        initSearchGridView();
        initAllKeysGridView();
        initT9KeysGridView();
        initKeySelectGridView();
        refshRequest();
        return view;
    }

    private void initAllDatas() {
        mMyOkHttp = ((IptvApplication) getActivity().getApplicationContext()).getOkHttp();
        // test data
        for (int i = 0; i < 50; i++) {
            mMoviceList.add(new MoviceMode());
        }
    }

    private void initT9KeysGridView() {
        t9KeyVgridview.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        t9KeyVgridview.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
        t9KeyVgridview.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        t9KeyVgridview.setNumColumns(3);
        int topSpace = AutoUtils.getPercentHeightSizeBigger(5);
        t9KeyVgridview.addItemDecoration(new SpaceItemDecoration (0, topSpace));
        t9KeyVgridview.setAdapter(new T9KeyItemAdapter());
    }

    private void initAllKeysGridView() {
        allKeyVgridview.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        allKeyVgridview.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
        allKeyVgridview.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        int topSpace = AutoUtils.getPercentHeightSizeBigger(5);
        allKeyVgridview.addItemDecoration(new SpaceItemDecoration(0, topSpace));
        allKeyVgridview.setNumColumns(ITEM_NUM_ROW);
        allKeyVgridview.setAdapter(new KeyItemAdapter());
    }

    MoviceAdapter mMoviceAdapter;

    private void initSearchGridView() {
        searchVgridview.setPadding(GRIDVIEW_LEFT_P, GRIDVIEW_TOP_P, GRIDVIEW_RIGHT_P, GRIDVIEW_BOTTOM_P);
        searchVgridview.setNumColumns(ITEM_NUM_ROW);
        int top = AutoUtils.getPercentHeightSizeBigger(ITEM_TOP_PADDING);
        int right = AutoUtils.getPercentWidthSizeBigger(ITEM_PADDING);
        searchVgridview.addItemDecoration(new SpaceItemDecoration(0, top));
        AutoUtils.autoSize(searchVgridview);
        mMoviceAdapter = new MoviceAdapter();
        searchVgridview.setAdapter(mMoviceAdapter);
        //
        searchVgridview.setOnLoadMoreListener(new VerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNum += 1;
                refshRequest();
            }
        });
    }

    private void initKeySelectGridView() {
        keySelectHgridview.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        keySelectHgridview.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
        keySelectHgridview.addItemDecoration(new KeySplitDecoration ());
        keySelectHgridview.setAdapter(new KeySelectAdapter());
        keySelectHgridview.getBaseGridViewLayoutManager().setOnChildSelectedListener(new OnChildSelectedListener () {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                if (position == 0) {
                    isAllKey = true;
                } else {
                    isAllKey = false;
                }
                allKeyVgridview.setVisibility(isAllKey ? View.VISIBLE : View.GONE);
                t9KeyVgridview.setVisibility(isAllKey ? View.GONE : View.VISIBLE);
            }
        });
        keySelectHgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
//                    Button view = (Button) keySelectHgridview.getChildAt(isAllKey ? 0 : 1);
                    Button view = (Button) keySelectHgridview.getFocusedChild();
                    if (view != null) {
                        view.setTextColor(getResources().getColor(R.color.title_select_color));
                    }
//                    allKeyVgridview.requestFocusFromTouch();
//                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (!isAllKey) {
                        searchVgridview.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void refshRequest() {
        loadingProgBar.setVisibility(View.VISIBLE);
        // TODO 搜索 504 错误，还需要去掉 sort_id, is_film ，后台需要更改返回的数据(是全部影视)
        Map<String, String> params = new HashMap<>();
        params.put("spell_name", searchShowTv.getText().toString());
//        params.put("sort_id", "11");
//        params.put("is_film", "1");
        params.put("page", mPageNum + "");
        params.put("page_num", Consts.PAGE_COUNT + "");
        mMyOkHttp.post().url(Consts.SEARCH_MOVICE).params(params)
                .enqueue(new GsonResponseHandler<ResultBean<List<MoviceMode>>>() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Logger.d("statusCode:" + statusCode + " error_msg:" + error_msg);
                        showToast(error_msg + "(" + statusCode + ")");
                        searchVgridview.endMoreRefreshComplete();
                        loadingProgBar.setVisibility(View.GONE);
                        showTipView();
                    }

                    @Override
                    public void onSuccess(int statusCode, ResultBean<List<MoviceMode>> response) {
                        List<MoviceMode> searchInfoBeanList = response.getData();
                        if (mPageNum == Consts.DEFUALT_PAGE) {
                            if (null != searchInfoBeanList) {
                                mMoviceList = searchInfoBeanList;
                            }
                        } else {
                            if (searchInfoBeanList != null && searchInfoBeanList.size() > 0) {
                                mMoviceList.addAll(searchInfoBeanList);
                            }
                        }
                        mMoviceAdapter.notifyDataSetChanged();
                        searchVgridview.endMoreRefreshComplete();
                        // 没有更多的数据.
                        if (mPageNum > Consts.DEFUALT_PAGE && (searchInfoBeanList == null || searchInfoBeanList.size() <= 0)) {
                            searchVgridview.endRefreshingWithNoMoreData();
                        }
                        loadingProgBar.setVisibility(View.GONE);
                        showTipView();
                    }
                });
    }

    private void showTipView() {
        if (null == mMoviceList || mMoviceList.size() <= 0) {
            tipTextView.setVisibility(View.VISIBLE);
        } else {
            tipTextView.setVisibility(View.GONE);
        }
    }

    boolean isFirstLoad = true;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private int mSaveT9Postion = -1;

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_UP == event.getAction() && KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (t9PopupFlyt.getVisibility() == View.VISIBLE) {
                hideT9Menu();
                return true;
            }
        }
        return false;
    }

    private void hideT9Menu() {
        if (t9PopupFlyt.getVisibility() == View.VISIBLE) {
            t9PopupFlyt.setVisibility(View.GONE);
            t9BgFlyt.setVisibility(View.GONE);
            t9KeyVgridview.requestFocusFromTouch();
            t9KeyVgridview.setSelectedPosition(mSaveT9Postion);
        }
    }

    private void setSearchText(String text) {
        searchShowTv.setText(text);
        // 搜索数据.
        mPageNum = Consts.DEFUALT_PAGE;
        mMoviceList.clear();
        mMoviceAdapter.notifyDataSetChanged();
        refshRequest();
    }

    /* 上部工具条点击事件 */
    @OnClick({R.id.clear_btn, R.id.del_btn})
    public void onTopBottomClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_btn:
                searchShowTv.setText("");
                break;
            case R.id.del_btn:
                String str = searchShowTv.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    setSearchText(str.substring(0, str.length() - 1));
//                    searchShowTv.setText(str.substring(0, str.length()-1));
                }
                break;
        }
    }

    /* T9键盘按钮点击事件 */
    @OnClick({R.id.one_btn, R.id.two_btn, R.id.three_btn, R.id.four_btn, R.id.five_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_btn:
                break;
            case R.id.two_btn:
                break;
            case R.id.three_btn:
                break;
            case R.id.four_btn:
                break;
            case R.id.five_btn:
                break;
        }
//        searchShowTv.setText(searchShowTv.getText().toString() + ((Button)view).getText().toString());
        setSearchText(searchShowTv.getText().toString() + ((Button) view).getText().toString());
        hideT9Menu();
    }

    ///////////////////// Adapter ////////////////////////////////////

    public class MoviceAdapter extends RecyclerView.Adapter<MoviceAdapter.ViewHolder> {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_movice_layout, null);
            // 保持影视比例.
//            mItemWidth = (AutoUtils.getPercentWidthSizeBigger(searchVgridview.getWidth()) - GRIDVIEW_LEFT_P - GRIDVIEW_RIGHT_P - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemWidth = 180; //(searchVgridview.getWidth() - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemHeight = mItemWidth / 3 * 4 + 45; // 保持影视item比例
//            mItemWidth = AutoUtils.getPercentWidthSizeBigger(mItemWidth);
//            mItemHeight = AutoUtils.getPercentHeightSizeBigger(mItemHeight);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
            view.setLayoutParams(lp);
            //
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            AutoUtils.auto(view);
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
                ((TextView) holder.nameTv).setText(mMoviceList.get(position).getProgram_name ());
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
                        Intent intent = new Intent(getActivity(), TvDetailsActivity.class);
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

    public class KeyItemAdapter extends RecyclerView.Adapter<KeyItemAdapter.ViewHolder> {

        String[] allKeys;

        public KeyItemAdapter() {
            allKeys = getResources().getStringArray(R.array.all_keys);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            int size = 80;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            AutoUtils.auto(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((Button) holder.itemView).setText(allKeys[position]);
            ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.whilte));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    searchShowTv.setText(searchShowTv.getText() + allKeys[position]);
                    setSearchText(searchShowTv.getText() + allKeys[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allKeys.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }

    }

    /**
     * T9键盘 Adapter.
     * TODO: 这里的所有 Adapter 需要代码重构
     */
    public class T9KeyItemAdapter extends RecyclerView.Adapter<T9KeyItemAdapter.ViewHolder> {

        String[] t9Keys;

        public T9KeyItemAdapter() {
            t9Keys = getResources().getStringArray(R.array.t9_keys);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_t9_key_layout, null);
            int size = 165;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
            view.setLayoutParams(lp);
            AutoUtils.auto(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final String keyStr = t9Keys[position];
            final String[] keyArry = keyStr.split(",");
            if (keyArry != null && keyArry.length >= 2) {
                holder.topTextView.setText(keyArry[0]);
                holder.bottomTextView.setText(keyArry[1].replace(".", ""));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            fiveBtn.setVisibility(View.VISIBLE);
                            oneBtn.setText(keyArry[0]);
                            twoBtn.setText(keyArry[1].substring(0, 1));
                            threeBtn.setText(keyArry[1].substring(2, 3));
                            fourBtn.setText(keyArry[1].substring(4, 5));
                            fiveBtn.setText(keyArry[1].substring(6));
                        } catch (Exception e) {
                            fiveBtn.setVisibility(View.GONE);
                        }
                        //
                        t9PopupFlyt.setVisibility(View.VISIBLE);
//                        t9BgFlyt.setVisibility(View.VISIBLE);
                        threeBtn.requestFocusFromTouch();
                        mSaveT9Postion = t9KeyVgridview.getSelectedPosition();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return t9Keys.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView topTextView;
            TextView bottomTextView;

            public ViewHolder(View view) {
                super(view);
                topTextView = view.findViewById(R.id.top_tv);
                bottomTextView = view.findViewById(R.id.bottom_tv);
            }

        }

    }

    public class KeySelectAdapter extends RecyclerView.Adapter<KeySelectAdapter.ViewHolder> {

        private List<String> keySelectList = new ArrayList<String>() {
            {
                add("全键盘");
                add("T9键盘");
            }
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(245, 80);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            view.setTextColor(getResources().getColor(R.color.whilte));
            AutoUtils.auto(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (null != keySelectList) {
                ((Button) holder.itemView).setText(keySelectList.get(position));
                ((Button) holder.itemView).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.whilte));
                        }
                    }
                });
            }
            // 设置默认.
            if (isFirstLoad) {
                if ((isAllKey && position == 0) || (!isAllKey && position == 1)) {
                    ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                    isFirstLoad = false;
                }
            }
        }

        @Override
        public int getItemCount() {
            return null != keySelectList ? keySelectList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }

    }

}
