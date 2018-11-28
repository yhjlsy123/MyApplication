package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.BalanceList;

import java.util.List;

/**
 * 余额历史小条目
 * Created by and on 2016/11/19.
 */

public class BalanceDayAdapter extends BAdapter<BalanceList> {
    public BalanceDayAdapter(Context context, int layoutId, List<BalanceList> list) {
        super(list, layoutId, context);//
    }


    @Override
    public void upDateView(int position, ViewHolder holder, BalanceList item) {
        holder.getView(R.id.log_week, TextView.class).setText(item.week);
        holder.getView(R.id.log_data, TextView.class).setText(item.date);
        holder.getView(R.id.log_icon, ImageView.class).setImageResource(getResource(item.icon));
        holder.getView(R.id.log_money, TextView.class).setText(item.price);
        holder.getView(R.id.log_type, TextView.class).setText(item.text);
        ImageView line = holder.getView(R.id.line_balance, ImageView.class);
        if (position == getCount() - 1) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
            line.setPadding(20, 0, 0, 0);
        }
    }

    /**
     * 获取本地存储的icon
     *
     * @param res
     * @return
     */
    private int getResource(String res) {
        int temp = R.mipmap.yue_zao;
        switch (res) {
            case "zao":
                temp = R.mipmap.yue_zao;
                break;
            case "tui":
                temp = R.mipmap.yue_tui;
                break;
            case "weixin":
                temp = R.mipmap.yue_weixin;
                break;
            case "alipay":
                temp = R.mipmap.yue_zhifubao;
                break;
            case "one_card":
                temp = R.mipmap.icon_one_card_solution;
                break;
        }
        return temp;
    }
}
