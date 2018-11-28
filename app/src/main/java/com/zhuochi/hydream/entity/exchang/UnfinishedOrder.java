package com.zhuochi.hydream.entity.exchang;


import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/18
 */

public class UnfinishedOrder {

    /**
     * order_item_id : 28
     * order_sn : 302018
     * pay_sn :
     * order_state : 1
     * client_org_id : 10
     * client_org_type : default
     * client_org_area_id : 15
     * building_id : 90
     * building_number :
     * goods_id : 1
     * gc_id : 0
     * goods_num : 1
     * device_number : 02
     * store_org_id : 0
     * store_id : 20
     * buyer_id : 25
     * device_key : b207d9f1-3a75-417a-9df2-f63c314eb1ae_faucet_02
     * controller_key : b207d9f1-3a75-417a-9df2-f63c314eb1ae
     * device_type_key : faucet
     * device_area_id : 12
     * settlement_area_id : 20
     * reserve_time : 1531897441
     * start_time : 1531897457
     * end_time : 1531897485
     * pay_time : 0
     * total_used_time : 28
     * device_return_time : 300
     * idle_time : 0
     * actual_usage : 100.00
     * order_amount : 0.01
     * coupon_amount : 0.00
     * cash_amount : 0.01
     * refund_amount : 0.00
     * refund_num : 0
     * activity_id : 0
     */

    private String order_item_id;
    private String order_sn;
    private String pay_sn;
    private String order_state;
    private String client_org_id;
    private String client_org_type;
    private String client_org_area_id;
    private String building_id;
    private String building_number;
    private String goods_id;
    private String gc_id;
    private String goods_num;
    private String device_number;
    private String store_org_id;
    private String store_id;
    private String buyer_id;
    private String device_key;
    private String controller_key;
    private String device_type_key;
    private String device_area_id;
    private String settlement_area_id;
    private String reserve_time;
    private String start_time;
    private String end_time;
    private String pay_time;
    private String total_used_time;
    private String device_return_time;
    private String idle_time;
    private String actual_usage;
    private double order_amount;
    private String coupon_amount;
    private String cash_amount;
    private String refund_amount;
    private String refund_num;
    private String activity_id;

    public static UnfinishedOrder objectFromData(String str) {

        return new Gson().fromJson(str, UnfinishedOrder.class);
    }

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
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

    public String getClient_org_id() {
        return client_org_id;
    }

    public void setClient_org_id(String client_org_id) {
        this.client_org_id = client_org_id;
    }

    public String getClient_org_type() {
        return client_org_type;
    }

    public void setClient_org_type(String client_org_type) {
        this.client_org_type = client_org_type;
    }

    public String getClient_org_area_id() {
        return client_org_area_id;
    }

    public void setClient_org_area_id(String client_org_area_id) {
        this.client_org_area_id = client_org_area_id;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getStore_org_id() {
        return store_org_id;
    }

    public void setStore_org_id(String store_org_id) {
        this.store_org_id = store_org_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
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

    public String getDevice_area_id() {
        return device_area_id;
    }

    public void setDevice_area_id(String device_area_id) {
        this.device_area_id = device_area_id;
    }

    public String getSettlement_area_id() {
        return settlement_area_id;
    }

    public void setSettlement_area_id(String settlement_area_id) {
        this.settlement_area_id = settlement_area_id;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getTotal_used_time() {
        return total_used_time;
    }

    public void setTotal_used_time(String total_used_time) {
        this.total_used_time = total_used_time;
    }

    public String getDevice_return_time() {
        return device_return_time;
    }

    public void setDevice_return_time(String device_return_time) {
        this.device_return_time = device_return_time;
    }

    public String getIdle_time() {
        return idle_time;
    }

    public void setIdle_time(String idle_time) {
        this.idle_time = idle_time;
    }

    public String getActual_usage() {
        return actual_usage;
    }

    public void setActual_usage(String actual_usage) {
        this.actual_usage = actual_usage;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
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

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_num() {
        return refund_num;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }
}
