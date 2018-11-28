package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/** 总线设备状态
 * @author Cuixc
 * @date on  2018/6/28
 */

public class ExChangDeviceState<T> {

    /**
     * uuid : 9019df64-ff05-433f-a8c0-51ce63d36265_faucet_01
     * deviceStatus : ready
     * reserveTime : 0
     * startTime : 0
     * userId : 0
     * orderSn : 82988
     */

    private String uuid;
    private String deviceStatus;
    private String reserveTime;
    private String startTime;
    private String userId;
    private String orderSn;

    public static ExChangDeviceState objectFromData(String str) {

        return new Gson().fromJson(str, ExChangDeviceState.class);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
