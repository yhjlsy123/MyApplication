package com.zhuochi.hydream.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;


/**
 * 简单的吹风机状态
 * Created by and on 2016/11/5.
 */

public class BlowerRoomView extends FrameLayout {
    private TextView mTvStatus;
    private int mStatus;
    public static final int USING=2;//使用中
    public static final int USABLE=1;//可用
    public static final int CHECKED=-99;

    public BlowerRoomView(Context context) {
        this(context, null);
    }

    public BlowerRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniview(context);
    }

    private void iniview(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.blower_view, null);
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
            mTvStatus.setBackgroundResource(R.mipmap.airblower_btn_d);
        } else if (status == CHECKED) {
            mTvStatus.setTextColor(Color.WHITE);
            mTvStatus.setBackgroundResource(R.mipmap.airblower_btn_s);
        } else {
            mTvStatus.setTextColor(Color.parseColor("#ff000000"));
            mTvStatus.setBackgroundResource(R.mipmap.airblower_btn_n);
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
