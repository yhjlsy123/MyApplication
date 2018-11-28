package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.PagerAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.fragment.FeedBackListFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.HomeFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.OrderListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 反馈对话
 */
public class FeedbackManageActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private List<String> tabIndicators = Arrays.asList(new String[]{"未回复", "已回复"});
    private ArrayList<Fragment> tabFragments;
    private PagerAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback__manage);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        toolbarTitle.setText("意见反馈管理");
        tabFragments = new ArrayList<>();
        for (String s : tabIndicators) {
            tlTab.addTab(tlTab.newTab().setText(s));/*获取类型名称*/
            switch (s) {
                case "未回复":
                    tabFragments.add(FeedBackListFragment.newInstance("no", null));
                    break;
                case "已回复":
                    tabFragments.add(FeedBackListFragment.newInstance("yes", null));
                    break;
            }

        }

        contentAdapter = new PagerAdapter(getSupportFragmentManager(), tabFragments, tabIndicators);
        vpContent.setAdapter(contentAdapter);
        tlTab.setupWithViewPager(vpContent);//将TabLayout和ViewPager关联起来。
        tlTab.setTabsFromPagerAdapter(contentAdapter);//给Tabs设置适配器
        tlTab.getTabAt(0).select();

    }


    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:

                break;
        }
    }


}
