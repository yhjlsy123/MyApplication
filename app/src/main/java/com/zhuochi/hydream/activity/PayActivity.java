package com.zhuochi.hydream.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.PayTypeGainAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.AliPayResultInfor;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.ErrorData;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.WXPayEntity;
import com.zhuochi.hydream.entity.exchang.OrderPay;
import com.zhuochi.hydream.entity.exchang.TurnOffPayType;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.VolleySingleton;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.WXPayResultReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单支付页
 * Created by and on 2016/11/11.
 */

public class PayActivity extends BaseAutoActivity {

    /*
    1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝7一卡通
     */
    private static final String ZHIFUBAO = "1";
    private static final String WEIXIN = "2";
    private static final String YIKATONG = "7";
    @BindView(R.id.tv_Consumption)
    TextView tvConsumption;
    private PayTypeGainAdapter adapter;

    //        private String final String baiduqianbao="3";
    private static final String YUE = "4";
    @BindView(R.id.pay_back)
    ImageView payBack;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.tv_Pay)
    TextView tvPay;
    @BindView(R.id.tv_blowerContentTime)
    TextView tvBlowerContentTime;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.linetip)
    LinearLayout linetip;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_realMoney)
    TextView tvRealMoney;
    private HashMap<String, String> map;//存储提交服务器的参数
    private final String TAG = "PayActivity";

    private String pay_way;//支付的方式
    private Double mMoney;
    private static final int SDK_PAY_FLAG = 1;
    //    private static final int SDK_AUTH_FLAG = 2;
    private TurnOffPayType mTurnOff;
    private long mPayTime;//支付时间
    private XiRequestParams params;
    private String morderSn;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    com.zhuochi.hydream.entity.PayResult payResult = new com.zhuochi.hydream.entity.PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    Log.i("lsy", resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show("支付成功");
                        payCallBack();
                        Map<String, Object> pram = new HashMap<String, Object>();
                        AliPayResultInfor apliPay = JSON.parseObject(payResult.getResult(), AliPayResultInfor.class);
                        pram.put("mobile", SPUtils.getString(Constants.PHONE_NUMBER, null));
                        pram.put("mode", "alipay");
                        pram.put("out_trade_no", apliPay.getAlipay_trade_app_pay_response().getOut_trade_no());
                        pram.put("trade_no", apliPay.getAlipay_trade_app_pay_response().getTrade_no());
                        alterTwo(pram);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）

                        if (TextUtils.equals(resultStatus, "6001")) {
                            ToastUtils.show("取消支付");
                        } else if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.show("支付结果确认中...");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.show("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initData();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "orderPay":
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                OrderPay pay = new Gson().fromJson(gson, OrderPay.class);
                mPayTime = pay.getPayTime();

                if (pay.getResult().equals("success")) {
                    if (pay_way.equals("weixinpay")) {//微信支付
                        String weixin = pay.getPayArgs();
                        WXPayEntity entity = new Gson().fromJson(weixin, WXPayEntity.class);
                        weixinPay(entity);
                    } else if (pay_way.equals("alipay")) {//支付宝支付
                        //保存订单号
                        final String orderInfo = pay.getPayArgs();   // 订单信息
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else {//余额支付
                        payCallBack();
                    }
                }
                break;
            case "getAllowedPayType":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                List<TurnOffPayType.PayTypeBean> list = JSON.parseArray(JSON.toJSONString(jsonArray), TurnOffPayType.PayTypeBean.class);
                payTypeList(list);
                break;

        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        String str = new Gson().toJson(s).toString();
        ErrorData errorData = new Gson().fromJson(str, ErrorData.class);
        int code = errorData.getCode();
        switch (tag) {
            case "orderPay":
                if (code == 281) {
                    Intent payintent = new Intent(this, RechargeActivity.class);
                    payintent.putExtra("PayType", "Recharge");//充值
                    startActivity(payintent);
                    finish();
                }
                break;
        }
        super.onRequestFailure(tag, s);
    }

    private void initData() {
        String entity = getIntent().getStringExtra("entity");
        //按流程正常结算时初始化当前界面
        if (!TextUtils.isEmpty(entity)) {
            mTurnOff = new Gson().fromJson(entity, TurnOffPayType.class);
            map = new HashMap<>();
            tvPay.setText(mTurnOff.getOrderAmount() + "元");
            tvRealMoney.setText(mTurnOff.getCashAmount() + "元");
            mMoney = mTurnOff.getOrderAmount();
            tvBlowerContentTime.setText(Tools.change(mTurnOff.getUsedTime()));//洗浴时长
            tvStartTime.setText(Common.Hourmin(String.valueOf(mTurnOff.getStartTime())));
            tvEndTime.setText(Common.Hourmin(String.valueOf(mTurnOff.getEndTime())));
            morderSn = mTurnOff.getOrderSn();
            getAllowedPayType(mTurnOff.getOrderSn());
//            tvConsumption.setText(mTurnOff.getOrderUsage()+"升");
            if (mTurnOff.getOrderUsage().equals("0.0") || mTurnOff.getOrderUsage().equals("0.00")) {
                tvConsumption.setText("----");
            } else {
                if (!TextUtils.isEmpty(mTurnOff.getOrderUsage())) {
                    tvConsumption.setText(mTurnOff.getOrderUsage() + "升");
                } else {
                    tvConsumption.setText("----");
                }
            }
        }
    }


    /**
     * 根据订单编号查询支持的支付类型
     *
     * @param orderSn 订单号
     */

    private void getAllowedPayType(String orderSn) {
        params.addCallBack(this);
        params.getAllowedPayType(orderSn);
    }

    /**
     * 二次修正
     */

    public void alterTwo(Map<String, Object> pram) {
        params.addCallBack(this);
        params.comonRequest(pram, "PaymentApi/correctPayment");
    }

    /**
     * 支付类型
     *
     * @param data
     */
    private void payTypeList(List<TurnOffPayType.PayTypeBean> data) {
        if (adapter == null) {
            adapter = new PayTypeGainAdapter(this, data);
            listView.setAdapter(adapter);
            adapter.setItemClickListener(new PayTypeGainAdapter.ItemClickListener() {
                @Override
                public void change(String item) {
                    pay_way = item;
                }
            });

        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.btn_submit, R.id.pay_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (mMoney == 0.0 || mMoney == 0) {
                    if (!pay_way.equals("sys_cash")) {
                        ToastUtils.show("请选择余额支付！！");
                    } else {
                        pay(pay_way, null);
                    }
                } else {
                    pay(pay_way, null);
                }
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    private void pay(String type, String pwd) {
        params.addCallBack(this);
        params.orderPay(mTurnOff.getOrderSn(), type, pwd);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(HomeActivity.PAYRESULT);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll("WX_PAY_RESULT");
        SPUtils.saveString(Constants.ORDER_ID, "");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mWxPayResultReceiver != null) {
            unregisterReceiver(mWxPayResultReceiver);
            mWxPayResultReceiver = null;
        }
        super.onDestroy();
    }

    private WXPayResultReceiver mWxPayResultReceiver;

    /**
     * 微信支付调用
     *
     * @param entity
     */
    private void weixinPay(WXPayEntity entity) {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        mWxPayResultReceiver = new WXPayResultReceiver("2") {
            @Override
            public void onReceive(Context context, Intent intent) {
                LoadingAnimDialog.dismissLoadingAnimDialog();
                int payResult = intent.getExtras().getInt("payResult");
                switch (payResult) {
                    case 0:
                        ToastUtils.show("支付成功");
                        payCallBack();
                        break;
                    case -1:
                        ToastUtils.show("支付失败");
                        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll("WX_PAY_RESULT");
                        SPUtils.saveString(Constants.ORDER_ID, "");
                        break;
                    case -2:
                        ToastUtils.show("支付取消");
                        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll("WX_PAY_RESULT");
                        SPUtils.saveString(Constants.ORDER_ID, "");
                        break;
                }
                abortBroadcast();
                if (mWxPayResultReceiver != null) {
                    unregisterReceiver(mWxPayResultReceiver);
                    mWxPayResultReceiver = null;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("WXPAYRESULT");
        registerReceiver(mWxPayResultReceiver, intentFilter);
        PayReq request = new PayReq();
        request.appId = entity.getAppid();
        request.partnerId = entity.getPartnerid();
        request.prepayId = entity.getPrepayid();
        request.packageValue = "Sign=WXPay";
        // request.packageValue = unifiedOrder.data.package;
        request.nonceStr = entity.getNoncestr();
        String time = String.valueOf(entity.getTimestamp());

        request.timeStamp = time;
        request.sign = entity.getSign();
        register2Weixin(entity.getAppid());
        mMsgApi.sendReq(request);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        }, 2000);
    }

    IWXAPI mMsgApi;

    /**
     * 注册AppID
     */
    private void register2Weixin(String appId) {
        mMsgApi = WXAPIFactory.createWXAPI(this, appId);
        if (mMsgApi.isWXAppInstalled()) {
            // 将该app注册到微信
            mMsgApi.registerApp(appId);
        } else {
            LoadingAnimDialog.dismissLoadingAnimDialog();
            ToastUtils.show("您还没有安装微信");
        }
    }

    /**
     * 微信/支付宝支付回调
     */
    private void payCallBack() {
        //正常流程
        Intent intent = new Intent(this, PayResult.class);
        intent.putExtra("BlowerTime", tvBlowerContentTime.getText().toString());//洗浴时长
        intent.putExtra("OrderAmount", String.valueOf(mTurnOff.getOrderAmount()));//订单费用
        intent.putExtra("CashAmount", String.valueOf(mTurnOff.getCashAmount()));//订单费用
        intent.putExtra("PayTime", String.valueOf(mPayTime));//支付时间
        intent.putExtra("OrderSn", morderSn);
        intent.putExtra("orderUsage", tvConsumption.getText().toString());

        startActivity(intent);
        finish();
    }


    @Nullable
    @Override
    public CharSequence onCreateDescription() {
        return super.onCreateDescription();
    }


    private void callPhone() {
        try {
            String number = SPUtils.getString("kefu", "");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {

        }

    }
}
