package com.adv.hxsoft.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.adv.hxsoft.APP;
import com.adv.hxsoft.BaseActivity;
import com.adv.hxsoft.R;
import com.adv.hxsoft.util.Constant;
import com.adv.hxsoft.util.SdCardUtil;
import com.adv.hxsoft.util.SpApplyTools;
import com.adv.hxsoft.widget.FullScreenView;
import com.adv.hxsoft.widget.MyNotiDialog;
import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;


@ContentView(R.layout.activity_img_video)
public class ImgVideoActivity extends BaseActivity implements MyNotiDialog.DialogPositionBack {
    @ViewInject(R.id.adv_img)
    private ImageView adv_img;

//    @ViewInject(R.id.advVideo)
//    private FullScreenView mVideoView;
    @ViewInject(R.id.advVideo)
    private FullScreenView film_video;


    private String[] videosUrl;
    private String[] imgesUrl;
    private int screenWidth;
    private int screenHeight;

    private static ImgVideoActivity imgVideo_instance;
    public static ImgVideoActivity getImgVideoActivity() {
        return imgVideo_instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view ().inject (this);
        this.imgVideo_instance = this;
        initData();
    }

    private void initData(){
        screenWidth = APP.getmScreenWidth();
        screenHeight = APP.getmScreenHeight ();
        if(screenWidth>screenHeight){


            int position = SpApplyTools.getInt (SpApplyTools.VIDEO_POSITION, 0);
            positionVideo (position);
        }else {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(screenWidth, screenHeight*2/3);
            adv_img.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams layoutVideoParams =
                    new RelativeLayout.LayoutParams(screenWidth, screenHeight/3);
            layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_BOTTOM);
            film_video.setLayoutParams(layoutVideoParams);
        }
        initVideo();
    }

    private void initVideo(){
        String usbPath = SpApplyTools.getString (SpApplyTools.USB_PATH,"");
        //查找U盘图片路径
        String imagePath = "";
        if(!TextUtils.isEmpty (usbPath)) {
            imagePath = usbPath + Constant.SD_IMAGE_PATH;
        }else {
            imagePath = SdCardUtil.initSdCard().getPath ()+ Constant.SD_IMAGE_PATH;
        }

        List<String> imgList = SdCardUtil.getImagePathFromSD (imagePath);
        if(imgList.size ()>0) {
            imgesUrl = new String[imgList.size ()];
            for (int i = 0; i < imgList.size (); i++) {
                imgesUrl[i] = imgList.get (i);
                System.out.println ("imgUrl=" + imgList.get (i));
            }
        }

        //查询U盘视频路径
        String videoPath = "";
        if(!TextUtils.isEmpty (usbPath)) {
            videoPath = usbPath + Constant.SD_VIDEO_PATH;
        }else {
            videoPath = SdCardUtil.initSdCard().getPath ()+ Constant.SD_VIDEO_PATH;
        }

        List<String> list = SdCardUtil.getVideoPathFromSD(videoPath);

        if(list.size ()>0) {
            videosUrl = new String[list.size ()];
            for (int i = 0; i < list.size (); i++) {
                videosUrl[i] = list.get (i);
                System.out.println ("videosUrl=" + list.get (i));
            }
            brcastVideo();
        }
        inintListener();

        if(imgList.size () == 0 && list.size ()==0){    //说明SD卡里面没有图片和视频
            SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,false);
            SpApplyTools.putString (SpApplyTools.PATHSTRING,"");
            SpApplyTools.putString (SpApplyTools.USB_PATH,"");
        }
    }

    private int i = 0;
    public void inintListener(){
        film_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                i++;
                film_video.stopPlayback();
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

    private void brcastVideo() {
        if (i < videosUrl.length) {
            String videoPath = videosUrl[i];
            File file = new File ((videoPath));
            if (file.exists ()) {
                //把视频的配套图片填充
                String FileEnd = videoPath.substring(videoPath.lastIndexOf("/") + 1,
                        videoPath.lastIndexOf("."));
                System.out.println ("videoPathName == "+FileEnd);
                String imageUrl = GetFilesImg(FileEnd);
                System.out.println ("imageUrl==="+imageUrl);
                if(!TextUtils.isEmpty (imageUrl)){
                    Glide.with (this).load (imageUrl).into (adv_img);
                }
                System.out.println ("videoPath==="+videoPath);
                if(film_video.isPlaying()){
//                    film_video.pause ();
                    film_video.stopPlayback ();
                }
//                film_video.initVideoView(NVideoView.PV_PLAYER__YoukuPlayer);
//                film_video.setDataSource (videoPath);

                film_video.setVideoPath(videoPath);
                film_video.start();
            }
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

    private String GetFilesImg(String fileName)
    {
        String imageUrl = "";
       if(imgesUrl!=null  && imgesUrl.length>0) {
           for (int i = 0; i < imgesUrl.length; i++) {
               String url = imgesUrl[i];
               String FileEnd = url.substring(url.lastIndexOf("/") + 1,
                       url.lastIndexOf("."));
               System.out.println ("fileName=="+fileName);
               if (fileName.equals (FileEnd)) {
                   imageUrl = url;
                   break;
               }
           }
       }
       return imageUrl;
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

    @Override
    public void buttonPositionBalck(RadioGroup group, int checkedId) {
        positionVideo(checkedId);
    }

    private void positionVideo(int position){
        System.out.println ("position=="+position);
        if(position == 4){
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams (screenWidth/2, screenHeight);
            layoutParams.addRule (RelativeLayout.ALIGN_PARENT_LEFT);
            adv_img.setLayoutParams (layoutParams);

            RelativeLayout.LayoutParams layoutVideoParams =
                    new RelativeLayout.LayoutParams (screenWidth / 2, screenHeight);
            layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutVideoParams.addRule (RelativeLayout.CENTER_VERTICAL);
            film_video.setLayoutParams(layoutVideoParams);
        }else {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams (screenWidth, screenHeight);
            adv_img.setLayoutParams (layoutParams);

            RelativeLayout.LayoutParams layoutVideoParams =
                    new RelativeLayout.LayoutParams (screenWidth / 2, screenHeight / 2);
            if (position == 1) {
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_TOP);
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_LEFT);
            } else if (position == 2) {
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_LEFT);
            } else if (position == 3) {
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_TOP);
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_RIGHT);
            } else {
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutVideoParams.addRule (RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            film_video.setLayoutParams(layoutVideoParams);
        }

    }
}
