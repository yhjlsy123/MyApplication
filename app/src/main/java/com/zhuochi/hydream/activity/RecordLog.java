package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.RankVPFragmentAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.entity.DeviceTypeEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.fragment.RecordLogFragment;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.utils.Common.REFRESH_STATE;

/**
 * 消费记录日志
 * Created by and on 2016/11/14.
 */

public class RecordLog extends BaseAutoActivity {
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mtablelayouts)
    TabLayout mTabLayout;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.dafault)
    View dafault;

    List<String> listtitle;//标题集合
    List<Fragment> listfragment;//fragemnt集合
    @BindView(R.id.img_problem)
    ImageView imgProblem;

    private XiRequestParams params;
    List<DeviceTypeEntity> entityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        params = new XiRequestParams(this);
        ButterKnife.bind(this);
        textTitle.setText("消费记录");
        imgProblem.setVisibility(View.GONE);
        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            lin.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        } else {
            getDeviceTypeByOrgAreaId();/*获取消费记录类型*/
        }

    }

    //    获取消费类型
    private void getDeviceTypeByOrgAreaId() {
        if (params == null) {
            params = new XiRequestParams(this);
        }
        int OrgAreaID = UserUtils.getInstance(this).getOrgAreaID();
        params.addCallBack(this);
        params.getDeviceTypeByOrgAreaId(OrgAreaID);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "selectDeviceTypeByOrgAreaId"://获取消费类型
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    entityList = JSON.parseArray(JSON.toJSONString(jsonArray), DeviceTypeEntity.class);
                    DeviceTypeEntity deviceTypeEntity = new DeviceTypeEntity();
                    deviceTypeEntity.setDevice_type_name("全部记录");
                    entityList.add(0, deviceTypeEntity);
                    initData();
                }
                break;
        }
        super.onRequestSuccess(tag, result);
    }

    private void initData() {
        listtitle = new ArrayList<>();
        listfragment = new ArrayList<>();
        mTabLayout.removeAllTabs();
        for (DeviceTypeEntity deviceTypeEntity : entityList) {
            mTabLayout.addTab(mTabLayout.newTab().setText(deviceTypeEntity.getDevice_type_name()));/*获取类型名称*/
            listtitle.add(deviceTypeEntity.getDevice_type_name());
            RecordLogFragment recordLogFragment = new RecordLogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("deviceTypeEntity", JSON.toJSONString(deviceTypeEntity));/*传入获取消费类型*/
            recordLogFragment.setArguments(bundle);
            if (!listfragment.contains(recordLogFragment)) {
                listfragment.add(recordLogFragment);
            }
        }
        getVpTitleData();

    }

    //    渲染viewpager+fragment
    public void getVpTitleData() {
        RankVPFragmentAdapter mAdapter = new RankVPFragmentAdapter(getSupportFragmentManager(), listfragment, listtitle);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(listfragment.size() - 1);

    }

    @OnClick(R.id.rank_back)
    public void onClick() {
        finish();
    }

    public void onResume() {
        if (REFRESH_STATE == 1) {
            REFRESH_STATE = 0;
            getDeviceTypeByOrgAreaId();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
