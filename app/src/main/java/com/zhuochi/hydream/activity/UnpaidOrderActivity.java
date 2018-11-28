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
import com.android.volley.ServerError;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.PayTypeGainAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.OneCardSolutionPWD;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.dialog.TipDialogEdit;
import com.zhuochi.hydream.entity.ErrorData;
import com.zhuochi.hydream.entity.PayTypeList;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.WXPayEntity;
import com.zhuochi.hydream.entity.exchang.OrderPay;
import com.zhuochi.hydream.entity.exchang.TurnOffPayType;
import com.zhuochi.hydream.entity.exchang.UnfinishedOrder;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.VolleySingleton;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.WXPayResultReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;

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
 * 未支付订单订单支付页
 * Created by and on 2016/11/11.
 */

public class UnpaidOrderActivity extends BaseAutoActivity {

    /*
    1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝7一卡通
     */
    private static final String ZHIFUBAO = "1";
    private static final String WEIXIN = "2";
    private static final String YIKATONG = "7";
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
    private UnfinishedOrder mOrder;
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
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show("支付成功");
                        Common.REFRESH_STATE = 1;

                        finish();
//                        payCallBack();
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
        setContentView(R.layout.activity_pay_unpaid_order);
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
                    } else if (pay_way.equals("alipay")) {
                        //支付宝支付
                        //保存订单号
                        final String orderInfo = pay.getPayArgs();   // 订单信息
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(UnpaidOrderActivity.this);
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
                    } else {
                        //余额支付
                        ToastUtils.show("支付成功");
                        Common.REFRESH_STATE = 1;

                        finish();
//                        payCallBack();
                    }
                }
                break;
            case "getAllowedPayType":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                List<TurnOffPayType.PayTypeBean> list = JSON.parseArray(JSON.toJSONString(jsonArray), TurnOffPayType.PayTypeBean.class);
                payTypeList(list);
                break;
            case "getOutstandingOrder"://获取未完成订单
                Map orderMap = (Map) result.getData().getData();
                String orderGson = GsonUtils.parseMapToJson(orderMap);
                mOrder = new Gson().fromJson(orderGson, UnfinishedOrder.class);
                initSetData(mOrder);
                break;

            case "getOtherAuthInfo":
                if (result.getData().getCode() == 100) {
                    Intent intent = new Intent(this, OneCardInfoActivity.class);
                    startActivity(intent);

                } else if (result.getData().getCode() == 200) {


                    OneCardSolutionPWD.Builder builder = new OneCardSolutionPWD.Builder(this);
                    builder.createPwd().show();
                    builder.setConfirmPwd(new OneCardSolutionPWD.OnConfirmListenerPWD() {
                        @Override
                        public void confirm(String pwd) {

                            if (mMoney == 0.0 || mMoney == 0) {
                                if (!pay_way.equals("sys_cash")) {
                                    ToastUtils.show("请选择余额支付！！");
                                } else {
                                    pay(pay_way, pwd);
                                }
                            } else {
                                pay(pay_way, pwd);
                            }


                        }
                    });


                }
        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
        if (JSON.toJSONString(s).contains("com.android.volley.ServerError")) {
            ToastUtils.show("系统错误");
            return;
        }
        String str = JSON.toJSONString(s);
        ErrorData errorData = JSON.parseObject(str, ErrorData.class);
        int code = errorData.getCode();
        switch (tag) {
            case "orderPay":
                if (code == 281) {
                    TipDialog2.show_(UnpaidOrderActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent payintent = new Intent(UnpaidOrderActivity.this, RechargeActivity.class);
                            payintent.putExtra("PayType", "Recharge");//充值
                            startActivity(payintent);
                            finish();
                        }
                    }, "友情提示", "余额不足..");

                }
                break;
        }

    }

    private void initData() {

        getOutstandingOrder();
    }

    /*接口中获取数据赋值*/
    private void initSetData(UnfinishedOrder order) {
        tvPay.setText(order.getOrder_amount() + "元");
        tvRealMoney.setText(order.getCash_amount() + "元");
        int time = Integer.valueOf(order.getTotal_used_time());
        mMoney = order.getOrder_amount();
        tvBlowerContentTime.setText(Tools.change(time));
        tvStartTime.setText(Common.Hourmin(String.valueOf(order.getStart_time())));
        tvEndTime.setText(Common.Hourmin(String.valueOf(order.getEnd_time())));
        morderSn = order.getOrder_sn();
        getAllowedPayType(order.getOrder_sn());
    }


    /**
     * 获取未完成订单
     */
    private void getOutstandingOrder() {
        params.addCallBack(this);
        params.getOutstandingOrder();
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
        // 轮询服务器，查询支付结果
//        if (TextUtils.equals(pay_way, WEIXIN)) {
//            if (!TextUtils.isEmpty(SPUtils.getString(Constants.ORDER_ID, ""))) {
//                payCallBack();
//            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.btn_submit, R.id.pay_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                if (adapter.getItem(adapter.getmPosition()) instanceof TurnOffPayType.PayTypeBean) {
                    TurnOffPayType.PayTypeBean itemPayType = (TurnOffPayType.PayTypeBean) adapter.getItem(adapter.getmPosition());
                    if (itemPayType.getTypeKey().equals("onecard")) {
                        params.addCallBack(this);
                        getOtherAuthInfo();

                    } else {
                        if (mMoney == 0.0 || mMoney == 0) {
                            if (!pay_way.equals("sys_cash")) {
                                ToastUtils.show("请选择余额支付！！");
                            } else {
                                pay(pay_way, null);
                            }
                        } else {
                            pay(pay_way, null);
                        }

                    }

                }

                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }

    /*获取认证信息*/
    private void getOtherAuthInfo() {
        params.addCallBack(this);
        params.getOtherAuthInfo(SPUtils.getString(Constants.MOBILE_PHONE, ""));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120) {
//            VolleySingleton.post(Neturl.PAY_PAGE, "pay", new HashMap<String, String>(), new VolleySingleton.CBack() {
//                @Override
//                public void onRequestSuccess(String result) {
//                    Order order = JsonUtils.parseJsonToBean(result, Order.class);
//                    if (order != null && order.data != null) {
////                        updateView(order.data);
//                    }
//                }
//            });
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    private void pay(String type, String pwd) {
        params.addCallBack(this);
        params.orderPay(mOrder.getOrder_sn(), type, pwd);
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
                        Common.REFRESH_STATE = 1;
//                        payCallBack();
                        finish();
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

        Intent intent = new Intent(this, PayResult.class);
        intent.putExtra("BlowerTime", tvBlowerContentTime.getText().toString());//洗浴时长
        intent.putExtra("OrderAmount", mOrder.getOrder_amount());//订单费用
        intent.putExtra("PayTime", String.valueOf(mPayTime));//支付时间
        intent.putExtra("OrderSn", morderSn);
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
