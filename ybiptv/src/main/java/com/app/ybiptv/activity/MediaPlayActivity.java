package com.app.ybiptv.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ybiptv.R;
import com.app.ybiptv.player.UpVideoView;
import com.app.ybiptv.utils.ViewUtils;
import com.app.ybiptv.view.KeySplitDecoration;
import com.open.leanback.widget.HorizontalGridView;
import com.open.library.utils.CommonUtil;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 点播界面
 *
 */
public class MediaPlayActivity extends BaseActivity {

    private static final int SEEK = 30 * 1000; // 10秒

    String path = "";
    boolean mIsMovice = true;

    @BindView(R.id.upVideoView)
    UpVideoView upVideoView;
    @BindView(R.id.hud_view)
    TableLayout hudView;
    @BindView(R.id.h_gridview)
    HorizontalGridView hGridview;

    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.current)
    TextView currentTv;
    @BindView(R.id.progress)
    SeekBar progressSeekBar;
    @BindView(R.id.total)
    TextView totalTv;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottomllyt;
    @BindView(R.id.play_state_iv)
    ImageView playStateIv;
    @BindView(R.id.textMovieName)
    TextView textMovieName;
    @BindView(R.id.textDirector)
    TextView textDirector;
    @BindView(R.id.textActor)
    TextView textActor;
    @BindView(R.id.textContent)
    TextView textContent;

    private ArrayList<String> tvData;


    public static final String PLAY_URL = "play_url";//播放链接
    public static final String PLAY_TYPE = "play_type";//播放类型,传入播放类型，判断是否为电影或电视剧. true，反之 false
    public static final String PLAY_DATA = "play_data";//当且仅当是电视剧的时候才会存在
    public static final String PLAY_ACTOR = "play_actor";//演员
    public static final String PLAY_DIRECTOR = "play_director";//导演
    public static final String PLAY_TITLE = "play_title";//电影或者电影名字
    public static final String PLAY_CONTENT = "play_content";//电影描述

    /**
     * 电影跳转
     *
     * @param context
     * @param url
     * @param type
     * @return
     */

    public static Intent MovieStartActivity(Context context, String url, boolean type) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra(PLAY_URL, url);
        intent.putExtra(PLAY_TYPE, type);
        return intent;
    }

    /**
     * 电视剧跳转 不显示集数
     */
    public static Intent TVStartActivity(Context context, String url, String title,
                                         String director, String actor, String content) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra(PLAY_URL, url);
        intent.putExtra(PLAY_TITLE, title);
        intent.putExtra(PLAY_ACTOR, actor);
        intent.putExtra(PLAY_DIRECTOR, director);
        intent.putExtra(PLAY_CONTENT, content);
        return intent;
    }


    /**
     * 电视剧跳转
     *
     * @param context
     * @param url
     * @return
     */
    public static Intent TVStartActivity(Context context, String url, ArrayList<String> data) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra(PLAY_URL, url);
        intent.putExtra(PLAY_TYPE, false);
        intent.putStringArrayListExtra(PLAY_DATA, data);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initBunldArgs(getIntent());
        //
        initAllDatas();
        // 初始化影视播放器
        initVideoPlayerView();
        // 测试 电视剧选集的翻页效果

        initRecyclerView();
        //
        refreshRequest();
    }

    public void initAllDatas() {
    }

    public void refreshRequest() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initBunldArgs(intent);
    }

    private void initBunldArgs(Intent intent) {
        //Intent intent = getIntent();
        String playUrl = intent.getStringExtra(PLAY_URL);
        boolean playType = intent.getBooleanExtra(PLAY_TYPE, true);
        if (!TextUtils.isEmpty(playUrl)) {
            // 设置播放地址.
            path = playUrl;
            // 设置播放地址
            upVideoView.setVideoPath(path);
            // 开始播放
            upVideoView.start();
            //电视剧数据
            tvData = intent.getStringArrayListExtra(PLAY_DATA);
            // 根据传入的类型，是否显示电视剧相关界面以及操作.
            if (null != tvData && tvData.size() > 1) {
                mIsMovice = playType;
            }
            textMovieName.setText(intent.getStringExtra(PLAY_TITLE));
            textActor.setText(intent.getStringExtra(PLAY_ACTOR));
            textDirector.setText(intent.getStringExtra(PLAY_DIRECTOR));
            textContent.setText(intent.getStringExtra(PLAY_CONTENT));
            initRecyclerView(); // 刷新界面.
        }
    }

    private void initVideoPlayerView() {
        // 显示播放器信息
//        upVideoView.setHudView(hudView);
        // 设置播放背景
//        upVideoView.setBackgroundResource(R.drawable.player_background);
        // 全屏
        upVideoView.fullScreen(this);
        // 监听事件
        upVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    startProgressTimer();
                    loading.setVisibility(View.INVISIBLE);
                    playStateIv.setImageResource(R.drawable.video_player_pause);
                }
                return false;
            }
        });
        upVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        upVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                // 播放结束
                currentTv.setText("00:00:00");
                progressSeekBar.setProgress(0);
                totalTv.setText("00:00:00");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新开始播放器
        upVideoView.resume();
        upVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        upVideoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        upVideoView.release(true);
        cancelProgressTimer();
    }

    //进度定时器
    protected Timer updateProcessTimer;
    //定时器任务
    protected ProgressTimerTask mProgressTimerTask;

    protected void startProgressTimer() {
        cancelProgressTimer();
        updateProcessTimer = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
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
                    int position = upVideoView.getCurrentPosition();
                    int duration = upVideoView.getDuration();
                    String positionTimeStr = CommonUtil.stringForTime(position);
                    String durationTimeStr = CommonUtil.stringForTime(duration);
                    // 更新界面
                    currentTv.setText(positionTimeStr);
                    totalTv.setText(durationTimeStr);
                    progressSeekBar.setMax(duration);
                    progressSeekBar.setProgress(position);

                    System.out.println ("positionTimeStr=="+positionTimeStr + "  durationTimeStr == "+durationTimeStr);
                    System.out.println ("position=="+position+ "  duration=="+duration);
                    if(position>300000){   //播放5分钟停止
                        upVideoView.release(true);
                        cancelProgressTimer();
                        Toast.makeText(getApplicationContext(), "试看5分钟结束", Toast.LENGTH_SHORT).show();
                    }

                    //
//                        Logger.d("position:" + position + " " + CommonUtil.stringForTime(position)
//                                + " duration:" + duration + " " + CommonUtil.stringForTime(duration));
//                    }
                }
            });
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        onDispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }

    /**
     * 按键处理 ( 确认按钮弹出进度信息，左右为快进，倒退， 向下是显示电视剧选集)
     */
    public void onDispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_CENTER: // 回车按键
                if (event.getAction() == KeyEvent.ACTION_UP) { // && hGridview.getVisibility() == View.INVISIBLE) {
                    if (upVideoView.isPlaying()) {
                        upVideoView.pause();
                        showBottomLLayoutAnimate();
                        playStateIv.setImageResource(R.drawable.video_player_start);
                    } else {
                        upVideoView.start();
                        layoutBottomllyt.setVisibility(View.GONE);
                        playStateIv.setImageResource(R.drawable.video_player_pause);
                    }
                    Logger.d("KEYCODE_DPAD_LEFT " + upVideoView.isPlaying());
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (hGridview.getVisibility() == View.INVISIBLE) {

                }
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    upVideoView.seekTo(upVideoView.getCurrentPosition() - SEEK);
                    showBottomLLayoutAnimate();
                } else if (event.getAction() == KeyEvent.ACTION_UP && upVideoView.isPlaying()) {
                    hideBottomLLayoutAnimate();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: // 向右
                if (hGridview.getVisibility() == View.INVISIBLE) {

                }
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    upVideoView.seekTo(upVideoView.getCurrentPosition() + SEEK);
                    showBottomLLayoutAnimate();
                } else if (event.getAction() == KeyEvent.ACTION_UP && upVideoView.isPlaying()) {
                    hideBottomLLayoutAnimate();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN: // 向下
                if (event.getAction() == KeyEvent.ACTION_UP && !mIsMovice) {
                    if (hGridview.getVisibility() == View.INVISIBLE) {
                        hGridview.setVisibility(View.VISIBLE);
                        hGridview.requestFocusFromTouch();
                    } else {
                        hGridview.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }
    }

    private void showBottomLLayoutAnimate() {
        layoutBottomllyt.clearAnimation();
        layoutBottomllyt.animate().cancel();
        layoutBottomllyt.setAlpha(1.0f);
        layoutBottomllyt.setVisibility(View.VISIBLE);
    }

    private void hideBottomLLayoutAnimate() {
        layoutBottomllyt.animate().alpha(0.1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layoutBottomllyt.setVisibility(View.GONE);
                layoutBottomllyt.setAlpha(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                layoutBottomllyt.setAlpha(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).setDuration(5000).start();
    }

    @Override
    public void onBackPressed() {
        if (hGridview.getVisibility() == View.VISIBLE) {
            hGridview.setVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
    }

    //////////////////////// 测试 电视剧集数翻页 //////////////////////////////////////////////////////////////////////

    public void initRecyclerView() {
//        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);

//        lm.setReverseLayout(true);
//        lm.setStackFromEnd(true);

//        hGridview.setLayoutManager(lm);

//        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
//        mLayoutManager.setAutoMeasureEnabled(true);
//        hGridview.setLayoutManager(mLayoutManager);
//        hGridview.getBaseGridViewLayoutManager().setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_PAGE); // 翻页效果.
        if (null != tvData && tvData.size() > 0) {
            hGridview.addItemDecoration(new KeySplitDecoration ());
            hGridview.setAdapter(new BookBaseAdapter());
            hGridview.setVisibility(View.INVISIBLE);
        }
    }

    public class BookBaseAdapter extends RecyclerView.Adapter<BookBaseAdapter.ViewHolder> {


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            view.setTextColor(getResources().getColor(R.color.whilte));
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(120, 120);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    ViewUtils.scaleView(view, b);
                }
            });
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            AutoUtils.auto(view);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position > getItemCount()) {
                holder.itemView.setVisibility(View.GONE);
            }
            Button button = ((Button) holder.itemView);
            button.setText(String.valueOf(position + 1));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upVideoView.setVideoPath(tvData.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return null != tvData ? tvData.size() : 0;
//            return DATA_COUNT + (10 - DATA_COUNT % (10));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }

    }


}