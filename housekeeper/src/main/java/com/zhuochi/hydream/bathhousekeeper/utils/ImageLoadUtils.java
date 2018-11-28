package com.zhuochi.hydream.bathhousekeeper.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片加载
 * Created by and on 2017/5/4.
 */

public class ImageLoadUtils {
    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, int res) {
        Glide.with(context).load(res).into(imageView);
    }
    /**
     * 加载图片
     */
    public static void loadImage(Context context, String url, ImageView view){
        try {
            Glide.with(context).load(url).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 加载图片 带默认图片
     */
    public static void loadImage(Context context, String url, int defResID, ImageView view) {
        try {
            if (url != null && !url.contains("http")) {
//                url = Common.url + url;
            }
            Glide.with(context).load(url).error(defResID).skipMemoryCache(false).into(view);
        } catch (Exception e) {
            Glide.with(context).load(defResID).into(view);
            e.printStackTrace();
        }

    }
}
