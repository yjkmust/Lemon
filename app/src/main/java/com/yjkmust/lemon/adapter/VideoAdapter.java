package com.yjkmust.lemon.adapter;

import android.content.Context;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.taurus.playerbase.entity.DataSource;
import com.yjkmust.fourleafclover.model.ViewAttr;
import com.yjkmust.fourleafclover.util.GlideUtil;
import com.yjkmust.fourleafclover.videoplayer.play.AssistPlayer;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.model.NewsBean;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by 11432 on 2018/8/3.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context mContext;
    private List<NewsBean> mList;
    private VideoListener listener;
    private int mPlayPosition;

    public VideoAdapter(Context mContext, List<NewsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {
        final NewsBean bean = mList.get(position);
        holder.title.setText(bean.getTitle());
        holder.message.setText(String.valueOf(bean.getCommentNum()));
        GlideUtil.Glide(mContext, bean.getImageUrl(), holder.image);
        GlideUtil.GlideCircle(mContext,R.mipmap.ic_launcher,holder.icon);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSource dataSource = new DataSource(bean.getVideoUrl());
                AssistPlayer.get(mContext).play(holder.container, dataSource);
                mPlayPosition = holder.getLayoutPosition();
                if (listener!=null){
                    listener.onVideoPlay(mPlayPosition);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getLayoutPosition() != mPlayPosition){
                    holder.image.performClick();
                }
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getLayoutPosition() != mPlayPosition) {
                    holder.image.performClick();
                } else {
                    ViewAttr attr = new ViewAttr();
                    int l[] = new int[2];
                    holder.container.getLocationOnScreen(l);
                    attr.setX(l[0]);
                    attr.setY(l[1]);
                    attr.setWidth(holder.container.getWidth());
                    attr.setHeight(holder.container.getHeight());
                    listener.onClickMessage(bean, attr,AssistPlayer.get(mContext).isPlaying());
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements AnimateViewHolder {
        public FrameLayout container;
        FrameLayout containerLayout;
        TextView title;
        TextView message;
        ImageView icon;
        ImageView image;
        LinearLayout bottomLayout;

        public VideoHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.adapter_video_list_container);
            title = itemView.findViewById(R.id.adapter_video_list_title);
            message = itemView.findViewById(R.id.adapter_video_list_message_num);
            icon = itemView.findViewById(R.id.adapter_video_list_icon);
            image = itemView.findViewById(R.id.adapter_video_list_image);
            bottomLayout = itemView.findViewById(R.id.bottom_layout);
            containerLayout = itemView.findViewById(R.id.adapter_video_list_container_layout);
        }

        @Override
        public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {

        }

        @Override
        public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

        }

        @Override
        public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {

        }

        @Override
        public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {

        }
    }
    public interface VideoListener{
        void onVideoPlay(int position);

        void onClickMessage(NewsBean bean, ViewAttr attr, boolean isPlaying);

        void onAnimationEnd();
    }
    public void setVideoListener(VideoListener listener){
        this.listener = listener;
    }
}
