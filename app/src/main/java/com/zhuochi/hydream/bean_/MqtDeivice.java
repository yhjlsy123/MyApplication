package com.zhuochi.hydream.bean_;

public class MqtDeivice {


    /**
     * orderSn : 101337457680012
     * reserveTime : 0
     * startTime : 0
     * uuid : 614fff6b-67de-4673-a2c5-cd138c7f9694_faucet_02
     * userId : 0
     * deviceStatus : ready
     */

    private String orderSn;
    private String reserveTime;
    private String startTime;
    private String uuid;
    private String userId;
    private String deviceStatus;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
