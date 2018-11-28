package com.klcxkj.quzhixiaoyuanbuletoothjar;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooronjar.BluetoothService;
import com.example.jooronjar.DealRateInfo;
import com.example.jooronjar.DownRateInfo;
import com.example.jooronjar.utils.AnalyTools;
import com.example.jooronjar.utils.CMDUtils;
import com.example.jooronjar.utils.DigitalTrans;
import com.example.jooronjar.utils.WaterCodeListener;
import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;
import com.klcxkj.quzhixiaoyuanbuletoothjar.util.Common;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.TimeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.ref.WeakReference;
/**
 * autor:OFFICE-ADMIN
 * time:2018/4/17
 * email:yinjuan@klcxkj.com
 * description: 用户用水
 */
public class MainActivity extends Activity implements WaterCodeListener{



    private BluetoothService mbtService = null;
    private BluetoothAdapter bluetoothAdapter;

    private ProgressBar progressBar;
    private ImageView imageView;
    String MAC ="";
    private TextView leftTxt,centerTxt,rightTxt;
    private TextView device_connect_state_txt;
    private int mStatus =0;
    private TimeView timeView;
    private int num =1;

    private boolean isDestory =false;
    private Handler myHandle;
    public static final int MSG_AUTO_CONNECT_COMPLETED = 0x09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initdata();
        initview();
        AnalyTools.setWaterCodeLisnter(this);

        mbtService = BluetoothService.sharedManager();
        mbtService.setHandlerContext(mHandler);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        statusFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);// 状态改变
        registerReceiver(mStatusReceive, statusFilter);

        myHandler = new MyHandler(this);
        connDecive();  //连接设备


        updateUI();
        bindclick();
        EventBus.getDefault().register(this);
    }



    private void connDecive() {
        if (mbtService.getState() ==BluetoothService.STATE_CONNECTED){
            CMDUtils.chaxueshebei(mbtService,true);
        }else {
            //取消扫描
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
                Log.d("MainActivity", "bluetoothAdapter.isDiscovering():" + bluetoothAdapter.isDiscovering());
            }
            mbtService.connect(bluetoothAdapter.getRemoteDevice(MAC));
        }
    }

    private void initview() {
        leftTxt =findViewById(R.id.start_search);
        centerTxt =findViewById(R.id.top_menu_title);
        rightTxt =findViewById(R.id.end_search);
        leftTxt.setText("＜ 返回");
        centerTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("设备登记");
        leftTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               skipUI();

            }
        });
        timeView =findViewById(R.id.washing_time);
        imageView =findViewById(R.id.device_state_img);
        progressBar =findViewById(R.id.progressbar);
        device_connect_state_txt =findViewById(R.id.device_connect_state_txt);
    }

    private void skipUI(){

        Intent intent =new Intent(MainActivity.this,Main2Activity.class);
        intent.putExtra("deciveMac",MAC);
        startActivity(intent);
        finish();
    }

    private void initdata() {
        Intent in =getIntent();
        MAC =in.getStringExtra("deciveMac");
        Log.d("MainActivity","设备的mac地址：="+MAC);
    }

    private void bindclick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mStatus){
                    case 31: //连接成功
                        showProgress();
                        startdDownfate( mprid,mBuffer,tac_Buffer);
                        break;
                    case 32: //结束费率
                        showProgress();
                        CMDUtils.jieshufeilv(mbtService,true);
                        break;
                    case 33:
                        startDeal(mprid,mdecived,mBuffer,tac_Buffer);
                        showProgress();
                        break;
                    case 35:
                        skipUI();
                        break;
                    case 41://配对失败
                        showProgress();
                        connDecive();  //连接设备
                        break;
                    case 42: //连接失败
                        showProgress();

                        connDecive();  //连接设备
                        break;
                    case 43: //断开连接
                        showProgress();
                        connDecive();  //连接设备
                        break;
                }
            }
        });
    }


    private void updateUI(){
        AnimationDrawable animationDrawable;
        switch (mStatus){
            case 0: //正在连接
                showProgress();
                break;
            case 31: //连接成功
                hideProgress();
                imageView.setImageResource(R.mipmap.dryer_connected);
                device_connect_state_txt.setText("开始使用");
                break;
            case 32: //下发费率成功
                hideProgress();
                imageView.setImageResource(R.drawable.animation_water);//图标R.drawable.animation_washing
                 animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                device_connect_state_txt.setText("结束使用");
                break;
            case 33://洗衣机的连接成功
                hideProgress();
                imageView.setImageResource(R.mipmap.dryer_connected);
                device_connect_state_txt.setText("开始使用");
                break;
            case 34:
                break;
            case 35: //未登记

                break;
            case 36://洗衣机开始交易
                imageView.setImageResource(R.drawable.animation_washing);//图标R.drawable.animation_washing
                 animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
                device_connect_state_txt.setText("洗衣中");
                hideProgress();
                timeView.setVisibility(View.VISIBLE);
                timeView.setTime(times);
                break;
            case 41: //配对失败
                hideProgress();
                device_connect_state_txt.setText("点击重试");
                break;
            case 42: //连接失败
                hideProgress();
                device_connect_state_txt.setText("点击重试");
                break;
            case 43: //断开连接
                imageView.setImageResource(R.mipmap.dryer_unconnected);
                device_connect_state_txt.setText("点击重连");
                timeView.setVisibility(View.GONE);
                break;
        }
    }

    private void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
        imageView.setEnabled(false);
    }
    private void hideProgress(){
        progressBar.setVisibility(View.GONE);
        imageView.setEnabled(true);
    }
    private BroadcastReceiver mStatusReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:

                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:

                }
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:// 正在配对
                        Log.d("MainActivity", "正在配对");
                        break;
                    case BluetoothDevice.BOND_BONDED:// 配对结束
                        Log.d("MainActivity", "配对结束");
                        mbtService.connect(device);

                        break;
                    case BluetoothDevice.BOND_NONE:// 取消配对/未配对

                        Log.d("MainActivity", "取消配对");
                        Toast.makeText(MainActivity.this, "配对失败", Toast.LENGTH_SHORT).show();
                        mStatus =41; //配对失败
                        break;
                    default:
                        break;
                }

            }

        }
    };



    private int i =0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_NO_XIAFACALLBACK:  //下发

                    break;

                case BluetoothService.MESSAGE_NO_JIESHUCALLBACK: //结束费率

                    break;

                case BluetoothService.MESSAGE_STATE_NOTHING: //状态通知
                    if (mbtService.getState()==BluetoothService.STATE_CONNECTED){
                        mbtService.stop();
                    }
                    break;

                case BluetoothService.MESSAGE_STATE_CHANGE: //状态变更
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED: //连上

                            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();


                           CMDUtils.chaxueshebei(mbtService,true);
                           // mbtService.startTime();
                           // resetHandler();
                            break;
                        case BluetoothService.STATE_CONNECTING: //连接中
                            Log.d("MainActivity", "链接中");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_CONNECTION_LOST:  //失连
                            mStatus =43; //断开连接
                            // break;
                        case BluetoothService.STATE_CONNECTION_FAIL: { //连接失败
                            mStatus =42;//连接失败
                            Log.d("MainActivity", "连接失败");
                            i++;
                            if (i<=3){
                                if (!isDestory){
                                    mbtService.connect(bluetoothAdapter.getRemoteDevice(MAC));
                                }

                            }


                        }
                        break;
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    updateUI();
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    break;
                case BluetoothService.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String result = DigitalTrans.bytesToHexString(readBuf);
                    //	processBluetoothDada(result);
                    //从蓝牙水表返回的数据
                    AnalyTools.analyWaterDatas(readBuf);
                    break;
                case BluetoothService.TIME_TXT:
                     diff =diff-1000;
                    Log.d("MainActivity", "diff:" + diff);

                    days = diff / (1000 * 60 * 60 * 24);
                    hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                    minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                    seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
                    if (minutes<10 && seconds<10){
                        setText("0"+minutes+":0"+seconds);
                    }else if (minutes<10 && seconds>10){
                        setText("0"+minutes+":"+seconds);
                    }else if (minutes>10 && seconds>10){
                        setText(minutes + ":" + seconds);
                    }else if (minutes>10 && seconds<10){
                        setText(minutes + ":0" + seconds);
                    }
                    break;
            }
        }
    };
    /**
     * 重置蓝牙断开的时间
     * time=60s
     */
    private void resetHandler(){
        mHandler.removeMessages(BluetoothService.MESSAGE_STATE_NOTHING);
        Message message = new Message();
        message.what = BluetoothService.MESSAGE_STATE_NOTHING;
        mHandler.sendMessageDelayed(message, 30000);
    }
    private void setText(String s) {
        Log.d("MainActivity", s);
    }

    private int diff =30000;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestory =true;
        if (mbtService != null) {
            if (mbtService.getState() == BluetoothService.STATE_CONNECTED) {
                mbtService.stop();
            }
        }
        Log.d("MainActivity", "onDestroy");
        unregisterReceiver(mStatusReceive);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventMsg(String msg){
        if (msg.equals("washingtimeover")){
            connDecive();
        }
    }


    private Dialog xiaofeiDialog;
    @Override
    public void caijishujuOnback(boolean b, String timeid, int mproductid, int mdeviceid,
                                 int maccountid, String accounttypeString, int usercount, String ykmoneyString, String consumeMoneString, String rateString, String macString) {

        if (b){
            Log.d("MainActivity", "数据采集成功");
            //

            // 清除消费数据
            try {
                //生成消费数据单据
                xiaofeiDialog = Common.createDingdanDialog(
                        MainActivity.this, timeid,
                        mproductid+"", mdeviceid+"",
                        maccountid+"", accounttypeString,
                        usercount+"", ykmoneyString,
                        consumeMoneString, rateString,
                        macString);
                CMDUtils.fanhuicunchu(mbtService, true, timeid,
                        mproductid, mdeviceid, maccountid, usercount);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Log.d("MainActivity", "数据采集失败");
        }
    }

    @Override
    public void jieshufeilvOnback(boolean b, int maccountid) {
        if (b){
            CMDUtils.caijishuju(mbtService,true);
        }

    }

    @Override
    public void xiafafeilvOnback(boolean b) {
        if (b){
            Toast.makeText(MainActivity.this, "下发费率成功", Toast.LENGTH_SHORT).show();
            mStatus =32; //下发费率后
            updateUI();
        }else {
            Toast.makeText(MainActivity.this, "下发费率失败", Toast.LENGTH_SHORT).show();
            mStatus =42;
            updateUI();
        }
    }

    @Override
    public void fanhuicunchuOnback(boolean b) {
        if (b){
            if (xiaofeiDialog != null) {
                xiaofeiDialog.show();
            }
            Toast.makeText(MainActivity.this, "清除缓存成功", Toast.LENGTH_SHORT).show();
           CMDUtils.chaxueshebei(mbtService,true);
        }
    }

    private int mprid;
    private int mdecived;
    private byte[] mBuffer;
    private byte[] tac_Buffer;
    private int times;
    private int dType;
    @Override
    public void chaxueNewshebeiOnback(boolean b, int charge, int mdeviceid, int mproductid, int maccountid,
                                      byte[] macBuffer, byte[] tac_timeBuffer, int macType,int lType, int constype, int macTime) {


        Log.d("MainActivity", "mdeviceid:" + mdeviceid);
        Log.d("MainActivity", "mproductid:" + mproductid);
        Log.d("-------", "maccountid:" + maccountid);
        Log.d("MainActivity", "macType:" + macType);
        Log.d("MainActivity", "lType:" + lType);
        Log.d("MainActivity", "charge:" + charge);
        mprid=mproductid;
        times =macTime;
        mdecived =mdeviceid;
        mBuffer =macBuffer;
        tac_Buffer =tac_timeBuffer;
        dType =constype;
        wtype =macType+"&"+lType;
        Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
        if (mproductid ==0){
            Toast.makeText(MainActivity.this, "此设备未登记，请先登记", Toast.LENGTH_SHORT).show();
            mStatus=35;
            updateUI();
            return;
        }
        switch (macType){
            case 0:  //水表
                centerTxt.setText("热水表");
                switch (charge){
                    case 0:
                        mStatus =31; //连接成功
                        updateUI();


                        break;
                    case 1:
                        mStatus=32;
                        updateUI();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "刷卡消费中", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        CMDUtils.caijishuju(mbtService,true);
                        break;
                }
                break;
            case 1://饮水机
                centerTxt.setText("饮水机");
                switch (charge){
                    case 0:
                        mStatus =31; //连接成功
                        updateUI();

                        break;
                    case 1:
                        mStatus=32;
                        updateUI();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "刷卡消费中", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        CMDUtils.caijishuju(mbtService,true);
                        break;
                }
                break;
            case 2://洗衣机
                centerTxt.setText("洗衣机");
                switch (charge){
                    case 0:
                        mStatus=33;
                        updateUI();


                        break;
                    case 1:
                        mStatus=36;
                        updateUI();
                        timeView.setTime(macTime);
                        break;
                    case 3:
                        CMDUtils.caijishuju(mbtService,true);
                        break;
                }
                break;
            case 3: //吹风机
                centerTxt.setText("吹风机");
                switch (charge){
                    case 0:
                        mStatus =31; //连接成功
                        updateUI();

                        break;
                    case 1:
                        mStatus=32;
                        updateUI();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "刷卡消费中", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        CMDUtils.caijishuju(mbtService,true);
                        break;
                }
                break;
            case 4:
                centerTxt.setText("充电器");
                switch (charge){
                    case 0:
                        mStatus =31; //连接成功
                        updateUI();

                        break;
                    case 1:
                        mStatus=32;
                        updateUI();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "刷卡消费中", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        CMDUtils.caijishuju(mbtService,true);
                        break;
                }
                break;
            case 5:
                centerTxt.setText("空调");
                break;
            default:
                centerTxt.setText("其他");
                break;
        }

    }

    /**
     * 下发费率
     */
    private void startdDownfate(int mproductid,byte[] macBuffer,byte[] tac_timeBuffer){
        DownRateInfo downRateInfo = new DownRateInfo();
        downRateInfo.ConsumeDT = DigitalTrans.getTimeID();  //时间
        downRateInfo.UseCount = 100;	//个人账号使用次数
        downRateInfo.PerMoney = 5000;	//预扣金额
        downRateInfo.ParaTypeID = 1;  //1标准水表2阶梯收费
        downRateInfo.Rate1 = 10;	//费率1
        downRateInfo.Rate2 = 500;
        downRateInfo.Rate3 = 500;
        downRateInfo.MinTime = 2;		//保底消费时间
        downRateInfo.MinMoney = 5;		//保底消费金额
        downRateInfo.ChargeMethod = 0;	//计费方式0 /17（16进制 0x00计时 0x11计量）
        downRateInfo.MinChargeUnit = 6;  //计费单位
        downRateInfo.AutoDisConTime = 12;	//自动断开时间（秒），6的倍数

        try {
            CMDUtils.xiafafeilv(mbtService, true, downRateInfo,
                    mproductid, 10001,2,macBuffer,tac_timeBuffer);

        } catch (IOException e1) {
            e1.printStackTrace();

        }
    }
    private String wtype;
    private void startDeal(int mproductid, int mdeviceid, byte[] macBuffer, byte[] tac_timeBuffer) {
        int tims =180;
        DealRateInfo dealRateInfo =new DealRateInfo();
        dealRateInfo.timeId =DigitalTrans.getTimeID();
        dealRateInfo.usecount =1;
        dealRateInfo.MacType =wtype;
        dealRateInfo.Constype =2; //扣费方式
        dealRateInfo.ykMoney =1000; //钱
        dealRateInfo.YkTimes =tims; //时间
        dealRateInfo.WRate1 =0;
        dealRateInfo.ChargeMethod =33; //计费方式
        dealRateInfo.MinChargeUnit =1; //计量单位
        dealRateInfo.AutoDisConTime =10000;
        dealRateInfo.ERate1 =0;
        dealRateInfo.GRate1 =0;
        dealRateInfo.ERate2 =0;
        dealRateInfo.Grate2 =0;
        dealRateInfo.ERate3 =0;
        dealRateInfo.Grate3 =0;
        dealRateInfo.fullw =0;
        dealRateInfo.fullTime =0;
        times =tims;
        try {
            CMDUtils.dealStart(mbtService,dealRateInfo,mproductid,180,2,macBuffer,tac_timeBuffer,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startDeal(boolean b) {
        if (b){
            Log.d("MainActivity", "开始交易");
            mStatus =36;
            updateUI();
        }else {
            Log.d("MainActivity", "交易开始失败");
        }
    }

    @Override
    public void stopDeal(boolean b) {
        if (b){
            Log.d("MainActivity", "结束交易");
            CMDUtils.caijishuju(mbtService,true);
        }
    }

    private MyHandler myHandler;
    private static class MyHandler extends Handler {
        public static final int MSG_RECEIVED_SPEED = 0x01;
        public static final int MSG_RECEIVED_STRING = 0x02;
        public static final int MESSAGE_CLEAR = 0x03;
        public static final int MSG_AUTO_WRITE_STARTED = 0x04;
        public static final int MSG_AUTO_WRITE_SPEED = 0x05;
        public static final int MSG_AUTO_WRITE_COMPLETED = 0x06;
        public static final int MSG_AUTO_CONNECT_STARTED = 0x07;
        public static final int MSG_AUTO_CONNECT = 0x08;
        public static final int MSG_AUTO_CONNECT_COMPLETED = 0x09;

        private final WeakReference<MainActivity> host;

        public MyHandler(MainActivity view) {
            host = new WeakReference<MainActivity>(view);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_AUTO_CONNECT_COMPLETED:


                    break;
            }
            super.handleMessage(msg);
        }
    }

}
