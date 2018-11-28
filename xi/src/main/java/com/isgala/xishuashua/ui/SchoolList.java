package com.isgala.xishuashua.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.adapter.SchoolListAdapter;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.base.OnItemClickListener;
import com.isgala.xishuashua.base.ViewHolder;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.School;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.TipDialog;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.isgala.xishuashua.R.id.schoollist_back;

/**
 * 学校信息
 * Created by and on 2016/11/7.
 */

public class SchoolList extends BaseAutoActivity {
    private String TAG = "SchoolList";
    @BindView(R.id.school_list1)
    ListView schoolList1;
    @BindView(schoollist_back)
    View backView;
    private SchoolListAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoollist);
        ButterKnife.bind(this);
        String from = getIntent().getStringExtra("from");
        if (TextUtils.equals(from, "UserInfoEntity")) {//来自用户信息页
            backView.setVisibility(View.VISIBLE);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initData();
    }


    private void initData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.LOCATON_LAT, SPUtils.getString(Constants.LOCATON_LAT, ""));
        map.put(Constants.LOCATON_LNG, SPUtils.getString(Constants.LOCATON_LNG, ""));
        VolleySingleton.post(Neturl.GET_SCHOOL, "get_school", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                School school_ = JsonUtils.parseJsonToBean2(result, School.class);
                if (school_ != null) {
                    if (TextUtils.equals("112011473", school_.status)) {
                        Dialog dialog = TipDialog.show_(SchoolList.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "提示信息", school_.msg);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                SPUtils.saveString(Constants.S_ID, "");
                                SPUtils.saveString(Constants.OAUTH_TOKEN, "");
                                SPUtils.saveString(Constants.OAUTH_TOKEN_SECRET, "");
                                SPUtils.saveString(Constants.CAMPUS, "");
                                SPUtils.saveBoolean(Constants.IS_LOGIN, false);
                                finish();
                            }
                        });
                        return;
                    }
                }
                School school = JsonUtils.parseJsonToBean(result, School.class);
                if (school != null && school.data != null) {
                    if (school.data.size() > 0) {
                        upDateList(school.data);
                    }
                }
            }
        });
    }

    /**
     * 检查附近是否有学校
     *
     * @param json
     */
    private void checkLocation(String json) {

    }

    private void upDateList(ArrayList<School.SchoolItem> data) {
        if (adapter1 == null) {
            adapter1 = new SchoolListAdapter(data, R.layout.item_school_list, this);
            schoolList1.setAdapter(adapter1);
        } else {
            adapter1.notifyDataSetChanged2(data);
        }
        adapter1.setOnItemClickListener(new OnItemClickListener<School.SchoolItem>() {
            @Override
            public void onItemClick(School.SchoolItem item, ViewHolder holder, int position) {
//                    modify(item.s_id);
                Intent intent = new Intent(SchoolList.this, SchoolAreaList.class);
                intent.putExtra("s_id", item.s_id);
                SPUtils.saveString(Constants.S_ID, item.s_id);
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
        VolleySingleton.post(Neturl.MODIFY_SCHOOL, "modify_school", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result bean = JsonUtils.parseJsonToBean(result, Result.class);
                if (bean != null && bean.data != null) {
                    Intent intent = new Intent(SchoolList.this, SchoolAreaList.class);
                    intent.putExtra("s_id", s_id);
                    SPUtils.saveString(Constants.S_ID, s_id);
                    startActivity(intent);
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backView.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            //返回桌面操作,类似按下HOME键,但是不退出app
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SchoolList");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SchoolList");
        MobclickAgent.onPause(this);
    }
}
