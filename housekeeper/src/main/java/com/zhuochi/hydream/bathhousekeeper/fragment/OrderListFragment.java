package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.OrderDetailActivity;
import com.zhuochi.hydream.bathhousekeeper.adapter.OrderListAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.bean.OrderListItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.DensityUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, BaseQuickAdapter.OnItemClickListener, HeightSelectView.SelcetCallBack {

    @BindView(R.id.rg_order)
    RadioGroup rg_order;
    @BindView(R.id.rb_order_all)
    RadioButton rbOrderAll;
    @BindView(R.id.rb_order_complete)
    RadioButton rbOrderComplete;
    @BindView(R.id.rb_order_micropayment)
    RadioButton rbOrderMicropayment;
    @BindView(R.id.rb_order_cancel)
    RadioButton rbOrderCancel;
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private HeightSelectView mHeightSelectView;
    private XiRequestParams params;
    private View mView;

    private String device_type = "faucet";
    private String order_state;
    private int org_id;
    private int org_area_id;
    private int device_area_id;
    private String start_date;
    private String end_date;
    private int page = 1;
    private int limit = 10;
    private OrderListAdapter adapter;
    private List<OrderListItemBean> mDataList;
    private boolean loadmore = false;
    private String device_key;
    private boolean isQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device_type = getArguments().getString("type");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_list_fragment, container, false);
        mView = view;
        ButterKnife.bind(this, view);
        mHeightSelectView = new HeightSelectView(getActivity(), this, 3, getResources().getString(R.string.select_school_bathroom_wei));
        headViewContaner.addView(mHeightSelectView);
        params = new XiRequestParams(getActivity());
        isQuery = false;
        initAdapter();
        if (getUserVisibleHint()) {
            //解决第一个fragment无法加载数据问题
            loadDataFromNet();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            if (null == mDataList || mDataList.size() == 0)
                loadDataFromNet();
        }
    }

    public void loadDataFromNet() {
        // LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        params.addCallBack(this);
        if (isQuery) {
            params.getOrderLists(SPUtils.getInt(Constants.USER_ID, 0), device_type, order_state, org_id, org_area_id, device_area_id,
                    device_key, start_date, end_date, page, limit);
        } else {
            params.getOrderLists(SPUtils.getInt(Constants.USER_ID, 0), device_type, null, 0, 0, 0, null,
                    null, null, page, limit);
        }

    }

    public void initView() {
        int dp40 = DensityUtil.dip2px(getActivity(), 40);
        //定义底部标签图片大小和位置
        Drawable drawable_rbOrderAll = getResources().getDrawable(R.drawable.rb_order_all_selector);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_rbOrderAll.setBounds(0, 0, dp40, dp40);
        //设置图片在文字的哪个方向
        rbOrderAll.setCompoundDrawables(null, drawable_rbOrderAll, null, null);

        Drawable drawable_rbOrderComplete = getResources().getDrawable(R.drawable.rb_order_complete_selector);
        drawable_rbOrderComplete.setBounds(0, 0, dp40, dp40);
        rbOrderComplete.setCompoundDrawables(null, drawable_rbOrderComplete, null, null);

        Drawable drawable_rbOrderMicropayment = getResources().getDrawable(R.drawable.rb_order_micropayment_selector);
        drawable_rbOrderMicropayment.setBounds(0, 0, dp40, dp40);
        rbOrderMicropayment.setCompoundDrawables(null, drawable_rbOrderMicropayment, null, null);

        Drawable drawable_rbOrderCancel = getResources().getDrawable(R.drawable.rb_order_cancel_selector);
        drawable_rbOrderCancel.setBounds(0, 0, dp40, dp40);
        rbOrderCancel.setCompoundDrawables(null, drawable_rbOrderCancel, null, null);

        rbOrderAll.setChecked(true);
        rg_order.setOnCheckedChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
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
        adapter = new OrderListAdapter(R.layout.item_order, mDataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        //上拉加载
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page += 1;
                loadDataFromNet();
            }
        }, recyclerView);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "Orderlists":

                JSONObject json = new JSONObject((Map) result.getData().getData());
                int total_all = json.getInteger("total_all");
                int total_success = json.getInteger("total_success");
                int total_cancel = json.getInteger("total_cancel");
                int total_unpay = json.getInteger("total_unpay");

                rbOrderAll.setText("全部订单\n" + total_all);
                rbOrderComplete.setText("已完成\n" + total_success);
                rbOrderMicropayment.setText("未支付\n" + total_unpay);
                rbOrderCancel.setText("已取消\n" + total_cancel);

                if (page == 1) {//第一页加载 清空集合再添加
                    mDataList.clear();
                }

                JSONArray jsonArray = json.getJSONArray("order");
                if (jsonArray.size() > 0) {
                    List<OrderListItemBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), OrderListItemBean.class);
                    mDataList.addAll(entity);
                    adapter.setNewData(mDataList);
//                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                }
                adapter.notifyDataSetChanged();

                LoadingTrAnimDialog.dismissLoadingAnimDialog();
                break;
        }
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        isQuery = true;
        switch (checkedId) {
            case R.id.rb_order_all:
                order_state = "";
                break;
            case R.id.rb_order_complete:
                order_state = "4";
                break;
            case R.id.rb_order_micropayment:
                order_state = "1";
                break;
            case R.id.rb_order_cancel:
                order_state = "6";
                break;

        }
        page = 1;
        loadDataFromNet();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("order_item_id", ((OrderListItemBean) adapter.getItem(position)).getOrder_item_id());
        startActivity(intent);
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        this.device_area_id = boothroom_id;
        this.device_key = device_key;
        start_date = StartTime;
        end_date = EndTime;
        isQuery = true;
        loadDataFromNet();
    }
}
