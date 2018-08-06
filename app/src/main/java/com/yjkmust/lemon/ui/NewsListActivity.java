package com.yjkmust.lemon.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.util.UiUtils;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.fourleafclover.videoplayer.play.DataInter;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.adapter.NewsAdapter;
import com.yjkmust.lemon.fragment.VideoFragment;
import com.yjkmust.lemon.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class NewsListActivity extends AppCompatActivity implements NewsAdapter.onVideoTitleClickListener, OnReceiverEventListener, OnPlayerEventListener, VideoFragment.onBackClickListener {
    @BindView(R.id.root)
    FrameLayout mRoot;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private boolean isLandScape;
    private FrameLayout mFullContainer;
    private VideoFragment videoFragment;
    private boolean isShowVideoList;
    private int clickPosition;//点击跳转到视频列表的position
    private List<NewsBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);
        initData();
        mAdapter = new NewsAdapter(getApplicationContext(), mList);
        mAdapter.setOnVideoTitleClickListener(this);
        AssistPlayer.get(getApplicationContext()).addOnReceiverEventListener(this);
        AssistPlayer.get(getApplicationContext()).addOnPlayerEventListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动停止
                if (newState == SCROLL_STATE_IDLE) {
                    //滑动到屏幕中间开始
                    LinearLayoutManager manger = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int first = manger.findFirstVisibleItemPosition();
                    int last = manger.findLastVisibleItemPosition();
                    for (int i = first; i <= last; i++) {
                        if (!TextUtils.isEmpty(mList.get(i).getVideoUrl())) {
                            //列表视频 getChidAt只能到屏幕显示的item
                            View view = recyclerView.getChildAt(i - first);
                            FrameLayout container = view.findViewById(R.id.news_video_container);
                            int location[] = new int[2];
                            container.getLocationOnScreen(location);
                            //y坐标包括状态栏所以要剪掉
                            int top = location[1] - UiUtils.getStatusBarHeight(NewsListActivity.this);
                            //获取y坐标，判断是否在屏幕中间
                            int screenHeight = UiUtils.getScreenHeight(getApplicationContext());
                            if (top >= screenHeight / 2 - UiUtils.dip2px(NewsListActivity.this, 200)
                                    && top <= screenHeight + UiUtils.dip2px(NewsListActivity.this, 200)
                                    && !AssistPlayer.get(getApplicationContext()).isPlaying()) {
                                ImageView imageView = view.findViewById(R.id.news_video_image);
                                imageView.performClick();//点击播放
                                break;

                            }

                        }
                    }
                    //滑出屏幕高度一半停止播放
                    int playPosition = mAdapter.getPlayPosition();
                    if (playPosition != -1) {
                        View view = recyclerView.getChildAt(playPosition - first);
                        if (view != null) {
                            FrameLayout container = view.findViewById(R.id.news_video_container);
                            int location[] = new int[2];
                            container.getLocationOnScreen(location);
                            int top = location[1] - UiUtils.getStatusBarHeight(getApplicationContext());
                            if (top < UiUtils.dip2px(NewsListActivity.this, -100)
                                    || top > UiUtils.getScreenHeight(NewsListActivity.this) - UiUtils.dip2px(NewsListActivity.this, 100)) {
                                AssistPlayer.get(getApplicationContext()).stop();
                                mAdapter.notifyItemRangeChanged(playPosition, 1);
                                mAdapter.setPlayPosition(-1);
                            }
                        }
                    } else {
                        AssistPlayer.get(getApplicationContext()).stop();
                        mAdapter.notifyItemRangeChanged(playPosition, 1);
                        mAdapter.setPlayPosition(-1);
                    }

                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 17; i++) {
            NewsBean bean = new NewsBean();
            bean.setType(NewsAdapter.NEWS_TYPE_NORMAL);
            bean.setTitle("我是新闻标题新闻标题我是新闻标题新闻标题我是新闻标题新闻标题" + i);
            int result = i % 3;
            switch (result) {
                case 0:
                    bean.setImageUrl("http://img5.imgtn.bdimg.com/it/u=2539397329,4056054332&fm=27&gp=0.jpg");
                    break;
                case 1:
                    bean.setImageUrl("http://img3.imgtn.bdimg.com/it/u=3159360602,2315537063&fm=27&gp=0.jpg");
                    break;
                case 2:
                    bean.setImageUrl("http://img1.imgtn.bdimg.com/it/u=2156236282,1270726641&fm=27&gp=0.jpg");
                    break;
            }
            mList.add(bean);
        }

        NewsBean v1 = new NewsBean();
        v1.setTitle("视频新闻1");
        v1.setType(NewsAdapter.NEWS_TYPE_VIDEO);
        v1.setImageUrl("http://img5.imgtn.bdimg.com/it/u=3577771133,2332148944&fm=27&gp=0.jpg");
        v1.setVideoUrl("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4");
        v1.setCommentNum(666);
        mList.add(4, v1);

        NewsBean v2 = new NewsBean();
        v2.setTitle("视频新闻2视频新闻2视频新闻2视频新闻2视频新闻2视频新闻2视频新闻2");
        v2.setType(NewsAdapter.NEWS_TYPE_VIDEO);
        v2.setImageUrl("http://img0.imgtn.bdimg.com/it/u=3622851037,3121030191&fm=27&gp=0.jpg");
        v2.setVideoUrl("https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/12/SD70VQJ74_sd.mp4");
        v2.setCommentNum(666);
        mList.add(9, v2);

        NewsBean v3 = new NewsBean();
        v3.setTitle("视频新闻3视频新闻3视频新闻3视频新闻3视频新闻3视频新闻3视频新闻3");
        v3.setType(NewsAdapter.NEWS_TYPE_VIDEO);
        v3.setImageUrl("http://img5.imgtn.bdimg.com/it/u=3974436224,4269321529&fm=27&gp=0.jpg");
        v3.setVideoUrl("https://mov.bn.netease.com/open-movie/nos/mp4/2017/12/04/SD3SUEFFQ_hd.mp4");
        v3.setCommentNum(666);
        mList.add(10, v3);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                onBackPressed();
                break;
            case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                setRequestedOrientation(isLandScape ?
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    public void onTitleClick(int position, ViewAttr attr) {
        if (videoFragment == null) {
            videoFragment = new VideoFragment();
            videoFragment.setOnBackClickListener(this);
        }
        clickPosition = position;
        isShowVideoList = true;
        Bundle bundle = new Bundle();
        bundle.putParcelable("attr", attr);
        bundle.putParcelable("news", mList.get(position));
        bundle.putBoolean("isAttach", AssistPlayer.get(getApplicationContext()).isPlaying());
        videoFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.root, videoFragment);
        transaction.commit();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            attachFullScreen();
        } else {
            attachList();
        }
        AssistPlayer.get(getApplicationContext()).getReceiverGroup().getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    @Override
    public void onBackPressed() {
        if (isLandScape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (isShowVideoList) {
            //显示了视频列表
            if (videoFragment.isShowComment) {
                //显示了评论数据
                videoFragment.closeCommentFragment();
            } else {
                removeVideoFragment();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.clearOnScrollListeners();
        AssistPlayer.get(getApplicationContext()).removeReceiverEventListener(this);
        AssistPlayer.get(getApplicationContext()).removePlayerEventListener(this);
        AssistPlayer.get(getApplicationContext()).destroy();
    }

    //全屏
    private void attachFullScreen() {
        AssistPlayer.get(getApplicationContext()).getReceiverGroup().getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        if (mFullContainer == null) {
            mFullContainer = new FrameLayout(this);
            mFullContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mRoot.addView(mFullContainer, -1);
        AssistPlayer.get(getApplicationContext()).play(mFullContainer, null);
    }

    private void attachList() {
        mRoot.removeView(mFullContainer);
        AssistPlayer.get(getApplicationContext()).getReceiverGroup().getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        if (isShowVideoList) {
            if (videoFragment.isShowComment()) {
                //绑定回评论页面
                videoFragment.attachCommentContainer();
            } else {
                //绑定回视频列表页面
                videoFragment.attachList();
            }
        } else {
            if (mAdapter != null) {
                NewsAdapter.VideoHolder holder = (NewsAdapter.VideoHolder) mRecyclerView.findViewHolderForLayoutPosition(mAdapter.getPlayPosition());
                AssistPlayer.get(getApplicationContext()).play(holder.container, null);
            }
        }
    }

    @Override
    public void removeVideoFragment() {
        isShowVideoList = false;
        if (videoFragment.isPlayingFirst()) {
            videoFragment.removeVideoList();
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(videoFragment);
                    transaction.commit();
                    NewsAdapter.VideoHolder holder = (NewsAdapter.VideoHolder) mRecyclerView.findViewHolderForLayoutPosition(clickPosition);
                    AssistPlayer.get(getApplicationContext()).play(holder.container, null);
                }
            }, 800);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(videoFragment);
            transaction.commit();
        }
    }
}
