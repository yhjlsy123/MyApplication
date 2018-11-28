package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.BalanceDetailActivity;
import com.zhuochi.hydream.base.BAdapter;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.BalanceBean;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.view.RoundedImageView;

import java.util.List;


public class BalanceAdapter extends BAdapter<BalanceBean.BalanceListBean> {
    public BalanceAdapter(List<BalanceBean.BalanceListBean> list, int layoutId, Context context) {
        super(list, layoutId, context);
    }

    @Override/*获取控件并赋值*/
    public void upDateView(int position, ViewHolder holder, final BalanceBean.BalanceListBean item) {
        RoundedImageView imgSign = holder.getView(R.id.img_sign, RoundedImageView.class);
        TextView tvbalance_money = holder.getView(R.id.tv_balance_money, TextView.class);
        TextView tvbalance_name = holder.getView(R.id.tv_balance_name, TextView.class);
        TextView tvbalance_time = holder.getView(R.id.tv_balance_time, TextView.class);
        LinearLayout item_balance = holder.getView(R.id.item_balance_bg, LinearLayout.class);
        tvbalance_money.setText(item.getBill_amount());
        tvbalance_name.setText(item.getPurpose_name());
        tvbalance_time.setText(Common.getDateToMDHMS(item.getCreate_time()));
        String ImageFaultUrl = RequestURL.ICON_URL + "/" + item.getIcon();
        ImageLoadUtils.loadImage(context, ImageFaultUrl, imgSign);

        item_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*跳转到详情页面*/
                Intent intent = new Intent(context, BalanceDetailActivity.class);
                intent.putExtra("balance", JSON.toJSONString(item));
                context.startActivity(intent);
            }
        });
    }
}