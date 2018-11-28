package com.xinglincloud.www.newsuperbath.MqttService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xinglincloud.www.newsuperbath.Activity.MainActivity;
import com.xinglincloud.www.newsuperbath.Activity.UpdateActivity;
import com.xinglincloud.www.newsuperbath.Base.AutoWaterMeasurement;
import com.xinglincloud.www.newsuperbath.Base.Box;
import com.xinglincloud.www.newsuperbath.ComAssistant.SerialHelper;
import com.xinglincloud.www.newsuperbath.CtrlInformation.CtrlInfo;
import com.xinglincloud.www.newsuperbath.CtrlInformation.InstructionInfo;
import com.xinglincloud.www.newsuperbath.Util.FileUtil;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.RandomNumUtils;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyMqttService extends Service {
    private static final String TAG = "MyMqttService";
    private String subTopic;
    private String subUpdateTopic;
    private static final String pubTopic = "device_call";
    private static final String pubUpdateTopic = "dev_update_response";
    private static final String heartBeatTopic = "device_heartbeat";
    private static final String exceptionTopic = "device_exception";

    private static MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;
    int faucetNumGlobal = -1;
    private String serverUri;
    private String clientId;
    private static ConcurrentLinkedQueue<String> linkedList = new ConcurrentLinkedQueue<>();
    private boolean isTrue = true;
    private boolean queueThreadSign = true;
    private boolean timeoutThreadSign = true;
    private int secondtime = 0;
    private int i = 0;
    private int j = 0;

    private static HashMap<String, Boolean> hashMapFaucet;
    private static HashMap<String, Boolean> hashMapBlower;

    private static String BaudRate = "9600";//波特率
    private static SerialHelper ComA;//串口类
    private static String Com3 = "/dev/ttyS3";//水控机串口的绝对地址
    private static SerialHelper ComB;//串口类
    private static String Com1 = "/dev/ttyS1";//吹风机串口的绝对地址
    private static SerialHelper ComC;//串口类
    private static String ComXRM0 = "/dev/ttyXRM0";//通风设备串口的绝对地址
    private static SerialHelper ComD;//串口类
    private static String Com4 = "/dev/ttyS4";//箱柜设备串口的绝对地址
    //串口命令
    Map<String, String> map;//水控机串口命令
    HashMap<String, String> ventilatorHashMap = CtrlInfo.ventilatorHashmapInit();
    static HashMap<String, String> boxHashMap = CtrlInfo.boxHashmapInit();
    Thread queuethread;
    AutoWaterMeasurement measurement = new AutoWaterMeasurement();//水量检测类
    HashMap<String, String> pwdHashMap = new HashMap<>();
    Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();
        mqttInit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CtrlInfo.communication = true;
        CtrlInfo.appCtrl = true;
        return START_NOT_STICKY;
    }

    private void mqttInit() {
        if (CtrlInfo.uuid.equals("0000000000")) {
            try {
                JSONObject js = new JSONObject(FileUtil.getSavedUserInfo());
                CtrlInfo.uuid = js.getString("uuid");
                clientId = CtrlInfo.uuid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Random ra =new Random();
        clientId = CtrlInfo.uuid+"_"+ra.nextInt(100);
        subTopic = "device_" + CtrlInfo.uuid;
        subUpdateTopic = "dev_update_" + CtrlInfo.uuid;
        //emq服务器版本选择
        if (CtrlInfo.version.equals("3.0")) {
            serverUri = CtrlInfo.SERVER_URI_V3;
        } else if (CtrlInfo.version.equals("4.0")) {
            serverUri = CtrlInfo.SERVER_URI_V4;
        } else {
            serverUri = CtrlInfo.SERVER_URI_V1;
        }
        quequeThead();
        hashMapFaucet = new HashMap<>();
        hashMapBlower = new HashMap<>();
        uartinit();
        map = CtrlInfo.faucetHashmapInit(CtrlInfo.faucetNum);
        //新建Client,以设备ID作为client ID
        mqttAndroidClient = new MqttAndroidClient(MyMqttService.this, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("restart"));
                    subscribeAllTopics();
                    CtrlInfo.connectState = true;
                } else {
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("start"));
                    CtrlInfo.connectState = true;
                }
                sendWarningBroadcast(true);
            }

            @Override
            public void connectionLost(Throwable cause) {
                CtrlInfo.connectState = false;
                sendWarningBroadcast(false);
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
                //即服务器成功delivery消息
            }
        });
        //新建连接设置
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(15);
        mqttConnectOptions.setKeepAliveInterval(30);
        mqttConnectOptions.setMaxInflight(20);
        //设置用户名密码
        mqttConnectOptions.setUserName(CtrlInfo.EMQ_USERNAME);
        mqttConnectOptions.setPassword(CtrlInfo.EMQ_PASSWORD.toCharArray());
        mqttConnectOptions.setWill(heartBeatTopic, JsonUtil.heartBeatString("lost").getBytes(), 2, false);
        //连接服务器
        connectmqtt();
    }

    //    连接mqtt服务器方法
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
                    //成功连接以后开始订阅
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
        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    //收到mqtt服务器的消息后的控制方法
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
                if (isJSONValid(payload)) {
                    JSONObject jsRev = new JSONObject(payload);
                    JSONObject head = new JSONObject(jsRev.getString("head"));
                    String ver = head.getString("ver");
                    if (ver.equals(CtrlInfo.version)) {
                        String result = JsonUtil.deStr(jsRev.getString("head"), jsRev.getString("data"));
                        LogUtil.i(TAG, "messageCtrl: 加入队列的result:" + result);
                        if (result != null && !result.equals("DesErro") && !result.equals("Sha1Erro") && !result.equals("err")) {
                            linkedList.add(result);
                        } else {
                            LogUtil.i(TAG, "messageCtrl: 接收的消息解析有误：" + result);
                        }
                    } else {
                        LogUtil.i(TAG, "消息的协议版本和本机的协议版本不一致" + payload);
                    }
                } else {
                    LogUtil.i(TAG, "messageCtrl: 非标准Json字符串" + payload);
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

    public static boolean isJSONValid(String test) {
        try {
            JSONObject jsonObject = new JSONObject(test);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }


    //订阅所有消息
    private void subscribeAllTopics() {
        if (CtrlInfo.uuid.equals("0000000000")) {
            try {
                JSONObject js = new JSONObject(FileUtil.getSavedUserInfo());
                LogUtil.i(TAG, "settingInit: 读取配置文件的信息是" + js.toString());
                CtrlInfo.uuid = js.getString("uuid");
                clientId = CtrlInfo.uuid;
            } catch (Exception e) {
                e.printStackTrace();
            }
            subTopic = "device_" + CtrlInfo.uuid;
            subUpdateTopic = "dev_update_" + CtrlInfo.uuid;
            subscribeToTopic(subTopic);
            subscribeToTopic(subUpdateTopic);
            LogUtil.i(TAG, "subscribeAllTopics: uuid为000了，订阅的主题是：" + "device_" + CtrlInfo.uuid);
        } else {
            subscribeToTopic(subTopic);
            subscribeToTopic(subUpdateTopic);
            LogUtil.i(TAG, "subscribeAllTopics: 订阅的主题是：" + subTopic + ",自动更新主题是：" + subUpdateTopic);
        }
    }

    public void subscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("subscribeSuccess"));
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    publishMessage(heartBeatTopic, JsonUtil.heartBeatString("subscribeFailure"));
                    subscribeAllTopics();
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
        if (CtrlInfo.communication && mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.publish(pubTopic, msg.getBytes(), 2, false);
                CtrlInfo.publishIsSure = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtil.i(TAG, "publishMessage: publish通讯阻断，不接受");
        }
    }

    public static void publishMessage(String topic, String msg) {
        if (CtrlInfo.communication && mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.publish(topic, msg.getBytes(), 2, false);
                LogUtil.d(TAG, "publishMessage: Message Published: " + msg);
                CtrlInfo.publishIsSure = true;
            } catch (Exception e) {
                LogUtil.d(TAG, "publishMessage: Error Publishing: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            LogUtil.i(TAG, "publishMessage: publish通讯阻断，不接受");
        }
    }

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
//                                        CtrlInfo.result = result;
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
                                        } else if (CtrlInfo.mode.equals(CtrlInfo.MODE_HAIRDRYER) && InstructionInfo.DEVICE_BLOWER.equals(deviceName)) {
                                            quequeDisposeHairDryer(jsSub.getString(0), Integer.valueOf(deviceId), js);
                                        } else if (CtrlInfo.mode.equals(CtrlInfo.MODE_MIXED)) {
                                            if (InstructionInfo.DEVICE_FAUCET.equals(deviceName)) {
                                                quequeDisposeBath(jsSub.getString(0), Integer.valueOf(deviceId), js);
                                            } else if (InstructionInfo.DEVICE_BLOWER.equals(deviceName)) {
                                                quequeDisposeHairDryer(jsSub.getString(0), Integer.valueOf(deviceId), js);
                                            } else {
                                                LogUtil.i(TAG, "run: 混合模式中，设备类型错误！");
                                                isTrue = true;
                                            }
                                        } else {
                                            LogUtil.i(TAG, "run: 当前模式和消息类型不匹配!当前模式为：" + CtrlInfo.mode + ",消息类型：" + deviceArray[0]);
                                            publishMessage(heartBeatTopic, JsonUtil.heartBeatString("ModeErro"));
                                            CtrlInfo.mode = CtrlInfo.MODE_BATH;
                                            linkedList.add(result);
                                            isTrue = true;
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
                        LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码正确，发送串口指令" + CtrlInfo.bathMsgSign);
                        CtrlInfo.bath_request_id = js.getString("msg_id");
                        CtrlInfo.bath_args = js.getString("args");
                        CtrlInfo.bath_sub_devices = device;
                        CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_ON;
                        //** 获取密码.*//
                        JSONObject jsonPwd = new JSONObject(js.getString("args"));
                        String pwd = "SYS_CALL";
                        if (jsonPwd.length() > 0) {
                            pwd = jsonPwd.getString("pwd");
                            LogUtil.d(TAG, "quequeDisposeBath: 获取到的密码是：" + pwd + "!!!!!!!");
                        }
                        if (!pwd.equals("SYS_CALL")) {
                            CtrlInfo.openPassword = pwd;
                        }
                        CtrlInfo.device_pwd = pwd;
                        if (!checkFaucetRunState(device)) {
                            //在这里更改设备状态，变更在串口接收06处更改设备状态
                            uartsend(map.get("wateropen_" + String.valueOf(deviceNum)));
                            measurement.addHashMapDevice(device);
                            measurement.addHashMapUsed(device, "0000000");
                            timeoutTheadBath();
                            LogUtil.i(TAG, "quequeDispose: 开启操作:设备未开启，直接开启");
                        } else {
                            //若果密码等于"SYS_CALL"
                            if (getUserPwd(device).equals("SYS_CALL")) {
                                LogUtil.i(TAG, "getUserPwd: 用户密码是sys_call");
                                isTrue = true;
                            } else {
                                //密码相等
                                if (getUserPwd(device).equals(CtrlInfo.device_pwd)) {
                                    LogUtil.i(TAG, "quequeDispose: 开启操作:设备已开启，密码箱等，向服务器回复开启成功");
                                    publishMessage(JsonUtil.enSendOpenResponseBathRepeat("0"));
                                    isTrue = true;
                                } else {
                                    //密码不相等
                                    LogUtil.i(TAG, "quequeDispose: 开启操作:设备已开启，密码不相等，重新开启");
                                    uartsend(map.get("wateropen_" + String.valueOf(deviceNum)));
                                    timeoutTheadBath();
                                }
                            }

                        }
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_Right;
                            LogUtil.i(TAG, "quequeDispose: 开启操作:设备密码正确,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
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
                        LogUtil.i(TAG, "quequeDispose: 关闭操作: 设备密码正确," + CtrlInfo.bathMsgSign);
                        js.put("action", InstructionInfo.ACTION_TURN_SELECT);
                        CtrlInfo.bath_action = InstructionInfo.ACTION_TURN_OFF;
                        CtrlInfo.bath_sub_devices = device;

                        JSONObject jsonPwd = new JSONObject(js.getString("args"));
                        String pwd = "SYS_CALL";
                        if (jsonPwd.length() > 0) {
                            pwd = jsonPwd.getString("pwd");
                            LogUtil.d(TAG, "quequeDisposeBath: 获取到的密码是：" + pwd + "!!!!!!!");
                        }
                        if (!pwd.equals("SYS_CALL") && CtrlInfo.boxNum > 0) {
                            List<Box> boxList = DataSupport.where("passwrod=?", pwd)
                                    .find(Box.class);
                            if (!boxList.isEmpty()) {
                                Box box = boxList.get(0);
                                LogUtil.i(TAG, "quequeDisposeBath: 数据库,查询成功，id：" + box.getBoxId() + ",password:" + box.getPasswrod());
                                int n = box.getBoxId();
                                //在这里进行箱柜串口操作
                                uartOpenOneBox(n);
                                if (box.isSaved()) {
                                    LogUtil.i(TAG, "quequeDisposeBath: 数据库，删除成功");
                                    RandomNumUtils.removeRandomNum(n);
                                    box.delete();
                                }
                            } else {
                                LogUtil.i(TAG, "quequeDisposeBath: 数据库,结束操作不存在该密码，忽略箱柜操作");
                            }
                        } else {
                            LogUtil.i(TAG, "箱柜功能未开启！");
                        }
                        uartsend(map.get("waterclose_" + String.valueOf(deviceNum)));
                        if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                            CtrlInfo.bathMsgSign = CtrlInfo.PWD_Right_Finish;
                            LogUtil.i(TAG, "quequeDispose: 关闭操作:设备密码正确,bathMsgSign值被更改" + CtrlInfo.bathMsgSign);
                        }
                        Thread.sleep(150);
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
                    //为构建Json消息做准备
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
     * 吹风机处理
     * */
    private void quequeDisposeHairDryer(String device, int deviceNum, JSONObject js) {
        try {
            String action = js.getString("action");
            switch (action) {
                case InstructionInfo.ACTION_TURN_ON:
                    switch (js.getString("result")) {
                        case "0":
                            if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                                CtrlInfo.hairdryerMsgSign = CtrlInfo.PWD_Right;
                                LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备密码正确,hairdryerMsgSign值被更改" + CtrlInfo.hairdryerMsgSign);
                            }
                            LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备密码正确，发送串口指令" + CtrlInfo.hairdryerMsgSign);
                            CtrlInfo.hair_dryer_request_id = js.getString("msg_id");
                            CtrlInfo.hair_dryer_args = js.getString("args");
                            CtrlInfo.hair_dryer_sub_devices = device;
                            CtrlInfo.hair_dryer_action = InstructionInfo.ACTION_TURN_ON;
                            if (!checkBlowerRunState(device)) {
                                setBlowerRunState(device, true);
                                //在这里写吹风机开启  倒计时操作
                                JSONObject json = new JSONObject();
                                json.put("device", device);
                                json.put("time", js.getString("time"));
                                sendBroadcast(json.toString());
                                isTrue = true;
                            } else {
                                isTrue = true;
                                LogUtil.i(TAG, "quequeDispose吹风机: checkRunState等于true，重复开启");
                            }
                            break;
                        case "1":
                            if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                                CtrlInfo.hairdryerMsgSign = CtrlInfo.PWD_WRONG;
                                LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备密码错了,bathMsgSign值被更改" + CtrlInfo.hairdryerMsgSign);
                            }
                            LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备密码错了，faucet_00" + CtrlInfo.hairdryerMsgSign);
                            isTrue = true;
                            break;
                        case "2":
                            if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                                CtrlInfo.hairdryerMsgSign = CtrlInfo.INSUFFICIENT_BALANCE;
                                LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:余额不足,bathMsgSign值被更改" + CtrlInfo.hairdryerMsgSign);
                            }
                            LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:余额不足，faucet_00" + CtrlInfo.hairdryerMsgSign);
                            isTrue = true;
                            break;
                        case "3":
                            if (CtrlInfo.device_msg_id.equals(js.getString("request_id"))) {
                                CtrlInfo.hairdryerMsgSign = CtrlInfo.DEVICE_DISABLE;
                                LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备停用,bathMsgSign值被更改" + CtrlInfo.hairdryerMsgSign);
                            }
                            LogUtil.i(TAG, "quequeDispose吹风机: 开启操作:设备停用，faucet_00" + CtrlInfo.hairdryerMsgSign);
                            isTrue = true;
                            break;
                    }
                    break;

                case InstructionInfo.ACTION_TURN_ON_RESULT:
                    LogUtil.i(TAG, "run: 吹风机turn_on_result接到服务器返回的接收成功信息后，应当删除验证信息");
                    isTrue = true;
                    break;
                case InstructionInfo.ACTION_TURN_OFF_RESULT:
                    LogUtil.i(TAG, "run: 吹风机turn_off_result接到服务器返回的接收成功信息后，应当删除验证信息");
                    isTrue = true;
                    break;
                default:
                    LogUtil.i(TAG, "run: 吹风机解析action操作，不是正常操作指令");
                    isTrue = true;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isTrue = true;
        }
    }


    public int getFaucetRunStateNum() {
        return hashMapFaucet.size();
    }

    public static boolean checkFaucetRunState(String device) {
        if (hashMapFaucet.containsKey(device)) {
            LogUtil.i(TAG, "checkRunState: hashMapFaucet.get(device)" + hashMapFaucet.get(device));
            return hashMapFaucet.get(device);
        } else {
            LogUtil.i(TAG, "checkRunState: 查不到key,设备未开启");
            return false;
        }
    }

    public static boolean getFaucetRunState(String device) {
        if (hashMapFaucet.containsKey(device)) {
            return hashMapFaucet.get(device);
        } else {
            return false;
        }
    }

    public static void setFaucetRunState(String device, Boolean is) {
        if (is) {
            hashMapFaucet.put(device, is);
        } else {
            hashMapFaucet.remove(device);
        }
    }

    public static HashMap getFaucetHashMap() {
        return hashMapFaucet;
    }

    public static boolean checkBlowerRunState(String device) {
        if (hashMapBlower.containsKey(device)) {
            LogUtil.i(TAG, "checkRunState: hashMapFaucet.get(device)" + hashMapBlower.get(device));
            return hashMapBlower.get(device);
        } else {
            LogUtil.i(TAG, "checkRunState: 查不到key，设置新设备状态");
            setBlowerRunState(device, true);
            return false;
        }
    }

    private static void setBlowerRunState(String device, boolean b) {
        if (b) {
            hashMapBlower.put(device, b);
        } else {
            hashMapBlower.remove(device);
        }
    }

    public static HashMap getBlowerHashMap() {
        return hashMapBlower;
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
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    secondtime++;
                    if (secondtime > 100) {
                        if (InstructionInfo.ACTION_TURN_ON.equals(CtrlInfo.bath_action)) {
                            timeoutThreadSign = false;
                            publishMessage(JsonUtil.enSendOpenResponseBath("1"));
                            LogUtil.i(TAG, "run: 串口未在" + secondtime + "x10 ms内响应，enSendOpenResponse硬件错误");
                        } else if (InstructionInfo.ACTION_TURN_OFF.equals(CtrlInfo.bath_action)) {
                            timeoutThreadSign = false;
                            publishMessage(JsonUtil.enSendCloseResponseBath("1", "000000"));
                            LogUtil.i(TAG, "run: 串口未在" + secondtime + "x10 ms内响应，enSendCloseResponse硬件错误");
                        } else if (InstructionInfo.ACTION_TURN_SELECT.equals(CtrlInfo.bath_action)) {
                            timeoutThreadSign = false;
                            publishMessage(JsonUtil.enSendCloseResponseBath("1", "000000"));
                            LogUtil.i(TAG, "run: 串口未在" + secondtime + "x10 ms内响应，enSendCloseResponse（select）硬件错误");
                        } else {
                            timeoutThreadSign = false;
                            LogUtil.i(TAG, "onDataReceived: 串口没有接收内容，未知错误！");
                        }
                    }
                }
                LogUtil.i(TAG, "run: timeout 线程在第" + secondtime + "x10 ms结束,时间：" + System.currentTimeMillis());
                isTrue = true;
            }
        }, "serialtimeout").start();
    }

    /*
     * 心跳包
     * */
    public void heartBeatThread() {
        //心跳时间
        final int heartBeatTime = 1000 * 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //在这里进行设备开启数量的检测
                int faucetNum = getFaucetRunStateNum();
                if (faucetNum == faucetNumGlobal) {
                    LogUtil.i(TAG, "run: 设备数目一致,当前为：" + faucetNum + "，不执行任何通风设备控制操作！");
                } else {
                    int n = -1;
                    faucetNumGlobal = faucetNum;
                    if (faucetNum > 0 && faucetNum <= (CtrlInfo.faucetNum / 4)) {
                        uartsendVentilator(ventilatorHashMap.get("low"));
                        n = 1;
                    } else if (faucetNum > (CtrlInfo.faucetNum / 4) && faucetNum <= 3 * (CtrlInfo.faucetNum / 4)) {
                        uartsendVentilator(ventilatorHashMap.get("medium"));
                        n = 2;
                    } else if (faucetNum > 3 * (CtrlInfo.faucetNum / 4)) {
                        uartsendVentilator(ventilatorHashMap.get("high"));
                        n = 3;
                    } else if (faucetNum == 0) {
                        uartsendVentilator(ventilatorHashMap.get("close"));
                        n = 0;
                    }
                }
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
                    this.cancel();
                }
            }

        }, 1000 * 30, heartBeatTime);
    }

    public void uartinit() {
        //水控机串口
        ComA = new SerialControlComA();
        ComA.setPort(Com3);
        ComA.setBaudRate(BaudRate);
        OpenComPort(ComA);
        //吹风机串口
        ComB = new SerialControlComB();
        ComB.setPort(Com1);
        ComB.setBaudRate(BaudRate);
        OpenComPort(ComB);
        //通风设备
        ComC = new SerialControlComC();
        ComC.setPort(ComXRM0);
        ComC.setBaudRate(BaudRate);
        OpenComPort(ComC);
        //箱柜设备
        ComD = new SerialControlComD();
        ComD.setPort(Com4);
        ComD.setBaudRate(BaudRate);
        OpenComPort(ComD);

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
        }
    }

    public static void hairuartsend(String string) {
        if (!string.equals("")) {
            if (!ComB.isOpen()) {
                LogUtil.i(TAG, "hairuartsend: 打开串口再发送" + string);
                ComB.setPort(Com1);
                ComB.setBaudRate(BaudRate);
                OpenComPort(ComB);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ComB.sendHex(string);
            } else {
                LogUtil.i(TAG, "hairuartsend: 直接发送" + string);
                ComB.sendHex(string);
            }
        }
    }

    public static void uartsendVentilator(String string) {
        if (!string.equals("")) {
            if (!ComC.isOpen()) {
                LogUtil.i(TAG, "Ventilator: 打开串口再发送" + string);
                ComC.setPort(ComXRM0);
                ComC.setBaudRate(BaudRate);
                OpenComPort(ComC);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ComC.sendHex(string);
            } else {
                LogUtil.i(TAG, "Ventilator: 直接发送" + string);
                ComC.sendHex(string);
            }
        }
    }

    public static void uartOpenOneBox(int i) {
        uartsendBox(boxHashMap.get("box_open_" + String.valueOf(i)));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uartsendBox(boxHashMap.get("box_close_" + String.valueOf(i)));
    }

    public static void uartsendBox(String string) {
        if (!string.equals("")) {
            if (!ComD.isOpen()) {
                LogUtil.i(TAG, "Box: 打开串口再发送" + string);
                ComD.setPort(Com4);
                ComD.setBaudRate(BaudRate);
                OpenComPort(ComD);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ComD.sendHex(string);
            } else {
                LogUtil.i(TAG, "Box: 直接发送" + string);
                ComD.sendHex(string);
            }
        }
    }

    private class SerialControlComA extends SerialHelper {

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            String rev = bytesToHexString(ComRecData.bRec);
            LogUtil.i(TAG, "onDataReceived: 串口接收：" + bytesToHexString(ComRecData.bRec));
            try {
                switch (CtrlInfo.bath_action) {
                    case InstructionInfo.ACTION_TURN_ON:
                        if (Objects.equals(rev, "06")) {
                            LogUtil.i(TAG, "onDataReceived: 串口开启设备成功");
                            setFaucetRunState(CtrlInfo.bath_sub_devices, true);
                            setUserPwd(CtrlInfo.bath_sub_devices, CtrlInfo.device_pwd);
                            publishMessage(JsonUtil.enSendOpenResponseBath("0"));
                        } else {
                            LogUtil.i(TAG, "onDataReceived: 在turn_on操作，串口接收的值不对" + rev);
                        }
                        break;
                    case InstructionInfo.ACTION_TURN_OFF:
                        if (Objects.equals(rev, "06")) {
                            LogUtil.i(TAG, "onDataReceived: 串口关闭设备成功");
                            setFaucetRunState(CtrlInfo.bath_sub_devices, false);
                            measurement.addRemoveList(CtrlInfo.bath_sub_devices);//采用列表管理删除，防止冲突
                        } else {
                            LogUtil.i(TAG, "onDataReceived: 在turn_off操作，串口接收的值不对" + rev);
                        }
                        break;
                    case InstructionInfo.ACTION_TURN_SELECT:
                        if (!Objects.equals(rev, "06") && Objects.requireNonNull(rev).length() == 18) {
                            LogUtil.i(TAG, "onDataReceived: 接收的水量信息是：" + rev.substring(8, 16));
                            LogUtil.i(TAG, "onDataReceived: 第4位开始截取（设备地址）：" + rev.substring(4, 6));
                            publishMessage(JsonUtil.enSendCloseResponseBath("0", Integer.valueOf(rev.substring(8, 16), 16).toString()));
                        } else {
                            LogUtil.i(TAG, "ACTION_TURN_SELECT时接收到了其他的值");
                        }

                        break;
                    case InstructionInfo.ACTION_MEASUREMENT:
                        if (!Objects.equals(rev, "06") && Objects.requireNonNull(rev).length() == 18) {
                            if (Objects.equals(measurement.getHashMapUsed(CtrlInfo.bath_sub_devices), rev.substring(8, 15))) {
                                if (measurement.getCountHashMapDevice(CtrlInfo.bath_sub_devices) >= 4) {
                                    //判断自动水量关闭之前，有没有用户主动关掉，没有关闭，执行关闭操作
                                    if (!measurement.getRemoveList(CtrlInfo.bath_sub_devices)) {
                                        MyMqttService.publishMessage(JsonUtil.enSendCloseRequestBath(getUserPwd(CtrlInfo.bath_sub_devices)));
                                    } else {
                                        LogUtil.d(TAG, "onDataReceived: " + CtrlInfo.bath_sub_devices + "已经被关掉！不启用自动关闭");
                                    }
                                } else {
                                    measurement.addCountHashMapDevice(CtrlInfo.bath_sub_devices);
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
                timeoutThreadSign = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //吹风机串口控制类，ComB
    private class SerialControlComB extends SerialHelper {

        @Override
        protected void onDataReceived(final ComBean ComRecData) {

            LogUtil.i(TAG, "SerialControlComB: 串口接收：" + bytesToHexString(ComRecData.bRec));
            try {
                Thread.sleep(200);
                // 先判断值，再构建加密消息
                timeoutThreadSign = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //通风设备串口控制类，ComC
    private class SerialControlComC extends SerialHelper {

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            LogUtil.i(TAG, "SerialControlComC: 串口接收：" + bytesToHexString(ComRecData.bRec));
        }
    }

    //串口ComD，箱柜串口接收类
    private class SerialControlComD extends SerialHelper {

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            LogUtil.i(TAG, "SerialControlComD: 串口接收：" + bytesToHexString(ComRecData.bRec));
        }
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

    //显示消息
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

    /*
     * 自定义应用内广播，处理开启吹风机
     * */
    private void sendBroadcast(String s) {
        Intent intent = new Intent("com.hair.action.service");
        intent.putExtra("action", s);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /*
     * 自定义应用内广播，处理开启吹风机
     * */
    private void sendWarningBroadcast(boolean is) {
        Intent intent = new Intent("com.bath.warning");
        intent.putExtra("state", is);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
            }
            queueThreadSign = false;
            timeoutThreadSign = false;
            CloseComPort(ComA);
            CloseComPort(ComB);
            CloseComPort(ComC);
            CloseComPort(ComD);
            CtrlInfo.communication = false;
            CtrlInfo.appCtrl = false;
            measurement.stop();
            if (mqttAndroidClient != null) {
                //服务退出时client断开连接，关闭串口
                mqttAndroidClient.unregisterResources();
                mqttAndroidClient.close();
                mqttAndroidClient.disconnect();
                LogUtil.i(TAG, "onDestroy: 断开mqtt服务器");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "MqttService onDestroy executed");
    }

    /*
     * 将异常信息转换成字符串
     * */
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