package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseActivity;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.dialog.RefundBalanceDialog;
import com.zhuochi.hydream.entity.RefuntInitViewEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.StatusBarUtil;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额退款界面
 * Created by and on 2017/5/11.
 */

public class BalanceRefundActivity extends BaseActivity {
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.wallet_benjinzhanghu)
    TextView walletBenjinzhanghu;
    @BindView(R.id.wallet_huodongyue)
    TextView walletHuodongyue;
    @BindView(R.id.ablerefund_money)
    TextView ablerefundMoney;
    @BindView(R.id.refund_back)
    ImageView refundBack;
    @BindView(R.id.activity_wallet_jine)
    TextView activityWalletJine;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.wallet_bjzh)
    TextView walletBjzh;
    @BindView(R.id.tv_returnBalanceType)
    TextView tvReturnBalanceType;
    @BindView(R.id.tv_GiveMoney)
    TextView tvGiveMoney;
    @BindView(R.id.recharge_refund_desc)
    TextView rechargeRefundDesc;
    @BindView(R.id.tv_returnMoneyTip)
    TextView tvReturnMoneyTip;
    @BindView(R.id.user_pwd)
    EditText mUserPwd;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.refund_commit)
    Button refundCommit;
    @BindView(R.id.tv_appName)
    TextView tvAppName;
    @BindView(R.id.user_acount)
    EditText userAcount;
    @BindView(R.id.pay_type)
    RadioGroup acountType;
    @BindView(R.id.aipay)
    RadioButton aipay;


    private XiRequestParams params;
    private String mWalletType;
    private String mHandleTip;
    private int refunId = 0;
    private String rcountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refund);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.base_color), 0);
        params = new XiRequestParams(this);
        initView();

    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "applicationForRefund"://请求退款了
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);
                RefuntInitViewEntity entity = new Gson().fromJson(gson, RefuntInitViewEntity.class);
                double tor = Double.valueOf(entity.getAvailable_balance());
                double torw = Double.valueOf(entity.getGiven_balance());
                double totle = tor + torw;
                walletMoney.setText(Tools.formatMoney(totle + "") + "");
                walletBenjinzhanghu.setText(entity.getAvailable_balance());//本金
                activityWalletJine.setText("积分：" + entity.getScore());
                walletHuodongyue.setText(entity.getGiven_balance());
                ablerefundMoney.setText(entity.getRefund_balance());
                if (entity.getHandle_status() == 1) {//1:未处理 2：退款成功 3：拒绝退款 4：退款失败 5:用户取消
                    String Tip = "您的申请已经提交，我们会及时处理您的申请，谢谢";
                    refunId = entity.getRefund_id();
                    tvReturnMoneyTip.setText(Tip);
                    mHandleTip = "确认取消申请退款吗？";
                    tvReturnBalanceType.setText("退款进度：" + entity.getHandle_status_text());
                    refundCommit.setText("取消申请");
                } else if (entity.getHandle_status() == 2) {
                    tvReturnMoneyTip.setText(entity.getTip());
                    refundCommit.setText("确定");
                } else if (entity.getHandle_status() == 3) {
                    tvReturnBalanceType.setText("退款进度：" + entity.getHandle_status_text());
                    if (TextUtils.isEmpty(entity.getHandle_remark())) {
                        tvReturnMoneyTip.setVisibility(View.GONE);
                    } else {
                        tvReturnMoneyTip.setText("备注：" + entity.getHandle_remark());
                    }
                    mHandleTip = "确认再次申请退款吗？";
                    refundCommit.setText("再次申请");
                } else if (entity.getHandle_status() == 4) {
                    tvReturnBalanceType.setText("退款进度：" + entity.getHandle_status_text());
                    if (TextUtils.isEmpty(entity.getHandle_remark())) {
                        tvReturnMoneyTip.setVisibility(View.GONE);
                    } else {
                        tvReturnMoneyTip.setText("备注：" + entity.getHandle_remark());
                    }

                    mHandleTip = "确认再次申请退款吗？";
                    refundCommit.setText("再次申请");
                } else {
                    tvReturnMoneyTip.setText(entity.getTip());
                    mHandleTip = entity.getHandle_tip();
                    tvReturnBalanceType.setText("退款说明");
                }
                break;
            case "doApplicationForRefund"://退款
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                ToastUtils.show(result.getData().getMsg());
                finish();
                break;
            case "userCancelRefund"://取消退款
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                ToastUtils.show(result.getData().getMsg());
                finish();
                break;
        }
        super.onRequestSuccess(tag, result);
    }


    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    private void initView() {
        mWalletType = getIntent().getStringExtra("walletType");
        tvAppName.setText(getResources().getText(R.string.app_name));
        aipay.setChecked(true);
        rcountType = "alipay";
        acountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.aipay:
                        rcountType = "alipay";
                        userAcount.setHint("请输入收款账号");
                        break;
                    case R.id.wei_chat:
                        rcountType = "weixinpay";
                        userAcount.setHint("仅支持曾用微信充值过的用户");
                        break;
                }
            }
        });
        forRefund();
    }

    @OnClick({R.id.refund_back, R.id.recharge_refund_desc, R.id.refund_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refund_back:
                finish();
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
            case R.id.refund_commit:
                if (mUserPwd.getText().toString().isEmpty()) {
                    ToastUtils.show("请输入您的账号密码！");
                    return;
                }
                showRefundDialog();
                break;


        }
    }

    /*
     * 确认弹窗
     * */
    private void showRefundDialog() {
        RefundBalanceDialog.Builder builder = new RefundBalanceDialog.Builder(this);
        builder.setOnClickListener(new RefundBalanceDialog.RefundListener() {
            @Override
            public void refund() {
                if (refundCommit.getText().toString().equals("取消申请")) {
                    //取消退款
                    userCancelRefund();
                } else {//请求退款
                    getRefund();
                }
            }
        });
        builder.create(mHandleTip).show();
    }

    /**
     * 申请退余额操作
     */
    private void forRefund() {
        params.addCallBack(this);
        params.applicationForRefund(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone(), mWalletType);
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
    }

    /*退款操作*/
    private void getRefund() {
        double Walle_balance = Double.valueOf(ablerefundMoney.getText().toString());
        String userPwd = mUserPwd.getText().toString();
        String mserAcount = userAcount.getText().toString();
/*        if (Walle_balance < 0.01) {
            ToastUtils.show("退款金额不能低于1角！");
            return;
        }
        if (TextUtils.isEmpty(mserAcount)) {
            ToastUtils.show("收款账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(rcountType)) {
            ToastUtils.show("收款账号类型不能为空！");
            return;
        }*/
        params.addCallBack(this);
        params.getRefund(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone(), userPwd, mWalletType, mserAcount, rcountType, Walle_balance);
    }

    /*用户取消未处理的退款申请*/
    private void userCancelRefund() {
        String userPwd = mUserPwd.getText().toString();
        params.addCallBack(this);
        params.userCancelRefund(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone(), userPwd, refunId);
    }
}
