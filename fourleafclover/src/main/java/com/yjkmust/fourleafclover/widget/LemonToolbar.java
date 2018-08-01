package com.yjkmust.fourleafclover.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yjkmust.fourleafclover.R;

/**
 * Created by yj on 2018/8/1.
 */

public class LemonToolbar extends Toolbar {
    private int DEFAULT_COLOR = Color.parseColor("#ffffff");
    private int mMidTitleColor;
    private float MIDTITLESIZE = 20f;
    private float mMidTitleSize;
    private int LEFT_DEFAULT_PIC = R.drawable.icon_left;
    private int mLeftPic;
    private int RIGHT_DEFAULT_PIC = R.drawable.icon_plus;
    private int mRightPic;
    //左侧title
    private TextView mLeftTitle;
    //中间title
    private TextView mMidTitle;
    //右侧title
    private TextView mRightTitle;
    //Toolbar
    private Toolbar toolbar;

    public LemonToolbar(Context context) {
        super(context,null);
    }

    public LemonToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        init(context,attrs);
    }

    public LemonToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs){
        if (attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LemonToolbar);
            mMidTitleColor = typedArray.getColor(R.styleable.LemonToolbar_mid_title_color, DEFAULT_COLOR);
            mMidTitleSize = typedArray.getDimension(R.styleable.LemonToolbar_mid_title_text_size, MIDTITLESIZE);
            mLeftPic = typedArray.getResourceId(R.styleable.LemonToolbar_left_pic, mLeftPic);
            mRightPic = typedArray.getResourceId(R.styleable.LemonToolbar_right_pic, mRightPic);
            typedArray.recycle();
        }
        mMidTitle.setTextColor(mMidTitleColor);
        mMidTitle.setTextSize(mMidTitleSize/2);
        setLeftPic(mLeftPic);
        setRightPic(mRightPic);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_lemon_toolbar, this);
        mLeftTitle = (TextView) findViewById(R.id.txt_left_title);
        mMidTitle = (TextView) findViewById(R.id.txt_mid_title);
        mRightTitle = (TextView) findViewById(R.id.txt_right_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    //设置左边title文字颜色及图标
    private void setLeftTitle(String text,int color, int res){
        if (text!=null&&text.length()>0){
            mLeftTitle.setText(text);
        }
        if (color!=0){
            mLeftTitle.setTextColor(color);
        }
        setLeftPic(res);
        mLeftTitle.setVisibility(View.VISIBLE);
    }
    private void setLeftPic(int res){
        if (res != 0){
            Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            mLeftTitle.setCompoundDrawables(dwLeft, null, null, null);
        }
    }

    //设置左边title文字颜色及图标
    private void setRightTitle(String text,int color, int res){
        if (text!=null&&text.length()>0){
            mRightTitle.setText(text);
        }
        if (color!=0){
            mRightTitle.setTextColor(color);
        }
        setRightPic(res);
        mRightTitle.setVisibility(View.VISIBLE);
    }
    private void setRightPic(int res){
        if (res != 0){
            Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
            dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
            mRightTitle.setCompoundDrawables(null, null, dwRight, null);
        }
    }

    //设置标题的内容及颜色
    private void setMidTitle(String text){
        this.setTitle("");
        mMidTitle.setText(text);
        mMidTitle.setVisibility(View.VISIBLE);
    }
    private void setToolbarBackground(int color){
        toolbar.setBackgroundColor(color);
    }
    private void setMidTitleColor(int res){
        mMidTitle.setTextColor(res);
    }
    //左右两边标题点击事件
    public void setLeftClickListener(OnClickListener leftClickListener){
        mLeftTitle.setOnClickListener(leftClickListener);
    }
    public void setRightClickListener(OnClickListener rightClickListener){
        mRightTitle.setOnClickListener(rightClickListener);
    }

}
