package com.isgala.xishuashua.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BAdapter;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.BalanceList;
import com.isgala.xishuashua.bean_.RechargeCardItem;

import java.util.List;

import static android.R.attr.data;
import static android.R.id.list;

/**
 * 充值卡
 * Created by and on 2016/12/9.
 */
public class RechargeCardAdapter extends BAdapter<RechargeCardItem> {
    private String selectRc_id;

    public RechargeCardAdapter(Context context, int layoutId, List<RechargeCardItem> list) {
        super(list, layoutId, context);//
    }

    @Override
    public void upDateView(int position, ViewHolder holder, RechargeCardItem item) {
        View rootView = holder.getView(R.id.recharge_root, View.class);
        TextView view = holder.getView(R.id.text_money, TextView.class);
        TextView view2 = holder.getView(R.id.text_money_give, TextView.class);
        if (!TextUtils.isEmpty(item.give)) {
            view2.setVisibility(View.VISIBLE);
            view2.setText("送" + item.give + "元");
        } else {
            view2.setVisibility(View.GONE);
        }
        view.setText(item.name);
        if (TextUtils.isEmpty(selectRc_id)) {
            selectRc_id = item.rc_id;
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(getItem(position), holder, position);
        }
        if (TextUtils.equals(selectRc_id, item.rc_id)) {
            view.setTextColor(Color.WHITE);
            view2.setTextColor(Color.WHITE);
            rootView.setBackgroundColor(Color.parseColor("#4DA9FF"));
        } else {
            view.setTextColor(Color.BLACK);
            view2.setTextColor(Color.BLACK);
            rootView.setBackgroundResource(R.drawable.selector_bg_gray_gray);
        }
    }

    /**
     * 设置默认,并刷新
     *
     * @param rc_id
     */
    public void setRc_id(String rc_id) {
        selectRc_id = rc_id;
        notifyDataSetChanged();
    }
}
