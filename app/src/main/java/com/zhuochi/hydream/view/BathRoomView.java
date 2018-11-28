package com.zhuochi.hydream.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;

import static com.zhuochi.hydream.config.Constants.JUNKED;
import static com.zhuochi.hydream.config.Constants.OFFLINE;
import static com.zhuochi.hydream.config.Constants.READY;
import static com.zhuochi.hydream.config.Constants.RESERVED;
import static com.zhuochi.hydream.config.Constants.RUNNING;
import static com.zhuochi.hydream.config.Constants.SELECTED;


/**
 * 简单的淋浴头状态
 * Created by and on 2016/11/5.
 */

public class BathRoomView extends FrameLayout {
    private TextView mTvStatus;
    private ImageView imageView;
    private int mStatus;
    public static final int USING = 2;//使用中
    public static final int USABLE = 1;//可用
    public static final int CHECKED = -99;//选中
    public static final int RESERVED_NO = 3;//
    public static final int JUNKED_NO = 4;
    private Context context;

    public BathRoomView(Context context) {
        this(context, null);
    }

    public BathRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniview(context);
    }

    private void iniview(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.shawerview, null);
        mTvStatus = (TextView) contentView.findViewById(R.id.bathroom_status);
        imageView = (ImageView) contentView.findViewById(R.id.img);
        Glide.with(context).load(Common.ICON_BASE_URL + "/" + READY).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        Log.d("cxc_load", Common.ICON_BASE_URL);
        mStatus = USABLE;
        removeAllViews();
        addView(contentView);
    }

    public synchronized void setStatus(Context context, int status) {
        if (status == mStatus) {
            return;
        }
        mStatus = status;
        if (status == USING) {
            //使用中
            mTvStatus.setTextColor(Color.WHITE);
            ImageLoadUtils.loadImage(context, Common.ICON_BASE_URL + "/" + RUNNING, imageView);
        } else if (status == CHECKED) {
            mTvStatus.setTextColor(Color.WHITE);
            ImageLoadUtils.loadImage(context, Common.ICON_BASE_URL + "/" + SELECTED, imageView);
        } else if (status == USABLE) {
            mTvStatus.setTextColor(Color.BLACK);
            ImageLoadUtils.loadImage(context, Common.ICON_BASE_URL + "/" + READY, imageView);
        } else if (status == RESERVED_NO) {
            mTvStatus.setTextColor(Color.WHITE);
            ImageLoadUtils.loadImage(context, Common.ICON_BASE_URL + "/" + RESERVED, imageView);
        } else if (status == JUNKED_NO) {
            mTvStatus.setTextColor(Color.WHITE);
            ImageLoadUtils.loadImage(context, Common.ICON_BASE_URL + "/" + JUNKED, imageView);
        }


    }

    public synchronized void setWarmStatus(Context context, int status) {
        if (status == mStatus) {
            mTvStatus.setTextColor(Color.BLACK);
            Glide.with(context).load(R.mipmap.warm_ready).into(imageView);
            return;
        }
        mStatus = status;
        if (status == USING) {
            //使用中
            mTvStatus.setTextColor(Color.WHITE);
            Glide.with(context).load(R.mipmap.warm_runing).into(imageView);
        } else if (status == CHECKED) {
            mTvStatus.setTextColor(Color.WHITE);
            Glide.with(context).load(R.mipmap.warm_selected).into(imageView);
        } else if (status == USABLE) {
            mTvStatus.setTextColor(Color.BLACK);
            Glide.with(context).load(R.mipmap.warm_ready).into(imageView);
        } else if (status == RESERVED_NO) {
            mTvStatus.setTextColor(Color.WHITE);
            Glide.with(context).load(R.mipmap.warm_reserved).into(imageView);

        } else if (status == JUNKED_NO) {
            mTvStatus.setTextColor(Color.WHITE);
        }


    }


    public synchronized void setText(CharSequence text) {
        mTvStatus.setText(text);
    }

    /**
     * 获取当前条目的状态
     *
     * @return
     */
    public int getStatus() {
        return mStatus;
    }
}
