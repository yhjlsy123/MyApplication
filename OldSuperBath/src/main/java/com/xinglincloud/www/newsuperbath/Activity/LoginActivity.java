package com.xinglincloud.www.newsuperbath.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.WeiboDialogUtils;

import java.lang.reflect.Method;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends FullScreenActivity {

    @BindView(R.id.bt_old_num_1)
    Button btOldNum1;
    @BindView(R.id.bt_old_num_2)
    Button btOldNum2;
    @BindView(R.id.bt_old_num_3)
    Button btOldNum3;
    @BindView(R.id.bt_old_num_4)
    Button btOldNum4;
    @BindView(R.id.bt_old_num_5)
    Button btOldNum5;
    @BindView(R.id.bt_old_num_6)
    Button btOldNum6;
    @BindView(R.id.bt_old_num_7)
    Button btOldNum7;
    @BindView(R.id.bt_old_num_8)
    Button btOldNum8;
    @BindView(R.id.bt_old_num_9)
    Button btOldNum9;
    @BindView(R.id.bt_old_num_del)
    Button btOldNumDel;
    @BindView(R.id.bt_old_num_0)
    Button btOldNum0;
    @BindView(R.id.bt_old_num_empty)
    Button btOldNumEmpty;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.bt_old_ok)
    Button btOldOk;
    @BindView(R.id.bt_old_cancel)
    Button btOldCancel;
    private int closetime = 10;
    private String pagestr;
    private Dialog mWeiboDialog,mdialog;
    private boolean btBlocking = false;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        disableShowSoftInput();
        Intent intent = getIntent();
        pagestr = intent.getStringExtra("page");
        Threadclose();
        processbar();
    }

    private void Threadclose() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int j= 0;
                while (closetime > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    j++;
                    if(j>10){
                        closetime--;
                        j=0;
                    }
                }
                LogUtil.i(TAG, "run: 页面关闭倒计时线程结束！");
                finish();
            }
        }).start();
    }

    @OnClick({R.id.bt_old_num_1, R.id.bt_old_num_2, R.id.bt_old_num_3, R.id.bt_old_num_4, R.id.bt_old_num_5, R.id.bt_old_num_6, R.id.bt_old_num_7, R.id.bt_old_num_8, R.id.bt_old_num_9, R.id.bt_old_num_del, R.id.bt_old_num_0, R.id.bt_old_num_empty, R.id.bt_old_ok, R.id.bt_old_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_old_num_1:
                add_num(1);
                break;
            case R.id.bt_old_num_2:
                add_num(2);
                break;
            case R.id.bt_old_num_3:
                add_num(3);
                break;
            case R.id.bt_old_num_4:
                add_num(4);
                break;
            case R.id.bt_old_num_5:
                add_num(5);
                break;
            case R.id.bt_old_num_6:
                add_num(6);
                break;
            case R.id.bt_old_num_7:
                add_num(7);
                break;
            case R.id.bt_old_num_8:
                add_num(8);
                break;
            case R.id.bt_old_num_9:
                add_num(9);
                break;
            case R.id.bt_old_num_del:
                add_num(-1);
                break;
            case R.id.bt_old_num_0:
                add_num(0);
                break;
            case R.id.bt_old_num_empty:
                add_num(-2);
                break;
            case R.id.bt_old_ok:
                send();
                break;
            case R.id.bt_old_cancel:
                finish();
                break;
        }
    }
    public void disableShowSoftInput() {

            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(etOldPwd, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(etOldPwd, false);
            } catch (Exception e) {
            }
    }
    private void processbar() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        mdialog = WeiboDialogUtils.createLoadingDialog(this);
    }

    private void add_num(int n) {
        closetime = 10;
        int start = etOldPwd.getSelectionStart();
        Editable editable = etOldPwd.getText();
        switch (n) {
            case -1:
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
                break;
            case -2:
                etOldPwd.setText("");
                break;
            default:
                editable.insert(start, Character.toString((char) (n + 48)));
                break;
        }
    }

    //通讯成功或者失败更新界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closetime = 15;
            if (msg.what == 101) {
                LogUtil.i(TAG, "handleMessage: 通讯成功！即将关闭页面！");
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.getTvDeviceId().setText(CtrlInfo.device_id+"号浴位");
                WeiboDialogUtils.getTvDeviceId().setVisibility(View.VISIBLE);
                WeiboDialogUtils.getImgText().setImageResource(R.drawable.loading_yes_text);
                WeiboDialogUtils.getImgState().setImageResource(R.drawable.loading_yes);
                mdialog.show();
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                btBlocking = false;
            } else if (msg.what == 102) {
                LogUtil.i(TAG, "handleMessage: 密码错误！请再次尝试！");
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.getImgText().setImageResource(R.drawable.loading_no_text);
                WeiboDialogUtils.getImgState().setImageResource(R.drawable.loading_no);
                mdialog.show();
                closetime = 10;
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                btBlocking = false;
            } else if (msg.what == 103) {
                LogUtil.i(TAG, "handleMessage: 结束成功！即将关闭页面！");
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.getImgText().setImageResource(R.drawable.loading_finish_text);
                WeiboDialogUtils.getImgState().setImageResource(R.drawable.loading_finish);
                mdialog.show();
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                btBlocking = false;
            } else if (msg.what == 100) {
                closetime = 4;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.getImgText().setImageResource(R.drawable.loading_lost_text);
                WeiboDialogUtils.getImgState().setImageResource(R.drawable.loading_lost);
                mdialog.show();
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                btBlocking = false;
            }else if(msg.what==99){
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.closeDialog(mdialog);
            }else if(msg.what==98){
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                WeiboDialogUtils.closeDialog(mdialog);
                closetime = 0;
            }else if(msg.what ==0){
                WeiboDialogUtils.getTextView().setText("加载中...");
            }
        }
    };

    //点击确认进行通讯
    public void send() {
        if (!btBlocking) {
            btBlocking = true;
            if (etOldPwd.getText().length() < 6) {
                Toast.makeText(LoginActivity.this, "密码不能小于6位！", Toast.LENGTH_SHORT).show();
                closetime = 10;
                btBlocking = false;
            } else {
                CtrlInfo.bathMsgSign = CtrlInfo.None;
                mHandler.sendEmptyMessage(0);
                mWeiboDialog.show();
                closetime = 20;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int count = 250;
                        while (count > 0) {
                            closetime = 15;
                            if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_Right)) {
                                LogUtil.i(TAG, "run:在线程检查中 CtrlInformation.msgSign的值：" + CtrlInfo.bathMsgSign);
                                mHandler.sendEmptyMessage(101);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendEmptyMessage(98);
                                break;
                            } else if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_FINISH)) {
                                LogUtil.i(TAG, "run: 在线程检查中进入message  pwderro");
                                mHandler.sendEmptyMessage(103);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendEmptyMessage(98);
                                break;
                            }else if (CtrlInfo.bathMsgSign.equals(CtrlInfo.PWD_WRONG)) {
                                LogUtil.i(TAG, "run: 在线程检查中进入message  pwderro");
                                mHandler.sendEmptyMessage(102);
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
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(99);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        closetime = 2;
                    }
                }).start();
                switch (pagestr) {
                    case "openwater":
                        LogUtil.i(TAG, "onClick: openwater界面确认被点击");
                        MyMqttService.publishMessage(JsonUtil.enSendOpenRequestBath(etOldPwd.getText().toString()));
                        break;
                    case "openbox":
                        LogUtil.i(TAG, "onClick: openbox界面确认被点击");
                        break;
                    case "closewater":
                        LogUtil.i(TAG, "onClick: closewater界面确认被点击");
                        MyMqttService.publishMessage(JsonUtil.enSendCloseRequestBath(etOldPwd.getText().toString()));
                        break;
                }
            }
        }else {
            LogUtil.i(TAG, "send: 已经点击了！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closetime=0;
        if(mWeiboDialog.isShowing()&&mWeiboDialog!=null){
           WeiboDialogUtils.closeDialog(mWeiboDialog);
           WeiboDialogUtils.closeDialog(mdialog);
        }
        if(mdialog.isShowing()&&mdialog!=null){
            WeiboDialogUtils.closeDialog(mdialog);
        }

    }
}
