package com.zhuochi.hydream.bathhousekeeper.bean;

public class OrderListItemBean {


    /**
     * order_item_id : 419
     * order_sn : 0817694017
     * order_amount : 0.00
     * cash_amount : 0.00
     * order_state : 使用中
     * order_state_key : 0
     * create_time : 2018-08-17 17:48:30
     * bathroom : 卓驰设备区域_1
     */

    private int order_item_id;
    private String order_sn;
    private String order_amount;
    private String cash_amount;
    private String order_state;
    private String order_state_key;
    private String create_time;
    private String bathroom;

    public int getOrder_item_id() {
        return order_item_id;
    }

    public String getOrder_state_key() {
        return order_state_key;
    }

    public void setOrder_state_key(String order_state_key) {
        this.order_state_key = order_state_key;
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

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }
}
