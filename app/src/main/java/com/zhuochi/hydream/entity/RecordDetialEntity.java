package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * Created by ztech on 2018/7/16.
 */

public class RecordDetialEntity {


    /**
     * order_item_id : 693
     * order_sn : 9680014
     * pay_sn : P100112990012
     * order_state : 4
     * device_key : abcdefafaefaefae_faucet_02
     * controller_key : abcdefafaefaefae
     * device_type_key : faucet
     * start_time : 1533882368
     * end_time : 1533882371
     * pay_time : 1533882389
     * total_used_time : 3
     * device_return_time : 0
     * idle_time : 0
     * actual_usage : 0.00
     * order_amount : 0.05
     * coupon_amount : 0.00
     * cash_amount : 0.05
     * title : 订单详情
     * external_sys_type : alipay
     * external_pay_sn : 2018081021001004040568847401
     * payInfo : {"pay_sn":"P100112990012","external_pay_sn":"2018081021001004040568847401","total_amount":"0.05","deduction_amount":"0.00","pay_currency":"CNY","real_pay_amount":"0.05","inner_pay_amount":"0.00","cross_sys_amount":"0.05","commission_amount":"0.00","commission_rate":"0.00000","commission_wallet_id":"1","refund_amount":"0.00","refund_commission_amount":"0.00","create_time":"1533882375","payment_time":"1533882388","refund_create_time":"0","refund_time":"0","payment_type":"sys_to_sys","purpose_key":"order_pay","payer_id":"25","payer_wallet_id":"25","payer_ext_account_id":"2088512055289043","payer_type":"user","payee_id":"31","payee_wallet_id":"53","payee_ext_account_id":"2018062660479035","payee_type":"store","settlement_area_id":"0","pay_state":"success","external_sys_pay_state":"success","external_sys_type":"alipay","relation_id":"","relation_type":""}
     * payTypeName : 支付宝支付
     */

    private int order_item_id;
    private String order_sn;
    private String pay_sn;
    private String order_state;
    private String device_key;
    private String controller_key;
    private String device_type_key;
    private long start_time;
    private long end_time;
    private int pay_time;
    private int total_used_time;
    private int device_return_time;
    private int idle_time;
    private String actual_usage;
    private String order_amount;
    private String coupon_amount;
    private String cash_amount;
    private String title;
    private String external_sys_type;
    private String external_pay_sn;
    private PayInfoBean payInfo;
    private String payTypeName;

    public static RecordDetialEntity objectFromData(String str) {

        return new Gson().fromJson(str, RecordDetialEntity.class);
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getController_key() {
        return controller_key;
    }

    public void setController_key(String controller_key) {
        this.controller_key = controller_key;
    }

    public String getDevice_type_key() {
        return device_type_key;
    }

    public void setDevice_type_key(String device_type_key) {
        this.device_type_key = device_type_key;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public int getTotal_used_time() {
        return total_used_time;
    }

    public void setTotal_used_time(int total_used_time) {
        this.total_used_time = total_used_time;
    }

    public int getDevice_return_time() {
        return device_return_time;
    }

    public void setDevice_return_time(int device_return_time) {
        this.device_return_time = device_return_time;
    }

    public int getIdle_time() {
        return idle_time;
    }

    public void setIdle_time(int idle_time) {
        this.idle_time = idle_time;
    }

    public String getActual_usage() {
        return actual_usage;
    }

    public void setActual_usage(String actual_usage) {
        this.actual_usage = actual_usage;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExternal_sys_type() {
        return external_sys_type;
    }

    public void setExternal_sys_type(String external_sys_type) {
        this.external_sys_type = external_sys_type;
    }

    public String getExternal_pay_sn() {
        return external_pay_sn;
    }

    public void setExternal_pay_sn(String external_pay_sn) {
        this.external_pay_sn = external_pay_sn;
    }

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public static class PayInfoBean {
        /**
         * pay_sn : P100112990012
         * external_pay_sn : 2018081021001004040568847401
         * total_amount : 0.05
         * deduction_amount : 0.00
         * pay_currency : CNY
         * real_pay_amount : 0.05
         * inner_pay_amount : 0.00
         * cross_sys_amount : 0.05
         * commission_amount : 0.00
         * commission_rate : 0.00000
         * commission_wallet_id : 1
         * refund_amount : 0.00
         * refund_commission_amount : 0.00
         * create_time : 1533882375
         * payment_time : 1533882388
         * refund_create_time : 0
         * refund_time : 0
         * payment_type : sys_to_sys
         * purpose_key : order_pay
         * payer_id : 25
         * payer_wallet_id : 25
         * payer_ext_account_id : 2088512055289043
         * payer_type : user
         * payee_id : 31
         * payee_wallet_id : 53
         * payee_ext_account_id : 2018062660479035
         * payee_type : store
         * settlement_area_id : 0
         * pay_state : success
         * external_sys_pay_state : success
         * external_sys_type : alipay
         * relation_id :
         * relation_type :
         */

        private String pay_sn;
        private String external_pay_sn;
        private String total_amount;
        private String deduction_amount;
        private String pay_currency;
        private String real_pay_amount;
        private String inner_pay_amount;
        private String cross_sys_amount;
        private String commission_amount;
        private String commission_rate;
        private String commission_wallet_id;
        private String refund_amount;
        private String refund_commission_amount;
        private String create_time;
        private String payment_time;
        private String refund_create_time;
        private String refund_time;
        private String payment_type;
        private String purpose_key;
        private String payer_id;
        private String payer_wallet_id;
        private String payer_ext_account_id;
        private String payer_type;
        private String payee_id;
        private String payee_wallet_id;
        private String payee_ext_account_id;
        private String payee_type;
        private String settlement_area_id;
        private String pay_state;
        private String external_sys_pay_state;
        private String external_sys_type;
        private String relation_id;
        private String relation_type;

        public static PayInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, PayInfoBean.class);
        }

        public String getPay_sn() {
            return pay_sn;
        }

        public void setPay_sn(String pay_sn) {
            this.pay_sn = pay_sn;
        }

        public String getExternal_pay_sn() {
            return external_pay_sn;
        }

        public void setExternal_pay_sn(String external_pay_sn) {
            this.external_pay_sn = external_pay_sn;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getDeduction_amount() {
            return deduction_amount;
        }

        public void setDeduction_amount(String deduction_amount) {
            this.deduction_amount = deduction_amount;
        }

        public String getPay_currency() {
            return pay_currency;
        }

        public void setPay_currency(String pay_currency) {
            this.pay_currency = pay_currency;
        }

        public String getReal_pay_amount() {
            return real_pay_amount;
        }

        public void setReal_pay_amount(String real_pay_amount) {
            this.real_pay_amount = real_pay_amount;
        }

        public String getInner_pay_amount() {
            return inner_pay_amount;
        }

        public void setInner_pay_amount(String inner_pay_amount) {
            this.inner_pay_amount = inner_pay_amount;
        }

        public String getCross_sys_amount() {
            return cross_sys_amount;
        }

        public void setCross_sys_amount(String cross_sys_amount) {
            this.cross_sys_amount = cross_sys_amount;
        }

        public String getCommission_amount() {
            return commission_amount;
        }

        public void setCommission_amount(String commission_amount) {
            this.commission_amount = commission_amount;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public String getCommission_wallet_id() {
            return commission_wallet_id;
        }

        public void setCommission_wallet_id(String commission_wallet_id) {
            this.commission_wallet_id = commission_wallet_id;
        }

        public String getRefund_amount() {
            return refund_amount;
        }

        public void setRefund_amount(String refund_amount) {
            this.refund_amount = refund_amount;
        }

        public String getRefund_commission_amount() {
            return refund_commission_amount;
        }

        public void setRefund_commission_amount(String refund_commission_amount) {
            this.refund_commission_amount = refund_commission_amount;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getRefund_create_time() {
            return refund_create_time;
        }

        public void setRefund_create_time(String refund_create_time) {
            this.refund_create_time = refund_create_time;
        }

        public String getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(String refund_time) {
            this.refund_time = refund_time;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getPurpose_key() {
            return purpose_key;
        }

        public void setPurpose_key(String purpose_key) {
            this.purpose_key = purpose_key;
        }

        public String getPayer_id() {
            return payer_id;
        }

        public void setPayer_id(String payer_id) {
            this.payer_id = payer_id;
        }

        public String getPayer_wallet_id() {
            return payer_wallet_id;
        }

        public void setPayer_wallet_id(String payer_wallet_id) {
            this.payer_wallet_id = payer_wallet_id;
        }

        public String getPayer_ext_account_id() {
            return payer_ext_account_id;
        }

        public void setPayer_ext_account_id(String payer_ext_account_id) {
            this.payer_ext_account_id = payer_ext_account_id;
        }

        public String getPayer_type() {
            return payer_type;
        }

        public void setPayer_type(String payer_type) {
            this.payer_type = payer_type;
        }

        public String getPayee_id() {
            return payee_id;
        }

        public void setPayee_id(String payee_id) {
            this.payee_id = payee_id;
        }

        public String getPayee_wallet_id() {
            return payee_wallet_id;
        }

        public void setPayee_wallet_id(String payee_wallet_id) {
            this.payee_wallet_id = payee_wallet_id;
        }

        public String getPayee_ext_account_id() {
            return payee_ext_account_id;
        }

        public void setPayee_ext_account_id(String payee_ext_account_id) {
            this.payee_ext_account_id = payee_ext_account_id;
        }

        public String getPayee_type() {
            return payee_type;
        }

        public void setPayee_type(String payee_type) {
            this.payee_type = payee_type;
        }

        public String getSettlement_area_id() {
            return settlement_area_id;
        }

        public void setSettlement_area_id(String settlement_area_id) {
            this.settlement_area_id = settlement_area_id;
        }

        public String getPay_state() {
            return pay_state;
        }

        public void setPay_state(String pay_state) {
            this.pay_state = pay_state;
        }

        public String getExternal_sys_pay_state() {
            return external_sys_pay_state;
        }

        public void setExternal_sys_pay_state(String external_sys_pay_state) {
            this.external_sys_pay_state = external_sys_pay_state;
        }

        public String getExternal_sys_type() {
            return external_sys_type;
        }

        public void setExternal_sys_type(String external_sys_type) {
            this.external_sys_type = external_sys_type;
        }

        public String getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(String relation_id) {
            this.relation_id = relation_id;
        }

        public String getRelation_type() {
            return relation_type;
        }

        public void setRelation_type(String relation_type) {
            this.relation_type = relation_type;
        }
    }
}
