package com.adv.hxsoft.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.adv.hxsoft.BaseActivity;
import com.adv.hxsoft.R;
import com.adv.hxsoft.anim.Anim;
import com.adv.hxsoft.anim.AnimBaiYeChuang;
import com.adv.hxsoft.anim.AnimCaChu;
import com.adv.hxsoft.anim.AnimHeZhuang;
import com.adv.hxsoft.anim.AnimJieTi;
import com.adv.hxsoft.anim.AnimLingXing;
import com.adv.hxsoft.anim.AnimLunZi;
import com.adv.hxsoft.anim.AnimPiLie;
import com.adv.hxsoft.anim.AnimQiPan;
import com.adv.hxsoft.anim.AnimQieRu;
import com.adv.hxsoft.anim.AnimShanXingZhanKai;
import com.adv.hxsoft.anim.AnimShiZiXingKuoZhan;
import com.adv.hxsoft.anim.AnimSuiJiXianTiao;
import com.adv.hxsoft.anim.AnimXiangNeiRongJie;
import com.adv.hxsoft.anim.AnimYuanXingKuoZhan;
import com.adv.hxsoft.anim.EnterAnimLayout;
import com.adv.hxsoft.util.Constant;
import com.adv.hxsoft.util.SdCardUtil;
import com.adv.hxsoft.util.SpApplyTools;
import com.adv.hxsoft.widget.AnimNotiDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Random;


@ContentView(R.layout.activity_main)
public class ImageActivity extends BaseActivity implements AnimNotiDialog.DialogAnimBack {
    @ViewInject(R.id.flipper)
    private AdapterViewFlipper flipper;
    @ViewInject(R.id.image_anim)
    private EnterAnimLayout image_anim;
    private int animNum = 0;
    private int anim_time = 7;
    private static ImageActivity image_instance;
    public static ImageActivity getImageActivity() {
        return image_instance;
    }


    private int[] imageIds = new int[]{
            R.mipmap.bg_0, R.mipmap.bg_1, R.mipmap.bg_2, R.mipmap.bg_3, R.mipmap.bg_4, R.mipmap.bg_5,
            R.mipmap.bg_6, R.mipmap.bg_7, R.mipmap.bg_8, R.mipmap.bg_9,
    };

    private String[] imageUrl;
    private boolean isOneImag = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.image_instance = this;
        x.view ().inject (this);
        initView();
    }

    private void initView(){
        animNum = SpApplyTools.getInt (SpApplyTools.IMAGE_ANIM, 0);
        anim_time = SpApplyTools.getInt (SpApplyTools.IMAGE_ANIM_TIME, 7);
        flipper.setFlipInterval (anim_time*1000);

        String usbPath = SpApplyTools.getString (SpApplyTools.USB_PATH,"");
        String path = "";
        if(!TextUtils.isEmpty (usbPath)) {
             path = usbPath + Constant.SD_IMAGE_PATH;
        }else {
             path = SdCardUtil.initSdCard().getPath ()+ Constant.SD_IMAGE_PATH;
        }
        System.out.println ("path=="+path);
        List<String> list = SdCardUtil.getImagePathFromSD(path);
        System.out.println ("list=="+list);
        if(list.size ()>0) {
            imageUrl = new String[list.size ()];
            for (int i = 0; i < list.size (); i++) {
                imageUrl[i] = list.get (i);
                System.out.println ("Homepath=" + list.get (i));
            }
            initData (imageUrl.length);
        }else{
            initData (imageIds.length);

            SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,false);
            SpApplyTools.putString (SpApplyTools.PATHSTRING,"");
            SpApplyTools.putString (SpApplyTools.USB_PATH,"");
        }

    }


    private BaseAdapter adapter;

    private void initData(final int imgLeng) {
        if (adapter == null && imgLeng > 0) {
            adapter = new BaseAdapter () {
                @Override
                public int getCount() {
                    return imgLeng;
                }

                @Override
                public Object getItem(int position) {
                    return position;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    //创建一个ImageView
                    ImageView imageView = new ImageView (ImageActivity.this);
                    if (imageUrl != null && imageUrl.length > 0) {
                        // 将图片显示到ImageView中
//                        Bitmap bm = BitmapFactory.decodeFile(imageUrl[position]);
//                        imageView.setImageBitmap(bm);
                        Glide.with (ImageActivity.this).load (imageUrl[position]).diskCacheStrategy(DiskCacheStrategy.NONE).into (imageView);
                    } else {
                        Glide.with (ImageActivity.this).load (imageIds[position]).into (imageView);
//                        imageView.setImageResource (imageIds[position]);
                    }
                    //设置ImageView的缩放类型
                    imageView.setScaleType (ImageView.ScaleType.FIT_XY);
                    //为ImageView设置布局参数
                    imageView.setLayoutParams (new ViewGroup.LayoutParams (
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    Animation animation;

                    if (isOneImag) {
                        isOneImag = false;
//                        animation = AnimationUtils.loadAnimation (MainActivity.this, R.anim.start_one);
//                        imageView.startAnimation (animation);
                    } else {
//                        animation = AnimationUtils.loadAnimation (MainActivity.this, R.anim.start);
//                        imageView.startAnimation (animation);
                        animNum(animNum);

                    }
                    return imageView;
                }
            };
            flipper.setAdapter (adapter);
            flipper.startFlipping ();
        }
    }

    private void animNum(int num){
        System.out.println ("num==="+num);
        if(num == 15){  //无动画

        }else{
            int randomNum = 0;
            if(num > 0) {
                randomNum = num -1;
            }else{  //随机动画
                Random random = new Random ();
                randomNum = random.nextInt (14);
            }
            System.out.println ("randomNum==="+randomNum);
            Anim anim = initAnim (randomNum);
            if (anim != null) {
                anim.startAnimation (2000);
            }
        }
    }

    private Anim initAnim(int num) {
//        System.gc ();
        Anim anim;
        switch (num) {
            case 0:
                anim = new AnimBaiYeChuang (image_anim);  //百叶窗
                break;
            case 1:
                anim = new AnimCaChu (image_anim);    //檫除效果
                break;
            case 2:
                anim = new AnimHeZhuang (image_anim); //盒装效果
                break;
            case 3:
                anim = new AnimJieTi (image_anim);  //阶梯效果
                break;
            case 4:
                anim = new AnimLunZi (image_anim);  //轮子效果
                break;
            case 5:
                anim = new AnimPiLie (image_anim);  //劈裂效果
                break;
            case 6:
                anim = new AnimQiPan (image_anim);   //棋盘效果
                break;
            case 7:
                anim = new AnimQieRu (image_anim);  //切入效果
                break;
            case 8:
                anim = new AnimShanXingZhanKai (image_anim);  //扇形展开效果
                break;
            case 9:
                anim = new AnimShiZiXingKuoZhan (image_anim);   //十字扩展效果
                break;
            case 10:
                anim = new AnimSuiJiXianTiao (image_anim);   //随机线条效果
                break;
            case 11:
                anim = new AnimXiangNeiRongJie (image_anim);   //向内溶解效果
                break;
            case 12:
                anim = new AnimYuanXingKuoZhan (image_anim);    //圆形扩展效果
                break;
            default:
                anim = new AnimLingXing (image_anim);   //菱形效果
                break;

        }
        return anim;
    }

    @Override
    public void buttonAnimBalck(RadioGroup group, int checkedId) {
        animNum = checkedId;
    }

    @Override
    public void buttonTimeBalck(int time) {
        anim_time = time;
        flipper.setFlipInterval (anim_time*1000);
    }
}
