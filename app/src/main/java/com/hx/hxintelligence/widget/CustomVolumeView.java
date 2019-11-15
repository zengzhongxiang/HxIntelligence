package com.hx.hxintelligence.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomVolumeView extends View {

    private static final int INVALID_X =-1234;
    private static final String TAG = CustomVolumeView.class.getSimpleName();
    private int mPressXIndex = INVALID_X;

    private static final int DEFAULT_TOTAL = 20;
    private int mTotal = DEFAULT_TOTAL;

    private int mBgColor = 0xff000000;
    private int mSelectColor = 0xff5BC1C3;

    private Rect mRect;
    private int mRectWidth = 10;
    private int mRectHeight = 10;
    private int space = 2;
    private int mPrecess = 7;
    private int mTop = 10;
    private int mBottom = mTop + mRectHeight;
    private int mPaddingX = 10;

    private Paint mPaint;

    public CustomVolumeView(Context context) {
        super(context);
        init();
    }

    public CustomVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVolumeView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i =1; i <= mTotal; i++) {
            drawRect(i, canvas);
        }
    }

    /**
     *
     * @param i
     * @param canvas
     */
    private void drawRect(int i, Canvas canvas) {
        int color = i <= mPrecess ? mSelectColor : mBgColor;
        mPaint.setColor(color);
        int startX = getLeft(i);
        canvas.drawRect(getLeft(i), mTop, startX + mRectWidth, mBottom, mPaint);
    }

    private int getLeft(int i) {
        return (i - 1) * (mRectWidth + space) + mPaddingX;
    }

    public void setProcess(int process) {
        this.mPrecess = process;

        invalidate();
    }

    public void setTotal(int total) {
        this.mTotal = total;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();

        Log.i(TAG, "event.getX( = " + event.getX());
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"ACTION_DOWN");
                handleX(x);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"ACTION_MOVE");
                handleX(x);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"ACTION_UP");
                handleX(x);
                break;
        }
        return true;
    }

    private void handleX(float x) {
        int index = getIndexByX(x);

        if (index != mPressXIndex) {
            mPressXIndex = index;

            mPrecess = mPressXIndex;
            if(mListener !=null) {
                mListener.onProcessChanged(mPressXIndex);
            }
        }
        invalidate();
    }

    public interface OnProcessListener {
        void onProcessChanged(int process);
    }

    private OnProcessListener mListener;
    public void setOnProcessListener(OnProcessListener listener) {
        mListener = listener;
    }

    private int getIndexByX(float x) {
        int index = (int) (x - mPaddingX);
        if(index <= 0) {
            return 0;
        }

        index = index / (space + mRectWidth) + 1;

        Log.i(TAG, "getIndexByX x= "+ x + "index = "+index);
        return index;
    }
}
