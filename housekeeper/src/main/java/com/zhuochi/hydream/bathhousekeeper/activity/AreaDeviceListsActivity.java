package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.client.android.CaptureActivity;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.AreaListsDeviceAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.AreaDeviceItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 设备列表
 */
public class AreaDeviceListsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_menu)
    ImageView toolbar_menu;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private XiRequestParams params;
    private AreaListsDeviceAdapter adapter;
    private List<AreaDeviceItemBean> mDataList;
    private int org_id;
    private int org_area_id;
    private String device_type_key;
    private int page = 1;
    private int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_device_lists);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        org_id = getIntent().getIntExtra("org_id", 0);
        org_area_id = getIntent().getIntExtra("org_area_id", 0);
        device_type_key = getIntent().getStringExtra("device_type_key");
        initView();
        initAdapter();
        initData();
    }

    private void initView() {
        toolbarTitle.setText("设备列表");
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, R.drawable.shap_divider_h));

    }

    private void initData() {
        loadDataFromNet();

    }

    public void initAdapter() {
        //上拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                page = 1;
                loadDataFromNet();
                refreshLayout.setRefreshing(false);


            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList<>();
        adapter = new AreaListsDeviceAdapter(R.layout.area_device_item, mDataList);
        recyclerView.setAdapter(adapter);


        //上拉加载
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                page += 1;
                loadDataFromNet();
                refreshLayout.setEnabled(true);
            }
        }, recyclerView);
        adapter.setOnItemClickListener(this);
    }


    private void loadDataFromNet() {
        params.addCallBack(this);
        params.getorgAreaDeviceLists(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_type_key, page, limit);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        if (page == 1) {
            mDataList.clear();
        } else {
            adapter.loadMoreComplete();
        }
        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (jsonArray.size() > 0) {
            mDataList.addAll(JSON.parseArray(JSON.toJSONString(jsonArray), AreaDeviceItemBean.class));
            adapter.setNewData(mDataList);
        } else {
            adapter.loadMoreEnd();
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ManageDeviceActivity.class);
        intent.putExtra("DeviceKey", mDataList.get(position).getDevice_key());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case 1:
                        page = 1;
                        loadDataFromNet();
                        break;
                }
                break;
        }
    }
}