package com.yjkmust.fourleafclover.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yjkmust.fourleafclover.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 11432 on 2018/7/31.
 */

public class RadarView extends View {
    //默认的主题颜色
    private int DEFAULT_COLOR = Color.parseColor("#00CC99");
    //圆圈和交叉线的颜色
    private int mCircleColor = DEFAULT_COLOR;
    //雷达的圆圈数量
    private int mCircleNum = 3;
    //扫描的颜色 RadarView会对这个颜色做渐变透明处理
    private int mSweepColor = DEFAULT_COLOR;
    //水滴的颜色
    private int mRainDropColor = DEFAULT_COLOR;
    //水滴最多同时能出现的数量
    private int mRaindropNum = 4;
    //是否显示交叉线
    private boolean isShowCross = true;
    //是否显示水滴
    private boolean isShowRaindrop = true;
    //扫描的速度，表示多少秒转一圈
    private float mSpeed = 3.0f;
    //水滴显示和消失的速度
    private float mFlicker = 3.0f;

    //圆的画笔
    private Paint mCirclePaint;// 圆的画笔
    //扫描效果的画笔
    private Paint mSweepPaint; //扫描效果的画笔
    //水滴的画笔
    private Paint mRaindropPaint; //水滴的画笔
    //是否扫描
    private boolean isScan = true;
    //扫描时的扫描旋转角度
    private float mDegrees;
    //保存水滴数据
    private ArrayList<Raindrop> listRaindrop = new ArrayList<>();

    public RadarView(Context context) {
        super(context, null);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultSize = dp2px(getContext(), 200);
        setMeasuredDimension(measureWidth(widthMeasureSpec, defaultSize), measureHeight(heightMeasureSpec, defaultSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算圆的半径
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;
        //计算圆的圆心
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        drawCircle(canvas, cx, cy, radius);
        if (isShowCross) {
            drawCross(canvas, cx, cy, radius);
        }
        if (isScan){
            if (isShowRaindrop){
                drawRaindrop(canvas,cx,cy,radius);
            }
            drawSweep(canvas, cx, cy, radius);
            //计算雷达扫描的旋转角度
            mDegrees = (mDegrees + (360 / mSpeed / 60)) % 360;
            //触发View重新绘制，通过不断的绘制View的扫描动画效果
            invalidate();
        }


    }

    /**
     * 获取自定义属性值
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            mCircleColor = mTypedArray.getColor(R.styleable.RadarView_circleColor, DEFAULT_COLOR);
            mCircleNum = mTypedArray.getInt(R.styleable.RadarView_circleNum, mCircleNum);
            if (mCircleNum < 1) {
                mCircleNum = 3;
            }
            mSweepColor = mTypedArray.getColor(R.styleable.RadarView_sweepColor, DEFAULT_COLOR);
            mRainDropColor = mTypedArray.getColor(R.styleable.RadarView_raindropColor, DEFAULT_COLOR);
            mRaindropNum = mTypedArray.getInt(R.styleable.RadarView_raindropNum, mRaindropNum);
            isShowCross = mTypedArray.getBoolean(R.styleable.RadarView_showCross, true);
            isShowRaindrop = mTypedArray.getBoolean(R.styleable.RadarView_showRaindrop, true);
            mSpeed = mTypedArray.getFloat(R.styleable.RadarView_speed, mSpeed);
            if (mSpeed <= 0) {
                mSpeed = 3;
            }
            mFlicker = mTypedArray.getFloat(R.styleable.RadarView_flicker, mFlicker);
            if (mFlicker <= 0) {
                mFlicker = 3;
            }
            mTypedArray.recycle();
        }
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mRaindropPaint = new Paint();
        mRaindropPaint.setStyle(Paint.Style.FILL);
        mRaindropPaint.setAntiAlias(true);

        mSweepPaint = new Paint();
        mSweepPaint.setAntiAlias(true);
    }

    //测量宽
    private int measureWidth(int measureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;
    }

    private int measureHeight(int measureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas, int cx, int cy, int radius) {
        //画mCircleNum个半径不等的圆圈。
        for (int i = 0; i < mCircleNum; i++) {
            canvas.drawCircle(cx, cy, radius - (radius / mCircleNum * i), mCirclePaint);
        }
    }

    /**
     * 画交叉线
     */
    private void drawCross(Canvas canvas, int cx, int cy, int radius) {
        canvas.drawLine(cx - radius, cy, cx + radius, cy, mCirclePaint);
        canvas.drawLine(cx, cy - radius, cx, cy + radius, mCirclePaint);
    }

    /**
     * 画扫描效果
     */
    private void drawSweep(Canvas canvas, int cx, int cy, int radius) {
        //扇形的透明的渐变效果
        SweepGradient sweepGradient = new SweepGradient(cx, cy,
                new int[]{Color.TRANSPARENT, changeAlpha(mSweepColor, 0), changeAlpha(mSweepColor, 168),
                        changeAlpha(mSweepColor, 255), changeAlpha(mSweepColor, 255)
                }, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});
        mSweepPaint.setShader(sweepGradient);
        //先旋转画布，再绘制扫描的颜色渲染，实现扫描时的旋转效果。
        canvas.rotate(-90 + mDegrees, cx, cy);
        canvas.drawCircle(cx, cy, radius, mSweepPaint);
    }
    /**
     * 画雨点（扫描中随机出现的点）
     */
    private void drawRaindrop(Canvas canvas, int cx, int cy, int radius){
        generateRaindrop(cx, cy, radius);
        for (Raindrop raindrop  : listRaindrop){
            mRaindropPaint.setColor(raindrop.changeAlpha());
            canvas.drawCircle(raindrop.x, raindrop.y, raindrop.radius, mRaindropPaint);
            //水滴的扩散和透明的渐变效果
            raindrop.radius += 1.0f * 20 / 60 / mFlicker;
            raindrop.alpha -= 1.0f * 255 / 60 / mFlicker;
        }
        removeRaindrop();
    }


    /**
     * 水滴数据类
     */
    private static class Raindrop {
        int x;
        int y;
        float radius;
        int color;
        float alpha = 255;

        public Raindrop(int x, int y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        /**
         * 获取改变透明度后的颜色值
         *
         * @return
         */
        public int changeAlpha() {
            return RadarView.changeAlpha(color, (int) alpha);
        }
    }

    /**
     * 水滴随机生成，并不是每次的调用都会生成一个水滴
     */
    private void generateRaindrop(int cx, int cy, int radius) {
        if (listRaindrop.size() < mRaindropNum) {
            //随机产生20以内的数字，如果这个数字是0，就产生一个水滴，控制水滴的生成概率
            boolean probability = (int) (Math.random() * 20) == 0;
            if (probability) {
                int x = 0;
                int y = 0;
                int xOffset = (int) (Math.random() * (radius - 20));
                int yOffset = (int) (Math.random() * (int) Math.sqrt(1.0 * (radius - 20) * (radius - 20) - xOffset * xOffset));

                if ((int) (Math.random() * 2) == 0) {
                    x = cx - xOffset;
                } else {
                    x = cx + xOffset;
                }
                if ((int) (Math.random() * 2) == 0) {
                    y = cy - yOffset;
                }else {
                    y = cy + yOffset;
                }
                listRaindrop.add(new Raindrop(x, y, 0, mRainDropColor));

            }

        }
    }
    /**
     * 删除雨滴
     */
    private void removeRaindrop(){
        Iterator<Raindrop> iterator = listRaindrop.iterator();
        while (iterator.hasNext()){
            Raindrop raindrop = iterator.next();
            if (raindrop.radius>20||raindrop.alpha<0){
                iterator.remove();
            }
        }
    }



    /**
     * a
     * 改变颜色的透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * dp转px
     */
    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    /**
     * 开始扫描
     */
    public void start() {
        if (!isScan) {
            isScan = true;
            invalidate();
        }
    }

    /**
     * 停止扫描
     */
    public void stop() {
        if (isScan) {
            isScan = false;
            listRaindrop.clear();
            mDegrees = 0.0f;
        }
    }
}
