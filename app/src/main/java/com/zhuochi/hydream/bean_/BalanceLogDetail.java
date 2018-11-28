package com.zhuochi.hydream.bean_;

/**
 * 余额明细详情
 * Created by and on 2016/11/19.
 */

public class BalanceLogDetail   {

    /**
     * id : 80
     * store_org_id : 0
     * store_id : 0
     * order_sn :
     * bill_amount : -0.01
     * dict_payment_purpose : order_pay
     * bill_purpose :
     * create_time : 1.531818254E9
     * creator_id : 54
     * happen_time : 1.531818254E9
     * bill_balance : 0.00
     * sys_type : cash
     * external_sys_type :
     * pay_sn :
     * refund_sn :
     * pay_account :
     * owner_id : 51
     * owner_type : user
     * owner_wallet_id : 50
     * pay_type : sys_cash
     * pay_type_name : 余额支付
     * icon : static/images/payment/sys_cash.png
     * purpose_name : 支付订单
     */

    private int id;
    private int store_org_id;
    private int store_id;
    private String order_sn;
    private String bill_amount;
    private String dict_payment_purpose;
    private String bill_purpose;
    private long create_time;
    private int creator_id;
    private long happen_time;
    private String bill_balance;
    private String sys_type;
    private String external_sys_type;
    private String pay_sn;
    private String refund_sn;
    private String pay_account;
    private int owner_id;
    private String owner_type;
    private int owner_wallet_id;
    private String pay_type;
    private String pay_type_name;
    private String icon;
    private String purpose_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_org_id() {
        return store_org_id;
    }

    public void setStore_org_id(int store_org_id) {
        this.store_org_id = store_org_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
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

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public long getHappen_time() {
        return happen_time;
    }

    public void setHappen_time(long happen_time) {
        this.happen_time = happen_time;
    }

    public String getBill_balance() {
        return bill_balance;
    }

    public void setBill_balance(String bill_balance) {
        this.bill_balance = bill_balance;
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

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getRefund_sn() {
        return refund_sn;
    }

    public void setRefund_sn(String refund_sn) {
        this.refund_sn = refund_sn;
    }

    public String getPay_account() {
        return pay_account;
    }

    public void setPay_account(String pay_account) {
        this.pay_account = pay_account;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public int getOwner_wallet_id() {
        return owner_wallet_id;
    }

    public void setOwner_wallet_id(int owner_wallet_id) {
        this.owner_wallet_id = owner_wallet_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
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
