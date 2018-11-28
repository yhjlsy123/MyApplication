package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.ComnTextAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.config.AppManager;
import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScDeviceSelectAcitvity extends BaseActivity implements HeightSelectView.SelcetCallBack {
    private XiRequestParams params;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.list)
    ListView listView;
    private List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition> data;
    private ComnTextAdapter adater;
    private DevicePram dpr;
    private ExcuteListener mlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select_acitvity);
        ButterKnife.bind(this);
        toolbarTitle.setText("设备列表");
        params = new XiRequestParams(this);
        params.addCallBack(this);
        params.getOrgBathroomPositionLists(SPUtils.getInt(Constants.USER_ID, 0));
        String dataStr = getIntent().getStringExtra(Constant.addition);
        dpr = (DevicePram) getIntent().getParcelableExtra(Constant.intentPram);
        if (!TextUtils.isEmpty(dataStr)) {
            data = JSON.parseArray(dataStr, SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition.class);
            if (data.size() > 0) {
                adater = new ComnTextAdapter(ScDeviceSelectAcitvity.this);
                adater.setScDevice(data);
                adater.setLimit(true);
                listView.setAdapter(adater);
            } else {
                Intent intent = new Intent();
                intent.putExtra("data", dpr);
                setResult(100, intent);
                finish();
            }


        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adater.setMapValue(position, true);
                dpr.setDevice_key(data.get(position).getDevice_key());
                dpr.setDevice_name(data.get(position).getDevice_name());
                adater.notifyDataSetChanged();
                if (TextUtils.isEmpty(data.get(position).getDevice_key())) {
                    if (null != BathHouseApplication.mHeightSelectView) {
                        BathHouseApplication.mHeightSelectView.setNameText(null == dpr.getOrg_name() ? "" : dpr.getOrg_name(), null == dpr.getOrg_area_name() ? "" : dpr.getOrg_area_name(), null == dpr.getBooth_name() ? "" : dpr.getBooth_name(), null == dpr.getDevice_name() ? "" : dpr.getDevice_name());
                        BathHouseApplication.mHeightSelectView.get_ids(dpr.getOrg_id(), dpr.getOrg_area_id(), dpr.getBoothroom_id(), dpr.getDevice_key());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data", dpr);
                    setResult(100, intent);
                    finish();
                    return;

                }
                if (null != BathHouseApplication.mHeightSelectView) {
                    BathHouseApplication.mHeightSelectView.setNameText(null == dpr.getOrg_name() ? "" : dpr.getOrg_name(), null == dpr.getOrg_area_name() ? "" : dpr.getOrg_area_name(), null == dpr.getBooth_name() ? "" : dpr.getBooth_name(), null == dpr.getDevice_name() ? "" : dpr.getDevice_name());
                    BathHouseApplication.mHeightSelectView.get_ids(dpr.getOrg_id(), dpr.getOrg_area_id(), dpr.getBoothroom_id(), dpr.getDevice_key());
                }
                Intent intent = new Intent();
                intent.putExtra("data", dpr);
                setResult(100, intent);
                finish();


            }
        });


    }


    @OnClick({R.id.toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
        }

    }


    public interface ExcuteListener {
        void setIds(int org_id, int org_area_id, int boothroom_id, String boothroomPosition);

        void setNames(String org_name, String org_area_name, String boothroom_name, String device_key_name);
    }

    public void setExcuteListener(ExcuteListener listener) {
        mlistener = listener;
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        org_id = dpr.getOrg_id();
        org_area_id = dpr.getOrg_area_id();
        boothroom_id = dpr.getBoothroom_id();
        device_key = dpr.getDevice_key();
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


