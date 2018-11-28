package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.SchoolListWithUserAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SchoolListWithUserNumEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户管理
 */
public class UserManageActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private XiRequestParams params;
    private SchoolListWithUserAdapter adapter;
    private List<SchoolListWithUserNumEntity> mDataList;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initView();
        initData();
    }

    private void initView() {
        toolbarTitle.setText("用户管理");
        //没有分页禁止下拉刷新
        refreshLayout.setEnabled(false);
    }

    private void initData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList<>();
        adapter = new SchoolListWithUserAdapter(R.layout.school_list_user_item, mDataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        params.addCallBack(this);
        params.getSchoolListsWithUserNumRequest(SPUtils.getInt(Constants.USER_ID, 0));
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

        JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
        if (jsonArray.size() > 0) {
            mDataList = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolListWithUserNumEntity.class);
            adapter.setNewData(mDataList);
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
        Intent intent = new Intent(getApplicationContext(), AreaUserListsActivity.class);
        intent.putExtra("org_area_id",mDataList.get(position).getOrg_area_id());
        intent.putExtra("org_id",mDataList.get(position).getOrg_id());
        startActivity(intent);
    }
}
