package com.xinglincloud.www.newsuperbath.Activity;

import android.app.smdt.SmdtManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.VersionUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HairDryerActivity extends FullScreenActivity {


    MyReceiver myReceiver;
    @BindView(R.id.hair_bt_admin)
    Button hairBtAdmin;
    @BindView(R.id.tv_hair_version)
    TextView tvHairVersion;
    private RollPagerView mRollViewPager;
    private Map<String, String> hairuartCtrl;

    private static final String TAG = "HairDryerActivity";
    @BindView(R.id.hair_tc_main)
    TextClock hairTcMain;
    @BindView(R.id.hair_rly1)
    RelativeLayout hairRly1;

    @BindView(R.id.hair_tv_left_text)
    TextView hairTvLeftText;
    @BindView(R.id.hair_bt_left_three)
    Button hairBtLeftThree;
    @BindView(R.id.hair_left_cdtime)
    TextView hairLeftCdtime;
    @BindView(R.id.hair_bt_left_five)
    Button hairBtLeftFive;
    @BindView(R.id.hair_tv_right_text)
    TextView hairTvRightText;
    @BindView(R.id.hair_bt_right_three)
    Button hairBtRightThree;
    @BindView(R.id.hair_right_cdtime)
    TextView hairRightCdtime;
    @BindView(R.id.hair_bt_right_five)
    Button hairBtRightFive;
    @BindView(R.id.hair_ll1)
    LinearLayout hairLl1;
    int num = 0;
    private boolean threadSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_dryer);
        ButterKnife.bind(this);
        hairuartCtrl = CtrlInfo.hairDryerHashmapInit();
        ActivityUtil.addActivityToList(this);
//        scollImageInit();
        scollview();
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.hair.action.service");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(myReceiver, filter);
        setStatusBar();
    }
    private void setStatusBar(){
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
        mRollViewPager = findViewById(R.id.hair_roll_view_pager);
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
                R.drawable.slider_hairdryer1,
                R.drawable.slider_hairdryer2,
                R.drawable.slider_hairdryer3,
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
    private String timeParse(long duration) {
        duration = duration - 2;
        String time = "";
        long minute = duration / 60;
        duration = duration % 60;
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (duration < 10) {
            time += "0";
        }
        time += duration;
        return time;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void startLeftHairDryer(int s) {
        hairBtLeftThree.setVisibility(View.GONE);
        hairBtLeftFive.setVisibility(View.GONE);
        hairLeftCdtime.setVisibility(View.VISIBLE);
        hairTvLeftText.setVisibility(View.VISIBLE);
        MyMqttService.hairuartsend(hairuartCtrl.get("leftopen"));
        MyMqttService.publishMessage(JsonUtil.enSendOpenResponseHAIRDRYER("blower_01", String.valueOf(s), "0"));

        CountDownTimer timer = new CountDownTimer(s * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                hairLeftCdtime.setText(timeParse(Math.round((float) millisUntilFinished / 1000)));

            }

            @Override
            public void onFinish() {
                hairBtLeftThree.setVisibility(View.VISIBLE);
                hairBtLeftFive.setVisibility(View.VISIBLE);
                hairLeftCdtime.setVisibility(View.GONE);
                hairTvLeftText.setVisibility(View.GONE);
                MyMqttService.hairuartsend(hairuartCtrl.get("leftclose"));
                MyMqttService.getBlowerHashMap().put("blower_01", false);
                MyMqttService.publishMessage(JsonUtil.enSendCloseResponseHAIRDRYER("blower_01", "0"));
            }
        }.start();
    }

    private void startRightHairDryer(int s) {
        hairBtRightThree.setVisibility(View.GONE);
        hairBtRightFive.setVisibility(View.GONE);
        hairRightCdtime.setVisibility(View.VISIBLE);
        hairTvRightText.setVisibility(View.VISIBLE);
        MyMqttService.hairuartsend(hairuartCtrl.get("rightopen"));
        MyMqttService.publishMessage(JsonUtil.enSendOpenResponseHAIRDRYER("blower_02", String.valueOf(s), "0"));
        CountDownTimer timer = new CountDownTimer(s * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                hairRightCdtime.setText(timeParse(Math.round((float) millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                hairBtRightThree.setVisibility(View.VISIBLE);
                hairBtRightFive.setVisibility(View.VISIBLE);
                hairRightCdtime.setVisibility(View.GONE);
                hairTvRightText.setVisibility(View.GONE);
                MyMqttService.hairuartsend(hairuartCtrl.get("rightclose"));
                MyMqttService.getBlowerHashMap().put("blower_02", false);
                MyMqttService.publishMessage(JsonUtil.enSendCloseResponseHAIRDRYER("blower_02", "0"));
            }
        }.start();
    }

    private void changeActivity(String value) {
        ActivityUtil.startActivity(HairDryerActivity.this, LoginHairActivity.class, "hair_device", value);

    }

    @OnClick({R.id.hair_bt_right_three, R.id.hair_bt_admin, R.id.hair_bt_right_five, R.id.hair_bt_left_three, R.id.hair_bt_left_five})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.hair_bt_left_three:
                changeActivity("hair_left_a");
                break;
            case R.id.hair_bt_left_five:
                changeActivity("hair_left_b");
                break;
            case R.id.hair_bt_right_three:
                changeActivity("hair_right_a");
                break;
            case R.id.hair_bt_right_five:
                changeActivity("hair_right_b");
                break;
            case R.id.hair_bt_admin:
                Threadcountdown();
                if (num > 6) {
                    ActivityUtil.startActivity(HairDryerActivity.this, LoginAdminActivity.class);
                } else {
                    num++;
                    LogUtil.i(TAG, "onViewClicked: i++");
                }
                break;
        }
    }

    private void Threadcountdown() {
        if (!threadSign) {
            threadSign = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num = 0;
                    threadSign = false;
                }
            }).start();
        }
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();


        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        return sb.toString();
    }

    /**
     * 广播接收Service消息
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "onReceive: MODE_HAIRDRYER收到了广播");
            if (CtrlInfo.mode.equals(CtrlInfo.MODE_HAIRDRYER)) {
                String value = intent.getStringExtra("action");
                try {//开启吹风机应该有：设备、时间,device,time
                    JSONObject json = new JSONObject(value);
                    String device = json.getString("device");
                    String time = json.getString("time");
                    LogUtil.i(TAG, "onReceive: 广播接收到了：" + json.toString());
                    switch (device) {
                        case "blower_01":
                            LogUtil.i(TAG, "onReceive: 运行startLeftHairDryer");
                            startLeftHairDryer(Integer.valueOf(time));
                            break;
                        case "blower_02":
                            LogUtil.i(TAG, "onReceive: 运行startRightHairDryer");
                            startRightHairDryer(Integer.valueOf(time));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LogUtil.i(TAG, "onReceive: 模式和广播不匹配！");
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setVersionTv(tvHairVersion);

        num = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy: 销毁了！");
    }
}
