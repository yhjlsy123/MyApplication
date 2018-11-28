package com.klcxkj.quzhixiaoyuanbuletoothjar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooronjar.BluetoothService;
import com.example.jooronjar.utils.AnalyAdminTools;
import com.example.jooronjar.utils.CMDUtils;
import com.example.jooronjar.utils.OnWaterAdminListener;
import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;

import java.io.IOException;
import java.util.Random;

/**
 * autor:OFFICE-ADMIN
 * time:2018/4/17
 * email:yinjuan@klcxkj.com
 * description: 管理员管理设备类
 */
public class Main2Activity extends AppCompatActivity implements OnWaterAdminListener{

    String MAC ="";
    private TextView tv;
    private RadioGroup water_group;
    private RadioGroup washing_group;
    private int bigType;
    private int littleType;
    private int mSize ; //新老设备的区分
    private LinearLayout water_layout;
    private LinearLayout washing_layout;
    private Button water_btn;
    private Button washing_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);// 11:29:28.248
        initdata();
        initview();
        bindclick();
        AnalyAdminTools.setWaterAdminListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mbtService = BluetoothService.sharedManager();
        mbtService.setHandlerContext(mHandler);

        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        statusFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);// 状态改变
        registerReceiver(mStatusReceive, statusFilter);


        if (mbtService.getState() ==BluetoothService.STATE_CONNECTED){
            CMDUtils.chaxueshebei(mbtService,true);
        }else {
            mbtService.connect(bluetoothAdapter.getRemoteDevice(MAC));
        }
        Log.d("Main2Activity", "onCreate");

    }



    private void bindclick() {
        water_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {

               switch (checkId){
                   case R.id.radioButton1: //水表
                       bigType =0;
                       littleType=1;
                       break;
                   case R.id.radioButton2: //饮水机
                       bigType =1;
                       littleType=2;
                       break;
                   case R.id.radioButton3: //吹风机
                       bigType =3;
                       littleType=47;
                       break;

               }
            }
        });
        washing_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                switch (checkId){
                    case R.id.radioButton5: //室内洗衣机
                        bigType =2;
                        littleType=46;
                        break;
                    case R.id.radioButton6: //走廊洗衣机
                        bigType =2;
                        littleType=45;
                        break;

                }
            }
        });
        //设备登记
        water_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connDecive();
            }
        });
        washing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connDecive();
            }
        });
    }
    private void connDecive() {
        if (mbtService.getState() ==BluetoothService.STATE_CONNECTED){

            CMDUtils.qingchushebei(mbtService,true);
        }else {
            mbtService.connect(bluetoothAdapter.getRemoteDevice(MAC));
        }
    }
    private void initview() {
        tv= (TextView) findViewById(R.id.textView4);
        washing_group = (RadioGroup) findViewById(R.id.radio_washing);
        water_group = (RadioGroup) findViewById(R.id.radio_water);
        water_layout = (LinearLayout) findViewById(R.id.layout_water);
        washing_layout = (LinearLayout) findViewById(R.id.layout_washing);
        washing_btn = (Button) findViewById(R.id.washing_btn);
        water_btn = (Button) findViewById(R.id.water_btn);
    }

    private void initdata() {
        Intent in =getIntent();
        MAC =in.getStringExtra("deciveMac");
        Log.d("MainActivity","设备的mac地址：="+MAC);
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
                        break;
                    default:
                        break;
                }

            }

        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_NO_XIAFACALLBACK:  //下发

                    break;

                case BluetoothService.MESSAGE_NO_JIESHUCALLBACK: //结束费率

                    break;

                case BluetoothService.MESSAGE_STATE_NOTHING: //状态通知

                    break;

                case BluetoothService.MESSAGE_STATE_CHANGE: //状态变更
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED: //连上
                            Toast.makeText(Main2Activity.this, "设备连接成功", Toast.LENGTH_SHORT).show();
                            CMDUtils.chaxueshebei(mbtService,true);
                            break;
                        case BluetoothService.STATE_CONNECTING: //连接中

                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_CONNECTION_LOST:  //失连
                            // break;
                        case BluetoothService.STATE_CONNECTION_FAIL: { //连接失败


                        }
                        break;
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    break;
                case BluetoothService.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    //从蓝牙水表返回的数据
                    AnalyAdminTools.analyWaterDatas(readBuf);
                    break;
                case 101:
                    Toast.makeText(Main2Activity.this, "成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    private BluetoothService mbtService = null;
    private BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mbtService != null) {
            if (mbtService.getState() == BluetoothService.STATE_CONNECTED) {
                mbtService.stop();
            }
        }
        Log.d("Main2Activity", "onDestroy");
        unregisterReceiver(mStatusReceive);
    }
    private void skipUI(){
        Intent intent =new Intent(Main2Activity.this,MainActivity.class);
        intent.putExtra("deciveMac",MAC);
        startActivity(intent);
        finish();
    }
    @Override
    public void settingDeciveType(boolean b) {
       if (b){
           Toast.makeText(Main2Activity.this, "设备登记成功", Toast.LENGTH_SHORT).show();
           skipUI();
       }

    }

    @Override
    public void qingchushebeiOnback(boolean b, int mproductid, int mdeviceid) {
      //  Toast.makeText(this, "clear:" + b, Toast.LENGTH_SHORT).show();
        if (b){
           int num =new Random().nextInt(1000000);
            try {
                CMDUtils.xiafaxiangmu(mbtService,num,1,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void xiafaxiangmuOnback(boolean b, int mproductids, int mdeviceids) {
      //  Toast.makeText(this, "peoject:" + b, Toast.LENGTH_SHORT).show();
        if (b){
            if (mSize ==28){
                if (bigType ==0 || littleType==0){
                    Toast.makeText(Main2Activity.this, "请选择登记类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    CMDUtils.settingDecive(mbtService,1,bigType,littleType,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Main2Activity.this, "设备登记成功", Toast.LENGTH_SHORT).show();
                skipUI();
            }

        }
    }

    @Override
    public void chaxueshebeiOnback(boolean b, int size, int macType) {
        Log.d("Main2Activity", "size:" + size);
        Log.d("Main2Activity", "macType:" + macType);

        mSize =size;
        if (mSize==28){
            tv.setText("设备预设置的类型：="+macType);
            if (macType ==2){
                water_layout.setVisibility(View.GONE);
                washing_layout.setVisibility(View.VISIBLE);
            }else {
                water_layout.setVisibility(View.VISIBLE);
                washing_layout.setVisibility(View.GONE);
            }
        }else if (mSize==23){
            water_layout.setVisibility(View.VISIBLE);
            water_group.setVisibility(View.VISIBLE);
            washing_layout.setVisibility(View.GONE);
            tv.setText("");
        }


    }


}
