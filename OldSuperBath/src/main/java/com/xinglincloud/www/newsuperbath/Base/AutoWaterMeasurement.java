package com.xinglincloud.www.newsuperbath.Base;

import android.util.Log;

import com.xinglincloud.www.newsuperbath.CtrlInformation.InstructionInfo;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoWaterMeasurement {

    private static final String TAG = "AutoWaterMeasurement";
    private String device;
    private String used;
    private ConcurrentHashMap<String, String> hashMapUsed = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> hashMapCount = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<String> removeList = new CopyOnWriteArrayList<>();

    private Thread autoWaterThread = null;

    private boolean measurementThreadSign = true;

    public void stop() {
        measurementThreadSign = false;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void addHashMapUsed(String device, String used) {
        hashMapUsed.put(device, used);
    }

    public void addHashMapDevice(String device) {
        hashMapCount.put(device, 0);
    }

    private void removeHashMapUsed(String device) {
        synchronized (hashMapUsed) {
            if (hashMapUsed.containsKey(device)) {
                hashMapUsed.remove(device);
            } else {
                Log.d(TAG, "removeHashMapDevice: 没有找到相应的key值");
            }
        }
    }

    public String getHashMapUsed(String device) {
        if (hashMapUsed.containsKey(device)) {
            return hashMapUsed.get(device);
        } else {
            return "00000000";
        }
    }

    private void removeHashMapDevice(String device) {
        synchronized (hashMapCount) {
            if (hashMapCount.containsKey(device)) {
                hashMapCount.remove(device);
            } else {
                Log.d(TAG, "removeHashMapDevice: 没有找到相应的key值");
            }
        }
    }

    public void addCountHashMapDevice(String device) {
        hashMapCount.put(device, (hashMapCount.get(device) + 1));
    }

    public int getCountHashMapDevice(String device) {
        return hashMapCount.get(device);
    }

    private String measurementJson(String device) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(device);
            jsonObject.put("action", InstructionInfo.ACTION_MEASUREMENT);
            jsonObject.put("sub_devices", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public boolean getRemoveList(String device) {
        return removeList.contains(device);
    }

    public void addRemoveList(String device) {
        removeList.add(device);
    }

    private void remove() {
        for (String device : removeList) {
            if (!MyMqttService.getRunState(device)) {
                removeHashMapUsed(device);
                removeHashMapDevice(device);
            } else {
                Log.i(TAG, "remove: 查询设备已经开启，不移除:" + device + "设备自动关闭");
            }
        }
        //最后清空list
        removeList.clear();
    }

    public void measurementThread() {
        if (autoWaterThread == null) {
            autoWaterThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (measurementThreadSign) {
                        try {
                            Thread.sleep(300* 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "run: 开启自动水量检测！！！");
                        try {
                            if (!hashMapCount.isEmpty()) {
                                for (String key : hashMapCount.keySet()) {
                                    //加入主服务的消息队列
                                    try {
                                        MyMqttService.addForLinkList(measurementJson(key));
                                        Log.i(TAG, "run: 查询水量加入队列的消息：" + measurementJson(key));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //此处延时，是多久构建一条指令，加入消息队列
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(TAG, "run: autoWaterThread等待再次循环！");
                                }
                            } else {
                                Log.i(TAG, "run: hashMapCount为空！！！");
                            }
                            //清除已经关闭的设备
                            remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "run: 水量检测停止运行！！！");
                }
            }, "measurementThread");
            autoWaterThread.start();
        } else {
            Log.i(TAG, "measurementThread: autoWaterThread线程已存在");
        }
    }
}
