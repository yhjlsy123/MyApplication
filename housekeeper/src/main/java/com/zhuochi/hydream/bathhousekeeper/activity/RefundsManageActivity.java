package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.PagerAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.fragment.RefundsManageFragment;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 退款管理
 */
public class RefundsManageActivity extends BaseActivity implements HeightSelectView.SelcetCallBack {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.mtablelayouts)
    TabLayout mtablelayouts;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    Unbinder unbinder;

    List<String> listtitle;/*标题*/
    List<Fragment> listfragment;/*fragment*/
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    private HeightSelectView mHeightSelectView;
    private int org_id;
    private int org_area_id;
    private String startTime;
    private String endTime;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refunds_manage);
        ButterKnife.bind(this);
        initView();
        getListNames();
    }

    private void initView() {
        toolbarTitle.setText("退款管理");
        mHeightSelectView = new HeightSelectView(this, this, 1, getResources().getString(R.string.select_school_campus));
        headViewContaner.addView(mHeightSelectView);

    }

    //获取数据名称列表
    private void getListNames() {
        //定义 title 集合 来存储  解析的data数据
        listtitle = new ArrayList<>();
        //创建集合 循环添加创建的Fragment
        listfragment = new ArrayList<>();

        //遍历 listtitle 集合 将title 添加经 TabLayou z中
        mtablelayouts.removeAllTabs();
        String[] titles = {"全部", "未处理", "退款失败", "已退款", "已取消"};
        int[] requestCodes = {0, 1, 4, 2, 5};
        for (int i = 0; i < 5; i++) {
            mtablelayouts.addTab(mtablelayouts.newTab().setText(titles[i]));/*获取类型名称*/
            listtitle.add(titles[i]);
            RefundsManageFragment fragment = new RefundsManageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("status", requestCodes[i]);//*传入请求的数据类型*//*
            fragment.setArguments(bundle);
            listfragment.add(fragment);
        }
        getVpTitleData();
    }

    private int currentPage = 0;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //渲染viewpager+fragment
    public void getVpTitleData() {

        PagerAdapter mAdapter = new PagerAdapter(getSupportFragmentManager(), listfragment, listtitle);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来。
        mtablelayouts.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mtablelayouts.setTabsFromPagerAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(listfragment.size() - 1);

    }


    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        startTime = StartTime;
        endTime = EndTime;

        ((RefundsManageFragment) listfragment.get(currentPage)).setSelectionAndSearch(org_id, org_area_id, boothroom_id, StartTime, EndTime);
    }
}
