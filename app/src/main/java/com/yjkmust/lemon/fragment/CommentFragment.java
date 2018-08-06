package com.yjkmust.lemon.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.taurus.playerbase.entity.DataSource;
import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.adapter.CommentAdapter;
import com.yjkmust.lemon.model.CommentBean;
import com.yjkmust.lemon.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 11432 on 2018/8/3.
 */

public class CommentFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.fragment_comment_video_container)
    FrameLayout mContainer;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.fragment_comment_close)
    ImageView mClose;
    @BindView(R.id.fragment_comment_num)
    TextView mCommentNum;
    @BindView(R.id.fragment_comment_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_comment_root)
    RelativeLayout mRoot;

    private Unbinder unbinder;
    private List<CommentBean> mList;
    private CommentAdapter mAdapter;
    private ViewAttr mAttr;
    private boolean isPlay;
    public static final int DURATION = 550;
    private int[] location;
    private onCloseClickListener listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.closeCommentFragment();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initData() {
        mAttr = getArguments().getParcelable("attr");
        isPlay = getArguments().getBoolean("play", false);
        startAnim();
        mList = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            CommentBean bean = new CommentBean();
            bean.setId(String.valueOf(i));
            bean.setUserName("梁非凡吃屎吧" + i);
            bean.setContent("APP主题颜色跟随手机壳变化00");
            bean.setPraiseNum(3334);
            mList.add(bean);
        }
        mAdapter = new CommentAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
    private void startAnim(){
        //背景色动画
        ObjectAnimator animator = ObjectAnimator.ofInt(mRoot, "backgroundColor", 0x00000000, 0xff000000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(DURATION);
        animator.start();
        location = new int[2];
        mContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //绘制完毕，开始执行动画
                mContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                mContainer.getLocationOnScreen(location);
                mContainer.setTranslationX(mAttr.getX() - location[0]);
                mContainer.setTranslationY(mAttr.getY() - location[1]);
                mContainer.setScaleX(mAttr.getWidth() / (float) mContainer.getMeasuredWidth());
                mContainer.setScaleY(mAttr.getHeight() / (float) mContainer.getMeasuredHeight());
                mRecyclerView.setAlpha(0);
                mTextView.setAlpha(0);
//                mClose.setAlpha(0);
                mCommentNum.setAlpha(0);
                mContainer.animate().translationX(0).translationY(0).scaleX(1).scaleY(1).setDuration(DURATION);
                mRecyclerView.animate().alpha(1).setDuration(DURATION);
                mTextView.animate().alpha(1).setDuration(DURATION);
//                mClose.animate().alpha(1).setDuration(DURATION);
                mCommentNum.animate().alpha(1).setDuration(DURATION);
                if (isPlay){
                    AssistPlayer.get(getContext()).play(mContainer, null);
                }else {
                    NewsBean bean = getArguments().getParcelable("news");
                    DataSource dataSource = new DataSource(bean.getVideoUrl());
                    AssistPlayer.get(getContext()).play(mContainer, dataSource);
                }
                return true;
            }
        });


    }
    public void closeFragment() {
        mRecyclerView.animate().alpha(0).setDuration(DURATION);
        mTextView.animate().alpha(0).setDuration(DURATION);
        mClose.animate().alpha(0).setDuration(DURATION);
        mCommentNum.animate().alpha(0).setDuration(DURATION);
        mContainer.animate().translationY(mAttr.getY() - location[1]).setDuration(DURATION);
        ObjectAnimator animator = ObjectAnimator.ofInt(mRoot, "backgroundColor", 0xff000000, 0x00000000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(DURATION);
        animator.start();
    }
    public interface onCloseClickListener{
        void closeCommentFragment();
    }
    public void setOnCloseClickListener(onCloseClickListener listener){
        this.listener = listener;
    }
    public void attachContainer() {
        AssistPlayer.get(getContext()).play(mContainer, null);
    }


}
