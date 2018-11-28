package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.view.ProgressedWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 关于我们
 * @author Cuixc
 * @date on  2018/7/27
 */

public class AboutusActivity extends BaseAutoActivity {
    @BindView(R.id.h5_title)
    TextView h5Title;
    @BindView(R.id.h5_content)
    ProgressedWebView h5Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title))
            h5Title.setText(title);
        WebView webView = h5Content.getWebView();
        webView.getSettings().setJavaScriptEnabled(true);
        String url = intent.getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @OnClick(R.id.h5_back)
    public void onClick() {
        finish();
    }
}
