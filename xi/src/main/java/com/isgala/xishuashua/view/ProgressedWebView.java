package com.isgala.xishuashua.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.isgala.xishuashua.R;


/**
 * 备注: 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得 webview的实例
 *
 * @author Administrator
 */
public class ProgressedWebView extends RelativeLayout {

    private Context context;
    private WebView mWebView = null;
    // 水平进度条
    private ProgressBar progressBar = null;
    // 包含圆形进度条的布局
    private RelativeLayout progressBar_circle = null;
    // 进度条样式,Circle表示为圆形，Horizontal表示为水平
    private int mProgressStyle;
    // 默认水平进度条高度
    public static int DEFAULT_BAR_HEIGHT = 8;
    // 水平进度条的高
    private int mBarHeight = DEFAULT_BAR_HEIGHT;

    public enum ProgressStyle {
        Horizontal, Circle;
    }



    public ProgressedWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ProgressedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressedWebView);
        mProgressStyle = attributes.getInt(R.styleable.ProgressedWebView_progressStyle, ProgressStyle.Circle.ordinal());
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.ProgressedWebView_barHeight, DEFAULT_BAR_HEIGHT);
        attributes.recycle();
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        mWebView = new WebView(context);
        mWebView.setOverScrollMode(OVER_SCROLL_NEVER);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        // 设置支持自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        this.addView(mWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
            progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            ProgressedWebView.this.addView(progressBar, LayoutParams.MATCH_PARENT, mBarHeight);
        } else {
            progressBar_circle = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.progress_circle, null);
            ProgressedWebView.this.addView(progressBar_circle, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.clearHistory();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setVisibility(View.VISIBLE);
                if (loadingListener != null) {
                    loadingListener.finishLoading();
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (progressBar_circle != null) {
                        progressBar_circle.setVisibility(View.GONE);
                    }
                } else {
                    if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar_circle.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
    }

    private WebViewLoadingListener loadingListener;

    /**
     * 设置WebView 加载的监听
     *
     * @param loadingListener
     */
    public void setLoadingListener(WebViewLoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    public WebView getWebView() {
        return mWebView;
    }

    public interface WebViewLoadingListener {
        void startLoading();

        void finishLoading();
    }
}
