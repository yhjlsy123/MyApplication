package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.DeviceEditAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceEditBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceEditTimeAcitivity extends BaseActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.list)
    ListView list;
    private XiRequestParams params;
    List<DeviceEditBean> data;
    private DeviceEditAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select_acitvity);
        ButterKnife.bind(this);
        toolbarTitle.setText("设备营业时间");
        params = new XiRequestParams(this);
        loadData();
        onEvent();
    }

    private void onEvent() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setMapValue(position, true);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("data", data.get(position));
                setResult(100, intent);
                finish();
            }
        });

    }

    private void loadData() {
        params.addCallBack(this);
        Map<String, Object> pram = Common.intancePram();
        pram.put("user_id", SPUtils.getInt(Constants.USER_ID, 0));
        params.comnRequest("Org/getTimerList", pram);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        switch (tag) {
            case "Org/getTimerList":
                if (result.getData().getCode() == 200) {
                    data = JSON.parseArray(JSON.toJSONString(result.getData().getData()), DeviceEditBean.class);
                    adapter = new DeviceEditAdapter(this, data);
                    list.setAdapter(adapter);
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                break;
        }
    }
}
