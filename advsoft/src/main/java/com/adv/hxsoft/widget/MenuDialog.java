package com.adv.hxsoft.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.adv.hxsoft.APP;
import com.adv.hxsoft.BaseActivity;
import com.adv.hxsoft.R;
import com.adv.hxsoft.activity.ImageActivity;
import com.adv.hxsoft.activity.ImgVideoActivity;
import com.adv.hxsoft.util.Display;
import com.adv.hxsoft.util.SDFileUtil;
import com.adv.hxsoft.util.SdCardUtil;
import com.adv.hxsoft.util.SpApplyTools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MenuDialog extends Dialog implements MyNotiDialog.DialogDismissBack, AnimNotiDialog.DialogDismissBack {
	private Display display;
	private Context context;
	private RadioGroup mode_rg;
	private RadioButton images_btn;  //图片
	private RadioButton videos_btn;   //视频
	private RadioButton up_down_btn;  //图片视频
	private Button video_setting_btn;
	private Button image_setting_btn;
	private Button copy_btn;  //复制到本机
	private MyNotiDialog myNotiDialog;
	private AnimNotiDialog animNotiDialog;

	public MenuDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public MenuDialog(Context context,Display display, int theme) {
		super(context, theme);
		this.context = context;
		this.display = display;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
        initfindView();
	}


	@Override
	protected void onStart() {
		super.onStart();
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, 5000);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		handler.removeMessages (0);
		handler.sendEmptyMessageDelayed (0, 5000);
		return super.onKeyDown(keyCode, event);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what){
			case 0:
				dismiss();
				break;
			}
		}
	};

	private void initfindView() {
		mode_rg = findViewById (R.id.mode_rg);
		images_btn = findViewById (R.id.images_btn);
		videos_btn =findViewById (R.id.videos_btn);
		up_down_btn =  findViewById (R.id.up_down_btn);
        copy_btn = findViewById (R.id.copy_btn);

		video_setting_btn  =  findViewById (R.id.video_setting_btn);
		video_setting_btn.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				handler.removeMessages (0);
				myNotiDialog = new MyNotiDialog (context);
				myNotiDialog.show ();
				if(ImgVideoActivity.getImgVideoActivity ()!=null) {
					myNotiDialog.setDialogCancelBack (ImgVideoActivity.getImgVideoActivity ());
				}
				myNotiDialog.setDialogDismissBack (MenuDialog.this);
			}
		});

		image_setting_btn  =  findViewById (R.id.image_setting_btn);
		image_setting_btn.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				handler.removeMessages (0);
				animNotiDialog = new AnimNotiDialog (context,R.style.CustomDialog);
				animNotiDialog.show ();

				WindowManager.LayoutParams params = animNotiDialog.getWindow().getAttributes();
				params.gravity = Gravity.CENTER;
				params.width = display.getWidth();
				params.height = display.getHeight();
				animNotiDialog.getWindow().setAttributes(params);

				if(ImageActivity.getImageActivity ()!=null) {
					animNotiDialog.setDialogCancelBack (ImageActivity.getImageActivity ());
				}
				animNotiDialog.setDialogDismissBack (MenuDialog.this);
			}
		});

        copy_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                copyUPSdCard();
            }
        });


		int player = SpApplyTools.getInt (SpApplyTools.MODE_IMG_VIDEO, 0);
		if(player == 0){
			images_btn.setChecked (true);
			images_btn.requestFocus ();
		}else if(player == 1){
			videos_btn.setChecked (true);
			videos_btn.requestFocus ();
		}else if(player == 2){
			up_down_btn.setChecked (true);
			up_down_btn.requestFocus ();
		}
		mode_rg.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = 0;
				if(images_btn.getId () == checkedId){  //图片
					id = 0;
				}else if(videos_btn.getId () == checkedId){  //视频
					id = 1;
				}else if(up_down_btn.getId () == checkedId){  //上图片下视频
					id = 2;
				}
				SpApplyTools.putInt (SpApplyTools.MODE_IMG_VIDEO, id);
				doPlayerBack(group,id);
			}
		});

	}

	@Override
	public void dismissBalck() {
		handler.removeMessages (0);
		handler.sendEmptyMessageDelayed (0, 5000);
	}


	public interface DialogButtonBack {
		void buttonPlayerBalck(RadioGroup group, int checkedId);//模式选择
	}

	private static DialogButtonBack dialogButtonBack;

	public void setDialogCancelBack(DialogButtonBack dialogButtonBackto) {
		dialogButtonBack = dialogButtonBackto;
	}

	public void doPlayerBack(RadioGroup group, int checkedId) {//模式选择
		if (dialogButtonBack != null) {
			dialogButtonBack.buttonPlayerBalck (group,checkedId);
		}
	}

    private LoadingDialog loadingDialog;
    private void copyUPSdCard(){
        APP app = APP.getApp ();
		System.out.println ("app=="+app);
        if(app!=null){
        	boolean blsd =  SpApplyTools.getBoolean (SpApplyTools.ISUSBSDCARD,false);
            if(blsd){
                String usbFile = SpApplyTools.getString (SpApplyTools.PATHSTRING,"");
                if(!TextUtils.isEmpty (usbFile)){
                    String baseDIR = usbFile+"/yunben";
                    File file = new File (baseDIR);
                    final String sdDIR =  SdCardUtil.initSdCard().getPath ();
                    System.out.println ("sdDIR=="+sdDIR);
                    if (file.exists()) {   //有woss这个目录
                        loadingDialog = new LoadingDialog(context,"正在导入文件，请稍后...",R.mipmap.ic_dialog_loading);
                        loadingDialog.show();

                        SDFileUtil.getInstance (context).copyFileToSD (baseDIR, sdDIR).setFileOperateCallback (new SDFileUtil.FileOperateCallback () {
                            @Override
                            public void onSuccess() {
                                // TODO: 文件复制成功时，主线程回调
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println ("文件复制成功");
                                        loadingDialog.dismiss ();
                                        Toast.makeText (context, "导入文件成功", Toast.LENGTH_LONG).show ();

                                    }
                                }, 20000);//10秒后执行 因为可能APK还没复制成功 0KB的情况
                            }

                            @Override
                            public void onFailed(String error) {
                                // TODO: 文件复制失败时，主线程回调
                                loadingDialog.dismiss ();
                                System.out.println ("文件复制失败");
                                Toast.makeText (context, "导入文件失败", Toast.LENGTH_LONG).show ();
//                                        loadingDialog.dismiss ();
                            }
                        });
                    }else {
                        Toast.makeText (context,"没有找到要导入的文件",Toast.LENGTH_LONG).show ();
                    }
                }else {
                    Toast.makeText (context,"U盘不可用",Toast.LENGTH_LONG).show ();
                }
            }else{
                Toast.makeText (context,"请重新插入U盘...",Toast.LENGTH_LONG).show ();
            }
        }
    }
}
