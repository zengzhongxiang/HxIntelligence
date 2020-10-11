package com.app.ybiptv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * 类名：ScrollTextView
 * 描述：自动滚动的TextView,跑马灯效果
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ScrollTextView extends TextView {

    /** 文字长度 */
    private float textLength = 0f;
    /** 滚动条长度 */
    private float viewWidth = 0f;
    /** 文本x轴 的坐标 */
    private float tx = 0f;
    /** 文本Y轴的坐标 */
    private float ty = 0f;
    /** 文本当前长度 */
    private float temp_tx1 = 0.0f;
    /** 文本当前变换的长度 */
    private float temp_tx2 = 0x0f;
    /** 文本滚动开关 */
    private boolean isStarting = false;
    /** 画笔对象 */
    private Paint paint = null;
    /** 显示的文字 */
    private String mText = "";
    /** 文本滚动速度 **/
    private float sudu = 1;

    public ScrollTextView(Context context) {
        super(context);
        initView();
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        // 得到画笔,获取父类的textPaint
        paint = this.getPaint();
        setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setSingleLine(true);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public void setText(String text, float su) {
        mText = text;
        sudu = su;
        textLength = paint.measureText(text);// 获得当前文本字符串长度

        viewWidth = this.getWidth();// 获取宽度return mRight - mLeft;
        if (viewWidth == 0) {
            // 获取当前屏幕的属性
            viewWidth = AutoLayoutConifg.getInstance().getScreenWidth();// 获取屏幕宽度  viewWidth 是滚动的开始位置，需要修改的
        }
        tx = textLength;
        temp_tx1 = viewWidth + textLength;
        temp_tx2 = viewWidth + textLength * 2;// 自己定义，文字变化多少
        // // 文字的大小+距顶部的距离
        ty = this.getTextSize() + this.getPaddingTop();
    }

    /**
     * 开始滚动
     */
    public void starScroll() {
        // 开始滚动
        isStarting = true;
        this.invalidate();// 刷新屏幕
    }

    /**
     * 停止方法,停止滚动
     */
    public void stopScroll() {
        // 停止滚动
        isStarting = false;
        this.invalidate();// 刷新屏幕
    }

    /** 重写onDraw方法 */
    @Override
    protected void onDraw(Canvas canvas) {
        if (isStarting) {
            // A-Alpha透明度/R-Read红色/g-Green绿色/b-Blue蓝色
//            paint.setARGB(255, 200, 200, 200);
            paint.setShadowLayer(4, 0, 4, Color.BLACK);//阴影
            canvas.drawText(this.mText, temp_tx1 - tx, ty, paint);
            tx += sudu;
            // 当文字滚动到屏幕的最左边
            if (tx > temp_tx2) {
                // 把文字设置到最右边开始
                tx = textLength;
            }
            this.invalidate();// 刷新屏幕
        }
        super.onDraw(canvas);
    }

}
