package com.isgala.xishuashua.ui;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean.UnifiedOrder;
import com.isgala.xishuashua.bean_.Order;
import com.isgala.xishuashua.bean_.PayBean;
import com.isgala.xishuashua.bean_.PayCallBack;
import com.isgala.xishuashua.bean_.PayResultBean;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.OneCardSolutionPWD;
import com.isgala.xishuashua.receiver.WXPayResultReceiver;
import com.isgala.xishuashua.utils.Common;
import com.isgala.xishuashua.utils.Des3;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.api.Neturl.GET_ONECARDPAY;
import static com.isgala.xishuashua.api.Neturl.GET_ONECARDRECHARGE;


/**
 * 订单支付页
 * Created by and on 2016/11/11.
 */

public class PayActivity extends BaseAutoActivity {
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.pay_money)
    TextView payMoney;
    @BindView(R.id.use_time)
    TextView useTime;
    @BindView(R.id.yue_count)
    TextView yueCount;
    @BindView(R.id.yue_pay_icon)
    ImageView yuePayIcon;
    @BindView(R.id.weixin_pay_icon)
    ImageView weixinPayIcon;
    @BindView(R.id.zhifubao_pay_icon)
    ImageView zhifubaoPayIcon;

    @BindView(R.id.onecard_recharge_icon)
    ImageView onecard_recharge;
    @BindView(R.id.pay_root)
    RelativeLayout payRoot;
    /*
    1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝7一卡通
     */
    private static final String ZHIFUBAO = "1";
    private static final String WEIXIN = "2";
    private static final String YIKATONG = "7";

    //        private String final String baiduqianbao="3";
    private static final String YUE = "4";
    @BindView(R.id.pay_weixin)
    RelativeLayout payWeixin;
    @BindView(R.id.pay_zhifubao)
    RelativeLayout payZhifubao;
    @BindView(R.id.recharge_one_card_solution)
    RelativeLayout oneCardSolution;
    private HashMap<String, String> map;//存储提交服务器的参数
    private final String TAG = "PayActivity";
    /**
     * 支付的方式
     */
    private String pay_way;
    private String Order_ids;
    private static final int SDK_PAY_FLAG = 1;
//    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    com.isgala.xishuashua.bean.PayResult payResult = new com.isgala.xishuashua.bean.PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show("支付成功");
                        payCallBack(ZHIFUBAO);
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
//                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        // 其他状态值则为授权失败
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
//
//                    }
//                    break;
//                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        map = new HashMap<>();
        pay_way = YUE;
        yuePayIcon.setImageResource(R.mipmap.pay_check);
        weixinPayIcon.setImageResource(R.mipmap.pay_uncheck);
        zhifubaoPayIcon.setImageResource(R.mipmap.pay_uncheck);
        onecard_recharge.setImageResource(R.mipmap.pay_uncheck);
        getRechargerype();
        VolleySingleton.post(Neturl.PAY_PAGE, "pay", new HashMap<String, String>(), new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Order order = JsonUtils.parseJsonToBean(result, Order.class);
                if (order != null && order.data != null) {
                    Order_ids=order.data.order_id;
                    updateView(order.data);
                    map.put("order_id", order.data.order_id);
                    map.put("money", order.data.payable);
                    map.put("price", order.data.payable);
                    map.put("type", "1");//代表订单
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }
    /*获取更多支付方式*/
    private void getRechargerype() {
//         Order_ids=getIntent().getStringExtra("order_id");
        VolleySingleton.post(Neturl.GET_RECHARGERYPE, "getRechargeRype", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                String recharge_type = "";
                Result resultType = JsonUtils.parseJsonToBean(result, Result.class);
                if (resultType != null && resultType.data != null) {
                    if (resultType.data.status.equals("1")) {
                        recharge_type = resultType.data.recharge_type;
                        isShowRecharge(recharge_type);
                    }
                }

            }
        }, null);
    }
    private void isShowRecharge(String str) {
        String[] arr = str.split(",");//分割字符串得到数组
        List list = java.util.Arrays.asList(arr);//字符数组转list
        for (int i = 0; i < list.size(); i++) {//支付宝
            if (list.get(i).toString().equals("1")) {
                payZhifubao.setVisibility(View.VISIBLE);
            } else if (list.get(i).toString().equals("2")) {//微信
                payWeixin.setVisibility(View.VISIBLE);
            } else if (list.get(i).toString().equals("7")) {//一卡通
                oneCardSolution.setVisibility(View.VISIBLE);
            }
        }
    }
    private void updateView(Order.OrderDetail detail) {
        payMoney.setText(detail.payable);
        useTime.setText(detail.total_time);
        yueCount.setText("(还剩 " + Tools.formatMoney(detail.account) + "元)");
        SPUtils.saveString("balance", detail.account);
        pay.setText(getResources().getString(R.string.payable) + " " + detail.payable + "元");
//        if (TextUtils.equals("0", detail.hide)) {
//            payWeixin.setVisibility(View.VISIBLE);
//            payZhifubao.setVisibility(View.VISIBLE);
//        } else {
//            payWeixin.setVisibility(View.INVISIBLE);
//            payZhifubao.setVisibility(View.INVISIBLE);
//        }
        payRoot.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        // 轮询服务器，查询支付结果
        MobclickAgent.onPageStart("PayActivity");
        MobclickAgent.onResume(this);
        if (TextUtils.equals(pay_way, WEIXIN)) {
            if (!TextUtils.isEmpty(SPUtils.getString(Constants.ORDER_ID, ""))) {
                payCallBack(WEIXIN);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayActivity");
        MobclickAgent.onPause(this);
    }


    @OnClick({R.id.pay, R.id.pay_yue, R.id.pay_weixin, R.id.pay_zhifubao, R.id.pay_quest, R.id.pay_back, R.id.pay_recharage,R.id.recharge_one_card_solution})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay:
                pay(pay_way);
                break;
            case R.id.pay_back:
                finish();
                break;
            case R.id.pay_quest:
                callPhone();
                break;
            case R.id.pay_recharage:
                recharge();
                break;
            case R.id.pay_yue:
                pay_way = YUE;
                yuePayIcon.setImageResource(R.mipmap.pay_check);
                weixinPayIcon.setImageResource(R.mipmap.pay_uncheck);
                zhifubaoPayIcon.setImageResource(R.mipmap.pay_uncheck);
                onecard_recharge.setImageResource(R.mipmap.pay_uncheck);
                break;
            case R.id.pay_weixin:
                pay_way = WEIXIN;
                yuePayIcon.setImageResource(R.mipmap.pay_uncheck);
                weixinPayIcon.setImageResource(R.mipmap.pay_check);
                zhifubaoPayIcon.setImageResource(R.mipmap.pay_uncheck);
                onecard_recharge.setImageResource(R.mipmap.pay_uncheck);
                break;
            case R.id.pay_zhifubao:
                pay_way = ZHIFUBAO;
                yuePayIcon.setImageResource(R.mipmap.pay_uncheck);
                weixinPayIcon.setImageResource(R.mipmap.pay_uncheck);
                zhifubaoPayIcon.setImageResource(R.mipmap.pay_check);
                onecard_recharge.setImageResource(R.mipmap.pay_uncheck);
                break;
            case R.id.recharge_one_card_solution:
                pay_way = YIKATONG;
                yuePayIcon.setImageResource(R.mipmap.pay_uncheck);
                weixinPayIcon.setImageResource(R.mipmap.pay_uncheck);
                zhifubaoPayIcon.setImageResource(R.mipmap.pay_uncheck);
                onecard_recharge.setImageResource(R.mipmap.pay_check);
                break;
        }
    }


    private void recharge() {
        Intent intent = new Intent(PayActivity.this, RechargeActivity.class);
        intent.putExtra("from", "PayActivity");
        startActivityForResult(intent, 120);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120) {
            VolleySingleton.post(Neturl.PAY_PAGE, "pay", new HashMap<String, String>(), new VolleySingleton.CBack() {
                @Override
                public void runUI(String result) {
                    Order order = JsonUtils.parseJsonToBean(result, Order.class);
                    if (order != null && order.data != null) {
                        updateView(order.data);
                    }
                }
            });
        }
        LoadingTrAnimDialog.dismissLoadingAnimDialog();
    }

    private void pay(String type) {
        switch (type) {
            case YUE:
                //余额支付
                yuEPay();
                break;
            case WEIXIN:
                //  微信支付
                weixinPay();
                break;
            case ZHIFUBAO:
                //支付宝支付
                zhifubaoPay();
                break;
            case YIKATONG:
                //一卡通支付
//                get_onecardpay();
                showOneCarPop();
                break;
        }
    }
    /*弹出输入一卡通密码*/
    private void showOneCarPop() {
        OneCardSolutionPWD.Builder builder = new OneCardSolutionPWD.Builder(this);
        builder.createPwd().show();
        builder.setConfirmPwd(new OneCardSolutionPWD.OnConfirmListenerPWD() {
            @Override
            public void confirm(String pwd) {
                try {
                    String oneCardPwd = Des3.encode(pwd);
                    get_OneCardPay(oneCardPwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //一卡通支付
    private void  get_OneCardPay(String card_pwd){
        LoadingAnimDialog.showLoadingAnimDialog(this);
        RequestParams params = new RequestParams();
//        params.addBodyParameter("type","1");//
        params.addBodyParameter("order_id",Order_ids);
        params.addBodyParameter("card_pwd", card_pwd);//一卡通密码 DES加密
        params.addBodyParameter("oauth_token", SPUtils.getString(Constants.OAUTH_TOKEN, ""));
        params.addBodyParameter("oauth_token_secret", SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.send(HttpRequest.HttpMethod.POST, GET_ONECARDPAY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(com.lidroid.xutils.http.ResponseInfo<String> responseInfo) {
                Result payBean = JsonUtils.parseJsonToBean(responseInfo.result, Result.class);
                LoadingAnimDialog.dismissLoadingAnimDialog();
                if (payBean != null && payBean.data != null) {
                    //同步余额
                    Common common = new Common();
                    common.synchroAmount();
                    ToastUtils.show(payBean.data.msg);
                    Intent intent = getIntent();
                    setResult(120, intent);
                    finish();
                }
                //同步余额
                Common common = new Common();
                common.synchroAmount();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show("一卡通扣款失败，请稍后重试！");
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
//        LoadingTrAnimDialog.showLoadingAnimDialog(this);
//        Map map=new HashMap();
//        map.put("type","1");
//        map.put("order_id",Order_ids);
//        map.put("card_pwd",card_pwd);
//
//        VolleySingleton.post(GET_ONECARDPAY, "oneCardPay", map, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                Result payBean = JsonUtils.parseJsonToBean(result, Result.class);
//                if (payBean != null && payBean.data != null) {
//                    ToastUtils.show(payBean.data.msg);
//                    //同步余额
//                    Common common=new Common();
//                    common.synchroAmount();
//                    Intent intent=getIntent();
//                    setResult(120,intent);
//                    finish();
//
//                }
//                //同步余额
//                Common common=new Common();
//                common.synchroAmount();
//                LoadingTrAnimDialog.dismissLoadingAnimDialog();
//            }
//        });


    }


    /**
     * 支付
     */
    private void yuEPay() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.YUE_PAY, "pay", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                PayCallBack payCallBack = JsonUtils.parseJsonToBean(result, PayCallBack.class);
                if (payCallBack != null && payCallBack.data != null) {
                    ToastUtils.show(payCallBack.data.msg);
                    payResult(map.get("order_id"));
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
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

    private void weixinPay() {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        mWxPayResultReceiver = new WXPayResultReceiver("2") {
            @Override
            public void onReceive(Context context, Intent intent) {
                LoadingAnimDialog.dismissLoadingAnimDialog();
                int payResult = intent.getExtras().getInt("payResult");
                switch (payResult) {
                    case 0:
                        ToastUtils.show("支付成功");
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
        VolleySingleton.post(Neturl.WEIXINPAY, "WX_PAY_RESULT", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                UnifiedOrder unifiedOrder = JsonUtils.parseJsonToBean(result, UnifiedOrder.class);
                if (unifiedOrder != null && unifiedOrder.data != null) {
                    SPUtils.saveString(Constants.ORDER_ID, unifiedOrder.data.out_trade_no);
                    PayReq request = new PayReq();
                    request.appId = unifiedOrder.data.appid;
                    request.partnerId = unifiedOrder.data.partnerid;
                    request.prepayId = unifiedOrder.data.prepayid;
                    request.packageValue = "Sign=WXPay";
                    // request.packageValue = unifiedOrder.data.package;
                    request.nonceStr = unifiedOrder.data.noncestr;
                    request.timeStamp = unifiedOrder.data.timestamp;
                    request.sign = unifiedOrder.data.sign;
                    register2Weixin(unifiedOrder.data.appid);
                    mMsgApi.sendReq(request);
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            LoadingAnimDialog.dismissLoadingAnimDialog();
                        }
                    }, 2000);
                }
            }
        });
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

    private void zhifubaoPay() {
        VolleySingleton.post(Neturl.ALIPAY, "pay", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                PayBean payBean = JsonUtils.parseJsonToBean(result, PayBean.class);
                if (payBean != null && payBean.data != null) {
                    //保存订单号
                    SPUtils.saveString(Constants.ORDER_ID, payBean.data.out_trade_no);
                    final String orderInfo = payBean.data.string;   // 订单信息
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
                }
            }
        });
    }

    /**
     * 微信/支付宝支付回调
     *
     * @param type 支付方式，1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝
     */
    private void payCallBack(final String type) {
        HashMap<String, String> para = new HashMap<>();
        para.put("out_trade_no", SPUtils.getString(Constants.ORDER_ID, ""));
        para.put("type", type);
        VolleySingleton.post(Neturl.PAY_RESULT_CALLBACK, "WX_PAY_RESULT", para, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                PayCallBack payCallBack = JsonUtils.parseJsonToBean(result, PayCallBack.class);
                if (payCallBack != null && payCallBack.data != null) {
                    ToastUtils.show(payCallBack.data.msg);
                    payResult(map.get("order_id"));
                } else {
                    ToastUtils.show("");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            payCallBack(type);
                        }
                    }, 1000);
                }
            }
        });
    }

    @Nullable
    @Override
    public CharSequence onCreateDescription() {
        return super.onCreateDescription();
    }

    /**
     * 支付成功后跳转支付结果页
     *
     * @param orderId
     */
    private void payResult(String orderId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        VolleySingleton.post(Neturl.PAY_RESULT, "pay_result", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                PayResultBean payResultBean = JsonUtils.parseJsonToBean(result, PayResultBean.class);
                if (payResultBean != null && payResultBean.data != null) {
                    Intent intent = new Intent(PayActivity.this, PayResult.class);
                    intent.putExtra("bean", result);
                    startActivity(intent);
                    finish();
                }
            }
        });
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
