package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.SchoolOrgListAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseAutoActivity;
import com.zhuochi.hydream.bathhousekeeper.base.OnItemClickListener;
import com.zhuochi.hydream.bathhousekeeper.base.ViewHolder;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.SchoolEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;

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
    private List<SchoolEntity.OrgAreaBean> entity;
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
        List<SchoolEntity.OrgAreaBean> org_area = (List<SchoolEntity.OrgAreaBean>) getIntent().getSerializableExtra("ORG_ID1");
        upDateList(org_area);
        params.addCallBack(this);
       // params.getOrgAreas(ORG_ID);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOrgAreas":
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    //entity = JSON.parseArray(JSON.toJSONString(jsonArray), SchoolAreaEntity.class);
                    upDateList(entity);
                }
                break;

        }
    }


    private void upDateList(List<SchoolEntity.OrgAreaBean> data) {
        if (adapter1 == null) {
            adapter1 = new SchoolOrgListAdapter(data, R.layout.item_school_list, this);
            schoolList1.setAdapter(adapter1);
        } else {
            adapter1.notifyDataSetChanged2(data);
        }
        adapter1.setOnItemClickListener(new OnItemClickListener<SchoolEntity.OrgAreaBean>() {
            @Override
            public void onItemClick(SchoolEntity.OrgAreaBean item, ViewHolder holder, int position) {
                Intent intent = new Intent(SchoolOrgList.this, SchoolAreaList.class);

                String area_id = String.valueOf(item.getOrg_area_id());
                intent.putExtra("ORG_AREA_ID", area_id);
                intent.putExtra("ORG_ID", getIntent().getStringExtra("ORG_ID"));
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
