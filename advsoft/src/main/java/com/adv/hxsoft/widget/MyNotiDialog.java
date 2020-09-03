package com.adv.hxsoft.widget;

/**
 *
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adv.hxsoft.R;
import com.adv.hxsoft.util.SpApplyTools;

public class MyNotiDialog extends Dialog {

    private Context context;
    private FlowRadioGroup position_group;
    private RadioButton p_left_top;
    private RadioButton p_left_bottom;
    private RadioButton p_right_top;
    private RadioButton p_right_bottom;
    private RadioButton p_fenpin;

    public MyNotiDialog(Context context) {
        super(context, R.style.NotiDialog);
        this.context = context;
    }

    public MyNotiDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_notidialog);

        this.setCanceledOnTouchOutside(true);

        position_group = findViewById (R.id.position_group);
        p_left_top = findViewById (R.id.p_left_top);
        p_left_bottom = findViewById (R.id.p_left_bottom);
        p_right_top = findViewById (R.id.p_right_top);
        p_right_bottom = findViewById (R.id.p_right_bottom);
        p_fenpin = findViewById (R.id.p_fenpin);

        int position = SpApplyTools.getInt (SpApplyTools.VIDEO_POSITION, 0);
        if(position == 0){
            p_right_bottom.setChecked (true);
            p_right_bottom.requestFocus ();
        }else if(position == 1){
            p_left_top.setChecked (true);
            p_left_top.requestFocus ();
        }else if(position == 2){
            p_left_bottom.setChecked (true);
            p_left_bottom.requestFocus ();
        }else if(position == 3){
            p_right_top.setChecked (true);
            p_right_top.requestFocus ();
        }else if(position == 4){
            p_fenpin.setChecked (true);
            p_fenpin.requestFocus ();
        }
        position_group.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = 0;
                if(p_right_bottom.getId () == checkedId){  //图片
                    id = 0;
                }else if(p_left_top.getId () == checkedId){  //视频
                    id = 1;
                }else if(p_left_bottom.getId () == checkedId){  //上图片下视频
                    id = 2;
                }else if(p_right_top.getId () == checkedId){  //上图片下视频
                    id = 3;
                }else if(p_fenpin.getId () == checkedId){  //左右分屏
                    id = 4;
                }
                SpApplyTools.putInt (SpApplyTools.VIDEO_POSITION, id);
                doPositionBack(group,id);
                dismissBalck();
                dismiss ();

            }
        });
    }

    public interface DialogPositionBack {
        void buttonPositionBalck(RadioGroup group, int checkedId);//位置
    }

    private static DialogPositionBack dialogButtonBack;

    public void setDialogCancelBack(DialogPositionBack dialogButtonBackto) {
        dialogButtonBack = dialogButtonBackto;
    }

    public void doPositionBack(RadioGroup group, int checkedId) {//模式选择
        if (dialogButtonBack != null) {
            dialogButtonBack.buttonPositionBalck (group,checkedId);
        }
    }

    public interface DialogDismissBack {
        void dismissBalck();//关闭对话框
    }

    private static DialogDismissBack dialogDismissBack;

    public void setDialogDismissBack(DialogDismissBack dialogDismissBackto) {
        dialogDismissBack = dialogDismissBackto;
    }

    public void dismissBalck() {//消失对话框
        if (dialogDismissBack != null) {
            dialogDismissBack.dismissBalck ();
        }
    }
}
