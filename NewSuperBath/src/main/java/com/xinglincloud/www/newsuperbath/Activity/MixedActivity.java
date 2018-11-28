package com.xinglincloud.www.newsuperbath.Activity;

import android.app.smdt.SmdtManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.RandomNumUtils;
import com.xinglincloud.www.newsuperbath.Util.VersionUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MixedActivity extends FullScreenActivity {

    @BindView(R.id.mixed_roll_view_pager)
    RollPagerView mixedRollViewPager;
    @BindView(R.id.mixed_tc_main)
    TextClock mixedTcMain;
    @BindView(R.id.mixed_imageView1)
    ImageView mixedImageView1;
    @BindView(R.id.mixed_textView1)
    TextView mixedTextView1;
    @BindView(R.id.mixed_tv_left_text)
    TextView mixedTvLeftText;
    @BindView(R.id.mixed_bt_left_three)
    Button mixedBtLeftThree;
    @BindView(R.id.mixed_left_cdtime)
    TextView mixedLeftCdtime;
    @BindView(R.id.mixed_bt_left_five)
    Button mixedBtLeftFive;
    @BindView(R.id.mixed_bt_openwater)
    Button mixedBtOpenwater;
    @BindView(R.id.mixed_textView14)
    TextView mixedTextView14;
    @BindView(R.id.mixed_bt_box)
    Button mixedBtBox;
    @BindView(R.id.mixed_textView5)
    TextView mixedTextView5;
    @BindView(R.id.mixed_bt_closewater)
    Button mixedBtClosewater;
    @BindView(R.id.mixed_textView2)
    TextView mixedTextView2;
    @BindView(R.id.mixed_imageView2)
    ImageView mixedImageView2;
    @BindView(R.id.mixed_textView18)
    TextView mixedTextView18;
    @BindView(R.id.mixed_tv_right_text)
    TextView mixedTvRightText;
    @BindView(R.id.mixed_bt_right_three)
    Button mixedBtRightThree;
    @BindView(R.id.mixed_right_cdtime)
    TextView mixedRightCdtime;
    @BindView(R.id.mixed_bt_right_five)
    Button mixedBtRightFive;
    @BindView(R.id.mixed_et_signkey)
    EditText mixedEtSignkey;
    @BindView(R.id.mixed_bt_admin)
    Button mixedBtAdmin;
    @BindView(R.id.tv_mix_version)
    TextView tvMixVersion;

    private Map<String, String> hairuartCtrl;
    MyReceiver myReceiver;
    private static final String TAG = "MixedActivity";
    int num = 0;
    private boolean threadSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed);
        ButterKnife.bind(this);
        ActivityUtil.addActivityToList(this);
        //吹风机串口控制
        hairuartCtrl = CtrlInfo.hairDryerHashmapInit();
        //滑动图片
        scollview();
        //初始化广播接收者
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.hair.action.service");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(myReceiver, filter);
        //外接键盘控制
        viewInit();
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

    /*
     * 外接键盘控制
     * */
    private void viewInit() {
        //设置监听
        mixedEtSignkey.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                LogUtil.i(TAG, "onKey: 主界面按键的值：" + i);
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP://键盘松开
                        //112清除，47开始，29箱柜，43结束
                        if (i == 47) {
                            changeActivityBath("openwater");
                        } else if (i == 29) {
                            openBox();
                        } else if (i == 43) {
                            changeActivityBath("closewater");
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

    /*
     * 轮播图片配置
     * */
    private void scollview() {

        //设置播放时间间隔
        mixedRollViewPager.setPlayDelay(4000);
        //设置透明度
        mixedRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mixedRollViewPager.setAdapter(new TestNormalAdapter());
        mixedRollViewPager.setHintView(null);

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
    //吹风机界面控制效果，和倒计时控制
    private void startLeftHairDryer(int s) {
        mixedBtLeftThree.setVisibility(View.GONE);
        mixedBtLeftFive.setVisibility(View.GONE);
        mixedLeftCdtime.setVisibility(View.VISIBLE);
        mixedTvLeftText.setVisibility(View.VISIBLE);
        MyMqttService.hairuartsend(hairuartCtrl.get("leftopen"));
        MyMqttService.publishMessage(JsonUtil.enSendOpenResponseHAIRDRYER("blower_01", String.valueOf(s), "0"));

        CountDownTimer timer = new CountDownTimer(s * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                mixedLeftCdtime.setText(timeParse(Math.round((float) millisUntilFinished / 1000)));
                LogUtil.i(TAG, "onTick: 毫秒数：" + millisUntilFinished + ",转换完成" + timeParse(Math.round((float) millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                mixedBtLeftThree.setVisibility(View.VISIBLE);
                mixedBtLeftFive.setVisibility(View.VISIBLE);
                mixedLeftCdtime.setVisibility(View.GONE);
                mixedTvLeftText.setVisibility(View.GONE);
                MyMqttService.hairuartsend(hairuartCtrl.get("leftclose"));
                MyMqttService.getBlowerHashMap().put("blower_01", false);
                MyMqttService.publishMessage(JsonUtil.enSendCloseResponseHAIRDRYER("blower_01", "0"));
            }
        }.start();
    }

    private void startRightHairDryer(int s) {
        mixedBtRightThree.setVisibility(View.GONE);
        mixedBtRightFive.setVisibility(View.GONE);
        mixedRightCdtime.setVisibility(View.VISIBLE);
        mixedTvRightText.setVisibility(View.VISIBLE);
        MyMqttService.hairuartsend(hairuartCtrl.get("rightopen"));
        MyMqttService.publishMessage(JsonUtil.enSendOpenResponseHAIRDRYER("blower_02", String.valueOf(s), "0"));
        CountDownTimer timer = new CountDownTimer(s * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

                mixedRightCdtime.setText(timeParse(Math.round((float) millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                mixedBtRightThree.setVisibility(View.VISIBLE);
                mixedBtRightFive.setVisibility(View.VISIBLE);
                mixedRightCdtime.setVisibility(View.GONE);
                mixedTvRightText.setVisibility(View.GONE);
                MyMqttService.hairuartsend(hairuartCtrl.get("rightclose"));
                MyMqttService.getBlowerHashMap().put("blower_02", false);
                MyMqttService.publishMessage(JsonUtil.enSendCloseResponseHAIRDRYER("blower_02", "0"));
            }
        }.start();
    }

    //时间换算
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

    /**
     * 广播接收Service消息
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "onReceive: MODE_MIXED收到了广播");
            if (CtrlInfo.mode.equals(CtrlInfo.MODE_MIXED)) {
                String value = intent.getStringExtra("action");
                try {
                    JSONObject json = new JSONObject(value);
                    String device = json.getString("device");
                    String time = json.getString("time");
                    LogUtil.i(TAG, "onReceive: 广播接收到了：" + json.toString());
                    switch (device) {
                        case "blower_01":
                            startLeftHairDryer(Integer.valueOf(time));
                            break;
                        case "blower_02":
                            startRightHairDryer(Integer.valueOf(time));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LogUtil.i(TAG, "onReceive: 模式不对，消息不处理");
            }
        }
    }

    /*
     * 页面跳转
     * */
    private void changeActivityHairDryer(String value) {
        ActivityUtil.startActivity(MixedActivity.this, LoginHairActivity.class, "hair_device", value);

    }

    private void changeActivityBath(String value) {
        ActivityUtil.startActivity(MixedActivity.this, LoginActivity.class, "page", value);

    }

    @OnClick({R.id.mixed_bt_left_three, R.id.mixed_bt_left_five, R.id.mixed_bt_openwater,R.id.mixed_bt_box, R.id.mixed_bt_closewater, R.id.mixed_bt_right_three, R.id.mixed_bt_right_five, R.id.mixed_bt_admin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mixed_bt_left_three:
                changeActivityHairDryer("hair_left_a");
                break;
            case R.id.mixed_bt_left_five:
                changeActivityHairDryer("hair_left_b");
                break;
            case R.id.mixed_bt_openwater:
                changeActivityBath("openwater");
                break;
            case R.id.mixed_bt_box:
                openBox();
                break;
            case R.id.mixed_bt_closewater:
                changeActivityBath("closewater");
                break;
            case R.id.mixed_bt_right_three:
                changeActivityHairDryer("hair_right_a");
                break;
            case R.id.mixed_bt_right_five:
                changeActivityHairDryer("hair_right_b");
                break;
            case R.id.mixed_bt_admin:
                Threadcountdown();
                if (num > 6) {
                    ActivityUtil.startActivity(MixedActivity.this, LoginAdminActivity.class);
                } else {
                    num++;
                    LogUtil.i(TAG, "onViewClicked: i++");
                }
                break;
        }
    }
    private void changeActivity(String key, String value) {
        ActivityUtil.startActivity(MixedActivity.this, LoginActivity.class, key, value);
    }
    private void openBox() {
        if(CtrlInfo.boxNum>0){
            if(RandomNumUtils.getRandomNumSize()<CtrlInfo.boxNum){
                changeActivity("page", "openbox");
            }else {
                Toast.makeText(getApplicationContext(), "没有闲置的箱柜！", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "功能未启用！", Toast.LENGTH_SHORT).show();
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

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVersionTv(tvMixVersion);

        //用EditText监听外接键盘
        //获取焦点
        mixedEtSignkey.setFocusable(true);
        mixedEtSignkey.setFocusableInTouchMode(true);
        mixedEtSignkey.requestFocus();
        num=0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy: 销毁了！");
    }
}
