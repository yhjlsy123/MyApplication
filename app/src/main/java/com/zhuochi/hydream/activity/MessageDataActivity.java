package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.entity.MessageDataEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.ProgressedWebView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息详情
 *
 * @author Cuixc
 * @date on  2018/6/7
 */

public class MessageDataActivity extends BaseAutoActivity {
    @BindView(R.id.message_back)
    ImageView messageBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.h5_content)
    ProgressedWebView h5Content;
//    @BindView(R.id.tv_content)
//    TextView tvContent;
//    @BindView(R.id.tv_author)
//    TextView tvAuthor;
//    @BindView(R.id.tv_time)
//    TextView tvTime;
//    private XiRequestParams params;
    private MessageDataEntity entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_data);
        ButterKnife.bind(this);
//        params = new XiRequestParams(this);
        InitView();

    }

    private void InitView() {
        String mtitle = getIntent().getStringExtra("title");
        String murl = getIntent().getStringExtra("url");
        title.setText(mtitle);
        WebView webView = h5Content.getWebView();
        webView.getSettings().setJavaScriptEnabled(true);
        if (!TextUtils.isEmpty(murl)) {
            webView.loadUrl(murl);
        }
        messageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        params.addCallBack(this);
//        params.messageDetail(UserUtils.getInstance(this).getMobilePhone(), id);
    }

//    @Override
//    public void onRequestSuccess(String tag, SonBaseEntity result) {
//        Map map = (Map) result.getData().getData();
//        try {
//            String gson = GsonUtils.parseMapToJson(map);
//            entity = new Gson().fromJson(gson, MessageDataEntity.class);
//            title.setText(entity.getTitle());
//            tvContent.setText(Html.fromHtml(entity.getContent()));
//            tvAuthor.setText(entity.getSender_name());
//            tvTime.setText(Common.getDateToString(entity.getCreate_time()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        super.onRequestSuccess(tag, result);
//    }



}
