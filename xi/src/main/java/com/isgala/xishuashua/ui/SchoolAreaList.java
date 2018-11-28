package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.CheckPWD;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.Room;
import com.isgala.xishuashua.bean_.SchoolArea;
import com.isgala.xishuashua.config.AppManager;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.SureSchoolChoice;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.LogUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 选择校区
 * 注：需要外部传一个s_id
 * Created by and on 2016/11/7.
 */

public class SchoolAreaList extends BaseAutoActivity {

    @BindView(R.id.schoolarealist)
    ExpandableListView schoolarealist;
    private String TAG = "SchoolAreaList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolarea);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private int mExpandPosition = -1;

    private void initView() {
        schoolarealist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (schoolarealist.isGroupExpanded(groupPosition)) {
                    // 恢复展开位置的默认值
                    mExpandPosition = -1;
                    schoolarealist.collapseGroup(groupPosition);
                } else {
                    mExpandPosition = groupPosition;
                    // 记录展开的位置
                    schoolarealist.expandGroup(groupPosition);
                }
                return true;
            }
        });
        schoolarealist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<SchoolArea.SchoolData> list = schoolAreaAdapter.getList();
                SchoolArea.SchoolData schoolData = list.get(groupPosition);
                Room room = schoolData.room.get(childPosition);
                showDialog(schoolData.s_id, room.b_id, schoolData.name, room.name);
                return true;
            }
        });
    }

    /**
     * 弹出确认选择的提示框
     *
     * @param campus
     * @param b_id
     */
    private void showDialog(final String campus, final String b_id, String name, final String room) {
        SureSchoolChoice.Builder builder = new SureSchoolChoice.Builder(this);
        builder.setOnClickListener(new SureSchoolChoice.Listener() {
            @Override
            public void sure() {
                modify(campus, b_id, room);
            }
        });
        builder.create("已选择" + name, room).show();
    }

    /**
     * 修改校区
     *
     * @param campus
     * @param b_id
     */
    private void modify(final String campus, final String b_id, final String room) {
        LoadingAnimDialog.showLoadingAnimDialog(SchoolAreaList.this);
        HashMap<String, String> map = new HashMap<>();
        map.put("campus", campus);
        map.put("b_id", b_id);
        map.put("s_id", s_id);
        VolleySingleton.post(Neturl.MODIFY_BATHROOM, "modify_campus", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result bean = JsonUtils.parseJsonToBean(result, Result.class);
                if (bean != null && TextUtils.equals("1", bean.status)) {
                    SPUtils.saveString(Constants.S_ID, s_id);
                    SPUtils.saveString(Constants.CAMPUS, campus);
                    SPUtils.saveString("b_id", b_id);
                    SPUtils.saveString("bathroomLoaction", room);
                    checkAuthe();
                } else {
                    LoadingAnimDialog.dismissLoadingAnimDialog();
                }
            }
        });

    }

    //如果栈里不包含HomeActivity 便启动一个
    private void check() {
        if (!AppManager.INSTANCE.containActivity(HomeActivity.class)) {
            startActivity(new Intent(SchoolAreaList.this, HomeActivity.class));
        }
    }

    /**
     * 检查是否需要实名认证
     */
    private void checkAuthe() {
        VolleySingleton.post(Neturl.CHECK_LOCK_PWD, "checkauthe", new HashMap<String, String>(), new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                CheckPWD bean = JsonUtils.parseJsonToBean(result, CheckPWD.class);
                if (bean != null && bean.data != null) {
                    String device_type=bean.data.device_type;
                    SPUtils.saveString(Constants.DEVICE_TYPE,device_type);
                    if (TextUtils.equals("1", bean.data.need_auth) && TextUtils.equals("0", bean.data.auth.result)) {//需要认证，却没认证
                        Intent intent = new Intent(SchoolAreaList.this, AutheActivity.class);
                        startActivity(intent);
                    } else {
                        check();
                    }
                    //关闭选择校区界面
                    finish();
                    AppManager.INSTANCE.finishActivity(SchoolList.class);
                }
                LoadingAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    private SchoolExpandAdapter schoolAreaAdapter;

    private void updateList(List<SchoolArea.SchoolData> data) {
        schoolAreaAdapter = new SchoolExpandAdapter(data);
        schoolarealist.setAdapter(schoolAreaAdapter);
    }

    private String s_id;

    private void initData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        HashMap<String, String> map = new HashMap<>();
        s_id = getIntent().getStringExtra("s_id");
        LogUtils.e("s_id", s_id);
        if (TextUtils.isEmpty(s_id)) {
            s_id = "";
        }
        map.put("s_id", s_id);
        map.put(Constants.LOCATON_LAT, SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put(Constants.LOCATON_LNG, SPUtils.getString(Constants.LOCATON_LNG, ""));
        VolleySingleton.post(Neturl.GET_CAMPUS, "campius", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
//                LogUtils.e(TAG, result);
                SchoolArea schoolArea = JsonUtils.parseJsonToBean(result, SchoolArea.class);
                if (schoolArea != null && schoolArea.data != null) {
                    updateList(schoolArea.data);
//                    for (int i = 0; i < schoolArea.data.size(); i++) {
//                        SchoolArea.SchoolData schoolData = schoolArea.data.get(i);
//                        if (TextUtils.equals(schoolData.s_id, s_id)) {
//                            mExpandPosition = i;
//                            // 记录展开的位置
//                            schoolarealist.expandGroup(i);
//                            ToastUtils.show("groupPosition "+mExpandPosition);
//                            break;
//                        }
//                    }
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    @OnClick(R.id.schoolarea_back)
    public void onClick() {
        finish();
    }

    public static class SchoolExpandAdapter implements ExpandableListAdapter {
        public List<SchoolArea.SchoolData> getList() {
            return list;
        }

        private List<SchoolArea.SchoolData> list;

        public SchoolExpandAdapter(List<SchoolArea.SchoolData> data) {
            list = new ArrayList<>();
            list.addAll(data);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            SchoolArea.SchoolData schoolData = list.get(groupPosition);
            if (schoolData != null) {
                List<Room> room = schoolData.room;
                if (room != null && room.size() > 0)
                    return room.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_schoolarea, null);
            }
            TextView groupTextView = (TextView) convertView.findViewById(R.id.group_schoolarea);
            String name = list.get(groupPosition).name;
            groupTextView.setText(name);
            ImageView arrow = (ImageView) convertView.findViewById(R.id.expand_arrow);
            arrow.setImageResource(isExpanded ? R.mipmap.xuanzexiaoqu_shangla : R.mipmap.xuanzexiaoqu_xiala);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_schoolarea, null);
            }
            TextView groupTextView = (TextView) convertView.findViewById(R.id.child_schoolarea);
            Room room = list.get(groupPosition).room.get(childPosition);
            groupTextView.setText(room.name);
            ImageView line = (ImageView) convertView.findViewById(R.id.schoolarea_child_line);
            if (childPosition == getChildrenCount(groupPosition) - 1) {
                line.setPadding(0, 0, 0, 0);
            } else {
                line.setPadding(90, 0, 0, 0);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
//            LogUtils.e(TAG, "onGroupExpanded" + groupPosition);
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
//            LogUtils.e(TAG, "onGroupCollapsed" + groupPosition);
        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return childId;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return groupId;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SchoolAreaList");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SchoolAreaList");
        MobclickAgent.onPause(this);
    }
}
