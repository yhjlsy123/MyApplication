package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.NoticeManageListAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.DeviceTypBean;
import com.zhuochi.hydream.bathhousekeeper.bean.NoticeManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公告管理
 */
public class NoticeManageActivity extends BaseActivity implements HeightSelectView.SelcetCallBack {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private HeightSelectView mHeightSelectView;
    private XiRequestParams params;
    private int org_id;
    private int org_area_id;
    private String startTime;
    private String endTime;
    int page = 1;
    private NoticeManageListAdapter adapter;
    private ArrayList<NoticeManageItemBean> mDataList;
    private int delPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_manage);
        ButterKnife.bind(this);
        mHeightSelectView = new HeightSelectView(this, this, 1, getResources().getString(R.string.select_school_campus));
        params = new XiRequestParams(this);
        initView();
        initAdapter();
        initData();
    }

    private void initView() {
        headViewContaner.addView(mHeightSelectView);
        toolbarTitle.setText("公告管理");
    }

    private void initData() {
        loadDataFromNet();
    }

    public void initAdapter() {
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
        adapter = new NoticeManageListAdapter(R.layout.item_notice_manage, mDataList);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                delAcitivity(pos);

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {


            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {


            }
        };

        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(onItemSwipeListener);
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
    }

    private void delAcitivity(int pos) {
        this.delPos=pos;
        Map<String, Object> data = Common.intancePram();
        data.put("user_id", SPUtils.getInt(Constants.USER_ID, 0));
        data.put("msg_id", mDataList.get(pos).getMsg_id());
        params.comnRequest("NoticeApi/delBullentin", data);

    }

    private void loadDataFromNet() {
        params.addCallBack(this);
        params.getNoticeList(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, startTime, endTime, page);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "noticeList":
                if (page == 1) {
                    mDataList.clear();
                }
                JSONArray jsonArray = new JSONArray((List) result.getData().getData());
                if (jsonArray.size() > 0) {
                    List<NoticeManageItemBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), NoticeManageItemBean.class);
                    mDataList.addAll(entity);
                    adapter.setNewData(mDataList);
                } else {
                    adapter.setNewData(mDataList);
                    adapter.loadMoreEnd();
                }
                break;
            case "NoticeApi/delBullentin":
                if (result.getData().getCode() == 200) {
                    mDataList.remove(delPos);
                    adapter.setNewData(mDataList);
                } else {
                    adapter.setNewData(mDataList);
                    adapter.loadMoreEnd();
                }
                break;
        }


    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu, R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                break;
            case R.id.img_add:
                startActivity(new Intent(this, ReleaseNoticeActivity.class));
                break;
        }
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        startTime = StartTime;
        endTime = EndTime;
        page = 1;
        loadDataFromNet();
    }


}
