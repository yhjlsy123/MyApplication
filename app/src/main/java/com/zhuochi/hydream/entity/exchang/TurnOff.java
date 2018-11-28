package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/29
 */

public class TurnOff {

    /**
     * uuid : 9019df64-ff05-433f-a8c0-51ce63d36265_faucet_05
     * startTime : 1530243887
     * endTime : 153024464
     * orderSn : 91014
     * autoPay : 1
     * isSoftPledge : 1
     * cashPledge : 5.00
     * startingFee : 3.00
     * orderAmount : 222.2
     */

    private String uuid;
    private long startTime;
    private long endTime;
    private String orderSn;
    private int autoPay;
    private String isSoftPledge;
    private String cashPledge;
    private String startingFee;
    private double orderAmount;
    private String orderUsage;

    public static TurnOff objectFromData(String str) {

        return new Gson().fromJson(str, TurnOff.class);
    }

    public String getOrderUsage() {
        return orderUsage;
    }

    public void setOrderUsage(String orderUsage) {
        this.orderUsage = orderUsage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(int autoPay) {
        this.autoPay = autoPay;
    }

    public String getIsSoftPledge() {
        return isSoftPledge;
    }

    public void setIsSoftPledge(String isSoftPledge) {
        this.isSoftPledge = isSoftPledge;
    }

    public String getCashPledge() {
        return cashPledge;
    }

    public void setCashPledge(String cashPledge) {
        this.cashPledge = cashPledge;
    }

    public String getStartingFee() {
        return startingFee;
    }

    public void setStartingFee(String startingFee) {
        this.startingFee = startingFee;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }
}
