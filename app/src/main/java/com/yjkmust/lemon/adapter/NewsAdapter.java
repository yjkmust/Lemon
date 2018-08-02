package com.yjkmust.lemon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.entity.DataSource;
import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.util.GlideUtil;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.model.NewsBean;

import java.util.List;

/**
 * Created by 11432 on 2018/8/2.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int NEWS_TYPE_NORMAL = 1;
    public static final int NEWS_TYPE_VIDEO = 2;
    private List<NewsBean> mList;
    private Context mContext;
    private int mPlayPosition;
    private onVideoTitleClickListener onVideoTitleClickListener;

    public NewsAdapter(Context context, List<NewsBean> list) {
        mList = list;
        mContext = context;
        mPlayPosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NEWS_TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_normal, parent, false);
            return new NormalHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_video, parent, false);
            return new VideoHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            setNormalData((NormalHolder) holder, position);
        } else if (holder instanceof VideoHolder) {
            setVideoData((VideoHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    private void setNormalData(NormalHolder holder, int position) {
        NewsBean newsBean = mList.get(position);
        holder.title.setText(newsBean.getTitle());
        GlideUtil.Glide(mContext, newsBean.getImageUrl(), holder.imageView);
    }

    private void setVideoData(final VideoHolder holder, int position) {
        final NewsBean newsBean = mList.get(position);
        holder.tittle.setText(newsBean.getTitle());
        GlideUtil.Glide(mContext, newsBean.getImageUrl(), holder.imageView);
        holder.container.removeAllViews();
        holder.tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onVideoTitleClickListener != null){
                    int location[] = new int[2];
                    holder.container.getLocationOnScreen(location);
                    ViewAttr attr = new ViewAttr();
                    attr.setX(location[0]);
                    attr.setY(location[1]);
                    attr.setWidth(holder.container.getWidth());
                    attr.setHeight(holder.container.getHeight());
                    onVideoTitleClickListener.onTitleClick(holder.getLayoutPosition(),attr);
                }

            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSource dataSource = new DataSource(newsBean.getVideoUrl());
                mPlayPosition = holder.getLayoutPosition();
                if (!dataSource.equals(AssistPlayer.get(mContext).getDataSource())){
                    AssistPlayer.get(mContext).play(holder.container,dataSource);
                }else {
                    AssistPlayer.get(mContext).play(holder.container, null);
                }
            }
        });
    }


    class NormalHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public NormalHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_normal_title);
            imageView = itemView.findViewById(R.id.news_normal_image);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        ImageView imageView;
        FrameLayout container;

        public VideoHolder(View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.news_video_title);
            imageView = itemView.findViewById(R.id.news_video_image);
            container = itemView.findViewById(R.id.news_video_container);
        }
    }

    public interface onVideoTitleClickListener {
        void onTitleClick(int position, ViewAttr attr);
    }

    public void setOnVideoTitleClickListener(NewsAdapter.onVideoTitleClickListener onVideoTitleClickListener) {
        this.onVideoTitleClickListener = onVideoTitleClickListener;
    }

    public int getPlayPosition() {
        return mPlayPosition;
    }

    public void setPlayPosition(int mPlayPosition) {
        this.mPlayPosition = mPlayPosition;
    }


}
