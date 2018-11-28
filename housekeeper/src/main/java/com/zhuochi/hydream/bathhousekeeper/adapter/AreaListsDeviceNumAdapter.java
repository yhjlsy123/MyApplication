package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.AreaDeviceNumBean;

import java.util.List;

public class AreaListsDeviceNumAdapter extends BaseQuickAdapter<AreaDeviceNumBean,BaseViewHolder> {

    public AreaListsDeviceNumAdapter(int layoutResId, @Nullable List<AreaDeviceNumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaDeviceNumBean item) {
        helper.setText(R.id.school, item.getOrg_name())
                .setText(R.id.area, item.getOrg_area_name())
                .setText(R.id.user_num, item.getNum()+"");
    }
}