package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.AreaUserItemBean;

import java.util.List;

public class AreaUserListsAdapter extends BaseQuickAdapter<AreaUserItemBean,BaseViewHolder> {
    String[] sexArr = {"保密","男","女"};
    public AreaUserListsAdapter(int layoutResId, @Nullable List<AreaUserItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaUserItemBean item) {
        helper.setText(R.id.name,item.getUser_nickname())
                .setText(R.id.capital,item.getAvailable_balance())
                .setText(R.id.mobile,item.getMobile())
                .setText(R.id.amount_grants,item.getGiven_balance())
                .setText(R.id.sex,sexArr[item.getSex()])
                .setText(R.id.account_balance,Double.parseDouble(item.getAvailable_balance())+Double.parseDouble(item.getGiven_balance())+"")
                .setText(R.id.school_name,item.getOrg_name()+"-"+item.getOrg_area_name())
                .setText(R.id.cash_pledge,item.getDeposit());
    }


}
