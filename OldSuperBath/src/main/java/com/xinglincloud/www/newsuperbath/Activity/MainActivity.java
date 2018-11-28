package com.xinglincloud.www.newsuperbath.Activity;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;

import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.FileUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.NetworkUtil;
import com.xinglincloud.www.newsuperbath.Util.timeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FullScreenActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_main_loading)
    TextView tvMainLoading;
    private boolean queryNetworkThreadSign;
    private static final String TAG = "MainActivity";
    private String netInfo = "none";
    SmdtManager smdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        smdt = SmdtManager.create(this);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        img.startAnimation(rotateAnimation);
        queryNetworkThreadSign = true;//网络检测线程标志
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume: MainActivity 运行。...............。:");
        queryNetwork();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tvMainLoading.setText("检测到网络！网络类型：" + NetworkUtil.getConnectedType(getApplicationContext()) + ",网络名称：" + netInfo);
                    break;
                case 2:
                    tvMainLoading.setText("测试网络连通...");
                    break;
                case 3:
                    tvMainLoading.setText("网络通讯正常！正在连接服务器...");
                    break;
                case 4:
                    tvMainLoading.setText("准备加载主界面！");
                    break;
                case 0:
                    tvMainLoading.setText("网络通讯失败！请检查网络连接！");
                    break;
                case -1:
                    tvMainLoading.setText("正在关闭网络端口！");
                    smdt.smdtSetEthernetState(false);
                    break;
                case -2:
                    tvMainLoading.setText("正在重置网络端口！");
                    smdt.smdtSetEthernetState(true);
                    break;
            }
        }
    };


    private void queryNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                settingInit();
                int i = 0;
                Handler mHandler;
                while (queryNetworkThreadSign) {
                    String EthIP = smdt.smdtGetEthIPAddress();
                    LogUtil.i(TAG, "以太网的ip是：" + EthIP);
                    i++;
                    LogUtil.i(TAG, "run: 第" + i + "次判断网络状态！！！");
                    if (i >= 85) {
                        if (timeUtils.between("05:00:00", "06:00:00")) {
                            SmdtManager smdt = SmdtManager.create(getApplicationContext());
                            smdt.smdtReboot("reboot");
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //如果有网络连接，就ping一下试试
                    if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
                        LogUtil.i(TAG, "run: 有网络连接，网络类型：" + NetworkUtil.getConnectedType(getApplicationContext()));
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo info = null;
                        if (connectivityManager != null) {
                            info = connectivityManager.getActiveNetworkInfo();
                        }
                        if (info != null && info.isAvailable()) {
                            netInfo = info.getTypeName();
                        }
                        handler.sendEmptyMessage(1);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(2);
                        if (NetworkUtil.ping()) {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(3);
                            LogUtil.i(TAG, "run: Ping连接通了");
                            queryNetworkThreadSign = false;
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(4);
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startApp();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //开启服务
                            startMqttService();
                        } else {
                            handler.sendEmptyMessage(0);
                            LogUtil.i(TAG, "run: Ping连接不通 -1");
                        }
                    } else {
                        LogUtil.i(TAG, "run: 由于无网络连接，重启以太网接口");
                        handler.sendEmptyMessage(-1);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(-2);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    private void startMqttService() {
        startService(new Intent(this, MyMqttService.class));
    }

    /*
     * 开启界面
     * */
    private void startApp() {
        //如果appCtrl为true，
        CtrlInfo.mode = CtrlInfo.MODE_BATH;
        ActivityUtil.startActivity(this, BathActivity.class);
        finish();
    }

    private void settingInit() {
        LogUtil.LOG_LEVEL = 6;
        //定时开关机
        smdt.smdtSetTimingSwitchMachine("01:30", "05:00", "1");
        //构建uuid，构成json字符串
        String uuid = UUID.randomUUID().toString();
        JSONObject js = new JSONObject();
        try {
            js.put("uuid", uuid);
            js.put("mode", "BATH");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //判断文件是否保存成功
        if (FileUtil.saveUserInfo(js.toString())) {
            //第一次打开应用，初始化配置文件
            CtrlInfo.uuid = uuid;
            CtrlInfo.mode = CtrlInfo.MODE_BATH;
        } else {
            //读取文件，配置设置
            while (true) {
                try {
                    js = new JSONObject(FileUtil.getSavedUserInfo());
                    LogUtil.i(TAG, "settingInit: 读取配置文件的信息是" + js.toString());
                    CtrlInfo.uuid = js.getString("uuid");
                    CtrlInfo.mode = js.getString("mode");
                    if (!CtrlInfo.uuid.equals("0000000000")) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //调试模式
        if (CtrlInfo.uuidCtrl) {
            CtrlInfo.uuid = "90b9df64-ff37-400f-a8c8-51ce63d30667";
        }
        LogUtil.i(TAG, "settingInit: 设备的UUID是：" + CtrlInfo.uuid);
        SharedPreferences.Editor editor = getSharedPreferences("update", MODE_PRIVATE).edit();
        editor.putBoolean("updateState", false);
        editor.apply();
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        CtrlInfo.version = sharedPreferences.getString("version", "1.0");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        LogUtil.i(TAG, "onDestroy: MainActivity 销毁了：");

    }
}
