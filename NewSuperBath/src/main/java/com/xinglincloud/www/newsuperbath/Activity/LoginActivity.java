package com.xinglincloud.www.newsuperbath.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.Toast;
import com.xinglincloud.www.newsuperbath.Base.Box;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.Key.KeyboardUtil;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.MyDialogUtils;
import com.xinglincloud.www.newsuperbath.Util.RandomNumUtils;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import java.lang.reflect.Method;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends FullScreenActivity {

    @BindView(R.id.tc_login)
    TextClock tcLogin;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.bt_pass)
    Button btPass;
    @BindView(R.id.ly_login)
    RelativeLayout lyLogin;

    KeyboardUtil keyboardUtil;
    private String pagestr;
    private static final String TAG = "LoginActivity";
    int time = 10;//关闭页面时间变量
    boolean isVer = false;//密码可见标志
    Thread threadtime;
    boolean isTrue = true;//倒计时线程标志

    private boolean sendClick = false;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //初始化键盘
        keyboardUtil = new KeyboardUtil(LoginActivity.this, false);
        keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
            @Override
            public void onOkClick() {
                send();
            }
        });
        Intent intent = getIntent();
        pagestr = intent.getStringExtra("page");
        viewinit();
        timerclock();
        processbar();

    }

    private void processbar() {
        myDialog = MyDialogUtils.createLoadingDialog(this, "加载中...");
    }

    private void viewinit() {
        tcLogin.bringToFront();
        //开启键盘
        keyboardUtil.attachTo(etPassword);
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP:             //键盘松开
                        //112清除，47开始，29箱柜，43结束,66确定
                        if (i == 112) {
                            etPassword.setText("");
                        } else if (i == 66) {
                            send();
                        }
                        break;
                    case KeyEvent.ACTION_DOWN:          //键盘按下
                        break;
                    default:
                        break;
                }
                time = 10;
                return false;
            }
        });
        lyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 10;
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                time = 10;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.i(TAG, "输入文本之前的状态");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.i(TAG, "输入文字后的状态");
            }
        });
        //隐藏密码
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        disableShowSoftInput();
    }

    @OnClick({R.id.et_password, R.id.bt_sure, R.id.bt_cancel, R.id.bt_pass, R.id.ly_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_password:
                keyboardUtil.attachTo(etPassword);//设置键盘可见
                break;
            case R.id.bt_sure:
                send();
                break;
            case R.id.bt_cancel:
                isTrue = false;
                finish();
                break;
            case R.id.bt_pass:
                passState();
                break;
            case R.id.ly_login:
                time = 10;
                break;
        }
    }

    //切换密码是否可见
    private void passState() {
        if (!isVer) {
            btPass.setBackgroundResource(R.drawable.xianshi);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVer = true;
        } else {
            btPass.setBackgroundResource(R.drawable.yincang);
            isVer = false;
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void disableShowSoftInput() {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(etPassword, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(etPassword, false);
        } catch (Exception e) {
        }
    }

    //关闭页面倒计时函数
    public void timerclock() {
        threadtime = new Thread() {
            @Override
            public void run() {
                while (isTrue) {
                    if (time < 0) {
                        finish();
                        break;
                    } else {
                        time--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };
        threadtime.start();
    }

    //通讯成功或者失败更新界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                LogUtil.i(TAG, "handleMessage: 通讯成功！即将关闭页面！");
                MyDialogUtils.getTextView().setText("密码正确！即将关闭页面！");
                MediaPlayer mMediaPlayer;
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio_turn_on_success);
                mMediaPlayer.start();
//                btBlocking = false;
            } else if (msg.what == 102) {
                LogUtil.i(TAG, "handleMessage: 密码错误！请再次尝试！");
                MyDialogUtils.getTextView().setText("密码错误！请再次尝试！");
                MediaPlayer mMediaPlayer;
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio_password_err);
                mMediaPlayer.start();
//                btBlocking = false;
            } else if (msg.what == 103) {
                LogUtil.i(TAG, "handleMessage: 密码正确！结束成功");
                MyDialogUtils.getTextView().setText("密码正确！结束成功！");
                MediaPlayer mMediaPlayer;
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio_password_finish);
                mMediaPlayer.start();
//                btBlocking = false;
            } else if (msg.what == 100) {
                MyDialogUtils.getTextView().setText("网络访问超时！请联系管理员！");
                MediaPlayer mMediaPlayer;
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio_connect_timeout);
                mMediaPlayer.start();
//                btBlocking = false;
            } else if (msg.what == 99) {
                MyDialogUtils.closeDialog(myDialog);
            } else if (msg.what == 0) {
                MyDialogUtils.getTextView().setText("加载中...");
            }
        }
    };

    //点击确认进行通讯
    public void send() {
        if (!sendClick) {
            sendClick = true;
            if (etPassword.getText().length() < 6) {
                Toast.makeText(LoginActivity.this, "密码不能小于6位！", Toast.LENGTH_SHORT).show();
                time = 10;
                sendClick = false;
            } else {
                time = 20;
                mHandler.sendEmptyMessage(0);
                myDialog.show();
                keyboardUtil.hideKeyboard();
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int count = 150;//通讯延时时间
                        Message msg = new Message();
                        com.xinglincloud.www.newsuperbath.Util.LogUtil.i(TAG, "run: CtrlInformation.msgSign" + CtrlInfo.bathMsgSign + "+," + msg.what);
                        while (count > 0) {
                            if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_Right)) {
                                CtrlInfo.bathMsgSign = CtrlInfo.None;
                                LogUtil.i(TAG, "run:在线程检查中 CtrlInformation.msgSign的值：" + CtrlInfo.bathMsgSign);
                                if (pagestr.equals("openwater")) {
                                    LogUtil.i(TAG, "只是显示密码正确的页面");
                                } else if (pagestr.equals("openbox")) {
                                    //在这里处理箱柜相关的程序,进行串口开启
                                    LogUtil.i(TAG, "显示密码正确的页面，并且打开箱柜");
                                    Box box = new Box();
                                    int n = RandomNumUtils.getRandomNum(CtrlInfo.boxNum);
                                    box.setBoxId(n);
                                    box.setPasswrod(CtrlInfo.openPassword);
                                    if(!CtrlInfo.openPassword.equals("default")){
                                        if (box.save()) {
                                            LogUtil.i(TAG, "run:数据库， 存储成功");
                                            //存储成功后重置openPassword密码，打开箱柜
                                            MyMqttService.uartOpenOneBox(n);
                                            CtrlInfo.openPassword = "default";
                                        } else {
                                            LogUtil.i(TAG, "run:数据库， 存储失败");
                                        }
                                    }else {
                                        RandomNumUtils.removeRandomNum(n);
                                    }
                                }
                                mHandler.sendEmptyMessage(101);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                time = 0;
                                sendClick = false;
                                break;
                            } else if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_Right_Finish)) {
                                CtrlInfo.bathMsgSign = CtrlInfo.None;
                                LogUtil.i(TAG, "run: 在线程检查中进入message  pwdcorrectfinish");
                                mHandler.sendEmptyMessage(103);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                time = 0;
                                sendClick = false;
                                break;
                            } else if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_WRONG)) {
                                CtrlInfo.bathMsgSign = CtrlInfo.None;
                                LogUtil.i(TAG, "run: 在线程检查中进入message  pwderro");
                                mHandler.sendEmptyMessage(102);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendEmptyMessage(99);
                                sendClick = false;
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count--;
                        }
                        if (count <= 0) {
                            mHandler.sendEmptyMessage(100);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        time = 0;
                    }
                }).start();
                if (pagestr.equals("openwater")) {
                    LogUtil.i(TAG, "onClick: openwater界面确认被点击");
                    MyMqttService.publishMessage(JsonUtil.enSendOpenRequestBath(etPassword.getText().toString()));
                } else if (pagestr.equals("openbox")) {
                    LogUtil.i(TAG, "onClick: openbox界面确认被点击");
                    MyMqttService.publishMessage(JsonUtil.enSendOpenRequestBath(etPassword.getText().toString()));
                } else if (pagestr.equals("closewater")) {
                    LogUtil.i(TAG, "onClick: closewater界面确认被点击");
                    MyMqttService.publishMessage(JsonUtil.enSendCloseRequestBath(etPassword.getText().toString()));
                }
            }
        } else {
            LogUtil.i(TAG, "send: Send已经被操作了，忽略");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTrue = false;
        if (myDialog.isShowing() && myDialog != null) {
            MyDialogUtils.closeDialog(myDialog);
        }
    }
}
