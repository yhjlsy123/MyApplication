package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.entity.FeedbackHistoryEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈对话界面
 */
public class FeedbackHistoryListActivity extends BaseActivity {


    @BindView(R.id.list_item)
    ListView listItem;
    @BindView(R.id.line_addfeedback)
    LinearLayout lineAddfeedback;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;


    private XiRequestParams params;
    private List<FeedbackHistoryEntity> entities;
    private MyFeedBackAdater adater;
    private String id = "";
    private String typeid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_history_list);
//        highApiEffects();
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initData();
        feedbackDetailList();
    }

    private  void  initData(){
        toolbarTitle.setText("反馈对话");
    }
    //获取反馈对话
    private void feedbackDetailList() {

        id = getIntent().getStringExtra("id");
        typeid = getIntent().getStringExtra("typeid");

        params.addCallBack(this);
        params.feedbackDetailList(UserUtils.getInstance(this).getUserID(), id);
    }
    @Override
    protected void onResume() {
        if (id!=null&& !TextUtils.isEmpty(id)){
            params.addCallBack(this);
            params.feedbackDetailList(UserUtils.getInstance(this).getUserID(), id);
        }
        super.onResume();
    }

    @OnClick({R.id.toolbar_back, R.id.line_addfeedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.line_addfeedback:
                Intent intent = new Intent(this, FeedBackHistoryActivity.class);
                intent.putExtra("FeedbackType", typeid);
                intent.putExtra("FeedbackID", id);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "feedbackDetailList":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                entities = JSON.parseArray(JSON.toJSONString(jsonArray), FeedbackHistoryEntity.class);
                if (jsonArray.size() > 0) {
                    if (adater == null) {
                        adater = new MyFeedBackAdater(this);
                        listItem.setAdapter(adater);
                    } else {
                        adater.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(result.getData().getMsg());
                }

                break;
        }
    }

    private class MyFeedBackAdater extends BaseAdapter {

        private Context mcontext;

        private MyFeedBackAdater(Context context) {
            mcontext = context;
        }

        @Override
        public int getCount() {
            return entities.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_feedbackhistory, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            if (entities.get(position).getSource_type().equals("user")) {
                holder.history_feedback_system.setText(entities.get(position).getContent());
                holder.history_content.setVisibility(View.GONE);
                holder.item_history_userphoto.setVisibility(View.GONE);
                holder.line_admin.setVisibility(View.GONE);
            } else if (entities.get(position).getSource_type().equals("admin")) {
                holder.history_content.setText(entities.get(position).getContent());
                holder.history_feedback_system.setVisibility(View.GONE);
                holder.roundedImageView.setVisibility(View.GONE);
                holder.line_user.setVisibility(View.GONE);
            }

            return convertView;
        }


        class ViewHolder {
            TextView history_feedback_system;
            TextView history_content;
            RoundedImageView roundedImageView;
            RoundedImageView item_history_userphoto;
            RelativeLayout line_admin;
            RelativeLayout line_user;

            public ViewHolder(View view) {
                history_feedback_system = (TextView) view.findViewById(R.id.item_history_feedback_system);
                history_content = (TextView) view.findViewById(R.id.item_history_content);
                roundedImageView = (RoundedImageView) view.findViewById(R.id.item_history_systemphoto);
                item_history_userphoto = (RoundedImageView) view.findViewById(R.id.item_history_userphoto);
                line_admin = (RelativeLayout) view.findViewById(R.id.line_admin);
                line_user = (RelativeLayout) view.findViewById(R.id.line_user);

            }
        }
    }
}
