package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;

import java.util.List;

/**
 * 设备管理  适配
 */
public class DeviceManageAdapter extends BaseQuickAdapter<DeviceManageItemBean,BaseViewHolder> {
    public DeviceManageAdapter(int layoutResId, @Nullable List<DeviceManageItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceManageItemBean item) {
        helper.setText(R.id.tv_device_name,item.getName_text());
        ImageLoadUtils.loadImage(mContext,item.getIcon(),(ImageView) helper.getView(R.id.device_iv));

//        Picasso.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.device_iv));
    }
}
