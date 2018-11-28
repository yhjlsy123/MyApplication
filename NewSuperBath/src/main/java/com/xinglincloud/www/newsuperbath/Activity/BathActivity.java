package com.xinglincloud.www.newsuperbath.Activity;

import android.app.smdt.SmdtManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.RandomNumUtils;
import com.xinglincloud.www.newsuperbath.Util.VersionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BathActivity extends FullScreenActivity {

    @BindView(R.id.bath_tc_main)
    TextClock bathTcMain;
    @BindView(R.id.bath_bt_openwater)
    Button bathBtOpenwater;
    @BindView(R.id.bath_bt_box)
    Button bathBtBox;
    @BindView(R.id.bath_bt_closewater)
    Button bathBtClosewater;
    @BindView(R.id.bath_et_signkey)
    EditText bathEtSignkey;
    @BindView(R.id.bath_bt_admin)
    Button bathBtAdmin;
    @BindView(R.id.ly_bath_version)
    LinearLayout lyBathVersion;
    @BindView(R.id.tv_bath_version)
    TextView tvBathVersion;
    @BindView(R.id.bath_tv_warning)
    TextView bathTvWarning;


    private RollPagerView mRollViewPager;
    private static final String TAG = "BathActivity";
    private int num = 0;
    WarningReceiver warningReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bath_new);
        ButterKnife.bind(this);
        ActivityUtil.addActivityToList(this);
        scollview();
        bathTcMain.bringToFront();
        //滑动图片初始化
        viewInit();
        setStatusBar();
        warningReceiver = new WarningReceiver();
        IntentFilter filter = new IntentFilter("com.bath.warning");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(warningReceiver, filter);
    }

    private void setStatusBar() {
        //状态栏
        SmdtManager smdt;
        smdt = SmdtManager.create(this);
        smdt.smdtSetStatusBar(getApplicationContext(), false);
    }

    private void setVersionTv(TextView tv) {
        tv.setText(VersionUtil.getVersion(this));
        tv.setTextSize(12);
    }

    private void scollview() {
        mRollViewPager = findViewById(R.id.roll_view_pager);
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(4000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(null);
    }


    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.slider_bath1,
                R.drawable.slider_bath2,
                R.drawable.slider_bath3,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }

    private void viewInit() {
        //用EditText监听外接键盘
        bathEtSignkey.setFocusable(true);
        bathEtSignkey.setFocusableInTouchMode(true);
        bathEtSignkey.requestFocus();
        //设置监听
        bathEtSignkey.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP://键盘松开
                        //112清除，47开始，29箱柜，43结束
                        if (i == 47) {
                            changeActivity("page", "openwater");
                        } else if (i == 29) {
                            openBox();
                        } else if (i == 43) {
                            changeActivity("page", "closewater");
                        }
                        break;
                    case KeyEvent.ACTION_DOWN:          //键盘按下
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    @OnClick({R.id.bath_bt_admin, R.id.bath_bt_openwater, R.id.bath_bt_box, R.id.bath_bt_closewater})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bath_bt_admin:
                adminonclick();
                break;
            case R.id.bath_bt_openwater:
                changeActivity("page", "openwater");
                break;
            case R.id.bath_bt_box:
                openBox();
                break;
            case R.id.bath_bt_closewater:
                changeActivity("page", "closewater");
                break;
        }
    }

    private void openBox() {
        if (CtrlInfo.boxNum > 0) {
            if (RandomNumUtils.getRandomNumSize() < CtrlInfo.boxNum) {
                changeActivity("page", "openbox");
            } else {
                Toast.makeText(getApplicationContext(), "没有闲置的箱柜！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "功能未启用！", Toast.LENGTH_SHORT).show();
        }
    }

    private void adminonclick() {
        if (num > 6) {
            ActivityUtil.startActivity(BathActivity.this, LoginAdminActivity.class);
        } else {
            num++;
        }
    }

    private void changeActivity(String key, String value) {
        ActivityUtil.startActivity(BathActivity.this, LoginActivity.class, key, value);
    }

    //** 广播接收者，用于做网络状态的提示.*//
    private class WarningReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean is = intent.getBooleanExtra("state",true);
            if (is) {
                bathTvWarning.post(new Runnable() {
                    @Override
                    public void run() {
                        bathTvWarning.setVisibility(View.GONE);
                    }
                });
            } else {
                bathTvWarning.post(new Runnable() {
                    @Override
                    public void run() {
                        bathTvWarning.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        num = 0;
        setVersionTv(tvBathVersion);

    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy: 销毁了！");
    }
}
