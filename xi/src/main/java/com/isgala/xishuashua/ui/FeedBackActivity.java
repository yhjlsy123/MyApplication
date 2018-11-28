package com.isgala.xishuashua.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.FeedBackHistoryAdapter;
import com.isgala.xishuashua.adapter.FeedBackTypeAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.FeedBackHistoryBean;
import com.isgala.xishuashua.bean_.FeedBackHistoryData;
import com.isgala.xishuashua.bean_.FeedBackHistoryItemBean;
import com.isgala.xishuashua.bean_.FeedBackTypeItemBean;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by and on 2016/11/23.
 */
public class FeedBackActivity extends BaseAutoActivity implements TextWatcher {
    @BindView(R.id.feedback_input)
    EditText feedbackInput;
    @BindView(R.id.feedback_tip)
    TextView feedbackTip;
    @BindView(R.id.feedback_history_listview)
    ListView feedbackListView;
    @BindView(R.id.feedback_type_gridview)
    GridView feedbackTypeGridView;
    @BindView(R.id.feedback_history)//历史记录的根部局
            View feedbackHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        feedbackInput.addTextChangedListener(this);
        loadHistory();
    }

    /**
     * 请求历史记录的数据
     */
    private void loadHistory() {
        VolleySingleton.post(Neturl.SUGGEST_HISTORY, "suggesst_history", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                FeedBackHistoryBean feedBackHistoryBean = JsonUtils.parseJsonToBean(result, FeedBackHistoryBean.class);
                if (feedBackHistoryBean != null) {
                    if (TextUtils.equals("1", feedBackHistoryBean.status)) {
                        FeedBackHistoryData data = feedBackHistoryBean.data;
                        if (data != null) {
                            updateList(data.list);
                            updateType(data.type_list);
                            if (data.type_list != null && data.type_list.size() > 0) {
                                feedbackTypeGridView.setVisibility(View.VISIBLE);
                            } else {
                                feedbackTypeGridView.setVisibility(View.GONE);
                            }
                            if (data.list != null && data.list.size() > 0) {
                                feedbackHistory.setVisibility(View.VISIBLE);
                            } else {
                                feedbackHistory.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        ToastUtils.show(feedBackHistoryBean.msg);
                    }
                }
            }
        }, null);
    }

    private FeedBackHistoryAdapter feedBackHistoryAdapter;

    /**
     * 意见反馈
     *
     * @param list
     */
    private void updateList(ArrayList<FeedBackHistoryItemBean> list) {
        if (feedBackHistoryAdapter == null) {
            feedBackHistoryAdapter = new FeedBackHistoryAdapter(this, R.layout.item_feedbackhistory, list);
            feedbackListView.setAdapter(feedBackHistoryAdapter);
        } else {
            feedBackHistoryAdapter.notifyDataSetChanged2(list);
        }
    }

    private FeedBackTypeAdapter feedBackTypeAdapter;

    /**
     * 意见反馈的类型
     */
    private void updateType(List<FeedBackTypeItemBean> list) {
        if (feedBackTypeAdapter == null) {
            feedBackTypeAdapter = new FeedBackTypeAdapter(list, R.layout.item_feedback_type, this);
            feedbackTypeGridView.setAdapter(feedBackTypeAdapter);
        } else {
            feedBackTypeAdapter.notifyDataSetChanged2(list);
        }
        feedBackTypeAdapter.setOnItemClickListener(new OnItemClickListener<FeedBackTypeItemBean>() {
            @Override
            public void onItemClick(FeedBackTypeItemBean item, ViewHolder holder, int position) {
                type = item.id;
                feedBackTypeAdapter.setPosition(position);
            }
        });
    }

    /**
     * 记录意见反馈的类型
     */
    private String type;

    @OnClick({R.id.feedback_back, R.id.feedback_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_back:
                finish();
                break;
            case R.id.feedback_commit:
                commit();
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void commit() {
        String content = feedbackInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show("请填写您的宝贵意见");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("content", content);
        if (!TextUtils.isEmpty(type))
            map.put("type", type);
        VolleySingleton.post(Neturl.SUB_SUGGEST, "feedback", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null) {
                    if (TextUtils.equals("1", result1.status)) {
                        ToastUtils.show(result1.data.msg);
                        feedbackInput.setText("");
                        loadHistory();
                    } else {
                        ToastUtils.show(result1.msg);
                    }
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        feedbackTip.setText("还可以输入" + (300 - s.length() + "个字符"));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
