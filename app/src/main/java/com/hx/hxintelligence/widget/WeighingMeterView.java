package com.hx.hxintelligence.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hx.hxintelligence.R;

/**
 * Created by will on 2018/7/13.
 */

public class WeighingMeterView extends View {

    //白色圆弧画笔
    private Paint whiteArcPaint;

    //蓝色圆弧画笔
    private Paint blueArcPaint;

    //称重数据画笔
    private Paint weightDataPaint;

    //刻度数据画笔
    private Paint scaleDataPaint;

    //圆弧矩形范围
    private RectF oval;

    //当前称重数据
    private float currentData;

    //圆弧经过的角度范围
    private float sweepAngle = 240;


    public WeighingMeterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        whiteArcPaint = new Paint();
        whiteArcPaint.setAntiAlias(true);
        whiteArcPaint.setColor(Color.parseColor("#000000"));
        whiteArcPaint.setStyle(Paint.Style.STROKE);

        blueArcPaint = new Paint();
        blueArcPaint.setAntiAlias(true);
        blueArcPaint.setColor(Color.parseColor("#5BC1C3"));
        blueArcPaint.setStyle(Paint.Style.STROKE);
        blueArcPaint.setShadowLayer((float)5,(float)5,(float)5,Color.parseColor("#99000000"));

        weightDataPaint = new Paint();
        weightDataPaint.setColor(Color.WHITE);
        weightDataPaint.setTextSize(26);
//        weightDataPaint.setStyle(Paint.Style.STROKE);

        scaleDataPaint = new Paint();
        scaleDataPaint.setColor(Color.WHITE);
        scaleDataPaint.setTextSize(26);

        currentData = 0;

        setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);

        whiteArcPaint.setStrokeWidth(width * (float)0.05);
        blueArcPaint.setStrokeWidth(width * (float)0.052);

        oval = new RectF(width * (float)0.2,width * (float) 0.2,width * (float) 0.8 ,width * (float) 0.8);

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWhiteArc(canvas);
        drawBlueArc(canvas);
        drawArrow(canvas);
        drawWeightingData(canvas);
//        drawScaleData(canvas);

    }

    private void drawWhiteArc(Canvas canvas){
        canvas.save();
        canvas.drawArc(oval,150,sweepAngle,false,whiteArcPaint);
    }

    private void drawBlueArc(Canvas canvas){
        canvas.save();
//        blueArcPaint.setShader(new SweepGradient(getWidth()/2,getHeight()/2,Color.parseColor("#3a84f4"),Color.parseColor("#6094fb")));
//        blueArcPaint.setShader(new SweepGradient(getWidth()/2,getHeight()/2,Color.parseColor("#3a84f4"),Color.parseColor("#cbcbcb")));
        canvas.drawArc(oval,150,sweepAngle * currentData / 300,false,blueArcPaint);
    }

    private void drawArrow(Canvas canvas){
        canvas.save();
        Bitmap oldBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
        int newWidth = (int) (getWidth() * 0.08);
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newWidth) / height;
        Matrix matrix = new Matrix();
        //TODO 下面这两个顺序不能变
        matrix.setRotate(-165 + (sweepAngle * currentData / 300),oldBitmap.getWidth() / 2,oldBitmap.getHeight() / 2);
        matrix.postScale(scaleWidth,scaleHeight);

        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap,0,0,width,height,matrix,true);
        canvas.drawBitmap(newBitmap,getWidth() / 2 - newBitmap.getWidth() / 2, getHeight() / 2 - newBitmap.getHeight() / 2,whiteArcPaint);
        oldBitmap.recycle();
        newBitmap.recycle();
    }

    private void drawWeightingData(Canvas canvas){
        canvas.save();
        Rect rect = new Rect();
        String data = "";
        if(currentData == 0){
            data = "低";
        }else if(currentData == 150){
            data = "中";
        }else if(currentData == 300){
            data = "高";
        }
//        String data = String.valueOf(currentData) + "级";
        weightDataPaint.getTextBounds(data,0,data.length(),rect);
        canvas.drawText(data,getWidth()/2 - rect.width()/2,(int) (getHeight() * (float)0.38),weightDataPaint);
    }

    private void drawScaleData(Canvas canvas){
        canvas.save();
        Rect startScaleText = new Rect();
        String data1 = "风速1";
        scaleDataPaint.getTextBounds(data1,0,data1.length(),startScaleText);
        int height1 = (int) (getWidth() * 0.3 * Math.cos(Math.PI / 6) / 2 + getWidth() * 0.6 * 0.5 + getWidth() * 0.25);
        int width1 = (int) ((getWidth() * 0.2 + getWidth() * 0.3 *0.25) - startScaleText.width() - getWidth() * 0.1);
        canvas.drawText(data1,width1,height1,scaleDataPaint);
        canvas.save();

        Rect endScaleText = new Rect();
        String data2 = "风速4";
        scaleDataPaint.getTextBounds(data2,0,data2.length(),endScaleText);
        int width2 = (int)(getWidth() * 0.8 - getWidth() * 0.3 *0.25 + getWidth() * 0.1);
        canvas.drawText(data2,width2,height1,scaleDataPaint);
        canvas.save();

        Rect middleScaleText = new Rect();
        String data3 = "150KG";
        scaleDataPaint.getTextBounds(data3,0,data3.length(),middleScaleText);
        int width3 = getWidth() / 2 - middleScaleText.width() / 2;
        int height2 = (int) (getWidth() * 0.12);
        canvas.drawText(data3,width3,height2,scaleDataPaint);
        canvas.save();

        Rect weighingText = new Rect();
        String data4 = "风速";
        scaleDataPaint.getTextBounds(data4,0,data4.length(),weighingText);
        int width4 = getWidth() / 2 - weighingText.width() / 2;
        int height = (int) (getWidth() * 0.74);
//        scaleDataPaint.setTextSize(44);
        canvas.drawText(data4,width4,height,scaleDataPaint);
        canvas.save();
    }

    public void setPercentData(final float data, TimeInterpolator interpolator){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(currentData,data);
        valueAnimator.setDuration((long) (Math.abs(currentData-data)*10));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                Log.e("value","value is :" + value);
                currentData=(float)(Math.round(value*10))/10;
                invalidate();
            }
        });
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }

}
