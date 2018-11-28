package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.Wallet;
import com.isgala.xishuashua.dialog.RefundBalanceDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额退款界面
 * Created by and on 2017/5/11.
 */

public class BalanceRefundActivity extends BaseAutoActivity {
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.wallet_benjinzhanghu)
    TextView walletBenjinzhanghu;
    @BindView(R.id.wallet_huodongyue)
    TextView walletHuodongyue;
    @BindView(R.id.ablerefund_money)
    TextView ablerefundMoney;
    @BindView(R.id.refund_commit)
    Button walletRefundCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refund);
        ButterKnife.bind(this);
        initView();
        loadView();
    }

    private void initView() {
        walletRefundCommit.setClickable(false);
        walletRefundCommit.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
    }

    private Wallet.WalletData data;

    private void loadView() {
        Intent intent = getIntent();
        data = (Wallet.WalletData) intent.getSerializableExtra("data");
        if (data != null) {
            walletMoney.setText(data.balance);
            walletBenjinzhanghu.setText(data.account);
            walletHuodongyue.setText(data.sub_account);
            ablerefundMoney.setText(data.account);

            if (TextUtils.equals("1", data.refund)) {//1 可以退款
                walletRefundCommit.setText("退 款");
                walletRefundCommit.setClickable(true);
                walletRefundCommit.setBackgroundResource(R.drawable.selector_bg_all_corner_blue);
            } else if (TextUtils.equals("2", data.refund)) { //2：退款办理中
                walletRefundCommit.setText("退款办理中");
                walletRefundCommit.setClickable(false);
                walletRefundCommit.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
            } else if (TextUtils.equals("0", data.refund)) {//0 本金不足或有未支付订单
                if (Tools.toFloat(data.account) > 0) {//有未支付订单
                    walletRefundCommit.setText("退 款");
                    walletRefundCommit.setClickable(true);
                    walletRefundCommit.setBackgroundResource(R.drawable.selector_bg_all_corner_blue);
                } else {//本金不足
                    walletRefundCommit.setText("退 款");
                    walletRefundCommit.setClickable(false);
                    walletRefundCommit.setBackgroundResource(R.drawable.shape_bg_corner_gray999);
                }
            }
        }
    }


    @OnClick({R.id.refund_back, R.id.recharge_refund_desc, R.id.refund_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refund_back:
                finish();
                break;
            case R.id.recharge_refund_desc:
                Intent intent = new Intent(BalanceRefundActivity.this, H5Activity.class);
                intent.putExtra("title", "充值及退款协议");
                intent.putExtra("url", Neturl.RECHARGE_RULE);
                startActivity(intent);
                break;
            case R.id.refund_commit:
                if (data != null) {
                    if (TextUtils.equals(data.refund, "1")) {
                        showRefundDialog(data.account_notice);
                    } else {
                        ToastUtils.show(data.account_notice);
                    }
                }
                break;
        }
    }

    private void showRefundDialog(String string) {
        RefundBalanceDialog.Builder builder = new RefundBalanceDialog.Builder(this);
        builder.setOnClickListener(new RefundBalanceDialog.RefundListener() {
            @Override
            public void refund() {
                refund_();
            }
        });
        builder.create(string).show();
    }

    /**
     * 申请退余额操作
     */
    private void refund_() {
        VolleySingleton.post(Neturl.BALANCE_REFUND_COMMIT, "refund_commit", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result resultBean = JsonUtils.parseJsonToBean(result, Result.class);
                if (resultBean != null) {
                    if (TextUtils.equals("1", resultBean.status)) {
                        ToastUtils.show(resultBean.data.msg);
                        finish();
                    } else {
                        ToastUtils.show(resultBean.msg);
                    }
                }
            }
        });
    }
}
