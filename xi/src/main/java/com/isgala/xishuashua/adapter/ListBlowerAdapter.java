package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.BathRoomFilter;

import java.util.List;

/**
 * Created by and on 2016/11/12.
 */

public class ListBlowerAdapter extends BAdapter<BathRoomFilter.Filter> {
    public ListBlowerAdapter(List<BathRoomFilter.Filter> list, int layoutId, Context context, String b_id) {
        super(list, layoutId, context);
        this.b_id = b_id;
    }

    @Override
    public void upDateView(int position, ViewHolder holder, BathRoomFilter.Filter item) {
        TextView leftTextView = holder.getView(R.id.filter_bathroom_name, TextView.class);
        leftTextView.setText(item.b_name);
        TextView rightTextView = holder.getView(R.id.filter_bathroom_ratio, TextView.class);
        rightTextView.setText(item.ratio);
        if (TextUtils.equals(b_id, item.b_id)) {
            leftTextView.setTextColor(Color.parseColor("#6ab840"));
            rightTextView.setTextColor(Color.parseColor("#6ab840"));
        } else {
            leftTextView.setTextColor(Color.BLACK);
            rightTextView.setTextColor(Color.BLACK);
        }
    }

    private String b_id;

    public void setBID(String b_id) {
        this.b_id = b_id;
    }
}
