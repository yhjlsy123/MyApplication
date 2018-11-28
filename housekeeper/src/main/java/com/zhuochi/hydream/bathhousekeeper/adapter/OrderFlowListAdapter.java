package com.zhuochi.hydream.bathhousekeeper.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.MyMultipleItem;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderFolwItemBean;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;

import java.util.List;

public class OrderFlowListAdapter extends BaseMultiItemQuickAdapter<MyMultipleItem, BaseViewHolder> {

    public OrderFlowListAdapter(List<MyMultipleItem> data) {
        super(data);
        //必须绑定type和layout的关系
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.order_flow_frist_type_layout);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.order_flow_second_type_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMultipleItem item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                OrderFolwItemBean orderFolwItemBean = (OrderFolwItemBean) item;
                helper.setText(R.id.date, (orderFolwItemBean.getDate()))
                        .setText(R.id.order_number, String.valueOf(orderFolwItemBean.getNum()))
                        .setText(R.id.sell_number, String.valueOf(orderFolwItemBean.getMoney()));
                break;
            case MyMultipleItem.SECOND_TYPE:
                OrderFolwItemBean.OrderBean orderBean = (OrderFolwItemBean.OrderBean) item;
//                Common.getDateToMDHMS(orderBean.getOrder_date()).split(" ")[0]
                helper.setText(R.id.date, Common.ymdToString(orderBean.getOrder_date() + ""))
                        .setText(R.id.order_number, String.valueOf(orderBean.getNum()))
                        .setText(R.id.sell_number, String.valueOf(orderBean.getMoney()));
                break;

        }
    }


}
