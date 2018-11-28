package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.ComnTextAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.config.AppManager;
import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.JumpUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//请求学校、校区的code 1
// 请求学校、校区 浴室的code 2
// 请求学校,校区,浴室,浴位的code 3
public class SchoolSelectAcitvity extends BaseActivity {
    private XiRequestParams params;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.list)
    ListView listView;
    private List<SchoolListThridBean> data;
    private ComnTextAdapter adater;
    private DevicePram dpr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select_acitvity);
        ButterKnife.bind(this);
        toolbarTitle.setText("学校列表");
        params = new XiRequestParams(this);
        params.addCallBack(this);
        params.getOrgBathroomPositionLists(SPUtils.getInt(Constants.USER_ID, 0));
        dpr = (DevicePram) getIntent().getParcelableExtra(Constant.intentPram);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adater.setMapValue(position, true);
                adater.notifyDataSetChanged();
                dpr.setOrg_id(data.get(position).getOrg_id());
                dpr.setOrg_name(data.get(position).getOrg_name());
                if (0 == data.get(position).getOrg_id() && null == data.get(position).getOrg_area()) {
                    Intent intent = new Intent();
                    intent.putExtra("data", dpr);
                    setResult(100, intent);
                    finish();
                    return;

                }
                if (null != data.get(position).getOrg_area() && data.get(position).getOrg_area().size() == 0) {
                    if (null != BathHouseApplication.mHeightSelectView) {
                        BathHouseApplication.mHeightSelectView.setNameText(null == dpr.getOrg_name() ? "" : dpr.getOrg_name(), null == dpr.getOrg_area_name() ? "" : dpr.getOrg_area_name(), null == dpr.getBooth_name() ? "" : dpr.getBooth_name(), null == dpr.getDevice_name() ? "" : dpr.getDevice_name());
                        BathHouseApplication.mHeightSelectView.get_ids(dpr.getOrg_id(), dpr.getOrg_area_id(), dpr.getBoothroom_id(), dpr.getDevice_key());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data", dpr);
                    setResult(100, intent);
                    finish();

                } else {
                    JumpUtils.jumpAcitvityParce(SchoolSelectAcitvity.this, ScAreaSelectAcitvity.class, dpr, JSON.toJSONString(data.get(position).getOrg_area()));
                }


            }
        });


    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        jsonArray = new JSONArray((ArrayList) result.getData().getData());
        data = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolListThridBean.class);
        adater = new ComnTextAdapter(this, data);
        if (!(dpr.getActivity().equals(ReleaseNoticeActivity.class) || dpr.getActivity().equals(ReleaseActivity.class))) {
            adater.setLimit(true);
        }

        listView.setAdapter(adater);

    }

    @OnClick({R.id.toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                setResult(resultCode, data);
                finish();
                break;
        }


    }
}


