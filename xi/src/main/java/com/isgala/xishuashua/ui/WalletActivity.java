package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.BalanceBean;
import com.isgala.xishuashua.bean_.RechargeLog;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.Wallet;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.RefundBalanceDialog;
import com.isgala.xishuashua.dialog.RefundDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;

import org.w3c.dom.Text;

import java.util.List;

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
    @BindView(R.id.wallet_yajin_balance)
    TextView walletYaJin;
    @BindView(R.id.wallet_benjinzhanghu)
    TextView walletBenJin;
    @BindView(R.id.wallet_huodongyue)
    TextView walletHuoDong;
    private final String TAG = "WalletActivity";
    @BindView(R.id.balance_recharge_listview)
    ListView listview;
    @BindView(R.id.ll_activity_balance_recharge)
    LinearLayout llActivityBalanceRecharge;
    @BindView(R.id.wallet_tuikuan)
    View wallet_tuikuan;
    @BindView(R.id.txt_give)
    TextView txt_give;
    public static final String YAJIN = "yanjin_";
    private int backMoney = 0;//默认不可退款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        String balance = SPUtils.getString("balance", "");//总余额
        if (!TextUtils.isEmpty(balance)) {
            walletMoney.setText(balance);
        }
        String account = SPUtils.getString("account", "");//主账户
        if (!TextUtils.isEmpty(account)) {
            walletBenJin.setText(account);
        }
        String sub_account = SPUtils.getString("sub_account", "");//副账户
        if (!TextUtils.isEmpty(sub_account)) {
            walletHuoDong.setText(sub_account);
        }
        String yajin = SPUtils.getString(YAJIN, "");
        setYaJinState(yajin);
        loadData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void loadData() {
        VolleySingleton.post(Neturl.GET_UER_WALLET, "wallet", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Wallet wallet = JsonUtils.parseJsonToBean(result, Wallet.class);
                if (wallet != null & wallet.data != null) {
                    walletMoney.setText(wallet.data.balance);
                    walletBenJin.setText(wallet.data.account);
                    walletHuoDong.setText(wallet.data.sub_account);
                    SPUtils.saveString("balance", wallet.data.balance);
                    SPUtils.saveString("sub_account", wallet.data.sub_account);
                    SPUtils.saveString("account", wallet.data.account);
                    if (wallet.data.recharge_handle_type.equals("1")) {//本金退款  可点击
                        backMoney = 1;
                    } else if (wallet.data.recharge_handle_type.equals("2")) {// 转赠  显示
                        txt_give.setVisibility(View.VISIBLE);
                    } //既不可退款也不可转赠
                    if (TextUtils.equals("1", wallet.data.show_deposit)) {
                        setYaJin(wallet);
                    } else {
                        wallet_tuikuan.setVisibility(View.GONE);
                    }
                }
            }
        }, null);
    }

    /**
     * 设置押金的显示状态
     */
    private void setYaJin(Wallet wallet) {
        if (TextUtils.equals("2", wallet.data.status)) {//正在退押金中
            walletYaJin.setText(wallet.data.yajin_notice);
            wallet_tuikuan.setClickable(false);
        } else {
            walletYaJin.setText("押金 " + wallet.data.deposit + " 元");
            wallet_tuikuan.setClickable(true);
        }
        wallet_tuikuan.setVisibility(View.VISIBLE);
        SPUtils.saveString(YAJIN, walletYaJin.getText().toString());
    }

    /**
     * 设置押金的初始状态
     *
     * @param yajin
     */
    private void setYaJinState(String yajin) {
        if (TextUtils.isEmpty(yajin) || TextUtils.equals("0.00", yajin)) {
            walletYaJin.setText("押金 " + "0.00" + " 元");
        } else {
            walletYaJin.setText(yajin);
        }
    }

    @OnClick({R.id.wallet_chongzhi, R.id.wallet_yuemingxi, R.id.wallet_back, R.id.wallet_tuikuan, R.id.wallet_money, R.id.txt_give})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_chongzhi:
                LoadingTrAnimDialog.showLoadingAnimDialog(this);
                // 重新请求钱包界面的接口
                VolleySingleton.post(Neturl.GET_UER_WALLET, "wallet", null, new VolleySingleton.CBack() {
                    @Override
                    public void runUI(String result) {
                        Wallet wallet = JsonUtils.parseJsonToBean(result, Wallet.class);
                        if (wallet != null & TextUtils.equals("1", wallet.status)) {
                            walletMoney.setText(wallet.data.balance);
                            walletBenJin.setText(wallet.data.account);
                            walletHuoDong.setText(wallet.data.sub_account);
                            SPUtils.saveString("balance", wallet.data.balance);
                            SPUtils.saveString("sub_account", wallet.data.sub_account);
                            SPUtils.saveString("account", wallet.data.account);

                            if (TextUtils.equals("1", wallet.data.show_deposit)) {//先判断是否需要交押金
                                setYaJin(wallet);
                                if (TextUtils.equals("1", wallet.data.could_recharge)) {//判断是否可以充值
                                    Intent intent = new Intent(WalletActivity.this, RechargeActivity.class);
                                    intent.putExtra("from", "WalletActivity");
                                    startActivityForResult(intent, 120);
                                } else {
                                    ToastUtils.show(wallet.data.recharge_notice);
                                }
                            } else {
                                wallet_tuikuan.setVisibility(View.GONE);
                                Intent intent = new Intent(WalletActivity.this, RechargeActivity.class);
                                intent.putExtra("from", "WalletActivity");
                                startActivityForResult(intent, 120);
                            }
                        }
                    }
                }, null);
                break;
            case R.id.wallet_yuemingxi:
                jumpLog();
                break;
            case R.id.wallet_back:
                finish();
                break;
            case R.id.wallet_money://退款
//                String account = SPUtils.getString("account", "0.00");
//                if (Tools.toFloat(account) > 0.00f)
                if (backMoney == 1) {//可退款
                    refundBalance();
                }

                break;
            case R.id.wallet_tuikuan:
                LoadingTrAnimDialog.showLoadingAnimDialog(this);
                // 重新请求钱包界面的接口
                VolleySingleton.post(Neturl.GET_UER_WALLET, "wallet", null, new VolleySingleton.CBack() {
                    @Override
                    public void runUI(String result) {
                        Wallet wallet = JsonUtils.parseJsonToBean(result, Wallet.class);
                        if (wallet != null & TextUtils.equals("1", wallet.status)) {
                            walletMoney.setText(wallet.data.balance);
                            walletBenJin.setText(wallet.data.account);
                            walletHuoDong.setText(wallet.data.sub_account);
                            SPUtils.saveString("balance", wallet.data.balance);
                            SPUtils.saveString("sub_account", wallet.data.sub_account);
                            SPUtils.saveString("account", wallet.data.account);

                            setYaJin(wallet);
                            if (TextUtils.equals("1", wallet.data.status)) {//充押金
                                startActivity(new Intent(WalletActivity.this, RechargeYaJinActivity.class));
                            } else if (TextUtils.equals("2", wallet.data.status)) {//正在退押金中
                                ToastUtils.show(wallet.data.yajin_notice);
                            } else if (TextUtils.equals("3", wallet.data.status)) {//退押金
                                showTuiKuanDialog(wallet.data.yajin_notice);
                            }
                        }
                    }
                }, null);
                break;
            case R.id.txt_give:
                Intent intent = new Intent(this, MoneyGiveActivity.class);
                intent.putExtra("MoneyNum", walletMoney.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120) {
            String yajin = SPUtils.getString(YAJIN, "");
            setYaJinState(yajin);
            loadData();
        }

    }

    /**
     * 退款(余额)
     */
    private void refundBalance() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.BALANCE_REFUND, "balance_refund", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Wallet wallet = JsonUtils.parseJsonToBean(result, Wallet.class);
                if (wallet != null && TextUtils.equals("1", wallet.status)) {
                    Intent intent = new Intent(WalletActivity.this, BalanceRefundActivity.class);
                    intent.putExtra("data", wallet.data);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 申请退款
     */
    private void showTuiKuanDialog(String content) {
        RefundDialog.Builder builder = new RefundDialog.Builder(this);
        builder.setOnClickListener(new RefundDialog.RefundListener() {
            @Override
            public void refund() {
                refund_();
            }
        });
        builder.create(content).show();
    }

    /**
     * 退押金
     */
    private void refund_() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.TUI_YAJIN_BACK, "tuiyajin", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null) {
                    if (TextUtils.equals("1", result1.status)) {
                        ToastUtils.show(result1.data.msg);
                        loadData();
                    } else {
                        ToastUtils.show(result1.msg);
                    }
                }
            }
        }, null);
    }

    /**
     * 跳转余额明细
     */
    private void jumpLog() {
        Intent intent = new Intent(WalletActivity.this, BalanceLog.class);
        startActivity(intent);
    }

}
