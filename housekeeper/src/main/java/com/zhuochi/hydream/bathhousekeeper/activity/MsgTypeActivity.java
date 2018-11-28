package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.DeviceTypeAdapter;
import com.zhuochi.hydream.bathhousekeeper.adapter.MsgTypeAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceTypBean;
import com.zhuochi.hydream.bathhousekeeper.bean.MsgTypeBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备类型
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class MsgTypeActivity extends BaseActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;
    private XiRequestParams params;
    private List<MsgTypeBean> entity;
    private MsgTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
        toolbarTitle.setText("消息类型");
        params = new XiRequestParams(this);
        params.addCallBack(this);
        getDeviceList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setMapValue(position, true);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("data", entity.get(position));
                setResult(100, intent);
                finish();
            }
        });
    }

    private void getDeviceList() {
        Map<String, Object> parm = Common.intancePram();
        parm.put("user_id", SPUtils.getInt(Constants.USER_ID, 0));
        params.comnRequest("NoticeApi/getMsgSort", parm);

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        switch (tag) {
            case "NoticeApi/getMsgSort":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    entity = JSON.parseArray(JSON.toJSONString(jsonArray), MsgTypeBean.class);
                    adapter = new MsgTypeAdapter(this, entity);
                    listView.setAdapter(adapter);
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }
}
