package com.xinglincloud.www.newsuperbath.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrlibrary.qrcode.utils.QRCodeUtil;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.CtrlInformation.InstructionInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminActivity extends FullScreenActivity implements View.OnClickListener {
    @BindView(R.id.bt_set_uuid)
    Button btSetUuid;
    @BindView(R.id.et_uuid)
    EditText etUuid;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.bt_version_v1)
    Button btVersionV1;
    @BindView(R.id.bt_version_v2)
    Button btVersionV2;
    @BindView(R.id.tv_sys_net)
    TextView tvSysNet;
    @BindView(R.id.img_sys_uuid)
    ImageView imgSysUuid;
    private Switch switch_publish;
    private Button btOpenallwater;
    private Button btCloseallwater;
    private Button btOpenallbox;
    private Button btCloseallbox;
    private EditText etWater;
    private Button btOpenonewater;
    private Button btCloseonewater;
    private EditText etBox;
    private Button btOpenonebox;
    private Button btCloseonebox;
    private TextView etId, tvUesd;
    private Button btChangeid;
    private Button btExitAdmin, btFinishApp;
    private Map<String, String> uartCtrl;
    private static final String TAG = "AdminActivity";
    private MyReceiver myReceiver;
    private Button btSysNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        CtrlInfo.bath_action = InstructionInfo.ACTION_DEBUG;
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.boomstack.preparehigh.service");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(myReceiver, filter);
        uartCtrl = CtrlInfo.faucetHashmapInit(36);
        init();

    }

    public void init() {
        switch_publish = findViewById(R.id.switch_publish);

        btExitAdmin = findViewById(R.id.bt_exit_admin);
        btFinishApp = findViewById(R.id.bt_finish_app);
        btOpenallwater = findViewById(R.id.bt_openallwater);
        btCloseallwater = findViewById(R.id.bt_closeallwater);
        btOpenallbox = findViewById(R.id.bt_openallbox);
        btCloseallbox = findViewById(R.id.bt_closeallbox);
        btOpenonewater = findViewById(R.id.bt_openonewater);
        btCloseonewater = findViewById(R.id.bt_closeonewater);
        btOpenonebox = findViewById(R.id.bt_openonebox);
        btCloseonebox = findViewById(R.id.bt_closeonebox);
        tvUesd = findViewById(R.id.tv_used);
        etWater = findViewById(R.id.et_water);
        etBox = findViewById(R.id.et_box);
        etId = findViewById(R.id.et_id);
        etId.setText(CtrlInfo.uuid);
        btOpenallwater.setOnClickListener(this);
        btCloseallwater.setOnClickListener(this);
        btOpenallbox.setOnClickListener(this);
        btCloseallbox.setOnClickListener(this);
        btOpenonewater.setOnClickListener(this);
        btCloseonewater.setOnClickListener(this);
        btOpenonebox.setOnClickListener(this);
        btCloseonebox.setOnClickListener(this);
        btExitAdmin.setOnClickListener(this);
        btFinishApp.setOnClickListener(this);
        btSysNet = (Button) findViewById(R.id.bt_sys_net);
        btSysNet.setOnClickListener(this);
        switch_publish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    CtrlInfo.communication = true;
                    Toast.makeText(getApplicationContext(), "通讯开启", Toast.LENGTH_SHORT).show();
                } else {
                    CtrlInfo.communication = false;
                    Toast.makeText(getApplicationContext(), "通讯关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvVersion.setText("当前协议版本：" + CtrlInfo.version);
        imgSysUuid.setImageBitmap(QRCodeUtil.CreateTwoDCode(CtrlInfo.uuid));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_openallwater:
                openAllWater();
                break;
            case R.id.bt_closeallwater:
                closeAllWater();
                break;
            case R.id.bt_openallbox:
                openAllBox();
                break;
            case R.id.bt_closeallbox:
                closeAllBox();
                break;
            case R.id.bt_openonewater:
                if (edittextget(etWater).equals("")) {

                } else {
                    MyMqttService.uartsend(uartCtrl.get("wateropen_" + etWater.getText().toString()));
                }
                break;
            case R.id.bt_closeonewater:
                if (edittextget(etWater).equals("")) {

                } else {
                    MyMqttService.uartsend(uartCtrl.get("waterclose_" + etWater.getText().toString()));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyMqttService.uartsend(uartCtrl.get("waterselect_" + etWater.getText().toString()));
                }
                break;
            case R.id.bt_openonebox:
                break;
            case R.id.bt_closeonebox:
                break;
            case R.id.bt_exit_admin:
                restartApp();
                break;
            case R.id.bt_finish_app:
                Toast.makeText(getApplicationContext(), "功能未启用！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_sys_net:
                getNetState();
                showToast("正在进行网络检测！请稍后。。。");
            default:
                break;
        }
    }

    private void getNetState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String baseURL = "http://lz.hydream.cn/available.htm";
                boolean connect = getResultForHttpGet(baseURL);
                if(connect){
                    tvSysNet.post(new Runnable() {
                        @Override
                        public void run() {
                            tvSysNet.setText("网络连接畅通！");
                        }
                    });
                }else {
                    tvSysNet.post(new Runnable() {
                        @Override
                        public void run() {
                            tvSysNet.setText("网络异常！");
                        }
                    });
                }
            }
        }).start();
    }
    public boolean getResultForHttpGet(String baseURL) {
        try {
            URL url = new URL(baseURL);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            Log.i(TAG, "getResultForHttpGet: 请求码为："+code);
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
    private void restartApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LaunchIntent);
            }
        }).start();// 1秒钟后重启应用


    }

    //    开关四连
    public void openAllWater() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(TAG, "openAllWater: openAllWater开始运行");
                for (int i = 1; i <= 36; i++) {
                    MyMqttService.uartsend(uartCtrl.get("wateropen_" + String.valueOf(i)));
                    LogUtil.i(TAG, "openAllWater: 读取的值" + uartCtrl.get("wateropen_" + String.valueOf(i)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void closeAllWater() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 36; i++) {
                    MyMqttService.uartsend(uartCtrl.get("waterclose_" + String.valueOf(i)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void openAllBox() {
        Toast.makeText(getApplicationContext(), "功能未启用", Toast.LENGTH_SHORT).show();
    }

    public void closeAllBox() {
        Toast.makeText(getApplicationContext(), "功能未启用", Toast.LENGTH_SHORT).show();
    }

    //  单个开关
    public void openOneWater(String s) {
        MyMqttService.uartsend(uartCtrl.get("wateropen_" + s));
    }

    public void closeOneWater(String s) {
        MyMqttService.uartsend(uartCtrl.get("waterclose_" + s));
    }

    public void openOneBox(String s) {
        Toast.makeText(getApplicationContext(), "功能未启用", Toast.LENGTH_SHORT).show();

    }

    public void closeOneBox(String s) {
        Toast.makeText(getApplicationContext(), "功能未启用", Toast.LENGTH_SHORT).show();

    }

    public void changId() {
        Toast.makeText(getApplicationContext(), "功能未启用", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.bt_set_uuid)
    public void onViewClicked() {
        String uuid = etUuid.getText().toString();
        if (checkUuid(uuid)) {
            CtrlInfo.uuid = uuid;
            Toast.makeText(getApplicationContext(), "uuid设置成功！" + CtrlInfo.uuid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "uuid设置失败！请检查uuid格式是否正确！！！", Toast.LENGTH_SHORT).show();

        }

    }

    public boolean checkUuid(String uuid) {
        if (uuid.length() == 36) {
            String[] s = uuid.split("-");
            LogUtil.i(TAG, "checkUuid: " + s[0] + "," + s[1] + "," + s[2] + "," + s[3] + "," + s[4] + ",");
            if (s[0].length() == 8) {
                if (s[1].length() == 4) {
                    if (s[2].length() == 4) {
                        if (s[3].length() == 4) {
                            if (s[4].length() == 12) {
                                //写入配置文件
                                File file = new File(Environment.getExternalStorageDirectory(), "SuperBathInfo.txt"); //context.getFilesDir()帮助我们返回一个目录 /date/date/包名/files   "Info.txt"文件名
                                try {
                                    JSONObject js = new JSONObject();
                                    js.put("uuid", uuid);
                                    js.put("mode", CtrlInfo.MODE_BATH);
                                    FileOutputStream fos = new FileOutputStream(file);// 创建输出流对象
                                    fos.write(js.toString().getBytes());// 向文件中写入信息
                                    fos.close();  //关闭输出流对象
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    return false;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return false;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "UUID长度不对！", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @OnClick({R.id.bt_version_v1, R.id.bt_version_v2})
    public void onViewClicked(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        switch (view.getId()) {
            case R.id.bt_version_v1:
                tvVersion.setText("当前协议版本：1.0");
                showToast("当前协议版本：1.0");
                editor.putString("version", "1.0");
                break;
            case R.id.bt_version_v2:
                tvVersion.setText("当前协议版本：3.0");
                showToast("当前协议版本：3.0");
                editor.putString("version", "3.0");
                break;
        }
        editor.apply();

    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 广播接收Service消息
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("used_data");
            LogUtil.i(TAG, "onReceive: used_data value" + value);
            tvUesd.setText(value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(myReceiver);

    }

    public String edittextget(EditText et) {
        String s = et.getText().toString();
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(), "设备号不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(s) > 0 && Integer.valueOf(s) < 37) {
            return s;
        } else {
            s = "";
            Toast.makeText(getApplicationContext(), "设备号应该在1到36之间", Toast.LENGTH_SHORT).show();
        }
        return s;
    }
}
