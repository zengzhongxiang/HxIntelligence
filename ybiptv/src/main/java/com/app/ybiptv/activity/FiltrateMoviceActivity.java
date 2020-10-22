package com.app.ybiptv.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.bean.MoviceTitlerMode;
import com.app.ybiptv.bean.NameInfo;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.fragment.FiltrateMoviceFragment;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.bumptech.glide.Glide;
import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.OnChildSelectedListener;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 影视筛选界面
 *
 */
public class FiltrateMoviceActivity extends BaseActivity {

    List<MoviceTitlerMode> mTitlerList = new ArrayList<MoviceTitlerMode>();
    List<FiltrateMoviceFragment> mFragmentList = new ArrayList<FiltrateMoviceFragment>();

    MyOkHttp mMyOkhttp;

    TitlerAdapter mTitlerAdapter;
    FragAdapter mFragAdapter;

    @BindView(R.id.title_hgridview)
    HorizontalGridView titleHgridview;
    @BindView(R.id.page_vp)
    ViewPager pageVp;

    @BindView(R.id.search_btn)
    Button searchBtn;

    @BindView(R.id.newMovice_btn)
    Button newMovice_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate_movice);
        ButterKnife.bind(this);
        initAllDatas();
        initAllViews();
        refshRequest();
    }

    private void initAllDatas() {
        mMyOkhttp = ((IptvApplication)getApplicationContext()).getOkHttp();
        //
//        mTitlerList.add(new MoviceTitlerMode("", "推荐"));
//        mTitlerList.add(new MoviceTitlerMode("","动作武侠"));
//        mTitlerList.add(new MoviceTitlerMode("","战争历史"));
//        mTitlerList.add(new MoviceTitlerMode("","犯罪心理"));
//        mTitlerList.add(new MoviceTitlerMode("","历史悬疑"));
//        mTitlerList.add(new MoviceTitlerMode("","爆笑喜剧"));
//        mTitlerList.add(new MoviceTitlerMode("","动漫少儿"));
    }

    private void initAllViews() {
        initTitlerRecyclerView();
        initContentViews();
    }

    private void initTitlerRecyclerView() {
//        titleHgridview.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
//        titleHgridview.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
//        titleHgridview.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
        mTitlerAdapter = new TitlerAdapter();
        titleHgridview.setPadding(60, 0, 60, 0);
        int top = AutoUtils.getPercentHeightSizeBigger(0);
        int right = AutoUtils.getPercentWidthSizeBigger(-50);
        titleHgridview.addItemDecoration(new SpaceItemDecoration (right, top));
        AutoUtils.autoSize(titleHgridview);
        titleHgridview.setAdapter(mTitlerAdapter);
        titleHgridview.getBaseGridViewLayoutManager().setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                // TODO xml 设置 android:duplicateParentState="true" selector 无效，临时这样处理.
                // 保持选中的颜色.
                if(view == null) return;
                TextView tv = view.findViewById(R.id.title_tv);
                tv.setTextColor(getResources().getColor(R.color.title_select_color));
                // 翻页.
                int index = (int) view.getTag();
                if (index >= 0) {
                    pageVp.setCurrentItem(index);
                }
            }
        });
        //
        titleHgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    // BUG:避免焦点跑到搜索框去.
                    searchBtn.setFocusableInTouchMode(true);
                    searchBtn.setFocusable(true);
                    searchBtn.requestFocusFromTouch();

                    newMovice_btn.setFocusableInTouchMode(true);
                    newMovice_btn.setFocusable(true);
//                    newMovice_btn.requestFocusFromTouch();
                }
                return false;
            }
        });
    }

    private void initContentViews() {
        mFragAdapter = new FragAdapter(getSupportFragmentManager());
        //
        searchBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchBtn.setBackgroundResource(b ? R.drawable.pw: R.drawable.trailer_btn_normal);
                searchBtn.setTextColor(getResources().getColor(b ? R.color.whilte : R.color.title_none_color));
                newMovice_btn.setTextColor(getResources().getColor(b ? R.color.whilte : R.color.title_none_color));
            }
        });
        searchBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    searchBtn.setFocusable(false);
                    newMovice_btn.setFocusable(false);
//                    searchBtn.setFocusableInTouchMode(false);
                    titleHgridview.requestFocusFromTouch();
                    return true;
                }
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (FiltrateMoviceActivity.this, SearchMoviceActivity.class));
            }
        });

        //
        newMovice_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                newMovice_btn.setBackgroundResource(b ? R.drawable.pw : R.drawable.trailer_btn_normal);
                newMovice_btn.setTextColor(getResources().getColor(b ? R.color.whilte : R.color.title_none_color));
                searchBtn.setTextColor(getResources().getColor(b ? R.color.whilte : R.color.title_none_color));
            }
        });
        newMovice_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    searchBtn.setFocusable(false);
                    newMovice_btn.setFocusable(false);

//                    newMovice_btn.setFocusableInTouchMode(false);
                    titleHgridview.requestFocusFromTouch();
                    return true;
                }
                return false;
            }
        });
        newMovice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (FiltrateMoviceActivity.this, ThemeMoviceActivity.class));
            }
        });


        pageVp.setOffscreenPageLimit(4); // 缓存3个页面
        pageVp.setAdapter(mFragAdapter);

        for (MoviceTitlerMode titleMode : mTitlerList) {
            mFragmentList.add(new FiltrateMoviceFragment(mMyOkhttp, titleMode));
        }
        mFragAdapter.notifyDataSetChanged();
    }

    private void refshRequest() {
//        http://ott.hengxsoft.com:8888/epg/router?appkey=hhzt&format=json&method=api.program.categoryList&v=1.0&session=&username=600101&programGroupId=26
//        appkey=hhzt&format=json&method=api.program.categoryList&v=1.0&session=&username=600101&programGroupId=26
//        Map<String, String> params = new HashMap<> ();
//        System.out.println ("getMac()=="+getMac());
//        params.put("format",  "json");
//        params.put("method","api.program.categoryList");
//        params.put("session","");
//        params.put("username","600101");
//        params.put("programGroupId","26");
        //https://beacon-api.aliyuncs.com/beacon/fetch/config/byappkey
//        String url ="http://ott.hengxsoft.com:8888/epg/router?appkey=hhzt&format=json&method=api.program.categoryList&v=1.0&session=&username=600101&programGroupId=26";
        System.out.println ("ROOT_ADDR=="+Consts.ROOT_ADDR);
        System.out.println ("GET_MOVICE_TITLE=="+Consts.GET_MOVICE_TITLE);
        mMyOkhttp.get ().url(Consts.GET_MOVICE_TITLE).enqueue(new GsonResponseHandler<ResultBean<List<MoviceTitlerMode>>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                System.out.println ("statusCode=="+statusCode+"   error_msg=="+error_msg);
            }

            @Override
            public void onSuccess(int statusCode, ResultBean<List<MoviceTitlerMode>> response) {
                System.out.println ("response=="+response);
                if(response.getCode () == 200 || response.getCode () == 0) {
                    mTitlerList = response.getData ();
                    System.out.println ("mTitlerList==" + mTitlerList);
                    mTitlerAdapter.notifyDataSetChanged ();
                    // 刷新
                    if (null != mTitlerList) {
                        mFragmentList.clear ();
                        for (MoviceTitlerMode titleMode : mTitlerList) {
                            mFragmentList.add (new FiltrateMoviceFragment (mMyOkhttp, titleMode));
                        }
                        mFragAdapter.notifyDataSetChanged ();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), response.getMsg (), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////   Adapter
    ////////////////////////////////////////////////////////////////////

    /**
     * 标题栏 Adapter.
     */
    public class TitlerAdapter extends RecyclerView.Adapter<TitlerAdapter.ViewHolder> {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_titler_layout, null);
            view.setDuplicateParentStateEnabled(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            AutoUtils.autoSize(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (null != mTitlerList) {
                holder.itemView.setTag(position);
                holder.nameTv.setText(mTitlerList.get(position).getProgramName ());
                holder.itemView.setTag(position);
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        // TODO xml 设置 android:duplicateParentState="true" selector 无效，临时这样处理.

                        TextView tv = view.findViewById(R.id.title_tv);
//                        tv.setTextColor(getResources().getColor( R.color.whilte));

                        View lineView = view.findViewById(R.id.title_line_view);

//                        AutoFrameLayout title_layout = view.findViewById (R.id.title_layout);
                        tv.setBackgroundResource (b ? R.drawable.title_normal : R.color.clear_color);

                        System.out.println ("view=="+view.getTag()+"   pageVp.getCurrentItem()=="+pageVp.getCurrentItem()+"  bbb="+b);
                        // 焦点已不再.
                        if ((int) view.getTag() != pageVp.getCurrentItem()) {
                            tv.setTextColor(getResources().getColor(b ? R.color.title_select_color : R.color.whilte));
                            lineView.setBackgroundColor(getResources().getColor(b ? R.color.title_select_color : R.color.clear_color));
                        }else{
                            int count = titleHgridview.getChildCount();
                            for (int i=0;i<count;i++)
                            {
                                View child = titleHgridview.getChildAt(i);
                                TextView textView = child.findViewById(R.id.title_tv);
                                if(textView!=null) {
                                    textView.setTextColor (getResources ().getColor (b ? R.color.whilte : R.color.title_none_color));
                                }
                            }

                            tv.setTextColor(getResources().getColor(b ? R.color.whilte : R.color.title_select_color));
                            lineView.setBackgroundColor(getResources().getColor(b ? R.color.clear_color : R.color.title_select_color));
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mTitlerList ? mTitlerList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTv;

            public ViewHolder(View view) {
                super(view);
                nameTv = view.findViewById(R.id.title_tv);
            }
        }

    }

    /**
     * 内容 framgnt Adapter.
     */
    public class FragAdapter extends FragmentPagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return null != mFragmentList ? mFragmentList.size() : 0;
        }

    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        System.out.println ("keyCode=="+keyCode);
//        System.out.println ("pageVp=="+pageVp.r);
        System.out.println ("event.getAction ()=="+event.getAction ());
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction () == KeyEvent.ACTION_DOWN) {
            View currentView= pageVp.findFocus();
            System.out.println ("currentView=="+currentView);
            if(currentView!=null) {   //说明当前焦点在电影上面
                System.out.println ("焦点==");
                titleHgridview.requestFocusFromTouch();
            }else {
                if ((System.currentTimeMillis () - exitTime) > 2000) {
                    Toast.makeText (getApplicationContext (), "再按一次退出程序", Toast.LENGTH_SHORT).show ();
                    exitTime = System.currentTimeMillis ();
                } else {
                    finish ();
                    System.exit (0);
                }
            }
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }
}
