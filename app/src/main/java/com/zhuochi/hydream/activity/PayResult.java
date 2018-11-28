package com.zhuochi.hydream.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.PayResultRankAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.RankItem;
import com.zhuochi.hydream.dialog.OrderDialog;
import com.zhuochi.hydream.dialog.TipDialogComm;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.exchang.TurnOff;
import com.zhuochi.hydream.entity.exchang.TurnOffPayType;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.Tools;
import com.zhuochi.hydream.view.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

/**
 * 支付结果页
 * Created by and on 2016/11/11.
 */

public class PayResult extends BaseAutoActivity {
    @BindView(R.id.pay_result_finish)
    Button payResultFinish;
    @BindView(R.id.payresult_rl)
    RelativeLayout payresultRl;
    @BindView(R.id.tv_orderCost)
    TextView tvOrderCost;
    @BindView(R.id.tv_blowerTime)
    TextView tvBlowerTime;
    @BindView(R.id.tv_Consumption)
    TextView tvConsumption;
    @BindView(R.id.tv_payTime)
    TextView tvPayTime;
    @BindView(R.id.tv_realMoney)
    TextView tvRealMoney;
    @BindView(R.id.pay_result_ques)
    TextView payResultQues;
    @BindView(R.id.payresult_morerank)
    TextView payresultMorerank;
    @BindView(R.id.payresult_rank_listview)
    CustomListView payresultRankListview;
    @BindView(R.id.pay_result_root)
    RelativeLayout payResultRoot;
    @BindView(R.id.line_payType)
    RelativeLayout linePayType;
    private TurnOffPayType mTurnOff;
    private PayResultRankAdapter rankAdapter;
    private final String TAG = "PayResult";
    private XiRequestParams params;
    private String bean = "";
    private String payType = "";
    private String OrderSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        iniData();
    }

    private void iniData() {
        bean = getIntent().getStringExtra("entity");
        payType = getIntent().getStringExtra("payType");
        if (payType != null || bean != null) {
            if (!payType.equals("faucet")) {//首页支付完成 跳转
                payresultMorerank.setVisibility(View.GONE);
                linePayType.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(bean)) {
                mTurnOff = new Gson().fromJson(bean, TurnOffPayType.class);
                updateView(mTurnOff);
            }
        } else {//手动支付完成跳转
            InitView();
        }
    }

    /**
     * 手动支付跳转
     */
    private void InitView() {
        Intent intent = getIntent();
        String payTime = intent.getStringExtra("PayTime");
        String blowerTime = intent.getStringExtra("BlowerTime");
        String orderAmount = intent.getStringExtra("OrderAmount");
        String orderUsage=intent.getStringExtra("orderUsage");
        String cashAmount=intent.getStringExtra("CashAmount");
        OrderSn = intent.getStringExtra("OrderSn");
        tvOrderCost.setText(orderAmount + "元");
        tvRealMoney.setText(cashAmount+"元");
        tvBlowerTime.setText(blowerTime);
        tvConsumption.setText(orderUsage);
        if (payTime != null) {
            tvPayTime.setText(Common.Hourmin(String.valueOf(payTime)));//支付时间
        }
        //获取排行榜
        getOrderRankingList();
    }

    /**
     * 自动支付
     *
     * @param off
     */
    private void updateView(TurnOffPayType off) {
        String payType = "";
        tvRealMoney.setText(off.getCashAmount()+"元");
        if (off.getOrderUsage().equals("0.0")||off.getOrderUsage().equals("0.00")){
            tvConsumption.setText("----");
        }else {
            if (!TextUtils.isEmpty(off.getOrderUsage())) {
                tvConsumption.setText(off.getOrderUsage() + "升");
            }else {
                tvConsumption.setText("----");
            }
        }
//        tvConsumption.setText(off.getOrderUsage()+"升");
        tvOrderCost.setText(off.getOrderAmount() + "元");//订单费用
        if (off.getIsSoftPledge() == 1) {//余额支付
            payType = "余额支付";
        }

//        Long s = (off.getEndTime() - off.getStartTime()) * 1000 / (1000 * 60);
        tvBlowerTime.setText(Tools.FormatChange(off.getUsedTime()));//洗浴时长
//        tvPayType.setText(payType);//支付方式

        tvPayTime.setText(Common.Hourmin(String.valueOf(off.getEndTime())));//支付时间
        //获取排行榜
        getOrderRankingList();
    }

    /*获取支付成功后的排行榜*/
    private void getOrderRankingList() {
        params.addCallBack(this);
        params.getOrderRankingList();
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOrderRankingList"://排行榜
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                List<RankItem> list = JSON.parseArray(JSON.toJSONString(jsonArray), RankItem.class);
                upDateList(list);
                break;
            case "doubtAboutOrder":
                ToastUtils.show(result.getData().getMsg());
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    private void upDateList(List<RankItem> list) {
        if (rankAdapter == null) {
            rankAdapter = new PayResultRankAdapter(list, R.layout.item_payresult_rank, this);
            payresultRankListview.setAdapter(rankAdapter);
        } else {
            rankAdapter.notifyDataSetChanged2(list);
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

    @OnClick({R.id.pay_result_finish, R.id.pay_result_ques, R.id.payresult_morerank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_result_finish:
                finish();
                break;
            case R.id.pay_result_ques:

//                getAboutOrde();
                showDialog();
//                callPhone();
                break;
            case R.id.payresult_morerank:
                startActivity(new Intent(PayResult.this, RankingActivity.class));
                break;
        }
    }

//    private void getAboutOrde() {
//        Dialog dialog = TipDialogComm.show_(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTurnOff != null) {
//                    params.addCallBack(PayResult.this);
//                    params.getDoubtAboutOrder(mTurnOff.getOrderSn());
//                    return;
//                }
//                if (OrderSn != null) {
//                    params.addCallBack(PayResult.this);
//                    params.getDoubtAboutOrder(OrderSn);
//                    return;
//                }
//            }
//        }, "提示", "对订单有疑问发送给后台？");
//    }
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
    /*对订单费用有疑问*/
    private void getAboutOrde(String editText) {
        if (mTurnOff != null) {
            params.addCallBack(PayResult.this);
            params.getDoubtAboutOrder(mTurnOff.getOrderSn(),editText);
            return;
        }
        if (OrderSn != null) {
            params.addCallBack(PayResult.this);
            params.getDoubtAboutOrder(OrderSn,editText);
            return;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(HomeActivity.PAYRESULT);
    }

    @Override
    protected void onDestroy() {
        setResult(HomeActivity.PAYRESULT);
        super.onDestroy();
    }
}
