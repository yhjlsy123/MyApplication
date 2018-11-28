package com.zhuochi.hydream.bean_;

import java.util.List;

/**
 * Created by and on 2016/11/19.
 */
public class BalanceBean {

    /**
     * balanceList : [{"id":80,"order_sn":"","bill_amount":"-0.01","dict_payment_purpose":"order_pay","bill_purpose":"","create_time":1.531818254E9,"bill_balance":"0.00","pay_sn":"","owner_type":"user","sys_type":"cash","external_sys_type":"","icon":"static/images/payment/sys_cash.png","purpose_name":"支付订单"}]
     * ifLast : 1
     */

    private int ifLast;
    private List<BalanceListBean> balanceList;

    public int getIfLast() {
        return ifLast;
    }

    public void setIfLast(int ifLast) {
        this.ifLast = ifLast;
    }

    public List<BalanceListBean> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<BalanceListBean> balanceList) {
        this.balanceList = balanceList;
    }

    public static class BalanceListBean {
        /**
         * id : 80
         * order_sn :
         * bill_amount : -0.01
         * dict_payment_purpose : order_pay
         * bill_purpose :
         * create_time : 1.531818254E9
         * bill_balance : 0.00
         * pay_sn :
         * owner_type : user
         * sys_type : cash
         * external_sys_type :
         * icon : static/images/payment/sys_cash.png
         * purpose_name : 支付订单
         */

        private int id;
        private String order_sn;
        private String bill_amount;
        private String dict_payment_purpose;
        private String bill_purpose;
        private long create_time;
        private String bill_balance;
        private String pay_sn;
        private String owner_type;
        private String sys_type;
        private String external_sys_type;
        private String icon;
        private String purpose_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getBill_amount() {
            return bill_amount;
        }

        public void setBill_amount(String bill_amount) {
            this.bill_amount = bill_amount;
        }

        public String getDict_payment_purpose() {
            return dict_payment_purpose;
        }

        public void setDict_payment_purpose(String dict_payment_purpose) {
            this.dict_payment_purpose = dict_payment_purpose;
        }

        public String getBill_purpose() {
            return bill_purpose;
        }

        public void setBill_purpose(String bill_purpose) {
            this.bill_purpose = bill_purpose;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getBill_balance() {
            return bill_balance;
        }

        public void setBill_balance(String bill_balance) {
            this.bill_balance = bill_balance;
        }

        public String getPay_sn() {
            return pay_sn;
        }

        public void setPay_sn(String pay_sn) {
            this.pay_sn = pay_sn;
        }

        public String getOwner_type() {
            return owner_type;
        }

        public void setOwner_type(String owner_type) {
            this.owner_type = owner_type;
        }

        public String getSys_type() {
            return sys_type;
        }

        public void setSys_type(String sys_type) {
            this.sys_type = sys_type;
        }

        public String getExternal_sys_type() {
            return external_sys_type;
        }

        public void setExternal_sys_type(String external_sys_type) {
            this.external_sys_type = external_sys_type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPurpose_name() {
            return purpose_name;
        }

        public void setPurpose_name(String purpose_name) {
            this.purpose_name = purpose_name;
        }
    }
}
