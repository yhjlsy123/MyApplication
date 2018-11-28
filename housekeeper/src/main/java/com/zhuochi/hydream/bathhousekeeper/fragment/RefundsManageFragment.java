package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.RefundsManageAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.bean.RefundsManageItemBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.AskDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RefundsManageFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String url_webView;
    private XiRequestParams params;
    private int page = 1;
    private int status;
    private int org_id;
    private int org_area_id;
    private String start_date;
    private String end_date;
    private ArrayList<RefundsManageItemBean> mDataList;
    private RefundsManageAdapter adapter;
    private AskDialog aDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt("status");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refunds_manage_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        initAdapter();
        initData();
        if (getUserVisibleHint()) {
            loadDataFromNet();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initData() {

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
        adapter = new RefundsManageAdapter(R.layout.item_refunds_manage, mDataList);
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
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.exit_money:
                        aDialog = new AskDialog(getActivity(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                aDialog.dismiss();
                                if (TextUtils.isEmpty(aDialog.getContentValue())) {
                                    ToastUtils.show("请输入同意理由或原因");
                                    return;
                                }

                                exitMoney(mDataList.get(position).getId() + "", aDialog.getContentValue(), "2");
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                aDialog.dismiss();
                                if (TextUtils.isEmpty(aDialog.getContentValue())) {
                                    ToastUtils.show("请输入拒绝理由或原因");
                                    return;
                                }

                                if (new BigDecimal(mDataList.get(position).getApply_refund_money()).floatValue() > 0.01) {
                                    ToastUtils.show("退款金额必须大于1角");
                                    return;
                                }
                                exitMoney(mDataList.get(position).getId() + "", aDialog.getContentValue(), "3");

                            }
                        });

                        break;
                }
            }
        });
    }

    private void exitMoney(String refund_id, String handle_remark, String handle_result) {
        params.addCallBack(this);
        Map<String, Object> pram = new HashMap<String, Object>();
        int userId = SPUtils.getInt(Constants.USER_ID, 0);
        pram.put("user_id", userId + "");
        pram.put("refund_id", refund_id);
        pram.put("handle_result", handle_result);
        pram.put("handle_remark", handle_remark);
        params.comnRequest("Refund/handleRefund", pram);
    }


    public void loadDataFromNet() {
        params.addCallBack(this);
        params.getRefundList(SPUtils.getInt(Constants.USER_ID, 0), status, org_id, org_area_id, start_date, end_date, page);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "Refund/handleRefund":
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    loadDataFromNet();
                }
                break;
            default:
                if (page == 1) {
                    mDataList.clear();
                } else {
                    adapter.loadMoreComplete();
                }
                JSONArray jsonArray = new JSONArray((List) result.getData().getData());
                if (jsonArray.size() > 0) {
                    List<RefundsManageItemBean> entity = JSON.parseArray(JSON.toJSONString(jsonArray), RefundsManageItemBean.class);
                    mDataList.addAll(entity);
                    adapter.setNewData(mDataList);
                } else {
                    adapter.setNewData(mDataList);
                    adapter.loadMoreEnd();
                }
                break;
        }

    }

    //设置选择条件 并搜索
    public void setSelectionAndSearch(int org_id, int org_area_id, int boothroom_id, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        start_date = StartTime;
        end_date = EndTime;
        page = 1;

        loadDataFromNet();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
