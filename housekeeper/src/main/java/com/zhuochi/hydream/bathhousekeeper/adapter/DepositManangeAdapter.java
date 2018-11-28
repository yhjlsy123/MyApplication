package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.DepositManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;

import java.util.List;

public class DepositManangeAdapter extends BaseQuickAdapter<DepositManageItemBean,BaseViewHolder> {
    public DepositManangeAdapter(int layoutResId, @Nullable List<DepositManageItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepositManageItemBean item) {
        helper.setText(R.id.name,item.getUser_nickname())
                .setText(R.id.school_name,item.getOrg_name())
                .setText(R.id.school_area_name,item.getOrg_area_name())
                .setText(R.id.mobile,item.getMobile())
                .setText(R.id.deposit,item.getDeposit())
                .setText(R.id.time, Common.getDateToYMDHM(item.getCreate_time()));

    }
}
