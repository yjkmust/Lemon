package com.yjkmust.lemon.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.util.UiUtils;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.adapter.VideoAdapter;
import com.yjkmust.lemon.itemDecoration.EmptyItemDecoration;
import com.yjkmust.lemon.model.NewsBean;
import com.yjkmust.lemon.ui.NewsListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;


public class VideoFragment extends Fragment implements VideoAdapter.VideoListener, CommentFragment.onCloseClickListener {
    @BindView(R.id.fragment_video_list_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_video_list_back)
    ImageView mBack;
    @BindView(R.id.fragment_video_list_top_layout)
    FrameLayout mTopLayout;
    @BindView(R.id.fragment_video_list_root)
    FrameLayout mRoot;

    private List<NewsBean> mList;
    private ViewAttr mAttr;
    private VideoAdapter mAdapter;
    public boolean isShowComment;
    private CommentFragment commentFragment;
    public static final int DURATION = 550;
    Unbinder unbinder;
    private onBackClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsListActivity){
            listener = (VideoFragment.onBackClickListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initData() {
        mAttr = getArguments().getParcelable("attr");
        boolean isAttach = getArguments().getBoolean("isAttach", false);
        NewsBean bean = getArguments().getParcelable("news");
        mList = new ArrayList<>();
        mList.add(bean);
        mAdapter = new VideoAdapter(getContext(), mList);
        mAdapter.setAttach(isAttach);
        mAdapter.setAttr(mAttr);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        mRecyclerView.addItemDecoration(new EmptyItemDecoration(UiUtils.dip2px(getContext(), 40)));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setVideoListener(this);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowComment){
                    closeCommentFragment();
                }else {
                    if (listener != null){
                        listener.removeVideoFragment();
                    }
                }
            }
        });
        if (!isAttach){
            addData();
        }
    }

    private void addData() {
        for (int i = 0; i < 14; i++) {
            NewsBean v3 = new NewsBean();
            v3.setTitle("视频新闻视频新闻视频新闻视频新闻视频新闻视频新闻视频新闻" + i);
//            v3.setType(R.layout.adapter_video);
            v3.setImageUrl("http://img5.imgtn.bdimg.com/it/u=3974436224,4269321529&fm=27&gp=0.jpg");
            v3.setVideoUrl("https://mov.bn.netease.com/open-movie/nos/mp4/2017/12/04/SD3SUEFFQ_hd.mp4");
            v3.setCommentNum(666);
            mList.add(v3);
        }
        mAdapter.notifyItemRangeInserted(1, 14);
        mTopLayout.animate().alpha(1f).setDuration(250);
    }


    @Override
    public void onVideoPlay(int position) {

    }

    @Override
    public void onClickMessage(NewsBean bean, ViewAttr attr, boolean isPlaying) {
        isShowComment = true;
        if (commentFragment == null) {
            commentFragment = new CommentFragment();
            commentFragment.setOnCloseClickListener(this);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("attr", attr);
        bundle.putParcelable("news", bean);
        bundle.putBoolean("play", isPlaying);
        commentFragment.setArguments(bundle);
        transaction.add(R.id.fragment_video_list_comment_container, commentFragment);
        transaction.commit();

    }

    @Override
    public void onAnimationEnd() {
        addData();
    }

    @Override
    public void closeCommentFragment() {
        isShowComment = false;
        commentFragment.closeFragment();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(commentFragment);
                transaction.commit();
                VideoAdapter.VideoHolder holder = (VideoAdapter.VideoHolder) mRecyclerView.findViewHolderForLayoutPosition(mAdapter.getPlayPosition());
                AssistPlayer.get(getContext()).play(holder.container, null);
            }
        }, DURATION);
    }

    public boolean isPlayingFirst() {
        return AssistPlayer.get(getContext()).isPlaying() && mAdapter.getPlayPosition() == 0;
    }
    public boolean isShowComment() {
        return isShowComment;
    }
    public void removeVideoList(){
        int size = mList.size() - 1;
        for (int i = size ; i>0;i--){
            mList.remove(i);
        }
        mAdapter.notifyItemRangeRemoved(1, size);
        final View view = mRecyclerView.getChildAt(0);
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        final ImageView image = view.findViewById(R.id.adapter_video_list_image);
        final FrameLayout container = view.findViewById(R.id.adapter_video_list_container);
        final TextView title = view.findViewById(R.id.adapter_video_list_title);
        final LinearLayout bottom = view.findViewById(R.id.bottom_layout);
        title.postDelayed(new Runnable() {
            @Override
            public void run() {
                title.setVisibility(View.GONE);
                bottom.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                container.animate().scaleX(container.getMeasuredWidth() / (float) mAttr.getWidth())
                        .scaleY(container.getMeasuredHeight() / (float) mAttr.getHeight())
                        .setDuration(DURATION);
                view.animate().translationY(mAttr.getY() - location[1]).setDuration(DURATION);
                ObjectAnimator animator = ObjectAnimator.ofInt(mRoot, "backgroundColor", 0xff000000, 0x00000000);
                animator.setEvaluator(new ArgbEvaluator());
                animator.setDuration(DURATION);
                animator.start();
            }
        }, 250);

    }



    public interface onBackClickListener {
        void removeVideoFragment();
    }

    public void setOnBackClickListener(onBackClickListener listener) {
        this.listener = listener;
    }
    public void attachCommentContainer() {
        commentFragment.attachContainer();
    }
    public void attachList() {
        VideoAdapter.VideoHolder holder = (VideoAdapter.VideoHolder) mRecyclerView.findViewHolderForLayoutPosition(mAdapter.getPlayPosition());
        AssistPlayer.get(getContext()).play(holder.container, null);
    }

}
