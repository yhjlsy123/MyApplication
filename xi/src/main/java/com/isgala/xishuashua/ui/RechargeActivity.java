package com.isgala.xishuashua.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.RechargeCardAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean.UnifiedOrder;
import com.isgala.xishuashua.bean_.PayBean;
import com.isgala.xishuashua.bean_.PayCallBack;
import com.isgala.xishuashua.bean_.RechargeCard;
import com.isgala.xishuashua.bean_.RechargeCardItem;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.OneCardSolutionPWD;
import com.isgala.xishuashua.receiver.WXPayResultReceiver;
import com.isgala.xishuashua.utils.Common;
import com.isgala.xishuashua.utils.Des3;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.CustomGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.api.Neturl.GET_ONECARDRECHARGE;


/**
 * 充值界面
 * Created by and on 2016/12/9.
 */
public class RechargeActivity extends BaseAutoActivity implements OnItemClickListener<RechargeCardItem> {
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.recharge_gridview)
    CustomGridView rechargeGridview;
    RechargeCardAdapter rechargeCardAdapter;
    @BindView(R.id.weixin_recharge_icon)
    ImageView weixinRecharge;
    @BindView(R.id.zhifubao_recharge_icon)
    ImageView zhifubaoRecharge;
    @BindView(R.id.onecard_recharge_icon)
    ImageView onecardsolutionRecharge;
    @BindView(R.id.wallet_benjinzhanghu)
    TextView walletBenJin;
    @BindView(R.id.wallet_huodongyue)
    TextView walletHuoDong;

    //微信
    @BindView(R.id.recharge_weixin)
    RelativeLayout recharge_weixin;
    //支付宝
    @BindView(R.id.recharge_zhifubao)
    RelativeLayout recharge_zhifubao;
    //一卡通
    @BindView(R.id.recharge_one_card_solution)
    RelativeLayout recharge_one_card;
    /*
   1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝
    */
    private static final String ZHIFUBAO = "1";
    private static final String WEIXIN = "2";
    private static final String YIKATONG = "7";

    private static final int SDK_PAY_FLAG = 1;
//    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
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
    private HashMap<String, String> map;
    private String recharge_way;
    private Double moneyD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge3);
        ButterKnife.bind(this);
//        weixinRecharge.setImageResource(R.mipmap.pay_check);
//        zhifubaoRecharge.setImageResource(R.mipmap.pay_uncheck);
//        onecardsolutionRecharge.setImageResource(R.mipmap.pay_uncheck);
//        recharge_way = WEIXIN;
        map = new HashMap<>();
        map.put("type", "2");//充值
        initData();
        String balance = SPUtils.getString("balance", "");
        if (!TextUtils.isEmpty(balance)) {
            walletMoney.setText(balance);
        }
        String account = SPUtils.getString("account", "");
        if (!TextUtils.isEmpty(account)) {
            walletBenJin.setText(account);
        }
        String sub_account = SPUtils.getString("sub_account", "");
        if (!TextUtils.isEmpty(sub_account)) {
            walletHuoDong.setText(sub_account);
        }
        getRechargerype();
    }

    /*获取更多支付方式*/
    private void getRechargerype() {
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
                recharge_zhifubao.setVisibility(View.VISIBLE);
                if (i == 0) {
                    weixinRecharge.setImageResource(R.mipmap.pay_uncheck);
                    zhifubaoRecharge.setImageResource(R.mipmap.pay_check);
                    onecardsolutionRecharge.setImageResource(R.mipmap.pay_uncheck);
                    recharge_way = ZHIFUBAO;
                }
            } else if (list.get(i).toString().equals("2")) {//微信
                recharge_weixin.setVisibility(View.VISIBLE);
                if (i == 0) {
                    recharge_way = WEIXIN;
                    weixinRecharge.setImageResource(R.mipmap.pay_check);
                    zhifubaoRecharge.setImageResource(R.mipmap.pay_uncheck);
                    onecardsolutionRecharge.setImageResource(R.mipmap.pay_uncheck);
                }
            } else if (list.get(i).toString().equals("7")) {//一卡通
                recharge_one_card.setVisibility(View.VISIBLE);
                if (i == 0) {
                    recharge_way = YIKATONG;
                    weixinRecharge.setImageResource(R.mipmap.pay_uncheck);
                    zhifubaoRecharge.setImageResource(R.mipmap.pay_uncheck);
                    onecardsolutionRecharge.setImageResource(R.mipmap.pay_check);
                }
            }
        }
    }

    private void initData() {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.GENERAL_CARD, "generalcard", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                RechargeCard rechargeCard = JsonUtils.parseJsonToBean(result, RechargeCard.class);
                if (rechargeCard != null && rechargeCard.data != null) {
                    if (rechargeCard.data.size() > 0) {
                        updateList(rechargeCard.data);
                    }
                }
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        }, null);
    }

    private void updateList(List<RechargeCardItem> data) {
        rechargeGridview.setVisibility(View.VISIBLE);
        if (rechargeCardAdapter == null) {
            rechargeCardAdapter = new RechargeCardAdapter(this, R.layout.item_recharge_card, data);
            rechargeGridview.setAdapter(rechargeCardAdapter);
            rechargeCardAdapter.setOnItemClickListener(this);
        } else {
            rechargeCardAdapter.notifyDataSetChanged(data);
        }
    }

    @OnClick(R.id.recharge_back)
    public void onClick() {
        finish();
    }

    private String mRechargeMoney;//充值金额

    @Override
    public void onItemClick(RechargeCardItem item, ViewHolder holder, int position) {
        rechargeCardAdapter.setRc_id(item.rc_id);
        mRechargeMoney = item.recharge;
    }


    private DecimalFormat df = new DecimalFormat("######0.00");

    @OnClick({R.id.recharge_weixin, R.id.recharge_zhifubao, R.id.recharge_commit, R.id.recharge_rule, R.id.recharge_one_card_solution})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_weixin:
                weixinRecharge.setImageResource(R.mipmap.pay_check);
                zhifubaoRecharge.setImageResource(R.mipmap.pay_uncheck);
                onecardsolutionRecharge.setImageResource(R.mipmap.pay_uncheck);
                recharge_way = WEIXIN;
                break;
            case R.id.recharge_zhifubao:
                zhifubaoRecharge.setImageResource(R.mipmap.pay_check);
                weixinRecharge.setImageResource(R.mipmap.pay_uncheck);
                onecardsolutionRecharge.setImageResource(R.mipmap.pay_uncheck);
                recharge_way = ZHIFUBAO;
                break;
            case R.id.recharge_one_card_solution:
                onecardsolutionRecharge.setImageResource(R.mipmap.pay_check);
                weixinRecharge.setImageResource(R.mipmap.pay_uncheck);
                zhifubaoRecharge.setImageResource(R.mipmap.pay_uncheck);
                recharge_way = YIKATONG;
                break;
            case R.id.recharge_commit:
//                mRechargeMoney = "10";
                if (TextUtils.isEmpty(mRechargeMoney)) {
                    ToastUtils.show("网络不稳定,请稍后再试");
                    return;
                }
                moneyD = Double.valueOf(mRechargeMoney);
                map.put("price", df.format(moneyD));
                if (TextUtils.equals(recharge_way, WEIXIN)) {
                    weixinPay();
                } else if (TextUtils.equals(recharge_way, ZHIFUBAO)) {
                    zhifubaoPay();
                } else if (TextUtils.equals(recharge_way, YIKATONG)) {
                    showOneCarPop();
                }
                break;
            case R.id.recharge_rule://充值协议
                Intent intent = new Intent(RechargeActivity.this, H5Activity.class);
                intent.putExtra("title", "充值及退款协议");
                intent.putExtra("url", Neturl.RECHARGE_RULE);
                startActivity(intent);
                break;
        }
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
                    finish();
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RechargeActivity");
        MobclickAgent.onResume(this);
        //同步余额
        Common common = new Common();
        common.synchroAmount();
        if (TextUtils.equals(recharge_way, WEIXIN)) {
            if (!TextUtils.isEmpty(SPUtils.getString(Constants.ORDER_ID, ""))) {
                payCallBack(WEIXIN);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RechargeActivity");
        MobclickAgent.onPause(this);
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
                            PayTask alipay = new PayTask(RechargeActivity.this);
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

    /*弹出输入一卡通密码*/
    private void showOneCarPop() {
        OneCardSolutionPWD.Builder builder = new OneCardSolutionPWD.Builder(this);
        builder.createPwd().show();
        builder.setConfirmPwd(new OneCardSolutionPWD.OnConfirmListenerPWD() {
            @Override
            public void confirm(String pwd) {
                try {
                    String oneCardPwd = Des3.encode(pwd);
                    OneCardPay(oneCardPwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*一卡通充值*/
    private void OneCardPay(String cardPwd) {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("price", df.format(moneyD));//金额
        params.addBodyParameter("card_pwd", cardPwd);//一卡通密码 DES加密
        params.addBodyParameter("oauth_token", SPUtils.getString(Constants.OAUTH_TOKEN, ""));
        params.addBodyParameter("oauth_token_secret", SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.send(HttpRequest.HttpMethod.POST, GET_ONECARDRECHARGE, params, new RequestCallBack<String>() {
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

    @Override
    protected void onDestroy() {
        if (mWxPayResultReceiver != null) {
            unregisterReceiver(mWxPayResultReceiver);
            mWxPayResultReceiver = null;
        }
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll("WX_PAY_RESULT");
        SPUtils.saveString(Constants.ORDER_ID, "");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
