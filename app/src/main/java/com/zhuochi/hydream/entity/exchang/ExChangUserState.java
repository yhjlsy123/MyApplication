package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/** 总线  用户状态
 * @author Cuixc
 * @date on  2018/6/28
 */

public class ExChangUserState {

    /**
     * userState : normal
     * queueInfo :
     * deviceInfo :
     * remainTime : 0
     * allowRemoteControl : 0
     */

    private String userState;
    private Object queueInfo;
    private Object deviceInfo;
    private int remainTime;
    private int allowRemoteControl;

    public static ExChangUserState objectFromData(String str) {

        return new Gson().fromJson(str, ExChangUserState.class);
    }

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
