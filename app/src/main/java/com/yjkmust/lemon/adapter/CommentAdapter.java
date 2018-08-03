package com.yjkmust.lemon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjkmust.fourleafclover.util.GlideUtil;
import com.yjkmust.lemon.R;
import com.yjkmust.lemon.model.CommentBean;

import java.util.List;

/**
 * Created by 11432 on 2018/8/3.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context mContext;
    private List<CommentBean> mList;

    public CommentAdapter(Context mContext, List<CommentBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CommentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
          CommentBean bean = mList.get(position);
        holder.name.setText(bean.getUserName());
        holder.num.setText(String.valueOf(bean.getPraiseNum()));
        holder.content.setText(bean.getContent());
        GlideUtil.GlideCircle(mContext, R.mipmap.ic_launcher, holder.icon);

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView num;
        private TextView content;

        public CommentHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.adapter_comment_icon);
            name = itemView.findViewById(R.id.adapter_comment_name);
            num = itemView.findViewById(R.id.adapter_comment_praiseNum);
            content = itemView.findViewById(R.id.comment_content);

        }
    }
}
