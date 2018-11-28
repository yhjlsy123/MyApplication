package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.http.RequestURL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通用H5页面
 *
 * @author Cuixc
 * @date on  2018/7/25
 */

public class HtmlActivity extends BaseAutoActivity {
    @BindView(R.id.message_back)
    ImageView messageBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.bind(this);
        InitView();
    }

    private void InitView() {
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        mTitle.setText(title);
        webview.loadUrl(RequestURL.ICON_URL + url);
        messageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
