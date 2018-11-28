package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.entity.ActivityEntity;

import java.util.List;

public class ActivityListAdapter extends BaseItemDraggableAdapter<ActivityEntity,BaseViewHolder> {
    public ActivityListAdapter(@LayoutRes int layoutResId, @Nullable List<ActivityEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ActivityEntity bean) {
        holder.setText(R.id.exercie_title,bean.getActivity_name());
//        TextView textView=holder.getView(R.id.order_state);
//        textView.setTextColor(Color.parseColor("#25bd33"));
//        if ("1".equals(bean.getOrder_state_key())){
//            textView.setText("未付款");
//            textView.setTextColor(Color.RED);
//
//        }else if ("4".equals(bean.getOrder_state_key())){
//            textView.setText("已完成");
//            textView.setTextColor(Color.GREEN);
//
//        }else if ("6".equals(bean.getOrder_state_key())){
//            textView.setText("已取消");
//            textView.setTextColor(Color.BLUE);
//
//        }else {
//            textView.setText("使用中");
//            textView.setTextColor(Color.parseColor("#52FFFF"));
//        }
        holder.setText(R.id.exercie_school,bean.getOrg_name()+"-"+bean.getOrg_area_name());
//        holder.setText(R.id.exercie_category,bean.getBathroom());
        holder.setText(R.id.exercie_sex,bean.getGender());
//        holder.setText(R.id.cash_amount,bean.getCash_amount());
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
