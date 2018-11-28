package com.isgala.xishuashua.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;


/**
 * 简单的淋浴头状态
 * Created by and on 2016/11/5.
 */

public class BathRoomView extends FrameLayout {
    private TextView mTvStatus;
    private int mStatus;
    public static final int USING=2;//使用中
    public static final int USABLE=1;//可用
    public static final int CHECKED=-99;

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
        mStatus = USABLE;
        removeAllViews();
        addView(contentView);
    }

    public synchronized void setStatus(int status) {
        if (status == mStatus)
            return;
        mStatus = status;
        if (status == USING) {
            mTvStatus.setTextColor(Color.WHITE);
            mTvStatus.setBackgroundResource(R.mipmap.shower_using_icon);
        } else if (status == CHECKED) {
            mTvStatus.setTextColor(Color.WHITE);
            mTvStatus.setBackgroundResource(R.mipmap.shower_choice_icon);
        } else {
            mTvStatus.setTextColor(Color.parseColor("#ff000000"));
            mTvStatus.setBackgroundResource(R.mipmap.shower_usable_icon);
        }
    }

    public synchronized void setText(CharSequence text) {
        mTvStatus.setText(text);
    }

    /**
     * 获取当前条目的状态
     * @return
     */
    public int getStatus() {
        return mStatus;
    }
}
