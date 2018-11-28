package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.PagerAdapter;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.fragment.OrderFlowFragment;
import com.zhuochi.hydream.bathhousekeeper.http.ResponseListener;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderFlowActivity extends FragmentActivity implements ResponseListener {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.mtablelayouts)
    TabLayout mtablelayouts;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    Unbinder unbinder;

    List<String> listtitle;/*标题*/
    List<Fragment> listfragment;/*fragment*/
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_flow);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.blue_system), 0);
        unbinder = ButterKnife.bind(this);
        params = new XiRequestParams(this);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
        toolbarTitle.setText("流水数据");
        toolbarMenuTv.setVisibility(View.VISIBLE);
        // toolbarMenuTv.setText("订单");
        getListNames();

    }

    //获取数据名称列表
    private void getListNames() {
        params.addCallBack(this);
        //此处没接口  记得更换
        params.getDeviceType(SPUtils.getInt(Constants.USER_ID, 0));
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
       /* Map map = (Map)result.getData().getData();
        ArrayList<String> titlesList = new ArrayList();
        ArrayList<String> typesList = new ArrayList();
        //String[] titles = {"洗浴", "吹风机", "洗衣机"};
        //String[] types = {"faucet", "blower", "washer"};
        Set<Map.Entry<String,String>> entry = map.entrySet();
        Iterator<Map.Entry<String,String>> ite = entry.iterator();
        while(ite.hasNext())
        {
            Map.Entry<String,String> en = ite.next();
            String key = en.getKey();
            typesList.add(key);
            String value = en.getValue();
            titlesList.add(value);
        }*/

        ArrayList<String> titlesList = new ArrayList();
        ArrayList<String> typesList = new ArrayList();
        JSONArray jsonArray = new JSONArray((List) result.getData().getData());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            typesList.add(obj.getString("name"));
            titlesList.add(obj.getString("name_text"));
        }
        //定义 title 集合 来存储  解析的data数据
        listtitle = new ArrayList<>();
        //创建集合 循环添加创建的Fragment
        listfragment = new ArrayList<>();

        //遍历 listtitle 集合 将title 添加经 TabLayou z中
        mtablelayouts.removeAllTabs();
        //String[] titles = {"洗浴", "吹风机", "洗衣机"};
        for (int i = 0; i < jsonArray.size(); i++) {
            mtablelayouts.addTab(mtablelayouts.newTab().setText(titlesList.get(i)));/*获取类型名称*/
            listtitle.add(titlesList.get(i));

            OrderFlowFragment orderFlowFragment = new OrderFlowFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", typesList.get(i));//*传入请求类型的*//*
            orderFlowFragment.setArguments(bundle);
            listfragment.add(orderFlowFragment);

        }
        getVpTitleData();
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

    @Override
    public void onRequestFailure(String tag, Object s) {

    }


    @OnClick(R.id.toolbar_menu_tv)
    public void onViewClicked() {
        //startActivity(new Intent(getActivity(),OrderFlowActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu_tv:
                break;
        }
    }
}
