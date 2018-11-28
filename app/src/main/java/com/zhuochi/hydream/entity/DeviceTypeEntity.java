package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/6/12
 */

public class DeviceTypeEntity {

    /**
     * device_type_key : faucet
     * device_type_name : 水控机
     * create_time : 0
     * device_driver : DeviceWaterControllerDriver
     * device_type_remark :
     * is_auto_generate : 1
     * is_hidden : 1
     * is_controller : 0
     * status : 1
     * basic_unit : 升
     * need_reserve : 1
     * payment_style : postpaid
     * icon_base_url : http://zaotang.94feel.com/static/images/faucet
     * short_tip : 浴位列表
     * orderby : 999
     * _num : 1
     * starting_fee : 0.00
     * unit_price : 2.70
     * charge_mode : quantity
     * fee_factor_quantity : 20.00000
     * fee_factor_time : 1.50000
     * fee_time_to_second : 60
     * fee_type_list : [{"price":"2.70","title":"4.05分钟"},{"price":"5.40","title":"8.1分钟"},{"price":"8.10","title":"12.15分钟"},{"price":"10.80","title":"16.2分钟"},{"price":"13.50","title":"20.25分钟"}]
     */

    private String device_type_key;
    private String device_type_name;
    private String create_time;
    private String device_driver;
    private String device_type_remark;
    private String is_auto_generate;
    private String is_hidden;
    private String is_controller;
    private String status;
    private String basic_unit;
    private int need_reserve;
    private String payment_style;
    private String icon_base_url;
    private String short_tip;
    private String orderby;
    private int _num;
    private String starting_fee;
    private String unit_price;
    private String charge_mode;
    private String fee_factor_quantity;
    private String fee_factor_time;
    private String fee_time_to_second;
    private List<FeeTypeListBean> fee_type_list;

    public static DeviceTypeEntity objectFromData(String str) {

        return new Gson().fromJson(str, DeviceTypeEntity.class);
    }

    public String getDevice_type_key() {
        return device_type_key;
    }

    public void setDevice_type_key(String device_type_key) {
        this.device_type_key = device_type_key;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDevice_driver() {
        return device_driver;
    }

    public void setDevice_driver(String device_driver) {
        this.device_driver = device_driver;
    }

    public String getDevice_type_remark() {
        return device_type_remark;
    }

    public void setDevice_type_remark(String device_type_remark) {
        this.device_type_remark = device_type_remark;
    }

    public String getIs_auto_generate() {
        return is_auto_generate;
    }

    public void setIs_auto_generate(String is_auto_generate) {
        this.is_auto_generate = is_auto_generate;
    }

    public String getIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(String is_hidden) {
        this.is_hidden = is_hidden;
    }

    public String getIs_controller() {
        return is_controller;
    }

    public void setIs_controller(String is_controller) {
        this.is_controller = is_controller;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBasic_unit() {
        return basic_unit;
    }

    public void setBasic_unit(String basic_unit) {
        this.basic_unit = basic_unit;
    }

    public int getNeed_reserve() {
        return need_reserve;
    }

    public void setNeed_reserve(int need_reserve) {
        this.need_reserve = need_reserve;
    }

    public String getPayment_style() {
        return payment_style;
    }

    public void setPayment_style(String payment_style) {
        this.payment_style = payment_style;
    }

    public String getIcon_base_url() {
        return icon_base_url;
    }

    public void setIcon_base_url(String icon_base_url) {
        this.icon_base_url = icon_base_url;
    }

    public String getShort_tip() {
        return short_tip;
    }

    public void setShort_tip(String short_tip) {
        this.short_tip = short_tip;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public String getStarting_fee() {
        return starting_fee;
    }

    public void setStarting_fee(String starting_fee) {
        this.starting_fee = starting_fee;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(String charge_mode) {
        this.charge_mode = charge_mode;
    }

    public String getFee_factor_quantity() {
        return fee_factor_quantity;
    }

    public void setFee_factor_quantity(String fee_factor_quantity) {
        this.fee_factor_quantity = fee_factor_quantity;
    }

    public String getFee_factor_time() {
        return fee_factor_time;
    }

    public void setFee_factor_time(String fee_factor_time) {
        this.fee_factor_time = fee_factor_time;
    }

    public String getFee_time_to_second() {
        return fee_time_to_second;
    }

    public void setFee_time_to_second(String fee_time_to_second) {
        this.fee_time_to_second = fee_time_to_second;
    }

    public List<FeeTypeListBean> getFee_type_list() {
        return fee_type_list;
    }

    public void setFee_type_list(List<FeeTypeListBean> fee_type_list) {
        this.fee_type_list = fee_type_list;
    }

    public static class FeeTypeListBean {
        /**
         * price : 2.70
         * title : 4.05分钟
         */

        private String price;
        private String title;

        public static FeeTypeListBean objectFromData(String str) {

            return new Gson().fromJson(str, FeeTypeListBean.class);
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
