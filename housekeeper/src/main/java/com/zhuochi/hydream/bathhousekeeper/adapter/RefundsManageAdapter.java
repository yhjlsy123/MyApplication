package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.RefundsManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;

import java.util.List;

public class RefundsManageAdapter extends BaseQuickAdapter<RefundsManageItemBean, BaseViewHolder> {
    //1:处理中 2：退款成功 3：拒绝退款 4：退款失败 5:用户取消
    private int[] arr = {R.color.black_rank_text, R.color.red_fe0000, R.color.green_25bd33, R.color.black_rank_text, R.color.yellow_f49b17, R.color.blue_system,};

    public RefundsManageAdapter(int layoutResId, @Nullable List<RefundsManageItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RefundsManageItemBean item) {
        helper.setText(R.id.name, item.getUser_nickname())
                .setText(R.id.staus, item.getHandle_status_text())
                .setTextColor(R.id.staus, mContext.getResources().getColor(arr[item.getHandle_status()]))
                .setText(R.id.mobile, item.getUser_nickname())
                .setText(R.id.time, Common.getDateToMDHMS(item.getCreated()))
                .setText(R.id.school_name, item.getOrg_name() + item.getOrg_area_name())
                .setText(R.id.money, item.getApply_refund_money());
        helper.addOnClickListener(R.id.exit_money);
        if (item.getHandle_status() == 1) {
            helper.setVisible(R.id.exit_money, true);
        } else {
            helper.setVisible(R.id.exit_money, false);
        }


    }
}
