package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.RecordBlowerList;
import com.isgala.xishuashua.bean_.RecordList;
import com.isgala.xishuashua.config.BathHouseApplication;
import com.isgala.xishuashua.ui.PayActivity;
import com.isgala.xishuashua.utils.Common;

import java.util.List;


/**
 * 消费历史
 * Created by and on 2016/11/15.
 */

public class RecordBlowerListAdapter extends BAdapter<RecordBlowerList.blower_consume_list> {
    public RecordBlowerListAdapter(List<RecordBlowerList.blower_consume_list> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder,final RecordBlowerList.blower_consume_list item) {
        holder.getView(R.id.record_creat, TextView.class).setText(item.created);
        holder.getView(R.id.record_pay_money, TextView.class).setText(item.payable + "元");
        ImageView line = holder.getView(R.id.record_line, ImageView.class);
        if (position == getCount() - 1) {
            line.setPadding(0, 0, 0, 0);
        } else {
            line.setPadding(20, 0, 0, 0);
        }
        TextView button = holder.getView(R.id.record_pay_status, TextView.class);
        //显示时间
            TextView textView= holder.getView(R.id.record_time,TextView.class);
            textView.setVisibility(View.VISIBLE);
            if (!item.total_time.isEmpty()){
                int time=Integer.valueOf(item.total_time);
                textView.setText(Common.change(time));
            }
//        if (TextUtils.equals("未支付", item.pay_status)) {
            button.setPadding(0, 0, 0, 0);
            button.setTextColor(Color.parseColor("#4DA9FF"));
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setText("已支付");
            holder.getView(R.id.record_root, View.class).setOnClickListener(null);
//        }
    }
}
