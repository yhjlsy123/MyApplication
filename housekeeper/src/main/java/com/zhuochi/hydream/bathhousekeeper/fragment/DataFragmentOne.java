package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 数据
 */
public class DataFragmentOne extends BaseFragment implements HeightSelectView.SelcetCallBack {

    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.webview)
    WebView webView;
    private HeightSelectView mHeightSelectView;
    Unbinder unbinder;
    private String url_webView;
    private XiRequestParams params;
    int reqCode = 0;

    private int org_id;
    private int org_area_id;
    private int device_area_id;
    private String device_key;
    private String start_date;
    private String end_date;
    private boolean isQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reqCode = getArguments().getInt("reqCode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        mHeightSelectView = new HeightSelectView(getActivity(), this, 3, getResources().getString(R.string.select_school_bathroom_wei));
        headViewContaner.addView(mHeightSelectView);
        params = new XiRequestParams(getActivity());
        isQuery = false;
        initData();
        if (getUserVisibleHint()) {
            loadDataFromNet();
        }
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            if (TextUtils.isEmpty(webView.getUrl())) {
                loadDataFromNet();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        initData();
    }

    private void initData() {
        webView.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    public void loadDataFromNet() {
        params.addCallBack(this);
        if (!isQuery) {
            org_id = 0;
            org_area_id = 0;
            device_key = null;
            start_date = null;
            end_date = null;
        }
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

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        Map map = (Map) result.getData().getData();
        url_webView = map.get("url").toString();
        webView.loadUrl(url_webView);//加载url
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String boothroomPosition, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        this.device_area_id = boothroom_id;
        device_key = boothroomPosition;
        start_date = StartTime;
        end_date = EndTime;
        isQuery = true;
        loadDataFromNet();

    }
}
