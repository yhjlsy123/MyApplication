package com.zhuochi.hydream.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.RecordAdapter;
import com.zhuochi.hydream.base.BaseFragment;
import com.zhuochi.hydream.entity.DeviceTypeEntity;
import com.zhuochi.hydream.entity.RecordEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.pulltorefresh2.RefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 消费记录
* */
public class RecordLogFragment extends BaseFragment {
    XiRequestParams params;
    Serializable obj;
    DeviceTypeEntity deviceTypeEntity;//上一个界面传过来的对象

    @BindView(R.id.record_listview)
    RefreshListView recordListview;
    @BindView(R.id.dafault)
    View dafault;

    private View headview;//顶部视图
    TextView textCostAll;
    String costall = "";

    private RefreshListView.State mCurrentState;//刷新状态
    private int page;//当前页
    private int limit = 10;//每页条数
    private int position = 0;//list显示位置
    List<RecordEntity.ConsumptionListBean> consumptionListBeen = new ArrayList<>();

    private boolean isViewCreated; //Fragment的View加载完毕的标记
    private boolean isUIVisible;  //Fragment对用户可见的标记

    final Handler handler = new Handler() { //刷新
        @Override
        public void handleMessage(Message msg) {//刷新顶部视图
            super.handleMessage(msg);
            if (msg.what == 1) {
                textCostAll.setText(costall);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = getArguments().getSerializable("deviceTypeEntity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_log, null);
        isViewCreated = true;/*Fragment的View加载完毕*/
        ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            recordListview.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        } else {
            init_Data();/*加载界面*/
        }
        return view;
    }



    private void init_Data() {
        if (isViewCreated && isUIVisible) {
            initHead();/*渲染头部*/
            init_View();/*渲染listview*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (costall.length() == 0 && obj != null) {
                        String jsonString = (String) obj;
                        deviceTypeEntity = JSON.parseObject(jsonString, DeviceTypeEntity.class);/*获取上一个页面传过来的对象*/
                        consumptionLog();/*消费明细列表*/
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    /*消费明细列表*/
    private void consumptionLog() {
        params.addCallBack(this);
        params.consumptionLog(UserUtils.getInstance(getActivity()).getMobilePhone(), UserUtils.getInstance(getActivity()).getUserID(), deviceTypeEntity.getDevice_type_key(), page, limit);
    }

    /*渲染头部*/
    private void initHead() {
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.header_record_log, null);
        textCostAll = headview.findViewById(R.id.text_cost_all);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "consumptionLog":/*消费明细*/
                Object object = result.getData().getData();
                try {
                    if (object != null) {
                        RecordEntity recordEntities = JSON.parseObject(JSON.toJSONString(object), RecordEntity.class);
                        costall = recordEntities.getTotalAmount() + "元";/*获取总金额*/
                        Message message = new Message();/*发送handler，刷新顶部视图*/
                        message.what = 1;
                        handler.sendMessage(message);
                        if (recordEntities.getConsumptionList().size() > 0) {/*有数据时*/
                            consumptionListBeen.addAll(recordEntities.getConsumptionList());
                            RecordAdapter recordAdapter = new RecordAdapter(consumptionListBeen, R.layout.item_record, getActivity());
                            recordListview.setAdapter(recordAdapter);
                            if (position != 0) {/*设置listview的显示位置，如果不是第一次加载，将原来的布局向上滑动两个item*/
                                recordListview.setSelection(position + 2);
                            } else {
                                recordListview.setSelection(position);
                            }
                            if (recordEntities.getConsumptionList() == null || recordEntities.getConsumptionList().size() < limit) {// 当条数小于
                            /*已加载完*/
                                recordListview.setPullLoadEnable(true);
                            } else {
                                /*未加载完*/
                                recordListview.setPullLoadEnable(true);
                                page++;
                            }
                            /*若状态为下拉刷新，则刷新完成；若状态为上拉加载，则停止刷新*/
                            if (mCurrentState == RefreshListView.State.PULL) {
                                finishLoad();
                            } else if (mCurrentState == RefreshListView.State.PUSH) {
                                stopLoad();
                            }
                        }
                    }
                } catch (Exception e) {
                }/*若数据为空，显示缺省页*/
                if (consumptionListBeen.size() == 0) {
                    dafault.setVisibility(View.VISIBLE);
                    recordListview.setVisibility(View.GONE);
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init_View() {
        mCurrentState = RefreshListView.State.NONE;/*设置刷新状态*/
        recordListview.addHeadView(headview);/*绑定顶部View*/
        recordListview.setXListViewListener(new RefreshListView.IXListViewListener() {/*设置监听*/
            @Override
            public void onRefresh() {
                /*上拉刷新，将page,position都设为0，从0开始*/
                if (mCurrentState == RefreshListView.State.NONE) {
                    mCurrentState = RefreshListView.State.PULL;
                    consumptionListBeen = new ArrayList<RecordEntity.ConsumptionListBean>();
                    page = 0;
                    position = 0;
                    LoadData();
                    return;
                }
                stopLoad();
            }

            @Override
            public void onLoadMore() {
                /*下拉加载,根据网络状态，判断是否是最后一页，执行操作，*/
                if (mCurrentState == RefreshListView.State.NONE) {
                    if (!NetworkUtil.isNetworkAvailable()) {
                        recordListview.errLoadMore();
                        return;
                    }
                    if (consumptionListBeen.size() % limit == 0) {
                        mCurrentState = RefreshListView.State.PUSH;
                        position = recordListview.getFirstVisiblePosition();/*获取listview当前显示位置*/
                        LoadData();
                    } else {
                        recordListview.showNOMore();

                    }
                }
            }
        });
    }


    /**
     * 停止刷新，
     */
    private void stopLoad() {
        recordListview.stopRefresh();
        recordListview.stopLoadMore();
        mCurrentState = RefreshListView.State.NONE;
    }


    /**
     * 刷新完成
     */
    private void finishLoad() {
        recordListview.finishRefresh();
        mCurrentState = RefreshListView.State.NONE;
    }

    private void LoadData() {
        consumptionLog();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            init_Data();
        } else {
            isUIVisible = false;
        }
    }

}
