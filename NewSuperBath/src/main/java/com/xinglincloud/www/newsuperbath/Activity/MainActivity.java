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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.FileUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.NetworkUtil;
import com.xinglincloud.www.newsuperbath.Util.ServiceUtils;
import com.xinglincloud.www.newsuperbath.Util.timeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FullScreenActivity {

    @BindView(R.id.tv_main_loading)
    TextView tvMainLoading;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.ly_main)
    LinearLayout lyMain;
    private boolean queryNetworkThreadSign;
    private static final String TAG = "MainActivity";
    private String netInfo = "none";
    SmdtManager smdt;
    private static final String baseURL = "http://lz.hydream.cn/available.htm";
    int httpGetNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        smdt = SmdtManager.create(this);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        img.startAnimation(rotateAnimation);
        //初始化界面图片
        viewInit();
        //系统设置初始化
        queryNetworkThreadSign = true;
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
                case 5:
                    tvMainLoading.setText("访问超时,再次测试网络连通...");
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

    private void viewInit() {
        Random rand = new Random();
        int n = rand.nextInt(3) + 1;
        switch (n) {
            case 1:
                lyMain.setBackgroundResource(R.drawable.welcome1);
                break;
            case 2:
                lyMain.setBackgroundResource(R.drawable.welcome2);
                break;
            case 3:
                lyMain.setBackgroundResource(R.drawable.welcome3);
                break;
        }
    }


    private void queryNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化配置
                settingInit();
                int i = 0;
                while (queryNetworkThreadSign) {
                    i++;
                    LogUtil.i(TAG, "run: 第" + i + "次判断网络状态！！！");
                    String EthIP = smdt.smdtGetEthIPAddress();
                    LogUtil.i(TAG, "以太网的ip是：" + EthIP);
                    if (i >= 85) {
                        if (timeUtils.between("05:00:00", "06:00:00")) {
                            SmdtManager smdt = SmdtManager.create(getApplicationContext());
                            smdt.smdtReboot("reboot");
                        }
                    }
                    try {
                        Thread.sleep(2000);
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
                            Thread.sleep(1500);
                            handler.sendEmptyMessage(2);
                            if (httpGetNum > 0) {
                                httpGetNum = 0;
                                handler.sendEmptyMessage(5);
                            }
                            if (getResultForHttpGet()) {
                                Thread.sleep(1500);
                                //网络连接成功
                                handler.sendEmptyMessage(3);
                                LogUtil.i(TAG, "run: Ping连接通了");
                                queryNetworkThreadSign = false;
                                Thread.sleep(1500);
                                //正在加载主界面
                                handler.sendEmptyMessage(4);
                                Thread.sleep(1500);
                                startApp();
                                LogUtil.i(TAG, "run: 开始跳转主界面");
                                Thread.sleep(3000);
                                LogUtil.i(TAG, "run: 准备开启活动");
                                startMqttService();
                            } else {
                                httpGetNum = 1;
                                handler.sendEmptyMessage(0);
                                Thread.sleep(1000);
                                LogUtil.i(TAG, "run: Ping连接不通 -1");
                            }
                        } else {
                            LogUtil.i(TAG, "run: 由于无网络连接，重启以太网接口");
                            handler.sendEmptyMessage(-1);
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(-2);
                            Thread.sleep(5000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, "MainActInit").start();
    }

    private void startMqttService() {

        String serviceName = "com.xinglincloud.www.newsuperbath.MqttService.MyMqttService";
        if (ServiceUtils.isServiceRunning(getApplicationContext(), serviceName)) {
            Log.i(TAG, "startApp: MyMqttService已经开启,关闭后重启！");
            stopService(new Intent(this, MyMqttService.class));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startService(new Intent(this, MyMqttService.class));
        } else {
            Log.i(TAG, "startApp: MyMqttService未开启，正在开启。。。");
            startService(new Intent(this, MyMqttService.class));
        }
    }

    /*
     * 开启界面
     * */
    private void startApp() {

        //如果appCtrl为true，
        switch (CtrlInfo.mode) {
            case CtrlInfo.MODE_BATH:
                ActivityUtil.startActivity(this, BathActivity.class);
                finish();
                break;
            case CtrlInfo.MODE_HAIRDRYER:
                ActivityUtil.startActivity(this, HairDryerActivity.class);
                finish();
                break;
            case CtrlInfo.MODE_MIXED:
                ActivityUtil.startActivity(this, MixedActivity.class);
                finish();
                break;
            default:
                CtrlInfo.mode = CtrlInfo.MODE_BATH;
                ActivityUtil.startActivity(this, BathActivity.class);
                finish();
                break;
        }

    }

    private void settingInit() {
        LogUtil.LOG_LEVEL = 6;
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
                    if (!CtrlInfo.uuid.equals("0000000000")) {
                        break;
                    }
                    js = new JSONObject(FileUtil.getSavedUserInfo());
                    LogUtil.i(TAG, "settingInit: 读取配置文件的信息是" + js.toString());
                    CtrlInfo.uuid = js.getString("uuid");
                    CtrlInfo.mode = js.getString("mode");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //调试模式
        //        CtrlInfo.uuidCtrl = true;
        if (CtrlInfo.uuidCtrl) {
            LogUtil.i(TAG, "settingInit: 调试模式已经开启！");
            LogUtil.d(TAG, "settingInit: 调试模式已经开启！");
            CtrlInfo.uuid = "90b9df64-ff37-400f-a8c8-51ce63d30666";
            CtrlInfo.mode = CtrlInfo.MODE_MIXED;
        }
        LogUtil.i(TAG, "settingInit: 设备的UUID是：" + CtrlInfo.uuid + ",模式是：" + CtrlInfo.mode);
        SharedPreferences.Editor editor = getSharedPreferences("update", MODE_PRIVATE).edit();
        editor.putBoolean("updateState", false);
        //在此配置相关的数据
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        //水控机数目
        CtrlInfo.faucetNum = sharedPreferences.getInt("faucetNum", 32);
        //箱柜最大数
        CtrlInfo.boxNum = sharedPreferences.getInt("boxNum", 0);
        //版本号
        CtrlInfo.version = sharedPreferences.getString("version", "3.0");
        editor.putString("status", "false");
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume: MainActivity 运行。。:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        queryNetworkThreadSign = false;
        LogUtil.i(TAG, "onDestroy: MainActivity 销毁了：");
    }

    public boolean getResultForHttpGet() {
        try {
            URL url = new URL(baseURL);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            Log.i(TAG, "getResultForHttpGet: 请求码为：" + code);
            if (code == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
