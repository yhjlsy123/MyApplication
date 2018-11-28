package com.zhuochi.hydream.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.H5Dialog;
import com.zhuochi.hydream.view.ProgressedWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and on 2016/11/23.
 */

public class AdH5Activity extends BaseAutoActivity {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.h5_title)
    TextView h5Title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h5_ad_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title))
            h5Title.setText(title);
        webView.getSettings().setJavaScriptEnabled(true);
        url = intent.getStringExtra("url");

        initView();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        } else {
            finish();
        }
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String titleValue) {
                super.onReceivedTitle(view, titleValue);
                h5Title.setText(titleValue);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                finish();

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel:")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                if (url.startsWith("sms:")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;

                }
                webView.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        //①设置WebView允许调用js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);//设定支持缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.addJavascriptInterface(this, "android");
    }

    @OnClick(R.id.h5_back)
    public void onClick() {
        finish();
    }
}
