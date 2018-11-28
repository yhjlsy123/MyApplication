package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/22
 */

public class DeviceStateEntity {

    /**
     * userState : reserved
     * queueInfo :
     * deviceInfo : {"device_key":"9019df64-ff05-433f-a8c0-51ce63d36265_faucet_01","controller_key":"9019df64-ff05-433f-a8c0-51ce63d36265","is_controller":"0","device_user_id":"1","device_name":"水控机01","device_number":"01","org_id":"10","org_area_id":"15","building_id":"90","building_number":"","device_type_key":"faucet","v_device_type_key":"dev_controller_2","device_area_id":"3","settlement_area_id":"4","device_status":"reserved","need_reserve":"1","longitude":"117.1297480","latitude":"36.6872410","user_id":"25","user_device_pwd":"byj1fJdKJBgk87FO4XlrrQ\\u003d\\u003d","goods_id":"0","last_operate_time":"0","max_wait_time":"480","reserve_time":"1529569322","start_time":"1529475745","end_time":"0","order_sn":"27982","stop_reason":"","uuid":"9019df64-ff05-433f-a8c0-51ce63d36265_faucet_01","user_device_password":"159357258","deviceAreaName":"1号楼东浴室"}
     * remainTime : 0
     * allowRemoteControl : 3
     */

    private String userState;
    private Object queueInfo;
    private Object deviceInfo;
    private int remainTime;
    private int allowRemoteControl;

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public Object getQueueInfo() {
        return queueInfo;
    }

    public void setQueueInfo(Object queueInfo) {
        this.queueInfo = queueInfo;
    }

    public Object getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(Object deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public int getAllowRemoteControl() {
        return allowRemoteControl;
    }

    public void setAllowRemoteControl(int allowRemoteControl) {
        this.allowRemoteControl = allowRemoteControl;
    }
}
