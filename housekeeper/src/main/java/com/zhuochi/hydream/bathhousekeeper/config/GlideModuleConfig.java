package com.zhuochi.hydream.bathhousekeeper.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by and on 2016/11/3.
 */

public class GlideModuleConfig implements GlideModule {

    // 在这里创建设置内容,图片质量就可以在这里设置
    // 还可以设置缓存池参数什么的
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    // 在这里注册ModelLoaders
    // 可以在这里清除缓存什么的
    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.setMemoryCategory(MemoryCategory.NORMAL);
    }
}
