package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.BathRoomFilter;
import com.zhuochi.hydream.entity.BatheFloorEntity;

import java.util.List;

/**
 * 首页楼层下拉
 * Created by and on 2016/11/12.
 */

public class ListFilterAdapter extends BAdapter<BatheFloorEntity> {
    public ListFilterAdapter(List<BatheFloorEntity> list, int layoutId, Context context, int b_id) {
        super(list, layoutId, context);
        this.b_id = b_id;
    }

    @Override
    public void upDateView(int position, ViewHolder holder, BatheFloorEntity item) {
        TextView leftTextView = holder.getView(R.id.filter_bathroom_name, TextView.class);
        leftTextView.setText(item.getDevice_area_name());
        TextView rightTextView = holder.getView(R.id.filter_bathroom_ratio, TextView.class);
        try {
            rightTextView.setText(item.getIdleNum()+"/"+item.getTotalNum());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (b_id == item.getDevice_area_id()) {
            leftTextView.setTextColor(Color.parseColor("#6ab840"));
            rightTextView.setTextColor(Color.parseColor("#6ab840"));
        } else {
            leftTextView.setTextColor(Color.BLACK);
            rightTextView.setTextColor(Color.BLACK);
        }
    }

    private int b_id;

    public void setBID(int b_id) {
        this.b_id = b_id;
    }
}
