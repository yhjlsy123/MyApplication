package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.entity.pushbean.InitSettingEntity;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.view.ProgressedWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 常见问题
 *
 * @author Cuixc
 * @date on  2018/6/26
 */

public class CommonProblemActivity extends BaseAutoActivity {
    @BindView(R.id.h5_title)
    TextView h5Title;
    @BindView(R.id.h5_content)
    ProgressedWebView h5Content;
    private String mGson;
    private InitSettingEntity mInitEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        ButterKnife.bind(this);
        mGson = SPUtils.getString("initSetting", "");
        if (!TextUtils.isEmpty(mGson)) {
            mInitEntity = new Gson().fromJson(mGson, InitSettingEntity.class);
        }
        h5Title.setText("常见问题");
        WebView webView = h5Content.getWebView();
//        WebViewSetting.initWebSetting(this, webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mInitEntity.getCommonProblemUrl());
    }

    @OnClick(R.id.h5_back)
    public void onClick() {
        finish();
    }

}
