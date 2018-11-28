package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatQueryAcitivity extends BaseActivity implements HeightSelectView.SelcetCallBack {

    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.webview)
    WebView webview;
    private HeightSelectView mHeightSelectView;
    private XiRequestParams params;
    private WebView webView;
    private int org_id;
    private int org_area_id;
    private int device_area_id;
    private String start_date;
    private String end_date;
    private String device_key;
    private int reqCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_fragment_one);
        ButterKnife.bind(this);
        mHeightSelectView = new HeightSelectView(ChatQueryAcitivity.this, this, 3, getResources().getString(R.string.select_school_bathroom_wei));
        headViewContaner.addView(mHeightSelectView);
        params = new XiRequestParams(ChatQueryAcitivity.this);
        initData();
        loadDataFromNet();
    }


    public void loadDataFromNet() {
        params.addCallBack(this);
        if (reqCode == 0) {
            params.getTopChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date);
        } else if (reqCode == 1) {
            params.getUserAverageConsumptionTopChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date);
        } else if (reqCode == 2) {
            params.getFrequencyChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date);
        } else {
            params.getRatioChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date);

        }

    }

    private void initData() {
        webView.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String boothroomPosition, String StartTime, String EndTime) {
        this.org_id = org_area_id;
        this.org_area_id = org_area_id;
        this.device_area_id = boothroom_id;
        device_key = boothroomPosition;
        start_date = StartTime;
        end_date = EndTime;
        loadDataFromNet();
    }
}
