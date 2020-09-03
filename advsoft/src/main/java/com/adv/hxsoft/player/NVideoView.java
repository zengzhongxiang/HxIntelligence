//package com.adv.hxsoft.player;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import java.io.FileDescriptor;
//import java.util.Map;
//
//import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
//import tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//import tv.danmaku.ijk.media.player.misc.IMediaFormat;
//import tv.danmaku.ijk.media.player.misc.ITrackInfo;
//import tv.danmaku.ijk.media.player.misc.IjkMediaFormat;
//
///**
// * Created by sh on 16/7/13.
// */
//
//public class NVideoView extends SurfaceView {
//    private String TAG = "NVideoView";
//    // settable by the client
//    private Uri mUri;
//    private Map<String, String> mHeaders;
//
//    // all possible internal states
//    private static final int STATE_ERROR = -1;
//    private static final int STATE_IDLE = 0;
//    private static final int STATE_PREPARING = 1;
//    private static final int STATE_PREPARED = 2;
//    private static final int STATE_PLAYING = 3;
//    private static final int STATE_PAUSED = 4;
//    private static final int STATE_PLAYBACK_COMPLETED = 5;
//
//    public static final int PV_PLAYER__Auto = 0;
//    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
//    public static final int PV_PLAYER__IjkMediaPlayer = 2;
//    public static final int PV_PLAYER__YoukuPlayer = 3;
//
//    private int pv_player_type = PV_PLAYER__Auto;
//
//    private SurfaceHolder mSurfaceHolder = null;
//    private IMediaPlayer mMediaPlayer = null;
//
//    private OnPreparedListener mPreparedListener;
//    private OnCompletionListener mCompletionListener;
//    private OnErrorListener mErrorListener;
//    private OnInfoListener mInfoListener;
//    private OnBufferingUpdateListener mBufferingUpdateListener;
//    private OnSeekCompleteListener mSeekCompleteListener;
//
//    private String url = "";
//
//    private Context mContext;
//
//    int a = 0;
//
//    public NVideoView(Context context) {
//        super(context);
//        mContext = context;
//    }
//
//    public NVideoView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//    }
//
//    public NVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mContext = context;
//    }
//
//    public void initVideoView(int player_type) {
//
//        initPlayer(player_type);
//
//        getHolder().addCallback(new SurfaceHolder.Callback() {
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                mSurfaceHolder = null;
//                pause();
//                // release();
////				Log.e("shenfei", "surfaceDestroyed");
//            }
//
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
////				Log.e("shenfei", "surfaceCreated");
//                mSurfaceHolder = holder;
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.setDisplay(mSurfaceHolder);
//                    mMediaPlayer.setScreenOnWhilePlaying(true);
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
////				Log.e("shenfei", "surfaceChanged");
//
//
//            }
//
//        });
//
//        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        // setFocusable(true);
//        // setFocusableInTouchMode(true);
//        // requestFocus();
//    }
//
//    private void initPlayer(int player_type) {
//        // if (mSurfaceHolder == null) {
//        // return;
//        // }
//
//        release();
//
//        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
////        am.setStreamVolume(AudioManager.STREAM_SYSTEM, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
//        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//
//        mMediaPlayer = createPlayer(player_type);
//        mMediaPlayer.setOnErrorListener (new OnErrorListener () {
//            @Override
//            public boolean onError(IMediaPlayer mp, int what, int extra) {
//                System.out.println ("what=="+what+"   extra=="+extra);
//                return false;
//            }
//        });
//
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//
//
//
//    }
//
//    public void reset() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
//        }
//    }
//
//    /*
//     * release the media player in any state
//     */
//    public void release() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//            am.abandonAudioFocus(null);
//        }
//    }
//
//    public IMediaPlayer createPlayer(int playerType) {
//
//        pv_player_type = playerType;
//
//        IMediaPlayer mediaPlayer = null;
//
//        switch (playerType) {
//            case PV_PLAYER__IjkMediaPlayer: {
//                IjkMediaPlayer ijkMediaPlayer = null;
//                ijkMediaPlayer = new IjkMediaPlayer ();
//                // ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
//
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 0);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//
//                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//                mediaPlayer = ijkMediaPlayer;
//            }
//            break;
//            case PV_PLAYER__AndroidMediaPlayer: {
//                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer ();
//                mediaPlayer = androidMediaPlayer;
//
//            }
//            break;
//            case PV_PLAYER__YoukuPlayer: {
//                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer ();
//                mediaPlayer = androidMediaPlayer;
//
//
//            }
//            break;
//            default: {
//                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer ();
//                mediaPlayer = androidMediaPlayer;
//            }
//            break;
//        }
//
//        return mediaPlayer;
//    }
//
//    public void optionPlayer(IMediaPlayer mMediaPlayer) {
//        // ((IjkMediaPlayer)
//        // mMediaPlayer).native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format",
//                IjkMediaPlayer.SDL_FCC_RV32);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 1);
//
//        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//    }
//
//    public void setDataSource(String url) {
//        if (mMediaPlayer == null || TextUtils.isEmpty(url)) {
//            return;
//        }
//        this.url = url;
//        try {
//            if (!"".equals(this.url)) {
//                mMediaPlayer.setDataSource(this.url);
//                prepareAsync();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setDataSource(FileDescriptor fd) {
//
//        try {
//            if (fd!=null) {
//                mMediaPlayer.setDataSource(fd);
//                prepareAsync();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setDataSource(Context context,Uri uri) {
//
//        try {
//            if (uri!=null) {
//                mMediaPlayer.setDataSource(context,uri);
//                prepareAsync();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    public void setDataSource(FileDescriptor fd, long s, long l) {
////        if (mMediaPlayer == null) {
////            return;
////        }
////        // this.url = url;
////        // if (!this.url.equals("")) {
////
////        try {
////            mMediaPlayer.setDataSource(fd, s, l);
////            prepareAsync();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        // }
////    }
//
//    public void changeDataSource(String url) {
//        if (mMediaPlayer == null) {
//            return;
//        }
//        this.url = url;
//        mMediaPlayer.reset();
//        if (pv_player_type == PV_PLAYER__IjkMediaPlayer) {
//            optionPlayer(mMediaPlayer);
//            mMediaPlayer.setDisplay(mSurfaceHolder);
//        } else if (pv_player_type == PV_PLAYER__AndroidMediaPlayer) {
//            mMediaPlayer.setOnPreparedListener(mPreparedListener);
//            mMediaPlayer.setOnInfoListener(mInfoListener);
//            // mMediaPlayer.setOnCompletionListener(mCompletionListener);
//            mMediaPlayer.setOnErrorListener(mErrorListener);
//            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
//            mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
//        }
//        try {
//            mMediaPlayer.setDataSource(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        prepareAsync();
//    }
//
//    public IMediaPlayer getMediaPlayer() {
//        return mMediaPlayer;
//    }
//
//    public void prepareAsync() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.prepareAsync();
//        }
//    }
//
//    public void start() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.start();
//            mMediaPlayer.setOnCompletionListener(mCompletionListener);
//        }
//    }
//
//    public void pause() {
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//            mMediaPlayer.pause();
//        }
//    }
//
//    public int getDuration() {
//        if (mMediaPlayer != null) {
//            return (int) mMediaPlayer.getDuration();
//        } else {
//            return 0;
//        }
//
//    }
//
//    public int getCurrentPosition() {
//        if (mMediaPlayer != null) {
//            return (int) mMediaPlayer.getCurrentPosition();
//        } else {
//            return 0;
//        }
//    }
//
//    public void seekTo(int msec) {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.seekTo(msec);
//        }
//    }
//
//    public boolean isPlaying() {
//        if (mMediaPlayer != null) {
//            return mMediaPlayer.isPlaying();
//        } else {
//            return false;
//        }
//    }
//
//    public int getAudioSessionId() {
//        if (mMediaPlayer != null) {
//            return mMediaPlayer.getAudioSessionId();
//        }
//        return 0;
//    }
//
//    // private OnPreparedListener preparedListener = new OnPreparedListener() {
//    //
//    // @Override
//    // public void onPrepared(IMediaPlayer mp) {
//    // start();
//    // }
//    // };
//    //
//    // private OnInfoListener infoListener = new OnInfoListener() {
//    //
//    // @Override
//    // public boolean onInfo(IMediaPlayer mp, int what, int extra) {
//    // switch (what) {
//    // case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
//    // Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//    // Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
//    // Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//    // Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//    // Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + extra);
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
//    // Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
//    // Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
//    // Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
//    // Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
//    // Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
//    // Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + extra);
//    // break;
//    // case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
//    // Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
//    // break;
//    // }
//    // return true;
//    // }
//    // };
//
//    public float getVideoOutputFramesPerSecond() {
//        if (mMediaPlayer != null && pv_player_type == PV_PLAYER__IjkMediaPlayer) {
//            return ((IjkMediaPlayer) mMediaPlayer).getVideoOutputFramesPerSecond();
//        } else {
//            return 0.0f;
//        }
//    }
//
//    public float getVideoDecodeFramesPerSecond() {
//        if (mMediaPlayer != null && pv_player_type == PV_PLAYER__IjkMediaPlayer) {
//            return ((IjkMediaPlayer) mMediaPlayer).getVideoDecodeFramesPerSecond();
//        } else {
//            return 0.0f;
//        }
//    }
//
//    public String getVideoByteRate() {
//        String byteRate = "";
//        ITrackInfo trackInfos[] = mMediaPlayer.getTrackInfo();
//        if (trackInfos != null) {
//            int index = -1;
//            for (ITrackInfo trackInfo : trackInfos) {
//                index++;
//
//                int trackType = trackInfo.getTrackType();
//                IMediaFormat mediaFormat = trackInfo.getFormat();
//                if (mediaFormat == null) {
//                } else if (mediaFormat instanceof IjkMediaFormat) {
//                    switch (trackType) {
//                        case ITrackInfo.MEDIA_TRACK_TYPE_VIDEO:
//                            byteRate = mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI);
//                            break;
//                        case ITrackInfo.MEDIA_TRACK_TYPE_AUDIO:
//                            break;
//                    }
//                }
//            }
//        }
//        byteRate = byteRate.substring(0, byteRate.length()-5);
//        float a = Integer.parseInt(byteRate) / 8.0f ;
//        return a + "kB/s";
//    }
//
//    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
//        mPreparedListener = l;
//        System.out.println ("mMediaPlayer=="+mMediaPlayer);
//        mMediaPlayer.setOnPreparedListener(l);
//    }
//
//    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
//        mCompletionListener = l;
//        // mMediaPlayer.setOnCompletionListener(l);
//    }
//
//    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
//        mErrorListener = l;
//        mMediaPlayer.setOnErrorListener(l);
//    }
//
//    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
//        mInfoListener = l;
//        mMediaPlayer.setOnInfoListener(l);
//    }
//
//    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
//        mBufferingUpdateListener = l;
//        mMediaPlayer.setOnBufferingUpdateListener(l);
//    }
//
//    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener l) {
//        mSeekCompleteListener = l;
//        mMediaPlayer.setOnSeekCompleteListener(l);
//    }
//}
