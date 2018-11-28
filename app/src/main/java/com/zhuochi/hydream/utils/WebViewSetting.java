package com.zhuochi.hydream.utils;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @author CuiXC
 * @date on 2017/3/27.
 */
public class WebViewSetting {

    public static void initWebSetting(Context context, WebView mWebView, final ProgressBar progressBar) {
        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.addJavascriptInterface(context, "OCModel");
        //支持localStorage 设置如下
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        mWebView.getSettings().setDefaultTextEncodingName("uft-8");
        String appCachePath = context.getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

//        mWebView.getSettings().setBlockNetworkImage(false);
//        mWebView.getSettings().setSupportMultipleWindows(false);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持webview放大缩小
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    public static void initWebSetting(Context context, WebView mWebView) {

//        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        mWebView.addJavascriptInterface(context, "OCModel");
        //支持localStorage 设置如下
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        mWebView.getSettings().setDefaultTextEncodingName("uft-8");
        String appCachePath = context.getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
//        mWebView.getSettings().setBlockNetworkImage(false);
//        mWebView.getSettings().setSupportMultipleWindows(false);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持webview放大缩小
        // 设置可以支持缩放
//        mWebView.getSettings().setSupportZoom(true);
//        // 设置出现缩放工具
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        //扩大比例的缩放
//        mWebView.getSettings().setUseWideViewPort(true);
//        //自适应屏幕
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.setWebChromeClient(new MyWebChromeClient());
//        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private static class MyWebViewClient extends WebViewClient {

    }

    private static class MyWebChromeClient extends WebChromeClient {

    }
}
