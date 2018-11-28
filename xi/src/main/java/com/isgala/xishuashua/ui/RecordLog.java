package com.isgala.xishuashua.ui;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.MessageAdapter;
import com.isgala.xishuashua.adapter.RecordBlowerListAdapter;
import com.isgala.xishuashua.adapter.RecordListAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.Message;
import com.isgala.xishuashua.bean_.RecordBlowerList;
import com.isgala.xishuashua.bean_.RecordList;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.Common;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.pulltorefresh2.RefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.list;
import static android.R.id.message;
import static com.lidroid.xutils.view.ResType.Text;

/**
 * 消费记录日志
 * Created by and on 2016/11/14.
 */

public class RecordLog extends BaseAutoActivity {
    private TextView recordTotalMoney;
    private TextView recordTotalTime;
    @BindView(R.id.record_listview)
    RefreshListView mListView;
    @BindView(R.id.no_recordlog)
    View noRecordLog;
    private View headview;
    private HashMap<String, String> map;
    private int totalPage = 1;
    private int page;
    private RefreshListView.State mCurrentState;
    @BindView(R.id.line_bathe)
    RelativeLayout linebathe;
    @BindView(R.id.line_blower)
    RelativeLayout lineblower;
    @BindView(R.id.radio_tab)
    RadioGroup radio_tab;
    private RecordListAdapter recordListAdapter;
    private RecordBlowerListAdapter blowerListAdapter;

    private  CheckBox checkBathe;
    private  CheckBox checkBlower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        initHead();
        initView();
    }

    private void initHead() {
        headview = LayoutInflater.from(RecordLog.this).inflate(R.layout.recordhead, null);
        recordTotalMoney = (TextView) headview.findViewById(R.id.record_total_money);
        recordTotalTime = (TextView) headview.findViewById(R.id.record_total_time);
        checkBathe = (CheckBox) findViewById(R.id.check_bathe);
        checkBathe.setChecked(true);
        checkBathe.setTextColor(getResources().getColor(R.color.blue4DA9FF));
        checkBlower = (CheckBox) findViewById(R.id.check_blower);
        checkBlower.setChecked(false);
        checkBlower.setTextColor(getResources().getColor(R.color.black));
        checkBathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordListAdapter != null) {
                    recordListAdapter = null;
                }
                checkBathe.setChecked(true);
                checkBathe.setTextColor(getResources().getColor(R.color.blue4DA9FF));
                checkBlower.setTextColor(getResources().getColor(R.color.black));
                checkBlower.setChecked(false);
                loadData();
            }
        });
        checkBlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blowerListAdapter != null) {
                    blowerListAdapter = null;
                }
                checkBlower.setChecked(true);
                checkBathe.setChecked(false);
                checkBathe.setTextColor(getResources().getColor(R.color.black));
                checkBlower.setTextColor(getResources().getColor(R.color.blue4DA9FF));
                loadDataBolwer();
            }
        });
    }

    private void initView() {
        map = new HashMap<>();
        map.put("limit", "30");
        page = 1;
        totalPage = 1;
        mCurrentState = RefreshListView.State.NONE;
        mListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if (mCurrentState == RefreshListView.State.NONE) {
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
                if (mCurrentState == RefreshListView.State.NONE) {
                    if (!NetworkUtil.isNetworkAvailable()) {
                        mListView.errLoadMore();
                        return;
                    }
                    if (totalPage > page) {
//                        page++;
                        page = 1;
                        map.put("page", page + "");
                        mCurrentState = RefreshListView.State.PUSH;
                        loadData();
                    } else {
                        mListView.showNOMore();
                    }
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
        mCurrentState = RefreshListView.State.NONE;
    }


    /**
     * 刷新完成
     */
    private void finishLoad() {
        mListView.finishRefresh();
        mCurrentState = RefreshListView.State.NONE;
    }

    /*洗澡日志*/
    private void loadData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.RECORD_LIST, "list", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                RecordList recordList = JsonUtils.parseJsonToBean(result, RecordList.class);
                if (recordList != null && recordList.data != null) {
                    noRecordLog.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    totalPage = Tools.toInt(recordList.data.totalPage);
                    List<RecordList.LogRecord> list = recordList.data.result;
                    if (mCurrentState == RefreshListView.State.NONE || mCurrentState == RefreshListView.State.PULL) {
                        if (list == null || list.size() < 1) {//没有数据时
                            noRecordLog.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {//有数据
                            if (recordListAdapter == null) {
                                recordListAdapter = new RecordListAdapter(list, R.layout.item_recordlog, RecordLog.this);
                                mListView.setAdapter(recordListAdapter);
                                if (recordList.data.total != null && !TextUtils.isEmpty(recordList.data.total.payable)) {
                                    updatetView(recordList.data.total);
                                }
                            } else {
                                recordListAdapter.notifyDataSetChanged2(list);
                            }
                        }
                    } else {
                        recordListAdapter.notifyDataSetChanged(list);
                    }
                    if (list == null || list.size() < 10) {// 当条数小于
                        // 不可以上拉加载更多
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                }
                if (mCurrentState == RefreshListView.State.PULL) {
                    finishLoad();
                } else if (mCurrentState == RefreshListView.State.PUSH) {
                    stopLoad();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                if (mCurrentState == RefreshListView.State.NONE) {
                    mListView.setVisibility(View.INVISIBLE);
                } else if (mCurrentState == RefreshListView.State.PUSH) {
                    mListView.errLoadMore();
                    mCurrentState = RefreshListView.State.NONE;
                } else {
                    stopLoad();
                }
            }
        });
    }

    /*吹风机日志*/
    private void loadDataBolwer() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.BLOWER_LIST, "list", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                RecordBlowerList recordList = JsonUtils.parseJsonToBean(result, RecordBlowerList.class);
                if (recordList != null && recordList.data != null) {
//                    totalPage = Tools.toInt(recordList.data.totalPage);
                    List<RecordBlowerList.blower_consume_list> list = recordList.data.blower_consume_list;
                    if (mCurrentState == RefreshListView.State.NONE || mCurrentState == RefreshListView.State.PULL) {
                        if (list == null || list.size() < 1) {//没有数据时
                            noRecordLog.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {//有数据
                            if (blowerListAdapter == null) {
                                blowerListAdapter = new RecordBlowerListAdapter(list, R.layout.item_recordlog, RecordLog.this);
                                mListView.setAdapter(blowerListAdapter);
//                                if (recordList.data.total != null && !TextUtils.isEmpty(recordList.data.total.payable)) {
                                    updatetView(recordList);
//                                }
                            } else {
                                blowerListAdapter.notifyDataSetChanged2(list);
                            }
                        }
                    } else {
                        blowerListAdapter.notifyDataSetChanged(list);
                    }
                    if (list == null || list.size() < 10) {// 当条数小于
                        // 不可以上拉加载更多
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                }else {
                    noRecordLog.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
                if (mCurrentState == RefreshListView.State.PULL) {

                    finishLoad();
                } else if (mCurrentState == RefreshListView.State.PUSH) {
                    stopLoad();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                if (mCurrentState == RefreshListView.State.NONE) {
                    mListView.setVisibility(View.INVISIBLE);
                } else if (mCurrentState == RefreshListView.State.PUSH) {
                    mListView.errLoadMore();
                    mCurrentState = RefreshListView.State.NONE;
                } else {
                    stopLoad();
                }
            }
        });
    }

    private void updatetView(RecordList.Total total) {
        recordTotalMoney.setText(getSpendSpanText(total.payable + "元"));
        recordTotalTime.setText(total.total_time);
        mListView.addHeadView(headview);
    }
    private void updatetView(RecordBlowerList total) {
        recordTotalMoney.setText(getSpendSpanText(total.data.total_price + "元"));
        int time=Integer.valueOf(total.data.blower_total_time);
        if (!total.data.blower_total_time.isEmpty()){
            recordTotalTime.setText(Common.change(time));
        }
        mListView.addHeadView(headview);
    }
//    private CharSequence getSpendTime(String time) {
//        if (TextUtils.isEmpty(time))
//            return "";
//        SpannableString spanText = new SpannableString(time);
//        if (time.contains("时")) {
//            spanText.setSpan(new AbsoluteSizeSpan(15, true), time.indexOf("时"),
//                    time.indexOf("时") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return spanText;
//    }

    private CharSequence getWaterSpanText(String text) {
        SpannableString spanText = new SpannableString("约" + text + "m³");
        spanText.setSpan(new AbsoluteSizeSpan(15, true), 0,
                1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(20, true), 1, text.length() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(15, true), text.length() + 1,
                spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    //设置字体样式
    private CharSequence getSpendSpanText(String text) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new AbsoluteSizeSpan(20, true), 0, text.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(15, true), text.length() - 1,
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanText;
    }


    @OnClick(R.id.record_back)
    public void onClick() {
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RecordLog");
        MobclickAgent.onResume(this);
        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RecordLog");
        MobclickAgent.onPause(this);
    }
}
