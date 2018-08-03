package com.yjkmust.lemon.fragment;

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

import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.adapter.VideoAdapter;
import com.yjkmust.lemon.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class VideoFragment extends Fragment implements VideoAdapter.VideoListener {
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
    private boolean isShowComment;
    private CommentFragment commentFragment;
    Unbinder unbinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onAttach(Context context) {
        super.onAttach(context);

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

    private void initData(){
        mAttr = getArguments().getParcelable("attr");
        NewsBean bean = getArguments().getParcelable("news");
        mList = new ArrayList<>();
        mList.add(bean);
        addData();
        mAdapter = new VideoAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setVideoListener(this);
    }
    private void addData(){
        for (int i = 0; i < 14; i++) {
            NewsBean v3 = new NewsBean();
            v3.setTitle("视频新闻视频新闻视频新闻视频新闻视频新闻视频新闻视频新闻" + i);
//            v3.setType(R.layout.adapter_video);
            v3.setImageUrl("http://img5.imgtn.bdimg.com/it/u=3974436224,4269321529&fm=27&gp=0.jpg");
            v3.setVideoUrl("https://mov.bn.netease.com/open-movie/nos/mp4/2017/12/04/SD3SUEFFQ_hd.mp4");
            v3.setCommentNum(666);
            mList.add(v3);
        }
//        mAdapter.notifyItemRangeInserted(1, 14);
//        mTopLayout.animate().alpha(1f).setDuration(250);
    }


    @Override
    public void onVideoPlay(int position) {

    }

    @Override
    public void onClickMessage(NewsBean bean, ViewAttr attr,boolean isPlaying) {
        isShowComment = true;
        if (commentFragment==null){
            commentFragment = new CommentFragment();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("attr", attr);
        bundle.putParcelable("news", bean);
        bundle.putBoolean("play",isPlaying);
        commentFragment.setArguments(bundle);
        transaction.add(R.id.fragment_video_list_comment_container, commentFragment);
        transaction.commit();

    }

    @Override
    public void onAnimationEnd() {

    }
}
