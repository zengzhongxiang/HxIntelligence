package com.adv.hxsoft.widget;

/**
 *
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.adv.hxsoft.R;
import com.adv.hxsoft.util.SpApplyTools;

public class AnimNotiDialog extends Dialog {

    private Context context;
    private FlowRadioGroup anim_group;
    private RadioButton sj_bottom;   //随机效果
    private RadioButton byc_bottom;  //百叶窗
    private RadioButton ccxg_bottom;   //檫除效果
    private RadioButton hzxg_bottom;   //盒装效果
    private RadioButton jtxg_bottom;   //阶梯效果
    private RadioButton lzxg_bottom;   //轮子效果
    private RadioButton plxg_bottom;   //劈裂效果
    private RadioButton qpxg_bottom;   //棋盘效果
    private RadioButton qrxg_bottom;   //切入效果
    private RadioButton sxzkxg_bottom;   //扇形展开效果
    private RadioButton szkzxg_bottom;   //十字扩展效果
    private RadioButton sjxtxg;   //随机线条效果
    private RadioButton xnrjxg_bottom;   //向内溶解效果
    private RadioButton yxkzxg_bottom;   //圆形扩展效果
    private RadioButton lxxg_bottom;   //菱形效果
    private RadioButton wu_bottom;   //无效果

    private EditText time_editText;   //时间
    private Button but_ok;

    public AnimNotiDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AnimNotiDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.anim_notidialog);

        this.setCanceledOnTouchOutside(true);

        time_editText = findViewById (R.id.time_editText);
        but_ok = findViewById (R.id.but_ok);

        anim_group = findViewById (R.id.anim_group);
        sj_bottom = findViewById (R.id.sj_bottom);
        byc_bottom = findViewById (R.id.byc_bottom);
        ccxg_bottom = findViewById (R.id.ccxg_bottom);
        hzxg_bottom = findViewById (R.id.hzxg_bottom);
        jtxg_bottom= findViewById (R.id.jtxg_bottom);
        lzxg_bottom= findViewById (R.id.lzxg_bottom);
        plxg_bottom= findViewById (R.id.plxg_bottom);
        qpxg_bottom= findViewById (R.id.qpxg_bottom);
        qrxg_bottom= findViewById (R.id.qrxg_bottom);
        sxzkxg_bottom = findViewById (R.id.sxzkxg_bottom);
        szkzxg_bottom   = findViewById (R.id.szkzxg_bottom);
        sjxtxg  = findViewById (R.id.sjxtxg);
        xnrjxg_bottom = findViewById (R.id.xnrjxg_bottom);
        yxkzxg_bottom = findViewById (R.id.yxkzxg_bottom);
        lxxg_bottom  = findViewById (R.id.lxxg_bottom);
        wu_bottom = findViewById (R.id.wu_bottom);

        int position = SpApplyTools.getInt (SpApplyTools.IMAGE_ANIM, 0);
        System.out.println ("position=="+position);
        if(position == 0){
            sj_bottom.setChecked (true);
            sj_bottom.requestFocus ();
        }else if(position == 1){
            byc_bottom.setChecked (true);
            byc_bottom.requestFocus ();
        }else if(position == 2){
            ccxg_bottom.setChecked (true);
            ccxg_bottom.requestFocus ();
        }else if(position == 3){
            hzxg_bottom.setChecked (true);
            hzxg_bottom.requestFocus ();
        }else if(position == 4){
            jtxg_bottom.setChecked (true);
            jtxg_bottom.requestFocus ();
        }else if(position == 5){
            lzxg_bottom.setChecked (true);
            lzxg_bottom.requestFocus ();
        }else if(position == 6){
            plxg_bottom.setChecked (true);
            plxg_bottom.requestFocus ();
        }else if(position == 7){
            qpxg_bottom.setChecked (true);
            qpxg_bottom.requestFocus ();
        }else if(position == 8){
            qrxg_bottom.setChecked (true);
            qrxg_bottom.requestFocus ();
        }else if(position == 9){
            sxzkxg_bottom.setChecked (true);
            sxzkxg_bottom.requestFocus ();
        }else if(position == 10){
            szkzxg_bottom.setChecked (true);
            szkzxg_bottom.requestFocus ();
        }else if(position == 11){
            sjxtxg.setChecked (true);
            sjxtxg.requestFocus ();
        }else if(position == 12){
            xnrjxg_bottom.setChecked (true);
            xnrjxg_bottom.requestFocus ();
        }else if(position == 13){
            yxkzxg_bottom.setChecked (true);
            yxkzxg_bottom.requestFocus ();
        }else if(position == 14){
            lxxg_bottom.setChecked (true);
            lxxg_bottom.requestFocus ();
        }else if(position == 15){
            wu_bottom.setChecked (true);
            wu_bottom.requestFocus ();
        }
        anim_group.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = 0;
                if(sj_bottom.getId () == checkedId){  //
                    id = 0;
                }else if(byc_bottom.getId () == checkedId){  //
                    id = 1;
                }else if(ccxg_bottom.getId () == checkedId){  //
                    id = 2;
                }else if(hzxg_bottom.getId () == checkedId){  //
                    id = 3;
                }else if(jtxg_bottom .getId ()== checkedId){  //
                    id = 4;
                }else if( lzxg_bottom.getId ()== checkedId){  //
                    id = 5;
                }else if( plxg_bottom.getId ()== checkedId){  //
                    id = 6;
                }else if( qpxg_bottom.getId ()== checkedId){  //
                    id = 7;
                }else if( qrxg_bottom.getId ()== checkedId){  //
                    id = 8;
                }else if( sxzkxg_bottom.getId () == checkedId){  //
                    id = 9;
                }else if( szkzxg_bottom.getId ()== checkedId){  //
                    id = 10;
                }else if( sjxtxg.getId ()== checkedId){  //
                    id = 11;
                }else if( xnrjxg_bottom.getId ()== checkedId){  //
                    id = 12;
                }else if( yxkzxg_bottom.getId ()== checkedId){  //
                    id = 13;
                }else if( lxxg_bottom.getId ()== checkedId){  //
                    id = 14;
                }else if( wu_bottom.getId ()== checkedId){  //
                    id = 15;
                }
                SpApplyTools.putInt (SpApplyTools.IMAGE_ANIM, id);
                doAnimBack(group,id);
                dismissBalck();
                dismiss ();

            }
        });

        int anim_time = SpApplyTools.getInt (SpApplyTools.IMAGE_ANIM_TIME, 7);
        time_editText.setText (anim_time+"");
        time_editText.setOnFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String timetxt = time_editText.getText ().toString ();
                if(!TextUtils.isEmpty (timetxt)){
                    time_editText.setSelection (timetxt.length ());
                }
            }
        });
        but_ok.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String timetxt = time_editText.getText ().toString ();
                if(!TextUtils.isEmpty (timetxt)){
                    int time = Integer.parseInt (timetxt);
                    if(time<2){
                        time =2;
                    }
                    SpApplyTools.putInt (SpApplyTools.IMAGE_ANIM_TIME, time);
                    doTimeBack(time);
                    dismissBalck();
                    dismiss ();
                }else {
                    Toast.makeText (context,"请输入图片切换时间",Toast.LENGTH_LONG).show ();
                }
            }
        });
    }

    public interface DialogAnimBack {
        void buttonAnimBalck(RadioGroup group, int checkedId);//位置
        void buttonTimeBalck(int time);//时间
    }

    private static DialogAnimBack dialogButtonBack;

    public void setDialogCancelBack(DialogAnimBack dialogButtonBackto) {
        dialogButtonBack = dialogButtonBackto;
    }

    public void doAnimBack(RadioGroup group, int checkedId) {//动画选择
        if (dialogButtonBack != null) {
            dialogButtonBack.buttonAnimBalck (group,checkedId);
        }
    }

    public void doTimeBack(int time) {//时间
        if (dialogButtonBack != null) {
            dialogButtonBack.buttonTimeBalck (time);
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
