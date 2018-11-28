package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.AreaUserListsAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.AreaUserItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户管理搜索界面
 */
public class AreaUserListsActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.use_name)
    EditText useName;
    private XiRequestParams params;
    private AreaUserListsAdapter adapter;
    private List<AreaUserItemBean> mDataList;

    private int org_id;
    private int org_area_id;
    private String user_nickname;
    private int page = 1;
    private int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_user_lists);
        ButterKnife.bind(this);
        org_area_id = getIntent().getIntExtra("org_area_id",0);
        org_id = getIntent().getIntExtra("org_id",0);
        params = new XiRequestParams(this);
        initView();
        initData();
    }

    private void initView() {
        toolbarTitle.setText("用户管理");
    }

    private void initData() {

        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                page = 1;
                loadDataFromNet();
                refreshLayout.setRefreshing(false);
                adapter.setEnableLoadMore(true);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList<>();
        adapter = new AreaUserListsAdapter(R.layout.area_user_list_item, mDataList);
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

        loadDataFromNet();
    }

    private void loadDataFromNet() {
        params.addCallBack(this);
        params.getOrgAreaUserLists(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, user_nickname, page, limit);
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
            List<AreaUserItemBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), AreaUserItemBean.class);
            mDataList.addAll(entity);
            adapter.setNewData(mDataList);
        }else {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    @OnClick({R.id.back, R.id.toolbar_menu, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.toolbar_menu:

                break;
            case R.id.search:
                 user_nickname = useName.getText().toString().trim();
                loadDataFromNet();
                break;
        }
    }


}
