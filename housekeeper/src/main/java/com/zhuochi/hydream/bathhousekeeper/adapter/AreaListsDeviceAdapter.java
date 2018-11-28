package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.AreaDeviceItemBean;

import java.util.List;

public class AreaListsDeviceAdapter extends BaseQuickAdapter<AreaDeviceItemBean, BaseViewHolder> {

    public AreaListsDeviceAdapter(int layoutResId, @Nullable List<AreaDeviceItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaDeviceItemBean item) {
        helper.setText(R.id.name, item.getBathroom() + "(" + item.getGender() + ")")
                .setText(R.id.uuid, item.getUuid())
                .setText(R.id.stauts, item.getStatus_text());

//        status状态(0离线；1正常；2禁用；4维护；8删除)

        switch (item.getStatus()) {
            case "0":
                helper.setBackgroundRes(R.id.stauts, R.drawable.circle_blue);
                break;
            case "1":
                helper.setBackgroundRes(R.id.stauts, R.drawable.circle_green);
                break;
            case "2":
                helper.setBackgroundRes(R.id.stauts, R.drawable.circle_orange);
            case "4":
                helper.setBackgroundRes(R.id.stauts, R.drawable.circle_red);
                break;
            case "8":
                helper.setBackgroundRes(R.id.stauts, R.drawable.circle_gray);
                break;

        }
    }
}
