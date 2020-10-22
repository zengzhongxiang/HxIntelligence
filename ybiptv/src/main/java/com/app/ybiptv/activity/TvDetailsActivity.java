package com.app.ybiptv.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.adapter.TestAdapter;
import com.app.ybiptv.bean.EpisodesMode;
import com.app.ybiptv.bean.MoviceMode;
import com.app.ybiptv.bean.RecommendMode;
import com.app.ybiptv.bean.ResultBean;
import com.app.ybiptv.player.IRenderView;
import com.app.ybiptv.player.UpVideoView;
import com.app.ybiptv.utils.Consts;
import com.app.ybiptv.utils.ViewUtils;
import com.app.ybiptv.view.SpaceItemDecoration;
import com.bumptech.glide.Glide;
import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.HorizontalGridView;
import com.open.library.utils.CommonUtil;
import com.orhanobut.logger.Logger;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class TvDetailsActivity extends BaseActivity {

    private int mMvDataCount = 22; // test

    /**
     * 播放布局
     */
    @BindView(R.id.play_content)
    UpVideoView playContent;

    @BindView(R.id.player_view)
    View playerBoardView;

    /**
     * 片名
     */
    @BindView(R.id.mv_title)
    TextView mvTitle;

    /**
     * 状态
     */
    @BindView(R.id.mv_status_info)
    TextView mvStatusInfo;

    /**
     * 语种
     */
    @BindView(R.id.mv_maked_language)
    TextView mvMakedLanguage;

    /**
     * 主演
     */
    @BindView(R.id.mv_main_actor)
    TextView mvMainActor;

    /**
     * 类型
     */
    @BindView(R.id.mv_type)
    TextView mvType;

    /**
     * 剧情简介
     */
    @BindView(R.id.mv_synopsis)
    TextView mvSynopsis;

    /**
     * 播放布局
     */
    @BindView(R.id.lay_play_fn)
    AutoRelativeLayout layPlayFn;

    /**
     * 付费
     */
    @BindView(R.id.lay_money)
    AutoRelativeLayout layMoney;

    /**
     * 剧情列表
     */
    @BindView(R.id.mv_count)
    TextView mvCount;

    /** 集数按钮*/
//    @BindView(R.id.mv_count_no)
//    HorizontalGridView mvRecyclerCountNo;

    /**
     * 分集
     */
//    @BindView(R.id.mv_counts_part)
    HorizontalGridView mvRecyclerCountsPart;

    @BindView(R.id.lay_mv_count_details)
    LinearLayout mLayCount;

    @BindView(R.id.mv_count_vpager)
    ViewPager mvCountVpager;

    //    @BindView(R.id.recommendViewPager)
//    ViewPager recommendViewPager;//相关推荐
    @BindView(R.id.recommend_hgridview)
    HorizontalGridView recommendHGridView;

    List<View> hGridViewList = new ArrayList<>();

    //    String playUrl = "https://res.exexm.com/cw_145225549855002";

    String playUrl; //= "http://116.153.32.50:18180/Movie_bst/20200911/英雄时代.mp4"; // rtmp://live.hkstv.hk.lxdns.com/live/hks
//    String playUrl = "http://118.212.169.134/PLTV/88888888/224/3221226431/index.m3u8";  //直播

    @BindView(R.id.mv_main_director)
    TextView mvMainDirector;
    //    String playUrl = "/mnt/sda/sda1/test.avi";
//    String playUrl = "http://pinganvote.qixun.tv:281/live/dell.flv";

    private List<EpisodesMode> episodesModeList;//电视剧数据源
    private MoviceMode moviceMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);

        ButterKnife.bind(this);
        initBundleArgs(getIntent());
        initAllViews();
        getRecommendData();

    }

    /**
     * 获取推荐的数据
     */
    private void getRecommendData() {
        MyOkHttp okHttp = new MyOkHttp();
        HashMap<String, String> params = new HashMap<>();
        params.put("number", "12");
        params.put("sort", "");
        okHttp.post().url(Consts.POST_RECOMMENT_MOVIE).params(params)
                .enqueue(new GsonResponseHandler<ResultBean<List<RecommendMode>>>() {
                    @Override
                    public void onSuccess(int statusCode, ResultBean<List<RecommendMode>> response) {
                        mRecommendList = response.getData();
                        mMoviceAdapter.notifyDataSetChanged();
//                        recommendViewPager.setAdapter(new RecommendAdapter(response.getData()));
//                        recommendViewPager.setOffscreenPageLimit(12);
//                        recommendViewPager.setCurrentItem(0);
//                        Log.i("TAG", "成功 == " + response.getData().size());
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Toast.makeText(TvDetailsActivity.this, "获取推荐视频失败", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "错误 == " + error_msg);
                    }
                });
    }


    private void initBundleArgs(Intent intent) {
        moviceMode = (MoviceMode) intent.getSerializableExtra("movice_mode");
        if (null != moviceMode) {
            Logger.d("moviceMode：" + moviceMode);
//            playContent.setVideoPath(moviceMode.getPlay_url ());
            // 初始化播放信息.
            mvTitle.setText(moviceMode.getProgram_name ());
            mvMakedLanguage.setText("" + moviceMode.getProduction_area () + " | " + moviceMode.getDouban_score ());
            mvMainDirector.setText("导演：" + moviceMode.getDirector());
            mvMainActor.setText("主演：" + moviceMode.getPerformer ());
            mvSynopsis.setText("剧情简介：" + moviceMode.getBrief_introduction ());
//            mvType.setText("类型：" + );
            playUrl = moviceMode.getPlay_url ();
            // 是否付费.
            layMoney.setVisibility(View.VISIBLE);
            // 根据 电影/电视剧 判断，显示相关界面.
            if ("电影".equals(moviceMode.getType())) {
                mvCount.setVisibility(View.GONE);
                mLayCount.setVisibility(View.GONE);
//                mvRecyclerCountsPart.setVisibility(View.GONE);
            } else {
//                // 电视剧集数
//                mMvDataCount = moviceMode.getCount();
//                if (mMvDataCount > 0) {
//                    mvCount.setVisibility(View.VISIBLE);
//                    mLayCount.setVisibility(View.VISIBLE);
////                    mvRecyclerCountsPart.setVisibility(View.VISIBLE);
//                    initMvCount();
//                }
            }
            // 播放第一集
            //                // 获取播放集数数据信息.
//            episodesModeList = moviceMode.getEpisodes();
//            if (null != episodesModeList && episodesModeList.size() > 0) {
//                playUrl = Consts.ROOT_ADDR + episodesModeList.get(0).getPlay_url();
//                Logger.d("playUrl:" + playUrl);
//                playContent.setVideoPath(playUrl);
//                playContent.start();
//            }
        }
//
        System.out.println ("playUrl=="+playUrl);
        if(!TextUtils.isEmpty (playUrl)) {
            playContent.setVideoPath(playUrl);
            playContent.start ();
        }
    }

    private void initMvCount() {
        // 总的 选集数
        if (mMvDataCount <= 0) {
            return;
        }
//        mMvDataCount = 200;
        mvCount.setText("剧情列表(共" + mMvDataCount + "集)");
        hGridViewList.clear();
        int mvCountPage = (mMvDataCount / TestAdapter.PAGE_COUNT) + ((mMvDataCount % TestAdapter.PAGE_COUNT) > 0 ? 1 : 0);
        for (int i = 0; i < mvCountPage; i++) {
            View pageview = View.inflate(this, R.layout.view_mv_page, null);
            HorizontalGridView horizontalGridView = pageview.findViewById(R.id.page_hgridview);
            horizontalGridView.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
            int count = TestAdapter.PAGE_COUNT;
            if (((i + 1) * TestAdapter.PAGE_COUNT) > mMvDataCount) {
                count = TestAdapter.PAGE_COUNT - (((i + 1) * TestAdapter.PAGE_COUNT) - mMvDataCount);
            }
            TestAdapter adapter = new TestAdapter(count, i * TestAdapter.PAGE_COUNT, true);
            horizontalGridView.setAdapter(adapter);
            hGridViewList.add(pageview);
        }
        mvCountVpager.setOffscreenPageLimit(mvCountPage);
        mvCountVpager.setAdapter(new MVPageAdapter());
        // 分集
        TestAdapter testAdapter = new TestAdapter(mMvDataCount, 0, false);
//        mvRecyclerCountsPart.setAdapter(testAdapter);
//        mvRecyclerCountsPart.setOnChildSelectedListener(new OnChildSelectedListener() {
//            @Override
//            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
//                mvCountVpager.setCurrentItem(position);
//            }
//        });
//        mvRecyclerCountsPart.setVisibility(View.GONE);// 暂时屏蔽.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initBundleArgs(intent);
    }

    MoviceAdapter mMoviceAdapter;

    private void initAllViews() {
        mMoviceAdapter = new MoviceAdapter();
        int right = AutoUtils.getPercentWidthSizeBigger(30);
        recommendHGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        recommendHGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(true, true);
        recommendHGridView.addItemDecoration(new SpaceItemDecoration (right, 0));
        recommendHGridView.setAdapter(mMoviceAdapter);
        //
        playContent.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
        playContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                playerBoardView.setBackgroundResource(b ? R.drawable.movice_board_s_bg : R.drawable.movice_board_bg);
            }
        });
        // 监听事件
        playContent.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    startProgressTimer();
                }
                return false;
            }
        });
        layPlayFn.requestFocus();
    }

    @OnClick({R.id.play_content, R.id.lay_play_fn, R.id.lay_money, R.id.player_view, R.id.lay_play_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_content:
            case R.id.player_view: //
            case R.id.lay_play_content: // 播放器
            case R.id.lay_play_fn: // 播放
//                ArrayList<String> strings = new ArrayList<>();
//                for (EpisodesMode mode : episodesModeList) {
//                    strings.add(Consts.ROOT_ADDR + mode.getPlay_url());
//                }

                startActivity(MediaPlayActivity.TVStartActivity(this, playUrl, moviceMode.getProgram_name (),
                        moviceMode.getDirector(), moviceMode.getPerformer (), moviceMode.getBrief_introduction ()));

                break;
            case R.id.lay_money: // 付费
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新开始播放器
        playContent.resume();
        playContent.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playContent.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playContent.release(true);
        cancelProgressTimer();
    }

    /**
     * 内容 framgnt Adapter.
     */
    public class MVPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return hGridViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = hGridViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }

    List<RecommendMode> mRecommendList = new ArrayList<RecommendMode>();
    int mItemWidth = 0;
    int mItemHeight = 0;
    int mPageNum = 0;

    private static final int ITEM_NUM_ROW = 8; // 一行多少个row item.
    private static final int GRIDVIEW_LEFT_P = 80;
    private static final int GRIDVIEW_RIGHT_P = 50;
    private static final int GRIDVIEW_TOP_P = 20;
    private static final int GRIDVIEW_BOTTOM_P = 50;
    private static final int ITEM_TOP_PADDING = 35;
    private static final int ITEM_PADDING = 30;

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
            if (null != mRecommendList) {
                final RecommendMode recommendMode = mRecommendList.get(position);
                Glide.with(getApplicationContext()) // getPoster_url
                        .load(Consts.ROOT_ADDR + recommendMode.getPoster_url()).into(((ImageView) holder.bgIv));
                ((TextView) holder.nameTv).setText(recommendMode.getName());
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
                        Intent intent = new Intent(getApplicationContext(), TvDetailsActivity.class);
                        Logger.d("传递数据:moviceMode:" + moviceMode);
                        intent.putExtra("movice_mode", moviceMode);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mRecommendList ? mRecommendList.size() : 0;
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


    //进度定时器
    protected Timer updateProcessTimer;
    //定时器任务
    protected TvDetailsActivity.ProgressTimerTask mProgressTimerTask;

    protected void startProgressTimer() {
        cancelProgressTimer();
        updateProcessTimer = new Timer();
        mProgressTimerTask = new TvDetailsActivity.ProgressTimerTask ();
        updateProcessTimer.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (updateProcessTimer != null) {
            updateProcessTimer.cancel();
            updateProcessTimer = null;
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
            mProgressTimerTask = null;
        }
    }

    private class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (upVideoView.isPlaying()) {
                    // 更新进度信息.
                    int position = playContent.getCurrentPosition();
                    System.out.println ("position=="+position);
                    if(position>300000){   //播放5分钟停止
                        cancelProgressTimer();
                        playContent.release(true);
                        Toast.makeText(getApplicationContext(), "试看5分钟结束", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
