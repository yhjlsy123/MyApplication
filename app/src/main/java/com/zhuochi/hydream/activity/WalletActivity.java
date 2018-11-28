package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.WalletEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包界面
 * Created by and on 2016/11/16.
 */

public class WalletActivity extends BaseAutoActivity {
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.wallet_benjinzhanghu)
    TextView walletBenJin;
    @BindView(R.id.wallet_huodongyue)
    TextView walletHuoDong;
    private final String TAG = "WalletActivity";
    @BindView(R.id.txt_give)
    TextView txt_give;
    public static final String YAJIN = "yanjin_";
    @BindView(R.id.line_Deposit)
    RelativeLayout lineDeposit;
    @BindView(R.id.wallet_back)
    ImageView walletBack;
    @BindView(R.id.activity_wallet_jine)
    TextView activityWalletJine;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.wallet_bjzh)
    TextView walletBjzh;
    @BindView(R.id.line_Principal)
    LinearLayout linePrincipal;
    @BindView(R.id.wallet_chongzhi)
    RelativeLayout walletChongzhi;
    @BindView(R.id.line_Give)
    RelativeLayout lineGive;
    @BindView(R.id.wallet_yuemingxi)
    RelativeLayout walletYuemingxi;
    @BindView(R.id.line_exchangeContent)
    RelativeLayout lineExchangeContent;
//    private int backMoney = 0;//默认不可退款
    private XiRequestParams params;
//    private boolean isPrincipal = false;//是否可以本金退款
//    private boolean isDeposit = false;//是否可以本金退款
    private String refundType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        params = new XiRequestParams(this);
        getHandleType();
        getWalletInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHandleType();
        getWalletInfo();
    }

    /*获取资金处理方式*/
    private void getHandleType() {
        params.addCallBack(this);
        params.getHandleType(UserUtils.getInstance(this).getMobilePhone());
    }

    private void getWalletInfo() {
        params.addCallBack(this);
        params.getWalletInfo();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getWalletInfo":
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                WalletEntity entity = new Gson().fromJson(gson, WalletEntity.class);
                activityWalletJine.setText("积分：" + entity.getScore());
                walletHuoDong.setText(entity.getGiven_balance());//活动余额
                walletBenJin.setText(Tools.formatMoney(entity.getAvailable_balance() + "") + "");
                double tor = Double.valueOf(entity.getAvailable_balance());
                double torw = Double.valueOf(entity.getGiven_balance());
                double totle = tor + torw;
                walletMoney.setText(Tools.formatMoney(totle + "") + "");
                break;
            case "getHandleType"://获取退款类型
            if (result.getData().getData()!=null) {
                refundType = result.getData().getData().toString();
                if (refundType.equals("do_nothing")){//不处理   不可退款  不可转赠

                }
            }
                break;
        }
        super.onRequestSuccess(tag, result);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick({R.id.wallet_chongzhi, R.id.wallet_yuemingxi, R.id.wallet_back, R.id.wallet_money, R.id.txt_give,
            R.id.line_exchangeContent, R.id.line_Give, R.id.line_Deposit, R.id.line_Principal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_exchangeContent://兑换
                startActivity(new Intent(this, ExchangeContentActivity.class));
                break;
            case R.id.line_Give://转赠
                if (refundType.equals("give_to_another")){//可转赠
                    startActivity(new Intent(this, MoneyGiveActivity.class));
                }else {
                    ToastUtils.show("暂不支持转赠");
                }

                break;
            case R.id.wallet_chongzhi://充值
                Intent payintent = new Intent(this, RechargeActivity.class);
                payintent.putExtra("PayType", "Recharge");//充值
                startActivity(payintent);
                break;
            case R.id.wallet_yuemingxi://余额明细
                jumpLog();
                break;
            case R.id.line_Deposit://退款
            case R.id.wallet_money://本金退款
            case R.id.line_Principal://本金退款
                if (refundType.equals("cash_refund")){//可退款
                    refundBalance();
                }else {
                    ToastUtils.show("暂不支持退款");
                }

                break;
            case R.id.txt_give://转赠
                Intent intent = new Intent(this, MoneyGiveActivity.class);
                intent.putExtra("MoneyNum", walletMoney.getText().toString());
                startActivity(intent);
                break;
            case R.id.wallet_back:
                finish();
                break;
             }

    }


    /**
     * 退款(余额)
     */
    private void refundBalance() {
        Intent intent = new Intent(WalletActivity.this, BalanceRefundActivity.class);
        startActivity(intent);

    }


    /**
     * 跳转余额明细
     */
    private void jumpLog() {
        Intent intent = new Intent(WalletActivity.this, BalanceLog.class);
        startActivity(intent);
    }

}
