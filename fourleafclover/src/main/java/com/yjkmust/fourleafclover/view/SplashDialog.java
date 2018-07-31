package com.yjkmust.fourleafclover.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yjkmust.fourleafclover.model.AdvertisingModel;
import com.yjkmust.fourleafclover.R;
import com.yjkmust.fourleafclover.util.UiUtils;

/**
 * Created by yj on 2018/7/31.
 */

public class SplashDialog extends Dialog {
    private Context mContext;
    private AdvertisingModel model;
    private ImageView imageView;
    private CountDownView countDownView;
    private OnSplashDetailClickListener listener;
    public SplashDialog(@NonNull Context context, AdvertisingModel model) {
        super(context, R.style.ADDialog);
        mContext = context;
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        full(true);
        initView();
    }
    /**
     * @param enable false 显示，true 隐藏
     */
    private void full(boolean enable) {
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        if (enable) {
            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;

        } else {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        getWindow().setAttributes(p);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    private void  initView(){
        imageView = (ImageView) findViewById(R.id.iv_ader);
        countDownView = (CountDownView) findViewById(R.id.cdv_ader);
        Glide.with(mContext).load(model.getImgUrl()).into(imageView);
//        countDownView.TimeStart();
        countDownView.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {

            }

            @Override
            public void onChangeCount(int second) {

            }

            @Override
            public void onFinishCount() {
                dismiss();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.makeText(mContext,"跳转到广告页面");
                if (listener != null) {
                    listener.onSplashDetailClick(model);
                }
                dismiss();
            }
        });
        countDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.makeText(mContext,"进入到APP主页");
                dismiss();
            }
        });
    }
    //默认5s
    public void setCountDownTime(){
        countDownView.TimeStart();
    }
    public void setCountDownTime(int time){
        countDownView.setCountDownTime(time);
    }
    public interface OnSplashDetailClickListener{
        void onSplashDetailClick(AdvertisingModel model);
    }
    public void setOnSplashDetailClickListener(OnSplashDetailClickListener listener){
        this.listener = listener;
    }

}
