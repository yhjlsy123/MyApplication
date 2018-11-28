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
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Constant;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SelectedList;
import com.zhuochi.hydream.bathhousekeeper.utils.SelectedListUser;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 获取学校
 *
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ListSchoolActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;

    XiRequestParams params;
    private ListAdapter adapter;
    private List<ListSchoolEntity> entities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);
        toolbarTitle.setText("选择学校");

        getSchool();
    }

    private void getSchool() {
        params.addCallBack(this);
        params.getOrg(UserUtils.getInstance(this).getUserID());

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getOrg":
                JSONArray array = new JSONArray((ArrayList) result.getData().getData());
                if (array.size() > 0) {
                    entities = JSON.parseArray(JSON.toJSONString(array), ListSchoolEntity.class);
                    if (adapter == null) {
                        adapter = new ListAdapter(this);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (Constant.REQUEST_CODE >= 2) {
                            Intent intent = new Intent(ListSchoolActivity.this, ListSchoolOrgActivity.class);
                            intent.putExtra("org_id", entities.get(position).getOrg_id());
                            intent.putExtra("org_name", entities.get(position).getOrg_name());

                            startActivityForResult(intent,103);
                        } else {
                            Intent intent=getIntent();
                            CombinationEntity entity=new CombinationEntity();
                            entity.setSchoolID(entities.get(position).getOrg_id());
                            entity.setSchoolName(entities.get(position).getOrg_name());
                            entity.setSchoolOrgId(0);
                            entity.setSchoolOrgName("");
                            entity.setSchoolAreaId(0);
                            entity.setSchoolAreaName("");
                            entity.setSchoolBathID("");
                            entity.setSchoolBathName("");
                            intent.putExtra("CombinationEntity",new Gson().toJson(entity));
                            setResult(102,intent);
                            finish();

                        }

                    }
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 103 && data != null) {
//            String entity = data.getStringExtra("CombinationEntity");
            Intent intent=getIntent();
            intent.putExtra("CombinationEntity",data.getStringExtra("CombinationEntity"));
            setResult(102,intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ListAdapter extends BaseAdapter {

        private Context mcontext;
        private SelectedListUser listInterface;

        private ListAdapter(Context context) {
            mcontext = context;
            listInterface = new SelectedListUser();
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
            holder.tv_name.setText(entities.get(position).getOrg_name());

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
