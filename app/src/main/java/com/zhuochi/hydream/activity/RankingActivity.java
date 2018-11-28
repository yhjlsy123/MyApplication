package com.zhuochi.hydream.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.adapter.RankVPFragmentAdapter;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.TipDialog2;
import com.zhuochi.hydream.dialog.TipDialogMessage;
import com.zhuochi.hydream.entity.RankingTypeEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.fragment.RankMonthFragment;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.interfaceimp.ImgProblemClick;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.fragment.RankMonthFragment.RANK_MESSAGE;

/**
 * 排行榜
 * Created by and on 2016/11/10.
 */

public class RankingActivity extends BaseAutoActivity {

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
    @BindView(R.id.img_problem)
    ImageView imgProblem;
    List<String> listtitle;//标题集合
    List<Fragment> listfragment;
    private XiRequestParams params;
    List<RankingTypeEntity> entityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        params = new XiRequestParams(this);
        ButterKnife.bind(this);
        textTitle.setText("排行榜");

        if ((!NetworkUtil.isNetworkAvailable())) {
                  /*没网显示缺省内容*/
            lin.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            dafault.setVisibility(View.VISIBLE);
        } else {
            getRankingType();/*获取排行榜类型*/
        }
    }

    //        获取排行类型
    public void getRankingType() {
        imgProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        if (params == null) {
            params = new XiRequestParams(this);
        }
        params.addCallBack(this);
        params.getRankingType(UserUtils.getInstance(this).getMobilePhone(), "blower");
    }



    public void onBackClick() {
        Dialog dialog = TipDialogMessage.show_(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
            }
        }, "", RANK_MESSAGE);

    }

    //渲染viewpager+fragment
    public void getVpTitleData() {
        RankVPFragmentAdapter mAdapter = new RankVPFragmentAdapter(getSupportFragmentManager(), listfragment, listtitle);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
//        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(listfragment.size() - 1);
    }
    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "getRankingType"://获取排行类型
                JSONArray jsonArray = new JSONArray((ArrayList) result.getData().getData());
                if (jsonArray.size() > 0) {
                    //定义 title 集合 来存储  解析的data数据
                    listtitle = new ArrayList<>();
                    //创建集合 循环添加创建的Fragment
                    listfragment = new ArrayList<>();
                    entityList = JSON.parseArray(JSON.toJSONString(jsonArray), RankingTypeEntity.class);
                    //遍历 listtitle 集合 将title 添加经 TabLayou z中
                    mTabLayout.removeAllTabs();
                    for (RankingTypeEntity rankingTypeEntity : entityList) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(rankingTypeEntity.getRanking_type_name()));/*获取类型名称*/
                        listtitle.add(rankingTypeEntity.getRanking_type_name());
                        RankMonthFragment rankMonthFragment = new RankMonthFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("getRankingType", JSON.toJSONString(rankingTypeEntity));/*传入排行榜类型*/
                        rankMonthFragment.setArguments(bundle);
                        listfragment.add(rankMonthFragment);
                    }
                    getVpTitleData();
                }
                break;


        }
        super.onRequestSuccess(tag, result);
    }


    @OnClick(R.id.rank_back)
    public void onClick() {
        finish();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
