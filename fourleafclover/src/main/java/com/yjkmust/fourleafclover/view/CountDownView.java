package com.yjkmust.fourleafclover.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yjkmust.fourleafclover.R;

/**
 * Created by yj on 2018/7/30.
 */

public class CountDownView extends View {
    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final float BORDER_WIDTH = 15f;
    private static final float TEXT_SIZE = 50f;
    private static final String DISPLAY_TEXT = "跳过";
    private static final int COUNT_DOWN_TIME = 0;

    private int backgroundcolor;
    private int bordercolor;
    private int textcolor;
    private float borderwidth;
    private float textsize;
    private String  displaytext;
    private int countdowntime;
    private int remaintime;


    private Paint circlePaint;
    private Paint borderPaint;
    private TextPaint textPaint;

    private int width;
    private int height;
    private int min;
    //进度条的进程
    private float progress = 360;
    //倒计时显示5秒
    private float lastTime = 5000;
    private boolean isFirst = true;

    // StaticLayout是android中处理文字换行的一个工具类，StaticLayout已经实现了文本绘制换行处理。
    private StaticLayout staticLayout;
    private CountDownTimerListener listener;


    public CountDownView(Context context) {
        super(context,null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        backgroundcolor = typedArray.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        bordercolor = typedArray.getColor(R.styleable.CountDownView_border_color, BORDER_COLOR);
        borderwidth = typedArray.getDimension(R.styleable.CountDownView_border_width, BORDER_WIDTH);
        displaytext = typedArray.getString(R.styleable.CountDownView_display_text);
        countdowntime = typedArray.getInteger(R.styleable.CountDownView_count_down_time, COUNT_DOWN_TIME);
        if (displaytext==null){
            displaytext = DISPLAY_TEXT;
            if (countdowntime!=0){
                lastTime = countdowntime*1000;
                displaytext = countdowntime + "S";
            }
        }
        textsize = typedArray.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
        textcolor = typedArray.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
        typedArray.recycle();
        init();
    }
    private void init(){
        circlePaint = new Paint();
        //设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢
        circlePaint.setAntiAlias(true);
        //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        circlePaint.setDither(true);
        circlePaint.setColor(backgroundcolor);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(textcolor);
        textPaint.setTextSize(textsize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(bordercolor);
        borderPaint.setStrokeWidth(borderwidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        initText();
    }
    private void initText(){
        int textWidth = (int) textPaint.measureText(displaytext.substring(0, displaytext.length()));
        staticLayout = new StaticLayout(displaytext, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY){
            width = staticLayout.getWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY){
            height = staticLayout.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        min = Math.min(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + borderwidth / 2, 0 + borderwidth / 2, width / 2 + min / 2 - borderwidth / 2, height - borderwidth / 2);
        } else {
            rectF = new RectF(borderwidth / 2, height / 2 - min / 2 + borderwidth / 2, width - borderwidth / 2, height / 2 - borderwidth / 2 + min / 2);
        }
        //画圆弧
        canvas.drawArc(rectF, -90, progress, false, borderPaint);
        //画居中的文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
    }
    public interface CountDownTimerListener{
        void onStartCount();
        void onChangeCount(int second);
        void onFinishCount();
    }

    public void setCountDownTimerListener(CountDownTimerListener listener) {
        this.listener = listener;
    }

    //倒计时
    public void TimeStart(){
        remaintime = countdowntime;
        isFirst = true;
        if (listener!=null){
            listener.onStartCount();
        }
        new CountDownTimer((long) lastTime,1010){

            @Override
            public void onTick(long l) {
                if (isFirst){
                    isFirst = false;
                    displaytext = countdowntime + "S";
                }else {
                    if (remaintime!=0){
                        remaintime--;
                        displaytext = remaintime + "S";
                    }
                }
                initText();
                invalidate();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        //倒计时器 第一个参数表示总的时间，第二个参数表示间隔时间
        new CountDownTimer((long) lastTime,50){

            @Override
            public void onTick(long l) {
                progress = 360 - ((lastTime - l) / lastTime) * 360;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress = 0;
                invalidate();
                if (listener!=null){
                    listener.onFinishCount();
                }

            }
        }.start();
    }
    public void setCountDownTime(int time){
        countdowntime = time;
        lastTime = countdowntime * 1000;
        TimeStart();
    }


}
