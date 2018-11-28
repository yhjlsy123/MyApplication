package com.zhuochi.hydream.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.PayTypeAdapter;
import com.zhuochi.hydream.adapter.RechargeCardAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.bean_.AliPayResultInfor;
import com.zhuochi.hydream.bean_.Result;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.dialog.OneCardSolutionPWD;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.dialog.TipDialogEdit;
import com.zhuochi.hydream.entity.DepositEntity;
import com.zhuochi.hydream.entity.PayResult;
import com.zhuochi.hydream.entity.PayTypeList;
import com.zhuochi.hydream.entity.RechargeableCardList;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.WXPayEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.VolleySingleton;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.receiver.WXPayResultReceiver;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.Des3;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.JsonUtils;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.CustomGridView;
import com.zhuochi.hydream.view.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.api.Neturl.GET_ONECARDRECHARGE;
import static com.zhuochi.hydream.api.Neturl.IFAUTHONECARD;


/**
 * 充值界面
 * Created by and on 2016/12/9.
 */
public class RechargeActivity extends BaseAutoActivity implements OnItemClickListener<RechargeableCardList> {
    @BindView(R.id.recharge_gridview)
    CustomGridView rechargeGridview;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    RechargeCardAdapter rechargeCardAdapter;
    PayTypeAdapter adapter;
    /*
   1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝
    */
    private static final String ZHIFUBAO = "1";
    private static final String WEIXIN = "2";
    private static final String YIKATONG = "7";

    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.listViews)
    ListView listView;
    @BindView(R.id.recharge_back)
    ImageView rechargeBack;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.img_user)
    RoundedImageView imgUser;
    @BindView(R.id.recharge_commit)
    Button rechargeCommit;
    @BindView(R.id.recharge_bottom)
    LinearLayout rechargeBottom;
    @BindView(R.id.tv_appName)
    TextView tvAppName;
    private XiRequestParams params;
    private int mSettlementAreaId = 0;// 结算区域ID
    private String payType = "";//支付类型
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
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
                        Map<String, Object> pram = new HashMap<String, Object>();
                        AliPayResultInfor apliPay = JSON.parseObject(payResult.getResult(), AliPayResultInfor.class);
                        pram.put("mobile", SPUtils.getString(Constants.PHONE_NUMBER, null));
                        pram.put("mode", "alipay");
                        pram.put("out_trade_no", apliPay.getAlipay_trade_app_pay_response().getOut_trade_no());
                        pram.put("trade_no", apliPay.getAlipay_trade_app_pay_response().getTrade_no());
                        alterTwo(pram);
                        finish();
//                        payCallBack(ZHIFUBAO);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "6001")) {
                            ToastUtils.show("取消支付");
                        } else if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.show("支付结果确认中...");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.show("支付失败")
                            ;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private String recharge_way;
    private Double moneyD;
    private String mPayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        mPayType = getIntent().getStringExtra("PayType");
        if (mPayType.equals("Recharge")) {
            tvTitle.setText("充值");
            getRechargeList();
            getRechargeType();
        } else if (mPayType.equals("deposit")) {
            tvTitle.setText("押金充值");
            getDepositInfo();
        }
        String phone = SPUtils.getString(Constants.MOBILE_PHONE, "");
        tvUserName.setText(phone);
//        String userName = SPUtils.getString("nickName", "");
        String avatar = SPUtils.getString("Avatar", "");
//        if (TextUtils.isEmpty(userName)){
//            String phone = SPUtils.getString(Constants.MOBILE_PHONE, "");
//            tvUserName.setText(phone);
//        }else {
//            tvUserName.setText(userName);
//        }
        if (TextUtils.isEmpty(avatar)) {
            imgUser.setBackgroundResource(R.mipmap.defaut_user_photo);
        } else {
            ImageLoadUtils.loadImage(this, imgUser, avatar);
        }
        tvAppName.setText(getResources().getText(R.string.app_name));

    }


    /**
     * 获取押金支付类型
     */
    private void getDepositInfo() {
        int device_area_id = SPUtils.getInt("Device_area_id", 0);
        params.addCallBack(this);
        params.getDepositInfo(device_area_id);
    }

    /**
     * 支付押金
     */
    private void payDeposit() {
        params.addCallBack(this);
        params.payDeposit(mSettlementAreaId, payType);
    }

    /*充值卡列表*/
    private void getRechargeList() {
        params.addCallBack(this);
        params.rechargeCards(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone());
    }

    /*充值方式*/
    private void getRechargeType() {
        params.addCallBack(this);
        params.allowRechargeMethod(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone());
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getDepositInfo"://获取押金支付类型
                Map map = (Map) result.getData().getData();

                DepositEntity entitys = new Gson().fromJson(GsonUtils.parseMapToJson(map), DepositEntity.class);
                mSettlementAreaId = entitys.getSettlementAreaId();
                if (!TextUtils.isEmpty(entitys.getAmount())) {//判断
                    tvMoney.setVisibility(View.VISIBLE);
                    tvMoney.setText(entitys.getAmount() + "元");
                }
                List<DepositEntity.PayTypeBean> payTypelist = entitys.getPayType();
                List<PayTypeList> lis = JSON.parseArray(JSON.toJSONString(payTypelist), PayTypeList.class);
                payTypeList(lis);
                break;
            case "payDeposit"://支付押金
                getDoRecharge(result);
                break;
            case "rechargeCards"://充值卡列表
                JSONArray jsonArray1 = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray1.size() > 0) {
                    List<RechargeableCardList> list = JSON.parseArray(JSON.toJSONString(jsonArray1), RechargeableCardList.class);
                    updateList(list);
                } else {
                    rechargeBottom.setVisibility(View.GONE);
                }
                break;
            case "allowRechargeMethod"://充值方式
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    List<PayTypeList> lists = JSON.parseArray(JSON.toJSONString(jsonArray), PayTypeList.class);
                    payTypeList(lists);
                }
                break;
            case "doRecharge"://充值
                getDoRecharge(result);
                break;

            case "getOtherAuthInfo":
                if (result.getData().getCode() == 100) {
                    Intent intent = new Intent(this, OneCardInfoActivity.class);
                    startActivity(intent);

                } else if (result.getData().getCode() == 200) {
                    if (mPayType.equals("Recharge")) {//点击充值
                        pay();
                    } else if (mPayType.equals("deposit")) {//点击押金充值
                        payDeposit();
                    }
                }
                break;
            case "PaymentApi/correctPayment":
                break;


        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        switch (tag) {
            case "rechargeCards":
                rechargeBottom.setVisibility(View.GONE);
                break;
        }
        super.onRequestFailure(tag, s);
    }

    /**
     * 二次修正
     */

    public void alterTwo(Map<String, Object> pram) {
        params.addCallBack(this);
        params.comonRequest(pram, "PaymentApi/correctPayment");
    }

    /**
     * 点击进行支付
     *
     * @param result 参数
     */
    private void getDoRecharge(SonBaseEntity result) {
        if (payType.equals("alipay")) {//支付宝支付
            final String data = result.getData().getData().toString();
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(RechargeActivity.this);
                    Map<String, String> results = alipay.payV2(data, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = results;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
        if (payType.equals("weixinpay")) {//微信支付
            try {
                String gson = result.getData().getData().toString();
                WXPayEntity entity = new Gson().fromJson(gson, WXPayEntity.class);
                Common.WX_APPID = entity.getAppid();
                weixinPay(entity);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("支付异常,请联系管理员");
            }
        }
        if ("onecard".equals(payType)) {
            if (result.getData().getCode() == 111) {
                //沒有认证过一卡通
                ToastUtils.show("请先来认证一卡通");
                startActivity(new Intent(this, OneCardInfoActivity.class));
            } else if (result.getData().getCode() == 200) {
                //一卡通支付成功
                ToastUtils.show("支付成功");
                finish();
            } else {
                ToastUtils.show(result.getData().getMsg());
            }
        }
    }


    /**
     * 支付类型显示  图标及样式 适配
     *
     * @param data
     */
    private void payTypeList(List<PayTypeList> data) {
        if (adapter == null) {
            adapter = new PayTypeAdapter(this, data);
            listView.setAdapter(adapter);
            adapter.setItemClickListener(new PayTypeAdapter.ItemClickListener() {
                @Override
                public void change(String item) {
                    payType = item;
                }
            });

        } else {
            adapter.notifyDataSetChanged();
        }
    }


    /*充值卡  适配*/
    private void updateList(List<RechargeableCardList> data) {
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

    private int selectRc_id;//充值金额

    @Override
    public void onItemClick(RechargeableCardList item, ViewHolder holder, int position) {
        rechargeCardAdapter.setRc_id(item.getId());
        selectRc_id = item.getId();
    }

    private DecimalFormat df = new DecimalFormat("######0.00");

    @OnClick({R.id.recharge_commit, R.id.recharge_refund_desc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_commit:

                if (adapter.getItem(adapter.getmPosition()) instanceof PayTypeList) {
                    PayTypeList itemPayType = (PayTypeList) adapter.getItem(adapter.getmPosition());
                    if (itemPayType.getMethod().equals("onecard")) {
                        params.addCallBack(this);
                        getOtherAuthInfo();

                    } else {
                        if (mPayType.equals("Recharge")) {//点击充值
                            pay();
                        } else if (mPayType.equals("deposit")) {//点击押金充值
                            payDeposit();
                        }

                    }

                } else {
                    if (mPayType.equals("Recharge")) {//点击充值
                        pay();
                    } else if (mPayType.equals("deposit")) {//点击押金充值
                        payDeposit();
                    }

                }


                break;
            case R.id.recharge_refund_desc:
                String mGson = SPUtils.getString("initSetting", "");
                if (!TextUtils.isEmpty(mGson)) {
                    InitSettingEntity mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
                    Intent intent2 = new Intent(this, H5Activity.class);
                    intent2.putExtra("title", "充值及退款协议");
                    intent2.putExtra("url", mInitEntity.getRefundAgreementUrl());
                    startActivity(intent2);
                }
                break;
        }
    }

    /*获取认证信息*/
    private void getOtherAuthInfo() {
        params.addCallBack(this);
        params.getOtherAuthInfo(SPUtils.getString(Constants.MOBILE_PHONE, ""));
    }


    /**
     * 微信/支付宝支付回调 type 支付方式，1支付宝2微信3百度钱包4平台余额5余额+微信6余额+支付宝
     */

    public void onResume() {
        super.onResume();
        //同步余额
        Common common = new Common();
        common.synchroAmount();
        if (TextUtils.equals(recharge_way, WEIXIN)) {
            if (!TextUtils.isEmpty(SPUtils.getString(Constants.ORDER_ID, ""))) {
//                payCallBack(WEIXIN);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void pay() {
        if ("onecard".equals(payType)) {//如果是一卡通  则进行一卡通参数调用
//            BalanceOrder();
            showOneCarPop();
        } else {
            params.addCallBack(this);
            params.doRecharge(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone(), payType, selectRc_id, "0");
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
                    int userid = UserUtils.getInstance(RechargeActivity.this).getUserID();
                    String userphone = UserUtils.getInstance(RechargeActivity.this).getMobilePhone();
                    params.addCallBack(RechargeActivity.this);
                    tvTitle.setTag(R.id.tv_title, pwd);
                    params.doRecharge(userid, userphone, payType, selectRc_id, pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*验证一卡通是否通过认证*/
    private void ifAuthOneCard() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("oauth_token", SPUtils.getString(Constants.OAUTH_TOKEN, ""));
        params.addBodyParameter("oauth_token_secret", SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.send(HttpRequest.HttpMethod.POST, IFAUTHONECARD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Result oneCardEntity = JsonUtils.parseJsonToBean(responseInfo.result, Result.class);
                if (oneCardEntity.data != null) {
                    if (oneCardEntity.data.is_checked.equals("1")) {
                        showOneCarPop();
                    } else {
                        ToastUtils.show("请先来认证一卡通");
                        startActivity(new Intent(RechargeActivity.this, UserActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
//                ToastUtils.show("一卡通没通过认证，请稍后重试！");
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
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
                        finish();
                        break;
                    case -1:
                        ToastUtils.show("支付失败");
//                        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll("WX_PAY_RESULT");
//                        SPUtils.saveString(Constants.ORDER_ID, "");
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
        if (entity != null) {
            PayReq request = new PayReq();
            request.appId = entity.getAppid();
            request.partnerId = entity.getPartnerid();
            request.prepayId = entity.getPrepayid();
            request.packageValue = "Sign=WXPay";
//                     request.packageValue = unifiedOrder.data.package;
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
//            }
//        });
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
