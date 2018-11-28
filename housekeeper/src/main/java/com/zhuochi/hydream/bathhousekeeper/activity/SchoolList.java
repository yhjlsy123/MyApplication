package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.SchoolListAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseAutoActivity;
import com.zhuochi.hydream.bathhousekeeper.base.OnItemClickListener;
import com.zhuochi.hydream.bathhousekeeper.base.ViewHolder;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SchoolEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 学校信息
 * Created by and on 2016/11/7.
 */

public class SchoolList extends BaseAutoActivity {
    @BindView(R.id.choice_schoolarea_title)
    TextView choiceSchoolareaTitle;
    private String TAG = "SchoolList";
    @BindView(R.id.school_list1)
    ListView schoolList1;

    private SchoolListAdapter adapter1;
    private XiRequestParams params;
    private String org_type = "default";
    private List<SchoolEntity> entity;
    private String jumpType = "";
    ImageView userinfo_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoollist);
        ButterKnife.bind(this);
        choiceSchoolareaTitle.setText("选择学校");
        params = new XiRequestParams(this);
        String from = getIntent().getStringExtra("from");
        jumpType = getIntent().getStringExtra("Jump");

//        if (TextUtils.equals(from, "UserEntity")) {//来自用户信息页
//            backView.setVisibility(View.VISIBLE);
//            backView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
        getOrgs();
//        initData();
    }

    private void getOrgs() {
        params.addCallBack(this);
        params.getSchoolList(SPUtils.getInt(Constants.USER_ID,0));
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "OrgetOrgLists":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolEntity.class);
                    upDateList(entity);
                }
                break;
            case "deviceSelector":
                ToastUtils.show(result.getData().getMsg());
                break;
        }
    }

    private void upDateList(List<SchoolEntity> data) {
        if (adapter1 == null) {
            adapter1 = new SchoolListAdapter(data, R.layout.item_school_list, this);
            schoolList1.setAdapter(adapter1);
        } else {
            adapter1.notifyDataSetChanged2(data);
        }
        adapter1.setOnItemClickListener(new OnItemClickListener<SchoolEntity>() {
            @Override
            public void onItemClick(SchoolEntity item, ViewHolder holder, int position) {
//                    modify(item.s_id);
                Intent intent = new Intent(SchoolList.this, SchoolOrgList.class);
                String ORG_ID = String.valueOf(item.getOrg_id());
                intent.putExtra("ORG_ID", ORG_ID);
                intent.putExtra("dataList", (Serializable)  item.getOrg_area());
                SPUtils.saveInt(Constants.ORG_ID, item.getOrg_id());
                startActivity(intent);
            }
        });
    }

    /**
     * 提交学校的修改
     *
     * @param s_id
     */
    private void modify(final String s_id) {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("s_id", s_id);

    }

    @Override
    public void onBackPressed() {
        if (!jumpType.isEmpty()) {
            finish();
            return;
        }
//        if (backView.getVisibility() == View.VISIBLE) {
//            finish();
//        } else {
//            //返回桌面操作,类似按下HOME键,但是不退出app
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//        }
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.userinfo_cancle)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
        }
    }
}
