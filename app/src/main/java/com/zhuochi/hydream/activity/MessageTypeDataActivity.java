package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.MessageTypeDataAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.entity.MessageTypeDataEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通知消息类型详情
 */

public class MessageTypeDataActivity extends BaseAutoActivity implements OnItemClickListener<MessageTypeDataEntity> {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.title)
    TextView mtitle;
    @BindView(R.id.no_data)
    RelativeLayout noData;
    private MessageTypeDataAdapter messageAdapter;
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        getMessageTypeList();
    }

    private void getMessageTypeList() {
        String title = getIntent().getStringExtra("title");
        int id = Integer.valueOf(getIntent().getStringExtra("typeID"));
        mtitle.setText(title);
        params.addCallBack(this);
        params.messageListByType(UserUtils.getInstance(this).getMobilePhone(), id);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (jsonArray.size() > 0) {
            List<MessageTypeDataEntity> entityList = JSON.parseArray(JSON.toJSONString(jsonArray), MessageTypeDataEntity.class);
            if (messageAdapter == null) {
                messageAdapter = new MessageTypeDataAdapter(entityList, R.layout.item_message_typedata, MessageTypeDataActivity.this);
                listview.setAdapter(messageAdapter);
                messageAdapter.setOnItemClickListener(MessageTypeDataActivity.this);
            } else {
                messageAdapter.notifyDataSetChanged2(entityList);
            }
        } else {
            noData.setVisibility(View.VISIBLE);
            ToastUtils.show(result.getData().getMsg());
        }
        super.onRequestSuccess(tag, result);
    }


    @OnClick(R.id.message_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(MessageTypeDataEntity item, ViewHolder holder, int position) {
        Intent intent = new Intent(this, MessageDataActivity.class);
        intent.putExtra("title", item.getTitle());
        intent.putExtra("url", item.getUrl());

        startActivity(intent);
    }
}
