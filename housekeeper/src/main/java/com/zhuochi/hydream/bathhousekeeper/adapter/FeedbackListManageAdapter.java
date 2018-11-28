package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.FeedbackManageItemBean;

import java.util.List;

public class FeedbackListManageAdapter extends BaseQuickAdapter<FeedbackManageItemBean, BaseViewHolder> {


    public FeedbackListManageAdapter(@LayoutRes int layoutResId, @Nullable List<FeedbackManageItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FeedbackManageItemBean bean) {
        holder.setText(R.id.org_name, bean.getOrg_name());
        holder.setText(R.id.user_mobile, bean.getUser_mobile());
        holder.setText(R.id.create_time, bean.getCreate_time());
        holder.setText(R.id.suggestion_type, bean.getSuggestion_type());
        holder.setText(R.id.suggestion_content, bean.getSuggestion_content());
        if (null == bean.getHas_reply()) {
            bean.setHas_reply("no");
        }
        switch (bean.getHas_reply()) {
            case "yes":
                holder.setImageResource(R.id.f_status, R.mipmap.f_do);
                break;
            case "no":
                holder.setImageResource(R.id.f_status, R.mipmap.f_undo);
                break;

        }
        holder.addOnClickListener(R.id.user_mobile);
        holder.addOnClickListener(R.id.order_detail);
        holder.addOnClickListener(R.id.re_money);
        if (!(TextUtils.isEmpty(bean.getOrder_sn())) && TextUtils.equals("no", bean.getHas_reply())) {
            holder.setVisible(R.id.re_money, true);
        } else {
            holder.setVisible(R.id.re_money, false);
        }

    }

}
