package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * 押金充值
 * @author Cuixc
 * @date on  2018/7/21
 */

public class DepositEntity {

    /**
     * amount : 10.00
     * payType : [{"typeKey":"weixinpay","typeName":"微信支付","typeImage":"weixinpay.png"},{"typeKey":"alipay","typeName":"支付宝","typeImage":"alipay.png"}]
     * deviceAreaId : 4
     * settlementAreaId : 20
     */

    private String amount;
    private String deviceAreaId;
    private int settlementAreaId;
    private List<PayTypeBean> payType;

    public static DepositEntity objectFromData(String str) {

        return new Gson().fromJson(str, DepositEntity.class);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeviceAreaId() {
        return deviceAreaId;
    }

    public void setDeviceAreaId(String deviceAreaId) {
        this.deviceAreaId = deviceAreaId;
    }

    public int getSettlementAreaId() {
        return settlementAreaId;
    }

    public void setSettlementAreaId(int settlementAreaId) {
        this.settlementAreaId = settlementAreaId;
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
        private String method;
        private String method_name;
        private String ico;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getMethod_name() {
            return method_name;
        }

        public void setMethod_name(String method_name) {
            this.method_name = method_name;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
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
