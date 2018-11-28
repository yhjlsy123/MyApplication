package com.xinglincloud.www.newsuperbath.Activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.smdt.SmdtManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminActivity extends FullScreenActivity implements View.OnClickListener {
    @BindView(R.id.et_left_hair)
    EditText etLeftHair;
    @BindView(R.id.tv_left_hair_time)
    TextView tvLeftHairTime;
    @BindView(R.id.tv_right_hair_time)
    TextView tvRightHairTime;
    @BindView(R.id.et_right_hair)
    EditText etRightHair;
    @BindView(R.id.admin_tv_mode)
    TextView adminTvMode;
    @BindView(R.id.et_uuid)
    EditText etUuid;
    @BindView(R.id.tv_faucetNum)
    TextView tvFaucetNum;
    @BindView(R.id.et_faucetNum)
    EditText etFaucetNum;
    @BindView(R.id.tv_status_bar)
    TextView tvStatusBar;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_boxNum)
    TextView tvBoxNum;
    @BindView(R.id.et_box)
    EditText etBox;
    @BindView(R.id.et_box_num)
    EditText etBoxNum;
    @BindView(R.id.bt_set_box_num)
    Button btSetBoxNum;
    @BindView(R.id.et_user_pwd1)
    EditText etUserPwd1;
    @BindView(R.id.et_user_pwd2)
    EditText etUserPwd2;
    @BindView(R.id.tv_sys_net)
    TextView tvSysNet;
    @BindView(R.id.bt_sys_net)
    Button btSysNet;
    @BindView(R.id.img_sys_uuid)
    ImageView imgSysUuid;
    private Switch switch_publish;
    private Button btOpenallwater;
    private Button btCloseallwater;
    private Button btOpenallbox;
    private EditText etWater;
    private Button btOpenonewater;
    private Button btCloseonewater;
    private Button btOpenonebox;
    private TextView etId, tvUesd;
    private Button btExitAdmin, btFinishApp;
    private Map<String, String> uartCtrl;
    private Map<String, String> hairuartCtrl;
    private HashMap<String, String> ventilatorHashmap;

    private static final String TAG = "AdminActivity";
    private MyReceiver myReceiver;
    int i = 0;
    boolean btLeftClick = false;
    boolean btRightClick = false;
    String adminMode = CtrlInfo.mode;
    SmdtManager smdt;
    AlertDialog.Builder builder1, builder2, builder3;
    AlertDialog mAlertDialogOpen, mAlertDialogClose, mAlertDialogOpenClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        smdt = SmdtManager.create(this);
        ActivityUtil.addActivityToList(this);

        CtrlInfo.bath_action = InstructionInfo.ACTION_DEBUG;
        receiverInit();
        hashmapInit();
        init();
        //视图数据初始化
        viewInit();
    }

    private void receiverInit() {
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.boomstack.preparehigh.service");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(myReceiver, filter);
    }

    private void hashmapInit() {
        uartCtrl = CtrlInfo.faucetHashmapInit(CtrlInfo.faucetNum);
        hairuartCtrl = CtrlInfo.hairDryerHashmapInit();
        ventilatorHashmap = CtrlInfo.ventilatorHashmapInit();
    }

    private void viewInit() {
        //初始化当前模式的视图数据
        adminTvMode.setText("当前模式：" + CtrlInfo.mode);
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        String s = "最大水控机数量为：" + String.valueOf(sharedPreferences.getInt("faucetNum", 32));
        tvFaucetNum.setText(s);
        String versionStr = "当前协议版本：" + sharedPreferences.getString("version", "3.0");
        tvVersion.setText(versionStr);
        String StatusBarStr = "当前状态栏状态：" + sharedPreferences.getString("status", "default");
        tvStatusBar.setText(StatusBarStr);
        String boxNum = "当前箱柜最大数：" + sharedPreferences.getInt("boxNum", 0);
        tvBoxNum.setText(boxNum);
        //二维码
        imgSysUuid.setImageBitmap(QRCodeUtil.CreateTwoDCode(CtrlInfo.uuid));

    }

    public void init() {
        switch_publish = findViewById(R.id.switch_publish);

        btExitAdmin = findViewById(R.id.bt_exit_admin);
        btFinishApp = findViewById(R.id.bt_finish_app);
        btOpenallwater = findViewById(R.id.bt_openallwater);
        btCloseallwater = findViewById(R.id.bt_closeallwater);
        btOpenallbox = findViewById(R.id.bt_openallbox);
        btOpenonewater = findViewById(R.id.bt_openonewater);
        btCloseonewater = findViewById(R.id.bt_closeonewater);
        btOpenonebox = findViewById(R.id.bt_openonebox);
        tvUesd = findViewById(R.id.tv_used);
        etWater = findViewById(R.id.et_water);
        etId = findViewById(R.id.et_id);
        etId.setText(CtrlInfo.uuid);

        btOpenallwater.setOnClickListener(this);
        btCloseallwater.setOnClickListener(this);
        btOpenallbox.setOnClickListener(this);
        btOpenonewater.setOnClickListener(this);
        btCloseonewater.setOnClickListener(this);
        btOpenonebox.setOnClickListener(this);
        btExitAdmin.setOnClickListener(this);
        btFinishApp.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_openallwater:
                builder1 = new AlertDialog.Builder(AdminActivity.this);
                builder1.setTitle("警告！！");
                builder1.setMessage("本操作会开启1到" + CtrlInfo.faucetNum + "的所有水控机，是否继续？");
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View view = View.inflate(getApplicationContext(), R.layout.dialog_admin_layout, null);
                        mAlertDialogOpen = new AlertDialog.Builder(AdminActivity.this).setView(view).setCancelable(false).create();
                        mAlertDialogOpen.setTitle("正在开启中，请稍后！");
                        mAlertDialogOpen.show();
                        openAllWater();
                    }
                });
                AlertDialog dialog = builder1.create();
                dialog.show();
                break;
            case R.id.bt_closeallwater:
                builder2 = new AlertDialog.Builder(AdminActivity.this);
                builder2.setTitle("警告！！");
                builder2.setMessage("本操作会关闭1到" + CtrlInfo.faucetNum + "的所有水控机，是否继续？");
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View view = View.inflate(getApplicationContext(), R.layout.dialog_admin_layout, null);
                        mAlertDialogClose = new AlertDialog.Builder(AdminActivity.this).setView(view).setCancelable(false).create();
                        mAlertDialogClose.setTitle("正在关闭中，请稍后！");
                        mAlertDialogClose.show();
                        closeAllWater();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
            case R.id.bt_openallbox:
                if (CtrlInfo.boxNum == 0) {
                    showToast("如果想开启所有箱柜，箱柜的数目必须大于0！");
                } else {
                    builder3 = new AlertDialog.Builder(AdminActivity.this);
                    builder3.setTitle("警告！！");
                    builder3.setMessage("本操作会开启1到" + CtrlInfo.boxNum + "的所有箱柜，是否继续？");
                    builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            View view = View.inflate(getApplicationContext(), R.layout.dialog_admin_layout, null);
                            mAlertDialogOpenClose = new AlertDialog.Builder(AdminActivity.this).setView(view).setCancelable(false).create();
                            mAlertDialogOpenClose.setTitle("正在关闭中，请稍后！");
                            mAlertDialogOpenClose.show();
                            openAllBox();
                        }
                    });
                    AlertDialog dialog3 = builder3.create();
                    dialog3.show();
                }
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
                String s = etBox.getText().toString();
                if (!s.equals("")) {
                    int n = Integer.valueOf(s);
                    openOneBox(n);
                } else {
                    showToast("箱柜号不能为空！");
                }
                break;
            case R.id.bt_exit_admin:
                String serviceName = "com.xinglincloud.www.newsuperbath.MqttService.MyMqttService";
                if (ServiceUtils.isServiceRunning(getApplicationContext(), serviceName)) {
                    stopService(new Intent(this, MyMqttService.class));
                }
                restartApp();
                break;
            case R.id.bt_finish_app:
                smdt.smdtSetStatusBar(getApplicationContext(), true);
                String serviceName2 = "com.xinglincloud.www.newsuperbath.MqttService.MyMqttService";
                if (ServiceUtils.isServiceRunning(getApplicationContext(), serviceName2)) {
                    LogUtil.i(TAG, " MyMqttService处于开启状态,关闭！");
                    stopService(new Intent(this, MyMqttService.class));
                }
                ActivityUtil.finishAllActivity();
                break;
            default:
                break;
        }
    }

    private void restartApp() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.finishAllActivity();
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LaunchIntent);


            }
        }).start();// 重启应用
    }

    private Handler openHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {
                mAlertDialogOpen.cancel();
            } else {
                TextView tv = mAlertDialogOpen.findViewById(R.id.tv_dialog);
                tv.setText("正在开启第" + what + "个设备！");
            }
        }
    };

    public void openAllWater() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(TAG, "openAllWater: openAllWater开始运行");
                for (int i = 1; i <= CtrlInfo.faucetNum; i++) {
                    MyMqttService.uartsend(uartCtrl.get("wateropen_" + String.valueOf(i)));
                    LogUtil.i(TAG, "openAllWater: 读取的值" + uartCtrl.get("wateropen_" + String.valueOf(i)));
                    openHandler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                openHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {
                mAlertDialogClose.cancel();
            } else {
                TextView tv = mAlertDialogClose.findViewById(R.id.tv_dialog);
                tv.setText("正在关闭第" + what + "个设备！");
            }
        }
    };

    private Handler openBoxHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {
                mAlertDialogOpenClose.cancel();
            } else {
                TextView tv = mAlertDialogOpenClose.findViewById(R.id.tv_dialog);
                tv.setText("正在开启第" + what + "个设备！");
            }
        }
    };

    public void closeAllWater() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= CtrlInfo.faucetNum; i++) {
                    MyMqttService.uartsend(uartCtrl.get("waterclose_" + String.valueOf(i)));
                    mHandler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void openAllBox() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= CtrlInfo.boxNum; i++) {
                    MyMqttService.uartOpenOneBox(i);
                    openBoxHandler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                openBoxHandler.sendEmptyMessage(0);
            }
        }).start();
    }


    public void openOneBox(int n) {
        if (n > CtrlInfo.boxNum) {
            showToast("箱柜编号不能大于最大箱柜数目！");
        } else {
            MyMqttService.uartOpenOneBox(n);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "不能重复开启吹风机", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    Handler handlerLefttime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvLeftHairTime.setText("剩余时间：" + msg.what);
        }
    };
    Handler handlerRighttime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvRightHairTime.setText("剩余时间：" + msg.what);
        }
    };

    /**
     * @Description: Button事件监听
     * @Param: [view]
     * @return: void
     * @Author: xxh
     * @Date: 2018/7/27
     */
    @OnClick({R.id.bt_open_left_hair, R.id.bt_open_right_hair,
            R.id.admin_bt_change_bath, R.id.admin_bt_change_hairdryer,
            R.id.admin_bt_change_mixed, R.id.bt_set_uuid, R.id.bt_faucetNum,
            R.id.bt_ventilator_low, R.id.bt_ventilator_medium, R.id.bt_ventilator_high,
            R.id.bt_ventilator_close, R.id.bt_status_bar_open, R.id.bt_status_bar_close,
            R.id.bt_version_v1, R.id.bt_version_v3, R.id.bt_set_box_num,
            R.id.bt_user_pwd})
    public void onViewClicked(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();

        switch (view.getId()) {
            case R.id.bt_open_left_hair:
                openLeftHair();
                break;
            case R.id.bt_open_right_hair:
                openRighttHair();
                break;
            case R.id.admin_bt_change_bath:
                adminMode = CtrlInfo.MODE_BATH;
                dialog("洗浴");
                break;
            case R.id.admin_bt_change_hairdryer:
                adminMode = CtrlInfo.MODE_HAIRDRYER;
                dialog("吹风机");
                break;
            case R.id.admin_bt_change_mixed:
                adminMode = CtrlInfo.MODE_MIXED;
                dialog("混合");
                break;
            case R.id.bt_set_uuid:
                setUUID();
                break;
            case R.id.bt_faucetNum:
                setFaucetNum();
                break;
            //以下为通风设备测试
            case R.id.bt_ventilator_low:
                MyMqttService.uartsendVentilator(ventilatorHashmap.get("low"));
                showToast("开启通风设备：低档！");
                break;
            case R.id.bt_ventilator_medium:
                MyMqttService.uartsendVentilator(ventilatorHashmap.get("medium"));
                showToast("开启通风设备：中档！");
                break;
            case R.id.bt_ventilator_high:
                MyMqttService.uartsendVentilator(ventilatorHashmap.get("high"));
                showToast("开启通风设备：高档！");
                break;
            case R.id.bt_ventilator_close:
                MyMqttService.uartsendVentilator(ventilatorHashmap.get("close"));
                showToast("关闭通风设备！");
                break;
            case R.id.bt_status_bar_open:
                smdt.smdtSetStatusBar(getApplicationContext(), true);
                editor.putString("status", "true");
                tvStatusBar.setText("当前状态栏状态：true");
                showToast("设置状态栏的状态为：开启！");
                break;
            case R.id.bt_status_bar_close:
                smdt.smdtSetStatusBar(getApplicationContext(), false);
                editor.putString("status", "false");
                tvStatusBar.setText("当前状态栏状态：false");
                showToast("设置状态栏的状态为：关闭！");
                break;
            case R.id.bt_version_v1:
                setVersionV1();
                break;
            case R.id.bt_version_v3:
                setVersionV3();
                break;
            case R.id.bt_set_box_num:
                setBoxNum();
                break;
            case R.id.bt_user_pwd:
                setUserPwd();
                break;
        }
        editor.apply();
    }

    private void setUserPwd() {
        String pwd1 = etUserPwd1.getText().toString();
        String pwd2 = etUserPwd2.getText().toString();
        if (!pwd1.equals("") && !pwd2.equals("")) {
            if (pwd1.length() >= 6 && pwd1.length() <= 12) {
                if (pwd1.equals(pwd2)) {
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("password", pwd1);
                    editor.apply();
                    showToast("密码设置成功!");
                } else {
                    showToast("两次输入的密码不一致！请重新输入！");
                    etUserPwd1.setText("");
                    etUserPwd2.setText("");
                }
            } else {
                showToast("密码位数必须在6-12位之间！");
            }
        } else {
            showToast("输入的密码不能为空！");
        }
    }

    /**
     * @Param: []
     * @return: void
     * @Description: This is a method.
     * 设置Box数目
     */
    private void setBoxNum() {
        if (etValue(etBoxNum, 16)) {
            int n = Integer.valueOf(etBoxNum.getText().toString());
            SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
            editor.putInt("boxNum", n);
            editor.apply();
            CtrlInfo.boxNum = n;
            tvBoxNum.setText("当前箱柜最大数：" + n);
        }
    }

    private void setVersionV1() {
        new AlertDialog.Builder(this)
                .setTitle("警告！！")
                .setMessage("是否更改协议版本为：1.0？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvVersion.setText("当前协议版本：1.0");
                        showToast("当前协议版本：1.0");
                        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
                        editor.putString("version", "1.0");
                        editor.apply();
                    }
                })
                .create().show();
    }

    private void setVersionV3() {
        new AlertDialog.Builder(this)
                .setTitle("警告！！")
                .setMessage("是否更改协议版本为：3.0？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvVersion.setText("当前协议版本：3.0");
                        showToast("当前协议版本：3.0");
                        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
                        editor.putString("version", "3.0");
                        editor.apply();
                    }
                })
                .create().show();
    }


    private void setFaucetNum() {
        if (etValue(etFaucetNum, 32)) {
            SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
            editor.putInt("faucetNum", Integer.valueOf(etFaucetNum.getText().toString()));
            editor.apply();
            SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
            String s = "最大水控机数量为：" + sharedPreferences.getInt("faucetNum", 32);
            tvFaucetNum.setText(s);
            CtrlInfo.faucetNum = Integer.valueOf(etFaucetNum.getText().toString());
            showToast(s);
        }
    }

    private boolean etValue(EditText et, int n) {
        String s = et.getText().toString();
        if (!s.equals("")) {
            if (Integer.valueOf(s) <= n) {
                return true;
            } else {
                showToast("所填数字应该在0到" + n + "之间！");
                return false;
            }
        } else {
            showToast("所填数值不能为空！");
            return false;
        }
    }

    private void openRighttHair() {
        if (!btRightClick) {
            btRightClick = true;
            String rightnum = etRightHair.getText().toString();
            if (!rightnum.equals("")) {
                i = Integer.valueOf(rightnum);
                MyMqttService.hairuartsend(hairuartCtrl.get("rightopen"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (i > 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            handlerRighttime.sendEmptyMessage(i);
                        }
                        btRightClick = false;
                        MyMqttService.hairuartsend(hairuartCtrl.get("rightclose"));
                    }
                }).start();
            } else {
                showToast("不能为空！");
            }
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private void openLeftHair() {
        if (!btLeftClick) {
            btLeftClick = true;
            String leftnum = etLeftHair.getText().toString();
            if (!leftnum.equals("")) {
                i = Integer.valueOf(leftnum);
                MyMqttService.hairuartsend(hairuartCtrl.get("leftopen"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (i > 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            handlerLefttime.sendEmptyMessage(i);
                        }
                        btLeftClick = false;
                        MyMqttService.hairuartsend(hairuartCtrl.get("leftclose"));
                    }
                }).start();
            } else {
                showToast("不能为空！");
            }
        } else {
            handler.sendEmptyMessage(0);
        }

    }

    private void setUUID() {
        String uuid = etUuid.getText().toString();
        if (checkUuid(uuid)) {
            CtrlInfo.uuid = uuid;
            etId.setText(CtrlInfo.uuid);
            Toast.makeText(getApplicationContext(), "uuid设置成功！" + CtrlInfo.uuid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "uuid设置失败！请检查uuid格式是否正确！！！", Toast.LENGTH_SHORT).show();

        }
    }

    private void dialog(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("严重警告！！");
        builder.setMessage("本操作会改变当前模式为:" + s + "模式，是否继续？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adminMode = CtrlInfo.mode;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (adminMode) {
                    case CtrlInfo.MODE_BATH:
                        if (setSetting(CtrlInfo.MODE_BATH)) {
                            CtrlInfo.mode = CtrlInfo.MODE_BATH;
                            showToast("更改模式成功！当前模式为：" + CtrlInfo.mode);
                            adminTvMode.setText("当前模式：" + CtrlInfo.mode);
                        } else {
                            showToast("更改模式失败！请联系管理员!模式：" + CtrlInfo.mode);
                        }
                        break;
                    case CtrlInfo.MODE_HAIRDRYER:
                        if (setSetting(CtrlInfo.MODE_HAIRDRYER)) {
                            CtrlInfo.mode = CtrlInfo.MODE_HAIRDRYER;
                            adminTvMode.setText("当前模式：" + CtrlInfo.mode);
                            showToast("更改模式成功！当前模式为：" + CtrlInfo.mode);
                        } else {
                            showToast("更改模式失败！请联系管理员!模式：" + CtrlInfo.mode);
                        }

                        break;
                    case CtrlInfo.MODE_MIXED:
                        if (setSetting(CtrlInfo.MODE_MIXED)) {
                            CtrlInfo.mode = CtrlInfo.MODE_MIXED;
                            adminTvMode.setText("当前模式：" + CtrlInfo.mode);
                            showToast("更改模式成功！当前模式为：" + CtrlInfo.mode);
                        } else {
                            showToast("更改模式失败！请联系管理员!模式：" + CtrlInfo.mode);
                        }
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean setSetting(String mode) {
        try {
            JSONObject json = new JSONObject();
            json.put("uuid", CtrlInfo.uuid);
            json.put("mode", mode);
            File file = new File(Environment.getExternalStorageDirectory(), "SuperBathInfo.txt");
            FileOutputStream fos = new FileOutputStream(file);// 创建输出流对象
            fos.write(json.toString().getBytes());// 向文件中写入信息
            fos.close();  //关闭输出流对象
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
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
                                    js.put("mode", CtrlInfo.mode);
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

    //网络检测
    @OnClick(R.id.bt_sys_net)
    public void onViewClicked() {
        showToast("检测网络！");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String baseURL = "http://lz.hydream.cn/available.htm";
                boolean connect = getResultForHttpGet(baseURL);
                if (connect) {
                    tvSysNet.post(new Runnable() {
                        @Override
                        public void run() {
                            tvSysNet.setText("网络连接畅通！");
                        }
                    });
                } else {
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


    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public String edittextget(EditText et) {
        String s = et.getText().toString();
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(), "设备号不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(s) > 0 && Integer.valueOf(s) <= CtrlInfo.faucetNum) {
            return s;
        } else {
            s = "";
            Toast.makeText(getApplicationContext(), "设备号应该在1到" + CtrlInfo.faucetNum + "之间", Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy: AdminActivity 销毁");
    }
}
