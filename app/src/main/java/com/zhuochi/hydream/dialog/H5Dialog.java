package com.zhuochi.hydream.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.AdH5Activity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 网页关闭调用方式
 * <script>
 * function closeAndroid(){
 * alert("关闭测试");
 * window.close();
 * }
 * <p>
 * function androidCloseDialog(){
 * alert("关闭测试");
 * window.android.close();
 * <p>
 * }
 */

public class H5Dialog extends Dialog {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;
    private Context context;
    private String url;
    private Boolean isBlank;

    public void setBlank(boolean blank) {
        isBlank = blank;
    }

    public H5Dialog(@NonNull Context context, String url) {
        super(context, R.style.CommonDialog);
        this.context = context;
        this.url = url;
    }

    public H5Dialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public H5Dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h5_dialog);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String titleValue) {
                super.onReceivedTitle(view, titleValue);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                H5Dialog.this.dismiss();

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

                if (url.startsWith("tel:") && context instanceof Activity) {
                    ((Activity) (context)).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                if (url.startsWith("sms:") && context instanceof Activity) {
                    ((Activity) (context)).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;

                }
                webView.setVisibility(View.GONE);
                if (null != isBlank && isBlank && context instanceof Activity) {
                    Intent intent = new Intent(context, AdH5Activity.class);
                    intent.putExtra("url", url);
                    ((Activity) (context)).startActivity(intent);
                    return true;
                }
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

    @JavascriptInterface
    public void close() {
        this.dismiss();
    }
}
