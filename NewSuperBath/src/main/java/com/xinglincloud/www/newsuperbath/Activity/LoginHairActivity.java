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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.Key.KeyboardUtil;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.MyDialogUtils;
import java.lang.reflect.Method;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginHairActivity extends FullScreenActivity {

    @BindView(R.id.hair_tc_login)
    TextClock hairTcLogin;
    @BindView(R.id.hair_imageView)
    ImageView hairImageView;
    @BindView(R.id.hair_et_mobile)
    EditText hairEtMobile;
    @BindView(R.id.hair_et_password)
    EditText hairEtPassword;
    @BindView(R.id.hair_bt_pass)
    Button hairBtPass;
    @BindView(R.id.hair_bt_sure)
    Button hairBtSure;
    @BindView(R.id.hair_bt_cancel)
    Button hairBtCancel;

    KeyboardUtil keyboardUtil;
    @BindView(R.id.hair_ly)
    RelativeLayout hairLy;
    private String hair_device;
    private static final String TAG = "LoginHairActivity";
    private static final String hair_a_time = "180";
    private static final String hair_b_time = "300";
    private Dialog myDialog;

    int time = 10;//关闭页面时间变量
    boolean isVer = false;//密码可见标志
    Thread threadtime;
    boolean isTrue = true;//倒计时线程标志
    private boolean sendClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_hair);
        ButterKnife.bind(this);

        keyboardUtil = new KeyboardUtil(LoginHairActivity.this, false);

        Intent intent = getIntent();
        hair_device = intent.getStringExtra("hair_device");
        LogUtil.i(TAG, "onCreate: 请求开启的吹风机参数是："+hair_device);
        viewinit();
        timerclock();
        processbar();
    }
    private void processbar() {

        myDialog = MyDialogUtils.createLoadingDialog(this, "加载中...");
    }
    private void viewinit() {
        hairEtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.attachTo(hairEtPassword);
                return false;
            }
        });
        hairEtMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.attachTo(hairEtMobile);
                return false;
            }
        });


        hairTcLogin.bringToFront();
        //开启键盘
        keyboardUtil.attachTo(hairEtMobile);
        //监测外接键盘  mobile
        hairEtMobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                LogUtil.i(TAG, "onKey: 按键的值" + i);
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP:
                        if (i == 112) {
                            hairEtMobile.setText("");
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
        //监测外接键盘 password
        hairEtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                LogUtil.i(TAG, "onKey: 按键的值" + i);
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP:
                        if (i == 112) {
                            hairEtPassword.setText("");
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
        //输入手机号和密码的时候time归10
        hairEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i(TAG, "输入文字中的状态，count是输入字符数");
                time = 10;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.i(TAG, "输入文本之前的状态");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.i(TAG, "输入文字后的状态");
                if(s.length()==11){
                    keyboardUtil.attachTo(hairEtPassword);
                    hairEtPassword.requestFocus();
                    hairEtPassword.setFocusable(true);
                }
            }
        });
        hairEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i(TAG, "输入文字中的状态，count是输入字符数");
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
        //        隐藏密码
        hairEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        disableShowSoftInput();
        hairEtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //要做的事
                    LogUtil.i(TAG, "onEditorAction: 确认被点击");
                }
                return false;
            }
        });
    }

    @OnClick({R.id.hair_bt_pass, R.id.hair_bt_sure, R.id.hair_bt_cancel,R.id.hair_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hair_bt_pass:
                passState();
                break;
            case R.id.hair_bt_sure:
                send();
                break;
            case R.id.hair_bt_cancel:
                isTrue = false;
                finish();
                break;
            case R.id.hair_ly:
                time = 10;
                break;
        }
    }

    //切换密码是否可见
    private void passState() {
        if (!isVer) {
            hairBtPass.setBackgroundResource(R.drawable.xianshi);
            hairEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVer = true;

        } else {
            hairBtPass.setBackgroundResource(R.drawable.yincang);
            isVer = false;
            hairEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void disableShowSoftInput() {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(hairEtPassword, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(hairEtPassword, false);
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
                LogUtil.i(TAG, "run: login页面倒计时线程结束");

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
                mMediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.audio_turn_on_success);
                mMediaPlayer.start();
                time = 0;
            } else if (msg.what == 102) {
                LogUtil.i(TAG, "handleMessage: 密码错误！请再次尝试！");
                MyDialogUtils.getTextView().setText("用户名或密码错误！请再次尝试！");
                time = 10;
                MediaPlayer mMediaPlayer;
                mMediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.audio_user_or_pwd_err_);
                mMediaPlayer.start();
            } else if (msg.what == 103) {
                LogUtil.i(TAG, "handleMessage: 余额不足！");
                MyDialogUtils.getTextView().setText("余额不足！");
                time = 10;
                MediaPlayer mMediaPlayer;
                mMediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.audio_no_money);
                mMediaPlayer.start();
            } else if (msg.what == 100) {
                MyDialogUtils.getTextView().setText("网络访问超时！请联系管理员！");
                time = 2;
                MediaPlayer mMediaPlayer;
                mMediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.audio_connect_timeout);
                mMediaPlayer.start();
            }else if (msg.what == 99) {
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
            if (hairEtMobile.getText().length() != 11) {
                Toast.makeText(LoginHairActivity.this, "手机号码输入错误！", Toast.LENGTH_SHORT).show();
            } else {
                if (hairEtPassword.getText().length() < 6) {
                    Toast.makeText(LoginHairActivity.this, "密码不能小于6位！", Toast.LENGTH_SHORT).show();
                    time = 10;
                } else {
                    mHandler.sendEmptyMessage(0);
                    myDialog.show();
                    time = 20;
                    keyboardUtil.hideKeyboard();
                    CtrlInfo.hairdryerMsgSign = CtrlInfo.None;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int count = 150;//通讯延时时间
                            Message msg = new Message();
                            LogUtil.i(TAG, "run: CtrlInformation.msgSign" + CtrlInfo.hairdryerMsgSign + "+," + msg.what);
                            while (count > 0) {
                                if (CtrlInfo.hairdryerMsgSign.equals(CtrlInfo.PWD_Right)) {
                                    CtrlInfo.hairdryerMsgSign = CtrlInfo.None;
                                    LogUtil.i(TAG, "run:在线程检查中 CtrlInformation.msgSign的值：" + CtrlInfo.hairdryerMsgSign);
                                    mHandler.sendEmptyMessage(101);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                } else if (CtrlInfo.hairdryerMsgSign.equals(CtrlInfo.PWD_WRONG)) {
                                    CtrlInfo.hairdryerMsgSign = CtrlInfo.None;
                                    LogUtil.i(TAG, "run: 在线程检查中进入message  pwderro");
                                    mHandler.sendEmptyMessage(102);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mHandler.sendEmptyMessage(99);
                                    break;
                                }else if (CtrlInfo.hairdryerMsgSign.equals(CtrlInfo.INSUFFICIENT_BALANCE)) {
                                    CtrlInfo.hairdryerMsgSign = CtrlInfo.None;
                                    LogUtil.i(TAG, "run: 在线程检查中进入message  INSUFFICIENT_BALANCE");
                                    mHandler.sendEmptyMessage(103);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mHandler.sendEmptyMessage(99);
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
                            sendClick = false;
                            time = 2;
                        }
                    }).start();
                    //读取手机号密码
                    String mobile = hairEtMobile.getText().toString();
                    String password = hairEtPassword.getText().toString();
                    switch (hair_device) {
                        case "hair_left_a":
                            LogUtil.i(TAG, "send: hair_left_a设备发送开启请求...");
                            MyMqttService.publishMessage(JsonUtil.enSendOpenRequestHAIRDRYER("blower_01",
                                    mobile, password, hair_a_time));
                            break;
                        case "hair_left_b":
                            LogUtil.i(TAG, "send: hair_left_b设备发送开启请求...");
                            MyMqttService.publishMessage(JsonUtil.enSendOpenRequestHAIRDRYER("blower_01",
                                    mobile, password, hair_b_time));
                            break;
                        case "hair_right_a":
                            LogUtil.i(TAG, "send: hair_right_a设备发送开启请求...");
                            MyMqttService.publishMessage(JsonUtil.enSendOpenRequestHAIRDRYER("blower_02",
                                    mobile, password, hair_a_time));
                            break;
                        case "hair_right_b":
                            LogUtil.i(TAG, "send: hair_right_b设备发送开启请求...");
                            MyMqttService.publishMessage(JsonUtil.enSendOpenRequestHAIRDRYER("blower_02",
                                    mobile, password, hair_b_time));
                            break;
                    }
                }
            }
        } else{
            LogUtil.i(TAG, "send: Send已经被操作了，忽略");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time=0;
        if(myDialog.isShowing()&&myDialog!=null){
            MyDialogUtils.closeDialog(myDialog);
        }
    }
}
