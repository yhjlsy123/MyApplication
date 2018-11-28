package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.RechargeableCardItemBean;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;

import java.util.List;

public class RechargeableCardManageAdapter extends BaseQuickAdapter<RechargeableCardItemBean,BaseViewHolder> {

    public RechargeableCardManageAdapter(int layoutResId, @Nullable List<RechargeableCardItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeableCardItemBean item) {
        helper.setText(R.id.card_name,item.getCard_name())
            .setText(R.id.created_time, Common.getDateToYMDHM(item.getCreated_time()))
            .setText(R.id.card_type,item.getCard_type())
            .setText(R.id.org_name,item.getOrg_name())
            .setText(R.id.give,item.getGive())
            .setText(R.id.org_area_name,item.getOrg_area_name());
    }
}
