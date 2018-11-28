package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.BalanceLogDetailData;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by and on 2016/11/19.
 */

public class BalanceLogDetail extends BaseAutoActivity {
    @BindView(R.id.logdetail_icon)
    ImageView logdetailIcon;
    @BindView(R.id.logdetail_title)
    TextView logdetailTitle;
    @BindView(R.id.logdetail_money)
    TextView logdetailMoney;
    @BindView(R.id.logdetail_status)
    TextView logdetailStatus;
    @BindView(R.id.logdetail_pay_type)
    TextView logdetailPayType;
    @BindView(R.id.logdetail_pay_type_)
    TextView logdetailPayType_;
    @BindView(R.id.logdetail_spend_type)
    TextView logdetailSpendType;
    @BindView(R.id.logdetail_spend_type_)
    TextView logdetailSpendType_;
    @BindView(R.id.logdetail_111)
    TextView logdetail111;
    @BindView(R.id.logdetail_221)
    TextView logdetail221;
    @BindView(R.id.logdetail_222)
    TextView logdetail222;
    @BindView(R.id.logdetail_331)
    TextView logdetail331;
    @BindView(R.id.logdetail_332)
    TextView logdetail332;
    @BindView(R.id.gone)
    RelativeLayout rl_gone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancelog_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        com.isgala.xishuashua.bean_.BalanceLogDetail balanceLogDetail = JsonUtils.parseJsonToBean(json, com.isgala.xishuashua.bean_.BalanceLogDetail.class);
        if (balanceLogDetail != null) {
            BalanceLogDetailData bean = balanceLogDetail.data;
            try{
                ImageLoadUtils.loadImage(this, logdetailIcon, getResource(bean.icon));
            }catch (Exception e){
                e.printStackTrace();
            }
            logdetailTitle.setText(bean.text);
            logdetailMoney.setText(bean.price);
            logdetailStatus.setText(bean.status);
            logdetailPayType.setText(bean.content.get(0).title);
            logdetailPayType_.setText(bean.content.get(0).info);
            logdetail331.setText(bean.content.get(bean.content.size() - 1).title);
            logdetail332.setText(bean.content.get(bean.content.size() - 1).info);
            //应该用这个判断if(TextUtils.equals("1",balanceLogDetail.data.operate))
            if (bean.info != null && bean.info.content != null && bean.content.size() > 0) {
                logdetailSpendType.setText(bean.info.content.get(0).title);
                logdetailSpendType_.setText(bean.info.content.get(0).content);
            } else {
                if (bean.content.size() > 2) {
                    rl_gone.setVisibility(View.GONE);
                    logdetailSpendType_.setText(bean.content.get(1).info);
                    logdetailSpendType.setText(bean.content.get(1).title);
                }
            }
        }
    }


    @OnClick(R.id.balancelog_back)
    public void onClick() {
        finish();
    }

    /**
     * 获取本地存储的icon
     *
     * @param res
     * @return
     */
    private int getResource(String res) {
        int temp = R.mipmap.yue_zao;
        switch (res) {
            case "zao":
                temp = R.mipmap.yue_zao;
                break;
            case "tui":
                temp = R.mipmap.yue_tui;
                break;
            case "weixin":
                temp = R.mipmap.yue_weixin;
                break;
            case "alipay":
                temp = R.mipmap.yue_zhifubao;
                break;
        }
        return temp;
    }
}
