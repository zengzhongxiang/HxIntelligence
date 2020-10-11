package com.adv.hxsoft.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.adv.hxsoft.BaseActivity;
import com.adv.hxsoft.R;
import com.adv.hxsoft.util.Constant;
import com.adv.hxsoft.util.SdCardUtil;
import com.adv.hxsoft.util.SpApplyTools;
import com.adv.hxsoft.widget.FullScreenView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

@ContentView(value = R.layout.activity_video)
public class VideoActivity extends BaseActivity {
    @ViewInject(R.id.advVideo)
    private FullScreenView film_video;

    private String[] videosUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view ().inject (this);
        initData();
    }
    private void initData(){
        String usbPath = SpApplyTools.getString (SpApplyTools.USB_PATH,"");
        String path = "";
        if(!TextUtils.isEmpty (usbPath)) {
            path = usbPath + Constant.SD_VIDEO_PATH;
        }else {
            path = SdCardUtil.initSdCard().getPath ()+ Constant.SD_VIDEO_PATH;
        }

        List<String> list = SdCardUtil.getVideoPathFromSD(path);
        if(list.size ()>0) {
            videosUrl = new String[list.size ()];
            for (int i = 0; i < list.size (); i++) {
                videosUrl[i] = list.get (i);
                System.out.println ("videosUrl=" + list.get (i));
            }
            brcastVideo();
        }else{
            SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,false);
            SpApplyTools.putString (SpApplyTools.PATHSTRING,"");
            SpApplyTools.putString (SpApplyTools.USB_PATH,"");
        }
        inintListener();
    }

    private int i = 0;
    public void inintListener(){

        film_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                i++;
                film_video.pause ();;
                if (videosUrl !=null && i >= videosUrl.length) {
                    i = 0;
                    brcastVideo();
                } else {
                    brcastVideo();
                }
            }
        });
//        film_video.setOnCompletionListener (new IMediaPlayer.OnCompletionListener () {
//            @Override
//            public void onCompletion(IMediaPlayer mp) {
//                i++;
//                film_video.pause ();;
//                if (videosUrl !=null && i >= videosUrl.length) {
//                    i = 0;
//                    brcastVideo();
//                } else {
//                    brcastVideo();
//                }
//            }
//        });

    }

    public void brcastVideo() {
        if (i < videosUrl.length) {
            String path = videosUrl[i];
            System.out.println ("path=="+path);
            File file = new File ((path));
            if (file.exists ()) {
                if(film_video.isPlaying()){
//                    film_video.pause ();
                    film_video.stopPlayback ();
                }
//                film_video.initVideoView(NVideoView.PV_PLAYER__YoukuPlayer);
//                film_video.setDataSource (path);

                film_video.setVideoPath(path);
                film_video.start();
            }
//            System.out.println ("film_video=="+film_video);
//            film_video.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//
//                @Override
//                public void onPrepared(IMediaPlayer arg0) {
//                    System.out.println ("path=="+film_video.getDuration()+"");
//                    film_video.seekTo(1);
//                    film_video.start();
//                }
//            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        closeMedia();
    }

    private void closeMedia() {
        try {
            if (film_video != null) {
                if (film_video.isPlaying ()) {
                    film_video.stopPlayback ();
                    film_video.suspend();
                    film_video.setOnCompletionListener(null);
                    film_video.setOnPreparedListener(null);
                    film_video = null;

                }
            }
        }catch (Exception e){

        }
//        try {
//            if (null != film_video) {
//                // 提前标志为false,防止在视频停止时，线程仍在运行。
//                // 如果正在播放，则停止。
//                if (film_video.isPlaying ()) {
//                    // videoView.stop();
//                    film_video.pause ();
//                }
//                // 释放mediaPlayer
//                System.out.println ("我销毁了");
//                film_video.release ();
//                film_video = null;
//            }
//        } catch (Exception e) {
//            System.out.println ("我异常销毁了");
//            e.printStackTrace ();
//        }
    }
}
