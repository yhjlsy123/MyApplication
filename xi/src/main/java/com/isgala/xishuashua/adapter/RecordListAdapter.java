package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.RecordList;
import com.isgala.xishuashua.config.BathHouseApplication;
import com.isgala.xishuashua.ui.PayActivity;
import com.isgala.xishuashua.utils.Common;

import java.util.List;


/**
 * 消费历史
 * Created by and on 2016/11/15.
 */

public class RecordListAdapter extends BAdapter<RecordList.LogRecord> {
    public RecordListAdapter(List<RecordList.LogRecord> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder,final RecordList.LogRecord item) {
        holder.getView(R.id.record_creat, TextView.class).setText(item.created);
//        holder.getView(R.id.record_time, TextView.class).setText(item.total_time2);
//        holder.getView(R.id.record_water, TextView.class).setText(item.water);

        holder.getView(R.id.record_pay_money, TextView.class).setText(item.payable + "元");
        ImageView line = holder.getView(R.id.record_line, ImageView.class);
        if (position == getCount() - 1) {
            line.setPadding(0, 0, 0, 0);
        } else {
            line.setPadding(20, 0, 0, 0);
        }
        TextView button = holder.getView(R.id.record_pay_status, TextView.class);
        //显示时间

        if (item.show_bath_type.equals("1")){
            TextView textView= holder.getView(R.id.record_time,TextView.class);
            textView.setVisibility(View.VISIBLE);
            try {
                int time=Integer.valueOf(item.total_time);
                if (time>0) {
                    textView.setText(Common.change(time));
                }
            }catch (Exception e){
                e.printStackTrace();
                textView.setText("0秒");
            }

            //显示水量
        }else if (item.show_bath_type.equals("2")){
            TextView textView= holder.getView(R.id.record_water,TextView.class);
            textView.setVisibility(View.VISIBLE);
            textView.setText(item.water);
        }
        if (TextUtils.equals("未支付", item.pay_status)) {
            button.setBackgroundResource(R.drawable.selector_bg_corner_blue);
            button.setText("去支付");
            button.setTextColor(Color.WHITE);
            button.setPadding(10, 0, 10, 0);
            holder.getView(R.id.record_root, View.class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BathHouseApplication.mApplicationContext, PayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("order_id",item.order_id);
                    BathHouseApplication.mApplicationContext.startActivity(intent);
                }
            });
        } else {
            button.setPadding(0, 0, 0, 0);
            button.setTextColor(Color.parseColor("#4DA9FF"));
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setText(item.pay_status);
            holder.getView(R.id.record_root, View.class).setOnClickListener(null);
        }
    }
}
