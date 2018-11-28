package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.PayResultRankAdapter;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.PayResultBean;
import com.isgala.xishuashua.bean_.RankItem;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.view.CustomListView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付结果页
 * Created by and on 2016/11/11.
 */

public class PayResult extends BaseAutoActivity {
    @BindView(R.id.pay_result_pay_money)
    TextView payMoney;
    @BindView(R.id.payresult_rank_listview)
    CustomListView payresultRankListview;
    @BindView(R.id.pay_result_usetime)
    TextView payResultUsetime;
    @BindView(R.id.pay_result_yue)
    TextView payResultYuE;
    @BindView(R.id.pay_result_water)
    TextView payResultWater;
    @BindView(R.id.pay_result_root)
    RelativeLayout payResultRoot;

    @BindView(R.id.line_time)
    RelativeLayout lineTime;
    @BindView(R.id.line_water)
    RelativeLayout lineWater;
    private PayResultRankAdapter rankAdapter;
    private final String TAG = "PayResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);
        ButterKnife.bind(this);
        iniData();
    }

    private void iniData() {
        String bean = getIntent().getStringExtra("bean");
        LogUtils.e("result pay ", bean);
        PayResultBean payResultBean = JsonUtils.parseJsonToBean(bean, PayResultBean.class);

        if (payResultBean != null && payResultBean.data != null) {
            if (payResultBean.data.show_bath_type.equals("1")) {//显示时间
                lineTime.setVisibility(View.VISIBLE);
            } else if (payResultBean.data.show_bath_type.equals("2")) {//显示用水量
                lineWater.setVisibility(View.VISIBLE);
            }
            updateView(payResultBean.data);
            upDateList(payResultBean.data.ranking);
            payResultRoot.setVisibility(View.VISIBLE);

        }
    }

    private void updateView(PayResultBean.PayResultData data) {

        payMoney.setText(data.payable + "元");
        payResultUsetime.setText(data.total_time);
        payResultYuE.setText(data.total_price + "元");
        payResultWater.setText(data.water);
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
        MobclickAgent.onPageStart("PayResult");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayResult");
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.pay_result_finish, R.id.pay_result_ques, R.id.payresult_morerank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_result_finish:
                finish();
                break;
            case R.id.pay_result_ques:
                callPhone();
                break;
            case R.id.payresult_morerank:
                startActivity(new Intent(PayResult.this, RankingActivity.class));
                break;
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
