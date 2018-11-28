package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderListItemBean;

import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderListItemBean, BaseViewHolder> {
    public OrderListAdapter(@LayoutRes int layoutResId, @Nullable List<OrderListItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderListItemBean bean) {
        holder.setText(R.id.order_sn, String.valueOf(bean.getOrder_sn()));
        TextView textView = holder.getView(R.id.order_state);
        textView.setTextColor(Color.parseColor("#25bd33"));
        if ("1".equals(bean.getOrder_state_key())) {
            textView.setText("未付款");
            textView.setTextColor(Color.RED);

        } else if ("4".equals(bean.getOrder_state_key())) {
            textView.setText("已完成");
            textView.setTextColor(Color.GREEN);

        } else if ("6".equals(bean.getOrder_state_key())) {
            textView.setText("已取消");
            textView.setTextColor(Color.BLUE);

        } else {
            textView.setText("使用中");
            textView.setTextColor(Color.parseColor("#52FFFF"));
        }
        holder.setText(R.id.create_time, bean.getCreate_time());
        holder.setText(R.id.bathroom, bean.getBathroom());
        holder.setText(R.id.order_amount, bean.getOrder_amount() + "元");
        holder.setText(R.id.cash_amount, bean.getCash_amount() + "元");
    }
   /* public OrderListAdapter(Context context, int layoutId, List<OrderListItemBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OrderListItemBean bean, int position) {
        holder.setText(R.id.order_sn,String.valueOf(bean.getOrder_sn()));
        holder.setText(R.id.order_state,bean.getOrder_state());
        holder.setText(R.id.create_time,bean.getCreate_time());
        holder.setText(R.id.bathroom,bean.getBathroom());
        holder.setText(R.id.order_amount,bean.getOrder_amount());
        holder.setText(R.id.cash_amount,bean.getCash_amount());
    }*/
}
