package com.isgala.xishuashua.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.RechargeLog;
import com.isgala.xishuashua.ui.RechargeActivity;

import java.util.List;

/**
 * 充值赠送
 * Created by and on 2016/11/28.
 */

public class RechargeAdapter extends BAdapter<RechargeLog> {

    public RechargeAdapter(List<RechargeLog> list, @Nullable int layoutId, @Nullable Context context) {
        super(list, layoutId, context);
    }

    @Override
    public void upDateView(int position, ViewHolder holder, final RechargeLog item) {
        holder.getView(R.id.tv_item_recharge_title, TextView.class).setText(item.name);
        holder.getView(R.id.tv_item_recharge_subhead, TextView.class).setText(item.give);
        holder.getView(R.id.tv_item_recharge_end_time, TextView.class).setText("截止时间：" + item.end_time);
        if (position % 3 == 0) {
            holder.getView(R.id.ll_activity_balance_recharge, View.class).setBackgroundResource(R.mipmap.balance_recharge_background1);
        } else if (position % 3 == 1) {
            holder.getView(R.id.ll_activity_balance_recharge, View.class).setBackgroundResource(R.mipmap.balance_recharge_background2);
        } else {
            holder.getView(R.id.ll_activity_balance_recharge, View.class).setBackgroundResource(R.mipmap.balance_recharge_background3);
        }
        holder.getView(R.id.ll_activity_balance_recharge, View.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RechargeActivity.class);
                intent.putExtra("price", item.recharge);
                intent.putExtra("rc_id", item.rc_id);
                intent.putExtra("from","ActivtyRechargeAdapter");
                intent.putExtra("type", "3");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
