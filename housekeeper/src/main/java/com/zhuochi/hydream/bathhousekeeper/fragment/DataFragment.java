package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.PagerAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*数据*/

public class DataFragment extends BaseFragment {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.mtablelayouts)
    TabLayout mtablelayouts;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    Unbinder unbinder;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;

    List<String> listtitle;/*标题*/
    List<Fragment> listfragment;/*fragment*/
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_data, container, false);
            unbinder = ButterKnife.bind(this, view);
            initData();
        }
        return view;
    }

    private void initData() {
        toolbarBack.setVisibility(View.GONE);
        toolbarTitle.setText("数据");
        // toolbarMenuTv.setVisibility(View.VISIBLE);
        // toolbarMenuTv.setText("流水");

        getListNames();

    }

    //获取数据名称列表
    private void getListNames() {
        //定义 title 集合 来存储  解析的data数据
        listtitle = new ArrayList<>();
        //创建集合 循环添加创建的Fragment
        listfragment = new ArrayList<>();

        //遍历 listtitle 集合 将title 添加经 TabLayou z中
        mtablelayouts.removeAllTabs();
        String[] titles = {"排行榜", "人均排行", "洗浴高峰", "比率"};
        for (int i = 0; i < 4; i++) {
            mtablelayouts.addTab(mtablelayouts.newTab().setText(titles[i]));/*获取类型名称*/
            listtitle.add(titles[i]);
            /*RankFragment rankFragment = new RankFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("getRankingType", JSON.toJSONString(new Object()));*//*传入排行榜类型*//*
            rankFragment.setArguments(bundle);
            listfragment.add(rankFragment);*/

            DataFragmentOne fragmentOne = new DataFragmentOne();
            Bundle bundle = new Bundle();
            bundle.putSerializable("reqCode", i);//*reqCode*//*
            fragmentOne.setArguments(bundle);
            listfragment.add(fragmentOne);
        }
        getVpTitleData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //渲染viewpager+fragment
    public void getVpTitleData() {

        // TabVPFragmentAdapter mAdapter = new TabVPFragmentAdapter(getActivity().getSupportFragmentManager(), listfragment, listtitle);
        PagerAdapter mAdapter = new PagerAdapter(getChildFragmentManager(), listfragment, listtitle);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来。
        mtablelayouts.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mtablelayouts.setTabsFromPagerAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(listfragment.size() - 1);
    }


}
