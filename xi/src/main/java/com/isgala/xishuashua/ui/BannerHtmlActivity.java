package com.isgala.xishuashua.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.bean_.UpgradeEntity;
import com.isgala.xishuashua.utils.VersionDialogFragment;

/**
 * Created by Administrator on 2018/3/15.
 */

public class BannerHtmlActivity extends Activity{
    private WebView mWebView;
    private ImageView h5_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_html);
        InitView();
    }
    private void InitView(){
        Intent intent=getIntent();
        String url=intent.getStringExtra("BannerUrl");
        mWebView=(WebView) findViewById(R.id.webView);
        h5_back=(ImageView) findViewById(R.id.h5_back);
        mWebView.loadUrl(url);
        h5_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
