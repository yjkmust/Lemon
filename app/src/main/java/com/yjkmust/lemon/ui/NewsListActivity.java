package com.yjkmust.lemon.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.fourleafclover.videoplayer.play.DataInter;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.adapter.NewsAdapter;
import com.yjkmust.lemon.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListActivity extends AppCompatActivity implements NewsAdapter.onVideoTitleClickListener,OnReceiverEventListener,OnPlayerEventListener{
    @BindView(R.id.root)
    FrameLayout mRoot;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private boolean isLandScape;
    private FrameLayout mFullContainer;
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

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            attachFullScreen();
        } else {
//            attachList();
        }
        AssistPlayer.get(getApplicationContext()).getReceiverGroup().getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    @Override
    public void onBackPressed() {
        if (isLandScape){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
