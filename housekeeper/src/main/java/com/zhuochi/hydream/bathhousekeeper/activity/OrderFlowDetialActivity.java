package com.zhuochi.hydream.bathhousekeeper.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderFlowDetialBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.GsonUtils;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.DensityUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderFlowDetialActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    Unbinder unbinder;
    @BindView(R.id.type_txt_img)
    TextView typeTxtImg;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.order_amount)
    TextView orderAmount;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.order_avage_price)
    TextView orderAvagePrice;
    @BindView(R.id.cash_amount)
    TextView cashAmount;
    @BindView(R.id.coupon_amount)
    TextView couponAmount;
    @BindView(R.id.date)
    TextView date;
    private XiRequestParams params;


    private long flow_date;
    private int org_id;
    private int org_area_id;
    private int device_area_id;
    private String device_type_key = "faucet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_flow_detial);
        unbinder = ButterKnife.bind(this);
        params = new XiRequestParams(this);
        flow_date = getIntent().getLongExtra("order_date", 0);
        device_type_key = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    private void initView() {
        int dp40 = DensityUtil.dip2px(this, 30);
        Drawable drawable = null;
        if ("blower".equals(device_type_key)) {
            //定义底部标签图片大小和位置
            drawable = getResources().getDrawable(R.mipmap.consumptiondetails_one);
            typeTxtImg.setText("吹风机");
        } else if ("faucet".equals(device_type_key)) {
            drawable = getResources().getDrawable(R.mipmap.consumptiondetails_two);
            typeTxtImg.setText("洗浴");
        } else {//washer
            drawable = getResources().getDrawable(R.mipmap.consumptiondetails_two);
            typeTxtImg.setText("洗衣机");
        }

        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable.setBounds(0, 0, dp40, dp40);
        //设置图片在文字的哪个方向
        typeTxtImg.setCompoundDrawables(drawable, null, null, null);
    }


    private void initData() {
        toolbarTitle.setText("账单详情");
        params.addCallBack(this);
        params.getOrderFlowDetial(SPUtils.getInt(Constants.USER_ID, 0), flow_date, org_id, org_area_id, device_area_id, device_type_key);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        String gson = GsonUtils.parseMapToJson((Map) result.getData().getData());
        OrderFlowDetialBean entity = JSON.parseObject(gson, OrderFlowDetialBean.class);
        total.setText(entity.getTotal());
        orderAmount.setText(entity.getOrder_amount());
        num.setText(entity.getNum());
        orderAvagePrice.setText(entity.getOrder_avage_price());
        cashAmount.setText(entity.getCash_amount());
        couponAmount.setText(entity.getCoupon_amount());
        date.setText(Common.ymdToString(entity.getDate()));
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
