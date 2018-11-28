package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alipay.android.phone.mrpc.core.NetworkUtils;
import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.MessageAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.Message;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.NetworkUtil;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.Tools;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.pulltorefresh2.RefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isgala.xishuashua.view.pulltorefresh2.RefreshListView.State;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.list;

/**
 * Created by and on 2016/11/8.
 */

public class MessageActivity extends BaseAutoActivity implements OnItemClickListener<Message.MessageItem> {
    @BindView(R.id.message_listview)
    RefreshListView mListView;
    @BindView(R.id.no_messgae)
    View noMessage;
    private MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private HashMap<String, String> map;
    private int totalPage = 1;
    private int page;
    private State mCurrentState;

    private void initView() {
        map = new HashMap<>();
        map.put("limit", "10");
        page = 1;
        totalPage = 1;
        mCurrentState = State.NONE;
        mListView.setXListViewListener(new RefreshListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if (mCurrentState == State.NONE) {
                    mCurrentState = State.PULL;
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
                    if (totalPage > page) {
                        page++;
                        map.put("page", page + "");
                        mCurrentState = State.PUSH;
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
        mCurrentState = State.NONE;
    }


    /**
     * 刷新完成
     */
    private void finishLoad() {
        mListView.finishRefresh();
        mCurrentState = State.NONE;
    }

    private void loadData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        VolleySingleton.post(Neturl.MESSAGE_LIST, "get_message", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                SPUtils.saveBoolean("new_message", false);
                Message message = JsonUtils.parseJsonToBean(result, Message.class);
                if (message != null && message.data != null) {
                    totalPage = Tools.toInt(message.data.totalPage);
                    List<Message.MessageItem> list = message.data.result;
                    if (mCurrentState == State.NONE || mCurrentState == State.PULL) {
                        if (list == null || list.size() < 1) {//没有数据时
                            noMessage.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {//有数据
                            if (messageAdapter == null) {
                                messageAdapter = new MessageAdapter(list, R.layout.item_message, MessageActivity.this);
                                mListView.setAdapter(messageAdapter);
                                messageAdapter.setOnItemClickListener(MessageActivity.this);
                            } else {
                                messageAdapter.notifyDataSetChanged2(list);
                            }
                        }
                    } else {
                        messageAdapter.notifyDataSetChanged(list);
                    }
                    if (list == null || list.size() < 10) {// 当条数小于
                        // 不可以上拉加载更多
                        mListView.setPullLoadEnable(false);
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MessageActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MessageActivity");
        MobclickAgent.onPause(this);
    }

    private void upDateList(ArrayList<Message.MessageItem> data) {
        if (data.size() > 0) {
            noMessage.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        if (messageAdapter == null) {
            messageAdapter = new MessageAdapter(data, R.layout.item_message, null);
            mListView.setAdapter(messageAdapter);
            messageAdapter.setOnItemClickListener(this);
        } else {
            messageAdapter.notifyDataSetChanged(data);
        }
    }

    @OnClick(R.id.message_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(Message.MessageItem item, ViewHolder holder, int position) {
        Intent intent = new Intent(this, H5Activity.class);
        intent.putExtra("title", "消息详情");
        intent.putExtra("url", item.href);
        startActivity(intent);
    }
}
