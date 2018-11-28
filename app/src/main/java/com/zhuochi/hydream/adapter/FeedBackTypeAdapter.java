package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.entity.FeedbackTypeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈类型
 * Created by and on 2016/11/8.
 */

public class FeedBackTypeAdapter extends BAdapter<FeedbackTypeEntity> {
    public FeedBackTypeAdapter(List<FeedbackTypeEntity> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    private int currentPosition = -1;

    @Override
    public void upDateView(int position, ViewHolder holder, FeedbackTypeEntity item) {
        if (currentPosition == -1) {//默认值
            currentPosition = position;
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(getItem(position), holder, position);
        }
        CheckBox button = holder.getView(R.id.item_feedback_type, CheckBox.class);
        button.setText(item.getName());
//        setOnItemClickListener(new OnItemClickListener<FeedbackTypeEntity>() {
//            @Override
//            public void onItemClick(FeedbackTypeEntity item, ViewHolder holder, int position) {
//                list.add(item.getId());
//            }
//        });
        if (currentPosition == position) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
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
