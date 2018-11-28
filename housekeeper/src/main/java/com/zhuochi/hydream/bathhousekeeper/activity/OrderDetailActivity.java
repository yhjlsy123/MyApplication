package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderDetailBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.GsonUtils;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.order_sn)
    TextView orderSn;
    @BindView(R.id.order_state)
    TextView orderState;
    @BindView(R.id.pay_time)
    TextView payTime;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.cash_amount)
    TextView cashAmount;
    @BindView(R.id.time_period)
    TextView timePeriod;
    @BindView(R.id.use_duration)
    TextView useDuration;
    @BindView(R.id.billing_duration)
    TextView billingDuration;
    @BindView(R.id.bathroom)
    TextView bathroom;
    @BindView(R.id.org_area_name)
    TextView orgAreaName;
    private XiRequestParams params;
    private int order_item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        order_item_id = getIntent().getIntExtra("order_item_id", 0);
        params = new XiRequestParams(this);
        initView();
        initData();
    }


    private void initView() {
        toolbarTitle.setText("订单详情");
    }

    private void initData() {
        params.addCallBack(this);
        params.getOrderDetail(SPUtils.getInt(Constants.USER_ID, 0), order_item_id);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        Map map = (Map) result.getData().getData();
        String gson = GsonUtils.parseMapToJson(map);
        OrderDetailBean orderDetailBean = JSON.parseObject(gson, OrderDetailBean.class);
        orderSn.setText(orderDetailBean.getOrder_sn());
        orderState.setText(orderDetailBean.getOrder_state());
        payTime.setText(Common.getDateToYMDHM(orderDetailBean.getPay_time()));
        mobile.setText(orderDetailBean.getMobile());
        cashAmount.setText(orderDetailBean.getCash_amount());
        String[] a = orderDetailBean.getStart_time().split(" ");
        timePeriod.setText(a[1]);
        //useDuration.setText( orderDetailBean.getTotal_used_time());
        //计费时长
        //billingDuration.setText( orderDetailBean.getTotal_used_time());
        bathroom.setText(orderDetailBean.getBathroom());
        orgAreaName.setText(orderDetailBean.getOrg_name() + orderDetailBean.getOrg_area_name());

    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:

                break;
        }
    }
}
