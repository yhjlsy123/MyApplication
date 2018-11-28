package com.zhuochi.hydream.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/** 预约界面
 * @author Cuixc
 * @date on  2018/6/14
 */

public class ShowerOrderEntity implements Serializable{
    private String deviceNumber;
    private String userDevicePwd;
    private String reserveTime;
    private int maxWaitTime;
    private int allowRemoteControl;
    private String uuid;
    private String deviceAreaId;
    private String deviceAreaName;
    private String deviceTypeKey;
    private int remainTime;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getUserDevicePwd() {
        return userDevicePwd;
    }

    public void setUserDevicePwd(String userDevicePwd) {
        this.userDevicePwd = userDevicePwd;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getAllowRemoteControl() {
        return allowRemoteControl;
    }

    public void setAllowRemoteControl(int allowRemoteControl) {
        this.allowRemoteControl = allowRemoteControl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceAreaId() {
        return deviceAreaId;
    }

    public void setDeviceAreaId(String deviceAreaId) {
        this.deviceAreaId = deviceAreaId;
    }

    public String getDeviceAreaName() {
        return deviceAreaName;
    }

    public void setDeviceAreaName(String deviceAreaName) {
        this.deviceAreaName = deviceAreaName;
    }

    public String getDeviceTypeKey() {
        return deviceTypeKey;
    }

    public void setDeviceTypeKey(String deviceTypeKey) {
        this.deviceTypeKey = deviceTypeKey;
    }
}
