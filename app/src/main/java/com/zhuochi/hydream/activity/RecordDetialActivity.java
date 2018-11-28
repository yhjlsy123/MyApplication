package com.zhuochi.hydream.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.OrderDialog;
import com.zhuochi.hydream.entity.RecordDetialEntity;
import com.zhuochi.hydream.entity.RecordEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.RequestURL;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费详情
 */
public class RecordDetialActivity extends BaseAutoActivity {


    XiRequestParams params;
    int order_item_id;
    RecordEntity.ConsumptionListBean consumptionListBean;
    @BindView(R.id.userinfo_cancle)
    ImageView userinfoCancle;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.img_sign)
    ImageView imgSign;
    @BindView(R.id.text_pay_type)
    TextView textPayType;
    @BindView(R.id.text_pay_money)
    TextView textPayMoney;
    @BindView(R.id.text_pay_state)
    TextView textPayState;
    @BindView(R.id.payment_method)
    TextView paymentMethod;
    @BindView(R.id.long_bath_time)
    TextView longBathTime;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.creation_time)
    TextView creationTime;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_doubt)
    TextView orderDoubt;
    @BindView(R.id.activity_record_detial)
    LinearLayout activityRecordDetial;

    @BindView(R.id.tv_realMoney)
    TextView tvRealMoney;

    @BindView(R.id.dafault)
    View dafault;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.tv_Consumption)
    TextView tvConsumption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detial);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initData();
    }

    /*获取上一个页面传过来额consumptionListBean，并获取order_item_id，填充页面图标*/
    private void initData() {
        String json = getIntent().getExtras().getString("order");
        if (json != null) {
            consumptionListBean = JSON.parseObject(json, RecordEntity.ConsumptionListBean.class);
            order_item_id = consumptionListBean.getOrder_item_id();
            if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
                lin.setVisibility(View.GONE);
                dafault.setVisibility(View.VISIBLE);
            } else {
                consumptionDetailed();
                String ImageFaultUrl = RequestURL.ICON_URL + "/" + consumptionListBean.getIcon();
                ImageLoadUtils.loadImage(this, ImageFaultUrl, imgSign);
            }

        }
    }

    /*消费详情*/
    private void consumptionDetailed() {
        params.addCallBack(this);
        params.consumptionDetailed(UserUtils.getInstance(this).getMobilePhone(), UserUtils.getInstance(this).getUserID(), order_item_id);
    }

    @OnClick({R.id.order_doubt, R.id.userinfo_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.order_doubt:
//                AboutOrd();
                showDialog();
                break;
        }

    }

    /*对订单费用有疑问*/
//    private void AboutOrd() {
//        Dialog dialog = TipDialog2.show_(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getAboutOrde();
//            }
//        }, "提示信息", "您有未支付的订单，请支付？");
//    }

    /*对订单费用有疑问*/
    private void getAboutOrde(String editText) {
        params.addCallBack(this);
        params.getDoubtAboutOrder(consumptionListBean.getOrder_sn(),editText);
    }


    /**
     * 弹出确认选择的提示框
     */
    private void showDialog() {
        OrderDialog.Builder builder = new OrderDialog.Builder(this);
        builder.create().show();
        builder.setOnClickListener(new OrderDialog.OrderListener() {
                                       @Override
                                       public void submit(String editText) {
                                           getAboutOrde(editText);
                                       }
                                   }
        );
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "consumptionDetailed"://获取详情并给控件赋值
                Object object = result.getData().getData();
                RecordDetialEntity recordDetialEntity = JSON.parseObject(JSON.toJSONString(object), RecordDetialEntity.class);

                if (recordDetialEntity != null) {
                    lin.setVisibility(View.VISIBLE);
                    dafault.setVisibility(View.GONE);
                    /*控件赋值*/
                    textPayType.setText(recordDetialEntity.getTitle());
                    textPayMoney.setText(recordDetialEntity.getCash_amount());
                    switch (recordDetialEntity.getOrder_state()) {
                        case "1":
                            textPayState.setText("未付款");
                            break;
                        default:
                            textPayState.setText("已完成");
                            break;
                    }
                    tvRealMoney.setText(recordDetialEntity.getCash_amount()+"元");
                    paymentMethod.setText(recordDetialEntity.getPayTypeName());
//                    longBathTime.setText(Tools.ToHMS(recordDetialEntity.getTotal_used_time()));
                    longBathTime.setText(Common.change(recordDetialEntity.getTotal_used_time()));

                    startTime.setText(Common.getHourmin(recordDetialEntity.getStart_time()));
                    endTime.setText(Common.getHourmin(recordDetialEntity.getEnd_time()));
                    if (recordDetialEntity.getPayInfo()!=null) {
                        if (!TextUtils.isEmpty(recordDetialEntity.getPayInfo().getExternal_pay_sn())) {
                            orderNumber.setText(recordDetialEntity.getPayInfo().getExternal_pay_sn());
                        } else {
                            orderNumber.setText(recordDetialEntity.getOrder_sn());
                        }
                    }else {
                        orderNumber.setText(recordDetialEntity.getOrder_sn());
                    }
                    if (recordDetialEntity.getActual_usage().equals("0.0")||recordDetialEntity.getActual_usage().equals("0.00")){
                        tvConsumption.setText("----");
                    }else {
                        if (!TextUtils.isEmpty(recordDetialEntity.getActual_usage())) {
                            tvConsumption.setText(recordDetialEntity.getActual_usage() + "升");
                        }else {
                            tvConsumption.setText("----");
                        }
                    }
                } else {
                    /*数据为空显示缺省内容*/
                    lin.setVisibility(View.GONE);
                    dafault.setVisibility(View.VISIBLE);
                }
                break;
            case "doubtAboutOrder":
                ToastUtils.show(result.getData().getMsg());
                break;
        }
        super.onRequestSuccess(tag, result);
    }

}
