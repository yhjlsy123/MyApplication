package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/6/29
 */

public class TurnOffPayType {


    /**
     * uuid : 9019df64-ff05-433f-a8c0-51ce63d36265_faucet_03
     * deviceNumber : 03
     * startTime : 1530692381
     * endTime : 1.530692396E9
     * usedTime : 15
     * orderSn : 167017
     * autoPay : 0
     * isSoftPledge : 1
     * cashPledge : 5.00
     * startingFee : 3.00
     * orderAmount : 222.2
     * payType : [{"typeKey":"weixinpay","typeName":"微信支付","typeImage":"weixinpay.png"},{"typeKey":"alipay","typeName":"支付宝","typeImage":"alipay.png"},{"typeKey":"sys_cash","typeName":"余额","typeImage":"sys_cash.png"}]
     */

    private String uuid;
    private String deviceNumber;
    private long startTime;
    private long endTime;
    private int usedTime;
    private String orderSn;
    private int autoPay;
    private int isSoftPledge;
    private String cashPledge;
    private String startingFee;
    private double orderAmount;
    private double cashAmount;
    private List<PayTypeBean> payType;
    private String orderUsage;

    public String getOrderUsage() {
        return orderUsage;
    }

    public void setOrderUsage(String orderUsage) {
        this.orderUsage = orderUsage;
    }

    public String getUuid() {
        return uuid;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
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

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
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

    public int getIsSoftPledge() {
        return isSoftPledge;
    }

    public void setIsSoftPledge(int isSoftPledge) {
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

    public List<PayTypeBean> getPayType() {
        return payType;
    }

    public void setPayType(List<PayTypeBean> payType) {
        this.payType = payType;
    }

    public static class PayTypeBean {
        /**
         * typeKey : weixinpay
         * typeName : 微信支付
         * typeImage : weixinpay.png
         */

        private String typeKey;
        private String typeName;
        private String typeImage;

        public static PayTypeBean objectFromData(String str) {

            return new Gson().fromJson(str, PayTypeBean.class);
        }

        public String getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(String typeKey) {
            this.typeKey = typeKey;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeImage() {
            return typeImage;
        }

        public void setTypeImage(String typeImage) {
            this.typeImage = typeImage;
        }
    }
}
