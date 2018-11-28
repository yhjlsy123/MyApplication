package com.zhuochi.hydream.bathhousekeeper.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*常见问题*/
public class CommonProblemActivity extends BaseActivity {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.activity_common_problem)
    LinearLayout activityCommonProblem;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
//        highApiEffects();
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        params.addCallBack(this);
        initData();
        webViewInit();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingAnimDialog.showLoadingAnimDialog(CommonProblemActivity.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LoadingAnimDialog.dismissLoadingAnimDialog();
                super.onPageFinished(view, url);
            }
        });
    }

    private void webViewInit() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);//设定支持viewport
        setting.setLoadWithOverviewMode(true);   //自适应屏幕
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        setting.setSupportZoom(true);//设定支持缩放
    }

    private void initData() {
        toolbarTitle.setText("常见问题");
        Map<String, Object> pram = Common.intancePram();
        pram.put("user_id", SPUtils.getInt(Constants.USER_ID, 0));
        params.comnRequest("SettingApi/commonProblem", pram);

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        switch (tag) {
            case "SettingApi/commonProblem":
                if (result.getData().getCode() == 200) {
                    if (null != result.getData().getData() && !(TextUtils.equals("[]", result.getData().getData().toString()))) {
                        webView.loadUrl(JSON.parseObject(JSON.toJSONString(result.getData().getData())).get("url").toString());
                    }
                }

                break;
        }
    }

    @OnClick({R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
        }
    }
}
