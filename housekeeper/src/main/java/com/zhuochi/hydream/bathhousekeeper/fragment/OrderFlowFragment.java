package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.OrderFlowDetialActivity;
import com.zhuochi.hydream.bathhousekeeper.adapter.OrderFlowListAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.bean.MyMultipleItem;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderFolwItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 订单管理
 */
public class OrderFlowFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, HeightSelectView.SelcetCallBack {
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.sell_num)
    TextView sellNum;
    private View mView;
    private HeightSelectView mHeightSelectView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private XiRequestParams params;
    Unbinder unbinder;

    private int org_id;
    int org_area_id;
    private int device_area_id;
    private String device_type_key = "faucet";
    String start_date;
    private String end_date;
    private int page = 1;
    private int limit = 10;
    private ArrayList<MyMultipleItem> mDataList;
    private OrderFlowListAdapter adapter;
    private String device_key;
    private boolean isQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device_type_key = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_flow_fragment, container, false);
        mView = view;
        unbinder = ButterKnife.bind(this, view);
        mHeightSelectView = new HeightSelectView(getActivity(), this, 3, getResources().getString(R.string.select_school_bathroom_wei));
        headViewContaner.addView(mHeightSelectView);
        Log.e("周OrderFlowFragment", "onCreateView周:" + device_type_key);
        params = new XiRequestParams(getActivity());
        isQuery = false;
        if (getUserVisibleHint()) {
            this.org_id = 0;
            this.org_area_id = 0;
            this.device_area_id = 0;
            this.device_key = null;
            loadDataFromNet();
        }
        initAdapter();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            if (null == mDataList || mDataList.size() == 0) {
                loadDataFromNet();
            }

        }
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDataList = new ArrayList<>();
        adapter = new OrderFlowListAdapter(mDataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

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

    public void loadDataFromNet() {
        params.addCallBack(this);
        if (isQuery) {
            params.getOrderFlow(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id
                    , device_type_key, device_key, start_date, end_date, page, limit);
        } else {
            params.getOrderFlow(SPUtils.getInt(Constants.USER_ID, 0), 0, 0, 0
                    , device_type_key, null, null, end_date, page, limit);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        JSONObject json = new JSONObject((Map) result.getData().getData());
        orderNum.setText(getFolatToString(json.get("totalNum")));
        sellNum.setText(TextUtils.isEmpty(json.get("totalMoney").toString()) ? "0" : json.get("totalMoney").toString());

        if (page == 1) {
            mDataList.clear();
        } else {
            adapter.loadMoreComplete();
        }
        JSONArray jsonArray = json.getJSONArray("all");
        if (jsonArray.size() > 0) {
            List<OrderFolwItemBean> list = JSON.parseArray(JSON.toJSONString(jsonArray), OrderFolwItemBean.class);
            if (list.size() == 0) {
                adapter.setEnableLoadMore(false);
            }
            mDataList.add(list.get(0));
            mDataList.addAll(list.get(0).getOrder());
        }
        adapter.setNewData(mDataList);
        if (!(TextUtils.isEmpty(start_date) || TextUtils.isEmpty(end_date))) {
            adapter.setEnableLoadMore(false);
        }

    }


    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        int itemType = adapter.getItemViewType(position);
        if (itemType == MyMultipleItem.SECOND_TYPE) {
            Intent intent = new Intent(getActivity(), OrderFlowDetialActivity.class);
//            ((OrderFolwItemBean.OrderBean) adapter.getItem(position)).getOrder_date()
            intent.putExtra("order_date", ((OrderFolwItemBean.OrderBean) adapter.getItem(position)).getOrder_date());
            intent.putExtra("type", device_type_key);
            startActivity(intent);
        }
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        isQuery = true;
        page = 1;
        this.org_area_id = org_area_id;
        this.org_id = org_id;
        this.device_area_id = boothroom_id;
        this.device_key = device_key;
        this.start_date = StartTime;
        this.end_date = EndTime;
        loadDataFromNet();
    }
}
