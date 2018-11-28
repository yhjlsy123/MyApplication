package com.zhuochi.hydream.bathhousekeeper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.SchoolListSecondAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListSecondBean;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.SelectBathingDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.pickerview.builder.OptionsPickerBuilder;
import com.zhuochi.hydream.bathhousekeeper.view.pickerview.listener.OnOptionsSelectListener;
import com.zhuochi.hydream.bathhousekeeper.view.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchoolListSecondActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private XiRequestParams params;
    private ArrayList<MultiItemEntity> mDataList;
    private SchoolListSecondAdapter adapter;
    private int request_type = SchoolListSecondAdapter.request_type_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list_second);
//        highApiEffects();
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        request_type = getIntent().getIntExtra("request_code", 1);
        initView();
        loadData();
    }

    private void initView() {
        toolbarTitle.setText("学校_校区列表");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mDataList.size() == 0) {
                    loadData();
                }
                refreshLayout.setRefreshing(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList<>();
        adapter = new SchoolListSecondAdapter(mDataList, request_type);
        recyclerView.setAdapter(adapter);

        adapter.setExcuteListener(new SchoolListSecondAdapter.ExcuteListener() {
            @Override
            public void setIds(int org_id, int org_area_id, int boothroom_id, String device_key) {
                BathHouseApplication.mHeightSelectView.get_ids(org_id, org_area_id, boothroom_id, device_key);
            }

            @Override
            public void setNames(String org_name, String org_area_name, String boothroom_name, String device_key_name) {
                BathHouseApplication.mHeightSelectView.setNameText(org_name, org_area_name, boothroom_name, device_key_name);
                finish();
            }
        });

    }

    private void loadData() {
        params.addCallBack(this);
        if (request_type == SchoolListSecondAdapter.request_type_1) {
            params.getSchoolList(SPUtils.getInt(Constants.USER_ID, 0));
        } else if (request_type == SchoolListSecondAdapter.request_type_2) {
            params.getOrgBathroomLists(SPUtils.getInt(Constants.USER_ID, 0));
        } else if (request_type == SchoolListSecondAdapter.request_type_3) {
            params.getOrgBathroomPositionLists(SPUtils.getInt(Constants.USER_ID, 0));
        }

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        mDataList.clear();
        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (request_type == SchoolListSecondAdapter.request_type_1) {
            if (jsonArray.size() > 0) {
                List<SchoolListSecondBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolListSecondBean.class);

                for (int i = 0; i < entity.size(); i++) {
                    SchoolListSecondBean lev0 = entity.get(i);
                    List<SchoolListSecondBean.OrgAreaBean> lev1_list = lev0.getOrg_area();
                    int k = lev1_list.size();
                    for (int j = 0; j < k; j++) {
                        lev0.addSubItem(lev1_list.get(j));
                    }
                    mDataList.add(lev0);
                }
                adapter.setNewData(mDataList);
            }
        } else if (request_type == SchoolListSecondAdapter.request_type_2) {
            if (jsonArray.size() > 0) {
                List<SchoolListThridBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolListThridBean.class);

                for (int i = 0; i < entity.size(); i++) {
                    SchoolListThridBean lev0 = entity.get(i);
                    List<SchoolListThridBean.OrgAreaBean> lev1_list = lev0.getOrg_area();
                    int k = lev1_list.size();
                    for (int j = 0; j < k; j++) {
                        SchoolListThridBean.OrgAreaBean lev1 = lev1_list.get(j);
                        List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean> lev2_list = lev1.getBathroom();
                        int y = lev2_list.size();
                        for (int h = 0; h < y; h++) {
                            lev1.addSubItem(lev2_list.get(h));
                        }
                        lev0.addSubItem(lev1);
                    }
                    mDataList.add(lev0);
                }
                adapter.setNewData(mDataList);
            }
        } else if (request_type == SchoolListSecondAdapter.request_type_3) {
            jsonArray = new JSONArray((ArrayList) result.getData().getData());
            if (jsonArray.size() > 0) {
                List<SchoolListThridBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolListThridBean.class);
                for (int i = 0; i < entity.size(); i++) {
                    SchoolListThridBean lev0 = entity.get(i);
                    List<SchoolListThridBean.OrgAreaBean> lev1_list = lev0.getOrg_area();
                    int k = lev1_list.size();
                    for (int j = 0; j < k; j++) {
                        SchoolListThridBean.OrgAreaBean lev1 = lev1_list.get(j);
                        List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean> lev2_list = lev1.getBathroom();
                        for (int p = 0; p < lev2_list.size(); p++) {
                            SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean lo = lev2_list.get(p);
                            List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition> lo_list = lo.getDevice();
                            int y = lo_list.size();
                            for (int h = 0; h < y; h++) {
                                lo.addSubItem(lo_list.get(h));
                            }

                            lev0.addSubItem(lev1);
                        }

                    }
                    mDataList.add(lev0);
                }
                adapter.setNewData(mDataList);

//                adapter.expandAll();
            }
        }

    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
