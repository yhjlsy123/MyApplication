package com.zhuochi.hydream.bathhousekeeper.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.bean.UpgradeEntity;
import com.zhuochi.hydream.bathhousekeeper.config.AppManager;
import com.zhuochi.hydream.bathhousekeeper.dialog.UpdateDialogApp;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.fragment.DataFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.HomeFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.ManagmentFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.MyFragment;
import com.zhuochi.hydream.bathhousekeeper.fragment.OrderFragment;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.Tools;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;

import java.util.Map;


public class MainActivity extends BaseActivity {
    // 定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    // 定义一个布局
    private LayoutInflater layoutInflater;

    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class,
            OrderFragment.class, ManagmentFragment.class, DataFragment.class,
            MyFragment.class};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home,
            R.drawable.tab_order, R.drawable.tab_admin,
            R.drawable.tab_data, R.drawable.tab_my};

    // Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "订单", "管理", "数据", "我的"};
    private XiRequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        params = new XiRequestParams(this);
        upgradeApp();
    }


    /**
     * 检测版本更新
     */
    private void upgradeApp() {
        params.addCallBack(this);
        Map<String, Object> pram = Common.intancePram();
        pram.put("client_type", 1);
        pram.put("device", 1);
        pram.put("version_code", SPUtils.getLocalVersion(this));
        params.comnRequest("UserApi/upgradeApp", pram);
    }


    private void showDialogUpdate(final UpgradeEntity entity) {
        if (entity.getStatus() == 1) {
            UpdateDialogApp.Builder builder = new UpdateDialogApp.Builder(this);
            builder.create(entity.getIntro(), entity.getIs_force(), entity.getNew_version()).show();
            builder.setConfirm(new UpdateDialogApp.OnConfirmListener() {
                @Override
                public void confirm() {
                    Toast.makeText(MainActivity.this, "进行更新操作吧", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(entity.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 得到fragment的个数
        int count = fragmentArray.length;


        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i)
//                    .setBackgroundResource(R.drawable.main_tab_item_bg);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.d("cxt", "tabId" + tabId);

            }

        });
    }

    //    R.anim.slide_out
    public void switchContent(Fragment from, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.slide_out_right);
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.hide(from).add(R.id.realtabcontent, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.main_bottom, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tab_img);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.tab_tv);
        textView.setText(mTextviewArray[index]);
//        if (imageView.isSelected()) {
//            textView.setTextColor(getResources().getColorStateList(R.color.blue_rank_bg));
//        }else {textView.setTextColor(getResources().getColorStateList(R.color.gray959595));}


        return view;
    }


    @Override
    public void onBackPressed() {
        if (Tools.dblClose()) {
            AppManager.INSTANCE.appExit(this);
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        super.onRequestSuccess(tag, result);
        switch (tag) {
            case "UserApi/upgradeApp":
                Map map1 = (Map) result.getData().getData();
//                String gs = GsonUtils.parseMapToJson(map1);
                UpgradeEntity entity = JSON.parseObject(JSON.toJSONString(map1), UpgradeEntity.class);
                showDialogUpdate(entity);
                break;


        }
    }
}

