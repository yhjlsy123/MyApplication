package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.BalanceAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.bean_.BalanceBean;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.pulltorefresh2.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额明细
 * Created by and on 2016/11/19.
 */

public class BalanceLog extends BaseAutoActivity {
    private XiRequestParams params;

    @BindView(R.id.record_listview)
    RefreshListView recordListview;
    @BindView(R.id.dafault)
    View dafault;

    private View headview;
    TextView textBanlance;
    ImageView imgBalance;
    String balanceTime = "";

    private RefreshListView.State mCurrentState;//刷新状态
    private int page;//当前页
    private int limit = 10;//每页条数
    private int position = 0;//listview位置
    List<BalanceBean.BalanceListBean> balanceListBeen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancelog);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);

        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            recordListview.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        }else {  init_Data();/*加载界面*/}

    }

    /*加载界面*/
    private void init_Data() {
        initHead();/*渲染头部*/
        init_View();/*渲染listview*/
        balanceLog("0", 0, 0);/*余额记录*/
    }

    /*渲染头部*/
    private void initHead() {
        headview = LayoutInflater.from(this).inflate(R.layout.header_balancelog, null);
        textBanlance = headview.findViewById(R.id.tv_balance_time);
        imgBalance = headview.findViewById(R.id.img_balance_time);
    }

    /*余额记录*/
    private void balanceLog(String handle_type, int search_begin, int search_end) {
        params.addCallBack(this);
        params.balanceLog(UserUtils.getInstance(this).getUserID(), UserUtils.getInstance(this).getMobilePhone(), handle_type, search_begin, search_end, page, limit);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "balanceLog":/*余额记录*/
                Object object = result.getData().getData();
                try {
                    if (object != null) {
                        BalanceBean balanceBean = JSON.parseObject(JSON.toJSONString(object), BalanceBean.class);
                        if (balanceBean.getBalanceList().size() > 0) {//有数据时
                            balanceListBeen.addAll(balanceBean.getBalanceList());
                            BalanceAdapter balanceAdapter = new BalanceAdapter(balanceListBeen, R.layout.item_balancelog_child, BalanceLog.this);
                            recordListview.setAdapter(balanceAdapter);
                            if (position != 0) {
                                recordListview.setSelection(position + 2);
                            } else {
                                recordListview.setSelection(position);
                            }
                            if (balanceBean.getBalanceList() == null || balanceBean.getBalanceList().size() < limit) {// 当条数小于
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
                }/*如果数据为空，加载缺省页*/
                if (balanceListBeen.size() <= 0) {
                    recordListview.setVisibility(View.GONE);
                    dafault.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @OnClick(R.id.balancelog_back)
    public void onClick() {
        finish();
    }

    private void init_View() {
        mCurrentState = RefreshListView.State.NONE;/*设置刷新状态*/
//        recordListview.addHeadView(headview);/*绑定顶部V*/
        recordListview.setXListViewListener(new RefreshListView.IXListViewListener() {/*设置监听*/
            @Override
            public void onRefresh() {
                   /*上拉刷新，将page,position都设为0，从0开始*/
                if (mCurrentState == RefreshListView.State.NONE) {
                    mCurrentState = RefreshListView.State.PULL;
                    balanceListBeen = new ArrayList<BalanceBean.BalanceListBean>();
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
                    if (balanceListBeen.size() % limit == 0) {
                        mCurrentState = RefreshListView.State.PUSH;
                        position = recordListview.getFirstVisiblePosition();
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
        balanceLog("0", 0, 0);
    }


}

