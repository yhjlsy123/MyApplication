package com.isgala.xishuashua.ui;

import android.os.Bundle;
import android.view.View;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.BalanceMonthAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.BalanceBean;
import com.isgala.xishuashua.bean_.BalanceBeanData;
import com.isgala.xishuashua.bean_.BalanceList;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.pulltorefresh2.RefreshListView;
import com.isgala.xishuashua.view.pulltorefresh2.RefreshListView.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.list;
import static com.isgala.xishuashua.R.id.monthlistview;

/**
 * Created by and on 2016/11/19.
 */

public class BalanceLog extends BaseAutoActivity {
    @BindView(monthlistview)
    RefreshListView mListView;
    @BindView(R.id.no_mingxi)
    View no_mingxi;
    private BalanceMonthAdapter balanceMothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancelog);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private int limit = 15;

    private void initView() {
        page = 1;
        map = new HashMap<>();
        map.put("perpage", limit + "");
        map.put("page", page + "");
        mCurrentState = State.NONE;
        mListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if (mCurrentState == State.NONE) {
                    mCurrentState = RefreshListView.State.PULL;
                    page = 1;
                    map.put("page", page + "");
                    loadData();
                    return;
                }
                stopLoad();
            }

            @Override
            public void onLoadMore() {
                if (mCurrentState == State.NONE) {
                    if (!NetworkUtil.isNetworkAvailable()) {
                        mListView.errLoadMore();
                        return;
                    }
                    page++;
                    map.put("page", page + "");
                    mCurrentState = State.PUSH;
                    loadData();
                }
            }
        });
    }


    /**
     * 停止刷新，
     */
    private void stopLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mCurrentState = State.NONE;
    }


    /**
     * 刷新完成
     */
    private void finishLoad() {
        mListView.finishRefresh();
        mCurrentState = State.NONE;
    }

    private HashMap<String, String> map;
    private int page;
    private RefreshListView.State mCurrentState;


    private void loadData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.BALANCE_LOG, "BALANCE_LOG", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                BalanceBean balanceBean = JsonUtils.parseJsonToBean(result, BalanceBean.class);
                if (balanceBean != null && balanceBean.data != null) {
                    List<BalanceBeanData> list = balanceBean.data;
                    no_mingxi.setVisibility(View.INVISIBLE);
                    if (mCurrentState == State.NONE || mCurrentState == State.PULL) {
                        if (list == null || list.size() < 1) {//没有数据时
                            no_mingxi.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {//有数据
                            if (mListView.getVisibility() != View.VISIBLE)
                                mListView.setVisibility(View.VISIBLE);
                            if (balanceMothAdapter == null) {
                                balanceMothAdapter = new BalanceMonthAdapter(list, R.layout.item_month_log, BalanceLog.this);
                                mListView.setAdapter(balanceMothAdapter);
                            } else {
                                balanceMothAdapter.notifyDataSetChanged2(list);
                            }
                        }
                    } else {
                        balanceMothAdapter.notifyDataSetChanged(list);
                    }
                    int tempCount = 0;//记录当前返回的条目数
                    for (BalanceBeanData initData : list) {
                        if (initData.list != null) {
                            tempCount += initData.list.size();
                        }
                    }
                    if (list == null || tempCount < limit) {// 当条数小于
                        // 不可以上拉加载更多
                        if (mCurrentState == State.NONE || mCurrentState == State.PULL)
                            mListView.setPullLoadEnable(false);
                        else
                            mListView.showNOMore();
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                }
                if (mCurrentState == State.PULL) {
                    finishLoad();
                } else if (mCurrentState == State.PUSH) {
                    stopLoad();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                if (mCurrentState == State.NONE) {
                    mListView.setVisibility(View.INVISIBLE);
                } else if (mCurrentState == State.PUSH) {
                    mListView.errLoadMore();
                    mCurrentState = State.NONE;
                } else {
                    stopLoad();
                }
            }
        });
    }

    @OnClick(R.id.balancelog_back)
    public void onClick() {
        finish();
    }
}
