package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.entity.CombinationEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.ListSchoolEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.ListSchoolOrgEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 获取校区
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ListSchoolOrgActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;

    XiRequestParams params;
    private ListAdapter adapter;
    private List<ListSchoolOrgEntity> entities;
    private int org_id = 0;
    private String org_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        toolbarTitle.setText("选择校区");
        getOrgSchool();
    }

    private void getOrgSchool() {
        org_id = getIntent().getIntExtra("org_id", 0);
        org_name = getIntent().getStringExtra("org_name");

        params.addCallBack(this);
        params.getOrgArea(UserUtils.getInstance(this).getUserID(), org_id);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOrgArea":
                JSONArray array = new JSONArray((ArrayList) result.getData().getData());
                if (array.size() > 0) {
                    entities = JSON.parseArray(JSON.toJSONString(array), ListSchoolOrgEntity.class);
                    if (adapter == null) {
                        adapter = new ListAdapter(this);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (Constant.REQUEST_CODE >= 3) {
                                Intent intent = new Intent(ListSchoolOrgActivity.this, ListSchoolOrgAreaActivity.class);
                                intent.putExtra("org_area_id", entities.get(position).getOrg_area_id());
                                intent.putExtra("org_id", org_id);
                                intent.putExtra("org_name", org_name);
                                intent.putExtra("org_area_name", entities.get(position).getOrg_area_name());
                                startActivityForResult(intent,104);
                            } else {
                                //返回  学校  校区id  name
                                Intent intent = getIntent();
                                CombinationEntity entity = new CombinationEntity();
                                entity.setSchoolID(entities.get(position).getOrg_id());
                                entity.setSchoolName(org_name);
                                entity.setSchoolOrgId(entities.get(position).getOrg_area_id());
                                entity.setSchoolOrgName(entities.get(position).getOrg_area_name());
                                entity.setSchoolAreaId(0);
                                entity.setSchoolAreaName("");
                                entity.setSchoolBathID("");
                                entity.setSchoolBathName("");
                                intent.putExtra("CombinationEntity", new Gson().toJson(entity));
                                setResult(103, intent);
                                finish();
                            }
                        }
                    });
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 104 && data != null) {
            Intent intent=getIntent();
            intent.putExtra("CombinationEntity",data.getStringExtra("CombinationEntity"));
            setResult(103,intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @OnClick({R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

        }
    }

    private class ListAdapter extends BaseAdapter {

        private Context mcontext;

        private ListAdapter(Context context) {
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
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tv_name.setText(entities.get(position).getOrg_area_name());

            return convertView;
        }


        class ViewHolder {
            TextView tv_name;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);


            }
        }
    }
}
