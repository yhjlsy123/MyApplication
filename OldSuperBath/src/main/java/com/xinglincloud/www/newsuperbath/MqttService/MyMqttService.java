package com.xinglincloud.www.newsuperbath.MqttService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.xinglincloud.www.newsuperbath.Activity.BathActivity;
import com.xinglincloud.www.newsuperbath.Activity.MainActivity;
import com.xinglincloud.www.newsuperbath.Activity.UpdateActivity;
import com.xinglincloud.www.newsuperbath.Base.AutoWaterMeasurement;
import com.xinglincloud.www.newsuperbath.ComAssistant.SerialHelper;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.CtrlInformation.InstructionInfo;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.FileUtil;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.bean.ComBean;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MyMqttService extends Service {
    private static final String TAG = "nlgMqttService";

    private String subTopic;
    private String subUpdateTopic;
    private static final String pubTopic = "device_call";
    private static final String heartBeatTopic = "device_heartbeat";
    private static final String pubUpdateTopic = "dev_update_response";
    private static MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;

    private String serverUri;
    private String clientId;
    private static ConcurrentLinkedQueue<String> linkedList = new ConcurrentLinkedQueue<>();
    private boolean isTrue = true;
    private boolean queueThreadSign = true;
    private boolean timeoutThreadSign = true;
    private int secondtime = 0;
    private int i = 0;
    private int j = 0;
    private static HashMap<String, Boolean> hashMapDevices;
    private static SerialHelper ComA;
    private static String Com3 = "/dev/ttyS3";
    private static String BaudRate = "9600";
    Map<String, String> map;
    Thread queuethread = null;
    AutoWaterMeasurement measurement = new AutoWaterMeasurement();
    HashMap<String, String> pwdHashMap = new HashMap<>();
    boolean isStop = false;
    int checkoutNum = 0;
    HashMap<String, Integer> checkoutHashMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate: 运行了Service的onCreate（）方法" +
                ",uuid:" + CtrlInfo.uuid);
        mqttInit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, " onStartCommand： 运行了Service的onStartCommand");
        CtrlInfo.communication = true;
        CtrlInfo.appCtrl = true;
        return START_NOT_STICKY;
    }

    private void mqttInit() {
        if (CtrlInfo.uuid.equals("0000000000")) {
            try {
                JSONObject js = new JSONObject(FileUtil.getSavedUserInfo());
                LogUtil.i(TAG, "settingInit: 读取配置文件的信息是" + js.toString());
                CtrlInfo.uuid = js.getString("uuid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        subTopic = "device_" + CtrlInfo.uuid;
        subUpdateTopic = "dev_update_" + CtrlInfo.uuid;
        clientId = CtrlInfo.uuid;
        //emq服务器版本选择
        if (CtrlInfo.version.equals("3.0")) {
            serverUri = CtrlInfo.SERVER_URI_V3;
        } else {
            serverUri = CtrlInfo.SERVER_URI_V1;
        }
        quequeThead();
        hashMapDevices = new HashMap<>();
        //uart初始化
        uartinit();
        map = CtrlInfo.faucetHashmapInit(36);
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //连接成功
                if (reconnect) {
                    LogUtil.d(TAG, "connectComplete方法，重连成功，serverURI:" + serverURI);
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("restart"));
                    subscribeAllTopics();
                    CtrlInfo.connectState = true;
                    LogUtil.i(TAG, "connectComplete: mqtt连接标志connectStated的值为：" + CtrlInfo.connectState);
                } else {
                    LogUtil.d(TAG, "connectComplete方法，连接成功，但不是重连，serverURI:" + serverURI);
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("start"));
                    CtrlInfo.connectState = true;
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                //连接断开
                if (BathActivity.getTvNetState() != null) {
                    BathActivity.getTvNetState().setVisibility(View.VISIBLE);
                }
                CtrlInfo.connectState = false;
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                //订阅的消息送达，推送notify
                if (CtrlInfo.communication) {
                    messageCtrl(topic, message);
                } else {
                    LogUtil.i(TAG, "messageArrived: messageReicive通讯阻断，不接受");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(30);
        mqttConnectOptions.setKeepAliveInterval(60);
        mqttConnectOptions.setWill(heartBeatTopic, JsonUtil.heartBeatString("lost").getBytes(), 2, false);
        connectmqtt();
    }

    private void connectmqtt() {
        try {
            LogUtil.d(TAG, "onCreate: Connecting to " + serverUri);
            //开始连接
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtil.d(TAG, "onSuccess: Success to connect to " + serverUri);
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    CtrlInfo.connectState = true;
                    LogUtil.i(TAG, "onSuccess: mqtt连接标志connectStated的值为：" + CtrlInfo.connectState);
                    subscribeAllTopics();
                    heartBeatThread();
                    measurement.measurementThread();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //连接失败
                    LogUtil.d(TAG, "onFailure: Failed to connect to " + serverUri);
                    exception.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void messageCtrl(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        LogUtil.i(TAG, "messageCtrl，接收到了来自服务器的消息，主题：" + topic + ",内容：" + payload);
        if (j >= 10000) {
            j = 0;
        } else {
            j++;
        }
        try {
            if (!topic.equals(subUpdateTopic)) {
                JSONObject jsRev = new JSONObject(payload);
                String result = JsonUtil.deStr(jsRev.getString("head"), jsRev.getString("data"));
                LogUtil.i(TAG, "messageCtrl: 加入队列的result:" + result);
                if (result != null && !result.equals("DesErro") && !result.equals("Sha1Erro") && !result.equals("err")) {
                    linkedList.add(result);
                } else {
                    LogUtil.i(TAG, "messageCtrl: 接收的消息解析有误：" + result);
                }
            } else {
                //在这里处理更新操作
                try {
                    JSONObject jsRev = new JSONObject(payload);
                    JSONObject jsresult = new JSONObject(JsonUtil.deStr(jsRev.getString("head"), jsRev.getString("data")));
                    SharedPreferences sp = getSharedPreferences("update", MODE_PRIVATE);
                    Boolean is = sp.getBoolean("updateState", false);
                    if (!is) {
                        LogUtil.i(TAG, "messageCtrl: " + jsresult.toString());
                        String path = jsresult.getString("path");
                        String md5 = jsresult.getString("md5");
                        LogUtil.i(TAG, "messageCtrl: 最后的地址为：" + path + ",Md5:" + md5);
                        SharedPreferences.Editor editor = getSharedPreferences("update", MODE_PRIVATE).edit();
                        editor.putString("path", path);
                        editor.putString("md5", md5);
                        editor.apply();
                        Intent dialogIntent = new Intent(getBaseContext(), UpdateActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(dialogIntent);
                    } else {
                        //任务已经存在,驳回
                        publishMessage(pubUpdateTopic, JsonUtil.UpdateRsponseString("2"));
                    }
                } catch (Exception e) {
                    //未知错误
                    publishMessage(pubUpdateTopic, JsonUtil.UpdateRsponseString("1"));
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //订阅所有消息
    private void subscribeAllTopics() {
        subscribeToTopic(subTopic);
        subscribeToTopic(subUpdateTopic);
        LogUtil.i(TAG, "subscribeAllTopics: 订阅的主题是：" + subTopic);
    }

    /**
     * 订阅消息
     */
    public void subscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtil.d(TAG, "onSuccess: Success to Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtil.d(TAG, "onFailure: Failed to subscribe");
                }
            });
        } catch (MqttException ex) {
            LogUtil.d(TAG, "subscribeToTopic: Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * 发布消息
     */
    public static void publishMessage(String msg) {
        if (CtrlInfo.communication) {
            if (mqttAndroidClient.isConnected()) {
                try {
                    mqttAndroidClient.publish(pubTopic, msg.getBytes(), 2, false);
                    LogUtil.d(TAG, "publishMessage: Message Published: " + msg);
                    CtrlInfo.publishIsSure = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                LogUtil.i(TAG, "publishMessage: publish通讯阻断，不接受");
            }
        }
    }

    public static void publishMessage(String topic, String msg) {
        if (CtrlInfo.communication && mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.publish(topic, msg.getBytes(), 2, false);
                LogUtil.d(TAG, "publishMessage: 心跳包的Message Published: " + msg);
                CtrlInfo.publishIsSure = true;
            } catch (Exception e) {
                LogUtil.d(TAG, "publishMessage: 心跳包的Error Publishing: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            LogUtil.i(TAG, "publishMessage: publish通讯阻断，不接受");
        }
    }

    /*
     * 消息队列线程
     *
     * */
    public void quequeThead() {
        if (queuethread == null) {
            queuethread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (queueThreadSign) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            synchronized (linkedList) {
                                if (!linkedList.isEmpty() && isTrue && CtrlInfo.connectState) {
                                    isTrue = false;
                                    LogUtil.d(TAG, "run:linkedList的长度size：" + linkedList.size() + " linkedList是否为空:" + linkedList.peek());
                                    String result = linkedList.poll();
                                    if (result != null && !Objects.equals("", result)) {
                                        CtrlInfo.result = result;
                                        JSONObject js = new JSONObject(result);
                                        JSONArray jsSub = new JSONArray(js.getString("sub_devices"));
                                        String[] deviceArray = jsSub.getString(0).split("_");
                                        String deviceName = deviceArray[0];
                                        String deviceId = "0";
                                        if (deviceArray.length == 2) {
                                            deviceId = deviceArray[1];
                                        } else {
                                            LogUtil.d(TAG, "run: deviceArray的长度不够！！！！");
                                        }
                                        if (CtrlInfo.mode.equals(CtrlInfo.MODE_BATH) && InstructionInfo.DEVICE_FAUCET.equals(deviceName)) {
                                            quequeDisposeBath(jsSub.getString(0), Integer.valueOf(deviceId), js);
                                        } else {
                                            linkedList.add(result);
                                            publishMessage(heartBeatTopic, JsonUtil.heartBeatString("ModeErro"));
                                            CtrlInfo.mode = CtrlInfo.MODE_BATH;
                                            isTrue = true;
                                            LogUtil.d(TAG, "run: 当前模式和消息类型不匹配!当前模式为：" + CtrlInfo.mode + ",消息类型：" + deviceArray[0]);
                                        }
                                    } else {
                                        isTrue = true;
                                        LogUtil.d(TAG, "run: result 为空");
                                    }
                                    if (i >= 10000) {
                                        i = 0;
                                        j = 0;
                                    } else {
                                        i++;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            publishMessage(heartBeatTopic, JsonUtil.heartBeatString("ExceptionErro", getExceptionAllInfo(e)));
                            isTrue = true;
                        }
                    }
                    LogUtil.e(TAG, "run: queque线程被终止了，查看相关信息");
                }
            }, "queuethread");
            LogUtil.i(TAG, "quequeThead: queue线程开启!!");
            queuethread.start();
        } else {
            LogUtil.i(TAG, "quequeThead: queue线程已经开启，并且活着，不重复开启！");
        }
    }


    private void quequeDisposeBath(String device, int deviceNum, JSONObject js) {
        try {
            String action = js.getString("action");
            switch (action) {
                case InstructionInfo.ACTION_TURN_ON:
                    if (js.getString("result").equals("0")) {
                        CtrlInfo.device_id = String.valueOf(deviceNum);
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_Right;
                            LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码正确,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
                        }
                        LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码正确，发送串口指令" + CtrlInfo.bathMsgSign);
                        CtrlInfo.bath_request_id = js.getString("msg_id");
                        CtrlInfo.bath_args = js.getString("args");
                        CtrlInfo.bath_sub_devices = device;
                        CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_ON;
                        if (!checkRunState(device)) {
                            setRunState(device, true);
                            uartsend(map.get("wateropen_" + String.valueOf(deviceNum)));
                            JSONObject jsonPwd = new JSONObject(js.getString("args"));
                            LogUtil.i(TAG, "quequeDisposeBath: " + jsonPwd);
                            String pwd = "SYS_CALL";
                            if (jsonPwd.length() > 0) {
                                pwd = jsonPwd.getString("pwd");
                                LogUtil.d(TAG, "quequeDisposeBath: 获取到的密码是：" + pwd + "!!!!!!!");
                            }
                            setUserPwd(device, pwd);
                            measurement.addHashMapDevice(device);
                            measurement.addHashMapUsed(device, "0000000");
                            timeoutTheadBath();
                        } else {
                            if (CtrlInfo.version.equals("3.0")) {
                                publishMessage(JsonUtil.enSendOpenResponseBath("0"));
                            }
                            isTrue = true;
                            LogUtil.i(TAG, "quequeDispose: checkRunState等于true，重复开启");
                        }
                    } else if (js.getString("result").equals("1")) {
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_WRONG;
                            LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码错了,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
                        }
                        LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码错了，faucet_00" + CtrlInfo.bathMsgSign);
                        isTrue = true;
                    }
                    Thread.sleep(100);
                    break;
                case InstructionInfo.ACTION_TURN_OFF:
                    if (js.getString("result").equals("0")) {
                        //密码正确
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_FINISH;
                            LogUtil.i(TAG, "quequeDispose: 关闭操作:设备密码正确,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
                        }
                        LogUtil.i(TAG, "quequeDispose: 关闭操作: 设备密码正确," + CtrlInfo.bathMsgSign);
                        js.put("action", InstructionInfo.ACTION_TURN_SELECT);
                        CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_OFF;
                        setRunState(device, false);
                        uartsend(map.get("waterclose_" + String.valueOf(deviceNum)));
                        measurement.addRemoveList(device);
                        Thread.sleep(100);
                        CtrlInfo.bath_request_id = js.getString("msg_id");
                        CtrlInfo.bath_args = js.getString("args");
                        CtrlInfo.bath_sub_devices = device;
                        CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_SELECT;
                        uartsend(map.get("waterselect_" + String.valueOf(deviceNum)));
                        timeoutTheadBath();
                        isTrue = true;
                    } else if (js.getString("result").equals("1")) {
                        //密码错误
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_WRONG;
                            LogUtil.i(TAG, "quequeDispose: 关闭操作:设备密码错了,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
                        }
                        LogUtil.i(TAG, "quequeDispose: 关闭操作:设备密码错了，faucet_00" + CtrlInfo.bathMsgSign);
                        isTrue = true;
                    }
                    Thread.sleep(100);
                    break;
                case InstructionInfo.ACTION_TURN_SELECT:
                    CtrlInfo.bath_request_id = js.getString("msg_id");
                    CtrlInfo.bath_args = js.getString("args");
                    CtrlInfo.bath_sub_devices = device;
                    CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_SELECT;
                    uartsend(map.get("waterselect_" + String.valueOf(deviceNum)));
                    timeoutTheadBath();
                    Thread.sleep(100);
                    break;
                case InstructionInfo.ACTION_TURN_ON_RESULT:
                    LogUtil.i(TAG, "run: turn_on_result接到服务器返回的接收成功信息后，应当删除验证信息");
                    isTrue = true;
                    break;
                case InstructionInfo.ACTION_TURN_OFF_RESULT:
                    LogUtil.i(TAG, "run: turn_off_result接到服务器返回的接收成功信息后，应当删除验证信息");
                    isTrue = true;
                    break;
                case InstructionInfo.ACTION_MEASUREMENT:
                    LogUtil.i(TAG, "quequeDisposeBath,运行水量检测: " + js);
                    //变更必要的信息
                    CtrlInfo.bath_action = InstructionInfo.ACTION_MEASUREMENT;
                    CtrlInfo.bath_sub_devices = device;
                    uartsend(map.get("waterselect_" + String.valueOf(deviceNum)));
                    timeoutTheadBath();
                    Thread.sleep(100);
                    break;
                default:
                    LogUtil.i(TAG, "run: 解析action操作，不是正常操作指令");
                    isTrue = true;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isTrue = true;
        }
    }


    /*
     * 检测设备是否开启
     *
     * */
    public static boolean checkRunState(String device) {
        if (hashMapDevices.containsKey(device)) {
            LogUtil.i(TAG, "checkRunState: hashMapFaucet.get(device)" + hashMapDevices.get(device));
            return hashMapDevices.get(device);
        } else {
            LogUtil.i(TAG, "checkRunState: 查不到key，设置新设备状态");
            setRunState(device, true);
            return false;
        }
    }

    public static boolean getRunState(String device) {
        if (hashMapDevices.containsKey(device)) {
            return hashMapDevices.get(device);
        } else {
            return false;
        }
    }

    public static void setRunState(String device, Boolean is) {
        hashMapDevices.put(device, is);
    }

    public static void addForLinkList(String s) {
        linkedList.add(s);
    }

    private void setUserPwd(String device, String pwd) {
        if (!pwd.equals("SYS_CALL")) {
            pwdHashMap.put(device, pwd);
        } else {
            LogUtil.d(TAG, "setUserPwd: 从args获取的的密码不对，不放入水量自检队列！！！！");
        }
    }

    private String getUserPwd(String device) {
        if (pwdHashMap.containsKey(device)) {
            return pwdHashMap.get(device);
        } else {
            return "SYS_CALL";
        }

    }

    public void timeoutTheadBath() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                secondtime = 0;
                timeoutThreadSign = true;
                while (timeoutThreadSign) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    secondtime++;
                    if (secondtime > 10) {
                        //1s超时后，每500ms超时
                        secondtime = 5;
                        if (InstructionInfo.ACTION_TURN_ON.equals(CtrlInfo.bath_action)) {
//                            timeoutThreadSign = false;
                            if (checkoutNum > 2) {
                                timeoutThreadSign = false;
                                publishMessage(JsonUtil.enSendOpenResponseBath("1"));
                                checkoutNum = 0;
                                LogUtil.i(TAG, "run: 串口未在" + secondtime + "*100ms内响应，enSendOpenResponse硬件错误");
                            } else {
                                LogUtil.i(TAG, "run:-" + CtrlInfo.device_id + "-串口未在" + secondtime + "*100ms内响应，重新尝试：" + checkoutNum);
                                uartsend(map.get("wateropen_" + CtrlInfo.device_id));
                                checkoutNum++;
                            }
                        } else if (InstructionInfo.ACTION_TURN_OFF.equals(CtrlInfo.bath_action)) {
//                            timeoutThreadSign = false;
                            if (checkoutNum > 2) {
                                timeoutThreadSign = false;
                                publishMessage(JsonUtil.enSendCloseResponseBath("1", "000000"));
                                checkoutNum = 0;
                                LogUtil.i(TAG, "run: 串口未在" + secondtime + "*100ms内响应，enSendCloseResponse硬件错误");
                            } else {
                                uartsend(map.get("waterclose_" + CtrlInfo.device_id));
                                checkoutNum++;
                                LogUtil.i(TAG, "run: 串口未在" + secondtime + "*100ms内响应，重新尝试：" + checkoutNum);
                            }
                        } else if (InstructionInfo.ACTION_TURN_SELECT.equals(CtrlInfo.bath_action)) {
                            if (checkoutNum > 2) {
                                timeoutThreadSign = false;
                                publishMessage(JsonUtil.enSendCloseResponseBath("1", "000000"));
                                checkoutNum = 0;
                                LogUtil.i(TAG, "run: 串口未在" + secondtime + "*100ms内响应，enSendCloseResponse（select）硬件错误");
                            } else {
                                uartsend(map.get("waterselect_" + CtrlInfo.device_id));
                                checkoutNum++;
                                LogUtil.i(TAG, "run: " + CtrlInfo.device_id + "串口未在" + secondtime + "*100ms内响应，重新尝试：" + checkoutNum);
                            }
                        } else {
                            timeoutThreadSign = false;
                            LogUtil.i(TAG, "onDataReceived: 串口没有接收内容，未知错误！");
                        }
                    }
                }
                LogUtil.i(TAG, "run: timeout 线程结束");
                isTrue = true;
            }
        }).start();
    }

    public void heartBeatThread() {
        //心跳时间
        final int heartBeatTime = 1000 * 60;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isStop) {
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatStrings("ok", String.valueOf(linkedList.size()),
                            String.valueOf(CtrlInfo.connectState), String.valueOf(isTrue), String.valueOf(queuethread.isAlive()), String.valueOf(i), String.valueOf(j)));
                    if (CtrlInfo.uuid.equals("0000000000") || (CtrlInfo.appCtrl = false)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).start();
                        this.cancel();//停止心跳包
                        LogUtil.i(TAG, "run: 心跳包定时器停止了");
                    }
                } else {
                    this.cancel();//停止心跳包
                }
                LogUtil.i(TAG, "run: 我是心跳包，ok");
            }
        }, 1000 * 30, heartBeatTime);


    }

    public void uartinit() {
        ComA = new SerialControl();
        ComA.setPort(Com3);
        ComA.setBaudRate(BaudRate);
        OpenComPort(ComA);
    }

    public static void uartsend(String string) {
        if (string != null && !string.equals("")) {
            if (!ComA.isOpen()) {
                LogUtil.i(TAG, "uartsend: 打开串口再发送" + string);
                ComA.setPort(Com3);
                ComA.setBaudRate(BaudRate);
                OpenComPort(ComA);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ComA.sendTxt(string);
            } else {
                LogUtil.i(TAG, "uartsend: 直接发送" + string);
                ComA.sendHex(string);
            }
        } else {
            LogUtil.i(TAG, "uartsend: 串口发送为空：" + string);
        }

    }

    private class SerialControl extends SerialHelper {

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            String rev = bytesToHexString(ComRecData.bRec);
            LogUtil.i(TAG, "onDataReceived: 串口接收：" + bytesToHexString(ComRecData.bRec));
            try {
                switch (CtrlInfo.bath_action) {
                    case InstructionInfo.ACTION_TURN_ON:
                        if (Objects.equals(rev, "06")) {
                            LogUtil.i(TAG, "onDataReceived: 串口开启设备成功");
                            publishMessage(JsonUtil.enSendOpenResponseBath("0"));
                        } else {
                            LogUtil.i(TAG, "onDataReceived: 在turn_on操作，串口接收的值不对" + rev);
                        }
                        break;
                    case InstructionInfo.ACTION_TURN_OFF:
                        if (Objects.equals(rev, "06")) {
                            LogUtil.i(TAG, "onDataReceived: 串口关闭设备成功");
                        } else {
                            LogUtil.i(TAG, "onDataReceived: 在turn_off操作，串口接收的值不对" + rev);
                        }
                        break;
                    case InstructionInfo.ACTION_TURN_SELECT:
                        if (!Objects.equals(rev, "06") && Objects.requireNonNull(rev).length() == 18) {
                            LogUtil.i(TAG, "onDataReceived: 接收的水量信息是：" + rev.substring(8, 16));
                            String address = rev.substring(4, 6);
                            if (checkUartData(rev)) {
                                publishMessage(JsonUtil.enSendCloseResponseBath("0", Integer.valueOf(rev.substring(8, 16), 16).toString()));
                                LogUtil.i(TAG, "校验正确");
                            } else {
                                LogUtil.i(TAG, "校验未通过");
                                if (GetCheckoutHashMapNum(address) < 2) {
                                    LogUtil.i(TAG, "重新加入队列");
                                    linkedList.add(CtrlInfo.result);
                                    checkoutHashMap.put(address, GetCheckoutHashMapNum(address) + 1);
                                } else {
                                    //如果校验多次数据还是错误的，告诉服务器
                                    checkoutHashMap.remove(address);
                                    LogUtil.i(TAG, "多次检测数据错误，告诉服务器");
                                    publishMessage(JsonUtil.enSendCloseResponseBath("10", Integer.valueOf(rev.substring(8, 16), 16).toString()));
                                }
                            }
                        } else {
                            LogUtil.i(TAG, "ACTION_TURN_SELECT时接收到了其他的值");
                        }
                        break;
                    case InstructionInfo.ACTION_MEASUREMENT:
                        if (!Objects.equals(rev, "06") && Objects.requireNonNull(rev).length() == 18) {
                            //得到水量后处理，如果一致，进行下一步判断
                            if (Objects.equals(measurement.getHashMapUsed(CtrlInfo.bath_sub_devices), rev.substring(8, 15))) {
                                LogUtil.i(TAG, "onDataReceived: ACTION_MEASUREMENT接收的水量信息和记录的一致：" + rev.substring(8, 15));
                                if (measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices) >= 4) {
                                    //判断自动水量关闭之前，有没有用户主动关掉，没有关闭，执行关闭操作
                                    if (!measurement.getRemoveList(CtrlInfo.bath_sub_devices)) {
                                        MyMqttService.publishMessage(JsonUtil.enSendCloseRequestBath(getUserPwd(CtrlInfo.bath_sub_devices)));
                                        LogUtil.d(TAG, "onDataReceived: getCountHashMapDevice的值是：" + measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices));
                                    } else {
                                        LogUtil.d(TAG, "onDataReceived: " + CtrlInfo.bath_sub_devices + "已经被关掉！不启用自动关闭");
                                    }
                                } else {
                                    LogUtil.i(TAG, "onDataReceived: 运行addCountHashMapDevice  运行设备加一，当前设备次数：" + measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices));
                                    measurement.addCountHashMapDevice(CtrlInfo.bath_sub_devices);
                                    LogUtil.i(TAG, "onDataReceived: 运行addCountHashMapDevice  设备次数加一，加之后：" + measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices));
                                }
                            } else {
                                measurement.addHashMapDevice(CtrlInfo.bath_sub_devices);
                                measurement.addHashMapUsed(CtrlInfo.bath_sub_devices, rev.substring(8, 15));
                                LogUtil.i(TAG, "onDataReceived: ACTION_MEASUREMENT中检测发现水量不一致,重置设备状态：" +
                                        measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices) + "当前水量：" + rev.substring(8, 15));
                            }
                        } else {
                            LogUtil.i(TAG, "ACTION_MEASUREMENT时接收到了其他的值");
                        }
                        break;
                    case InstructionInfo.ACTION_DEBUG:
                        //在这里接收流量
                        if (!Objects.equals(rev, "06") && Objects.requireNonNull(rev).length() == 18) {
                            LogUtil.i(TAG, "onDataReceived: 模式为debug，接收的水量信息是：" + rev);
                            Intent intent = new Intent("com.boomstack.preparehigh.service");
                            intent.putExtra("used_data", Integer.valueOf(rev.substring(8, 16), 16).toString());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        }
                        break;
                    default:
                        LogUtil.i(TAG, "onDataReceived: Action指令不对！串口接收的值为" + rev);
                        break;
                }
                checkoutNum = 0;
                timeoutThreadSign = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int GetCheckoutHashMapNum(String key) {
        if (!checkoutHashMap.containsKey(key)) {
            return 0;
        } else {
            return checkoutHashMap.get(key);
        }
    }

    public boolean checkUartData(String str) {
        return makeChecksum(str.substring(0, 16)).equals(str.substring(16));
    }

    //返回十六进制累加值
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    //关闭串口
    private static void CloseComPort(SerialHelper ComPort) {
        if (ComPort != null) {
            ComPort.stopSend();
            ComPort.close();
            if (!ComA.isOpen()) {
                LogUtil.i(TAG, "串口关闭成功！ ");
            }
        }
    }

    //打开串口
    private static void OpenComPort(SerialHelper ComPort) {
        try {
            ComPort.open();
            LogUtil.i(TAG, "打开串口成功！");
        } catch (SecurityException e) {
            ShowMessage("打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            ShowMessage("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            ShowMessage("打开串口失败:参数错误!");
        }
    }

    private static void ShowMessage(String sMsg) {
        LogUtil.i(TAG, "ShowMessage: " + sMsg);
    }

    //    byte数组转Hex
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void mqttDisconnect() {
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        queueThreadSign = false;
        timeoutThreadSign = false;
        isStop = true;
        CloseComPort(ComA);
        measurement.stop();
        try {
            if (!mqttAndroidClient.equals("")) {
                //服务退出时client断开连接，关闭串口
                mqttAndroidClient.close();
                mqttAndroidClient.disconnect();
                LogUtil.i(TAG, "onDestroy: 断开mqtt服务器");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "MqttService onDestroy executed");
    }

    public static String getExceptionAllInfo(Exception ex) {
        ByteArrayOutputStream out;
        String ret;
        out = new ByteArrayOutputStream();
        try (PrintStream pout = new PrintStream(out)) {
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            out.close();
        } catch (Exception e) {
            return ex.getMessage();
        }
        return ret;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}