package com.zhuochi.hydream.bathhousekeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zhuochi.hydream.bathhousekeeper.entity.ListSchoolOrgAreaBathEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.ListSchoolOrgAreaEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 获取校区浴室中 浴卫
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ListSchoolOrgAreaBathActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;

    XiRequestParams params;
    private ListAdapter adapter;
    private List<ListSchoolOrgAreaBathEntity> entities;
    private int org_id = 0;
    private int org_area_id = 0;
    private int device_area_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        toolbarTitle.setText("选择校区浴室浴卫");
        getOrgSchool();
    }

    private void getOrgSchool() {
         org_id = getIntent().getIntExtra("org_id", 0);
         org_area_id = getIntent().getIntExtra("org_area_id", 0);
         device_area_id = getIntent().getIntExtra("device_area_id", 0);


        params.addCallBack(this);
        params.getBathPosition(UserUtils.getInstance(this).getUserID(), org_id, org_area_id, device_area_id, "");
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getBathPosition":
                JSONArray array = new JSONArray((ArrayList) result.getData().getData());
                if (array.size() > 0) {
                    entities = JSON.parseArray(JSON.toJSONString(array), ListSchoolOrgAreaBathEntity.class);
                    if (adapter == null) {
                        adapter = new ListAdapter(this);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    ToastUtils.show("暂无数据！");
                }
                break;
        }
        super.onRequestSuccess(tag, result);
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
            holder.tv_name.setText(entities.get(position).getDevice_name());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(ListSchoolOrgAreaBathActivity.this,)
                    //返回  学校  校区  浴室  浴位id  name
                    ToastUtils.show("选择学校校区浴室浴卫返回！！");
                    String org_name = getIntent().getStringExtra("org_name");
                    String org_area_name = getIntent().getStringExtra("org_area_name");
                    String device_area_name = getIntent().getStringExtra("device_area_name");
                    Intent intent = getIntent();
                    CombinationEntity entity = new CombinationEntity();
                    entity.setSchoolID(org_id);
                    entity.setSchoolName(org_name);
                    entity.setSchoolOrgId(org_area_id);
                    entity.setSchoolOrgName(org_area_name);
                    entity.setSchoolAreaId(device_area_id);
                    entity.setSchoolAreaName(device_area_name);
                    entity.setSchoolBathID(entities.get(position).getDevice_key());
                    entity.setSchoolBathName(entities.get(position).getDevice_name());
                    intent.putExtra("CombinationEntity", new Gson().toJson(entity));
                    setResult(105, intent);
                    finish();
                }
            });
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
