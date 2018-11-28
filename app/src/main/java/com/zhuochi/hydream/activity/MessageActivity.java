package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.MessageAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.entity.MessageTypeEntity;
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
 * 通知消息
 */

public class MessageActivity extends BaseAutoActivity implements OnItemClickListener<MessageTypeEntity> {
    @BindView(R.id.listview)
    ListView listview;
    private MessageAdapter messageAdapter;
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
        params.addCallBack(this);
        params.messageType(UserUtils.getInstance(this).getMobilePhone());
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (jsonArray.size() > 0) {
            List<MessageTypeEntity> entityList = JSON.parseArray(JSON.toJSONString(jsonArray), MessageTypeEntity.class);
            if (messageAdapter == null) {
                messageAdapter = new MessageAdapter(entityList, R.layout.item_message, MessageActivity.this);
                listview.setAdapter(messageAdapter);
                messageAdapter.setOnItemClickListener(MessageActivity.this);
            } else {
                messageAdapter.notifyDataSetChanged2(entityList);
            }
        }else {
            ToastUtils.show(result.getData().getMsg());
        }
        super.onRequestSuccess(tag, result);
    }



    @OnClick(R.id.message_back)
    public void onClick() {
        finish();
    }


    @Override
    public void onItemClick(MessageTypeEntity item, ViewHolder holder, int position) {
        Intent intent = new Intent(this, MessageTypeDataActivity.class);
        String id=String.valueOf(item.getId());
        intent.putExtra("title", item.getType_name());
        intent.putExtra("typeID", id);
        startActivity(intent);
    }
}
