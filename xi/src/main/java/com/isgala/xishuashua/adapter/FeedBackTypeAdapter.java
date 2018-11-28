package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.FeedBackTypeItemBean;
import com.isgala.xishuashua.bean_.Message;

import java.util.List;

/**
 * 意见反馈类型
 * Created by and on 2016/11/8.
 */

public class FeedBackTypeAdapter extends BAdapter<FeedBackTypeItemBean> {
    public FeedBackTypeAdapter(List<FeedBackTypeItemBean> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    private int currentPosition = -1;

    @Override
    public void upDateView(int position, ViewHolder holder, FeedBackTypeItemBean item) {
        if (currentPosition == -1) {//默认值
            currentPosition = position;
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(getItem(position), holder, position);
        }
        Button button = holder.getView(R.id.item_feedback_type, Button.class);
        button.setText(item.name);
        if (currentPosition == position) {
            button.setBackgroundResource(R.mipmap.yijianfankuixuanzhong);
            button.setTextColor(Color.parseColor("#4DA9FF"));
        } else {
            button.setBackgroundResource(R.mipmap.yijianfankuiweixuanzhong);
            button.setTextColor(Color.parseColor("#959595"));
        }
    }

    /**
     * 设置当前选中的位置,同时刷新界面
     *
     * @param position
     */
    public void setPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }
}
