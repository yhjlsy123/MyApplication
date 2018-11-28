package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.SchoolOrgListAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.base.OnItemClickListener;
import com.zhuochi.hydream.base.ViewHolder;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.entity.SchoolAreaEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 校区信息
 * Created by and on 2016/11/7.
 */

public class SchoolOrgList extends BaseAutoActivity {
    @BindView(R.id.choice_schoolarea_title)
    TextView choiceSchoolareaTitle;
    private String TAG = "SchoolList";
    @BindView(R.id.school_list1)
    ListView schoolList1;
    //    @BindView(schoollist_back)
//    View backView;
    private SchoolOrgListAdapter adapter1;
    private XiRequestParams params;
    private String org_type = "default";
    private List<SchoolAreaEntity> entity;
    ImageView userinfo_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoollist);
        ButterKnife.bind(this);
        choiceSchoolareaTitle.setText("选择校区");
        params = new XiRequestParams(this);
//        String from = getIntent().getStringExtra("from");
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
        String ORG_ID = getIntent().getStringExtra("ORG_ID");
        params.addCallBack(this);
        params.getOrgAreas(ORG_ID);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOrgAreas":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolAreaEntity.class);
                    upDateList(entity);
                }
                break;

        }
    }


    private void upDateList(List<SchoolAreaEntity> data) {
        if (adapter1 == null) {
            adapter1 = new SchoolOrgListAdapter(data, R.layout.item_school_list, this);
            schoolList1.setAdapter(adapter1);
        } else {
            adapter1.notifyDataSetChanged2(data);
        }
        adapter1.setOnItemClickListener(new OnItemClickListener<SchoolAreaEntity>() {
            @Override
            public void onItemClick(SchoolAreaEntity item, ViewHolder holder, int position) {
                Intent intent = new Intent(SchoolOrgList.this, SchoolAreaList.class);

                String area_id = String.valueOf(item.getOrg_area_id());
                String org_name = getIntent().getStringExtra("ORG_NAME");
                String schoolNameOrg = org_name + item.getOrg_area_name();
                intent.putExtra("ORG_AREA_ID", area_id);
                intent.putExtra("ORG_ID", getIntent().getStringExtra("ORG_ID"));
                intent.putExtra("SchoolNameOrg", schoolNameOrg);

                SPUtils.saveInt(Constants.ORG_ID, item.getOrg_area_id());
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
