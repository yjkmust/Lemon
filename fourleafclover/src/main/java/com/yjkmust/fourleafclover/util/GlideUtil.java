package com.yjkmust.fourleafclover.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by 11432 on 2018/8/2.
 */

public class GlideUtil {
    public static void Glide(Context context,String url,  ImageView view){
        Glide.with(context).load(url).into(view);
    }
    public static void GlideCircle(Context context,int url, ImageView view){
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context).load(url).apply(options).into(view);
    }

}
