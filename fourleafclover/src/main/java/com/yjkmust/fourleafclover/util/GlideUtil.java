package com.yjkmust.fourleafclover.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 11432 on 2018/8/2.
 */

public class GlideUtil {
    public static void Glide(Context context,String url,  ImageView view){
        Glide.with(context).load(url).into(view);
    }

}
