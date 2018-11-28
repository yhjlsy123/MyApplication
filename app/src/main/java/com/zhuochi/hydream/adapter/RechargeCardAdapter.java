package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.RechargeCardItem;
import com.zhuochi.hydream.entity.RechargeableCardList;

import java.util.List;

/**
 * 充值卡
 * Created by and on 2016/12/9.
 */
public class RechargeCardAdapter extends BAdapter<RechargeableCardList> {
    private int selectRc_id = 0;

    public RechargeCardAdapter(Context context, int layoutId, List<RechargeableCardList> list) {
        super(list, layoutId, context);//
    }

    @Override
    public void upDateView(int position, ViewHolder holder, RechargeableCardList item) {
        View rootView = holder.getView(R.id.recharge_root, View.class);
        TextView view = holder.getView(R.id.text_money, TextView.class);
        TextView view2 = holder.getView(R.id.text_money_give, TextView.class);
//        if (!TextUtils.isEmpty(item.give)) {
//            view2.setVisibility(View.VISIBLE);
//            view2.setText("送" + item.give + "元");
//        } else {
//            view2.setVisibility(View.GONE);
//        }
        view.setText(item.getDenomination()+"元");
        if (item.getGive().equals("0.00")) {
            view2.setVisibility(View.GONE);
        } else {
            view2.setText("送" + item.getGive() + "元");
        }
        if (selectRc_id == 0) {
            selectRc_id = item.getId();
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(getItem(position), holder, position);
        }
        if (selectRc_id == item.getId()) {
            view.setTextColor(Color.WHITE);
            view2.setTextColor(Color.WHITE);
            rootView.setBackgroundResource(R.drawable.shape_bg_corner_blue_press);
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
    public void setRc_id(int rc_id) {
        selectRc_id = rc_id;
        notifyDataSetChanged();
    }
}
