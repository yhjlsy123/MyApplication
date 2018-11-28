package com.zhuochi.hydream.entity;

import java.util.List;

/**
 * Created by Inga on 2018/7/6.
 * 消费明细
 */

public class RecordEntity {

    /**
     * consumptionList : [{"order_item_id":22,"order_sn":"296012","order_state":"4","device_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08_faucet_02","controller_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08","start_time":1.531881376E9,"end_time":1.531881395E9,"device_type_key":"faucet","total_used_time":19,"device_return_time":300,"order_amount":"0.01","coupon_amount":"0.00","cash_amount":"0.01","icon":"/static/images/consumption/faucet.png"},{"order_item_id":68,"order_sn":"342015","order_state":"4","device_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08_faucet_01","controller_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08","start_time":1.531963731E9,"end_time":1.531963736E9,"device_type_key":"faucet","total_used_time":5,"device_return_time":300,"order_amount":"0.01","coupon_amount":"0.00","cash_amount":"0.01","icon":"/static/images/consumption/faucet.png"},{"order_item_id":72,"order_sn":"346016","order_state":"1","device_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08_faucet_02","controller_key":"f5a4b0da-f0b2-462d-9a3f-546ff689de08","start_time":1.531967227E9,"end_time":1.531967231E9,"device_type_key":"faucet","total_used_time":4,"device_return_time":300,"order_amount":"0.01","coupon_amount":"0.00","cash_amount":"0.01","icon":"/static/images/consumption/faucet.png"}]
     * ifLast : 1
     * totalAmount : 0.03
     */


    private double totalAmount;
    private List<ConsumptionListBean> consumptionList;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ConsumptionListBean> getConsumptionList() {
        return consumptionList;
    }

    public void setConsumptionList(List<ConsumptionListBean> consumptionList) {
        this.consumptionList = consumptionList;
    }

    public static class ConsumptionListBean {
        /**
         * order_item_id : 22
         * order_sn : 296012
         * order_state : 4
         * device_key : f5a4b0da-f0b2-462d-9a3f-546ff689de08_faucet_02
         * controller_key : f5a4b0da-f0b2-462d-9a3f-546ff689de08
         * start_time : 1.531881376E9
         * end_time : 1.531881395E9
         * device_type_key : faucet
         * total_used_time : 19
         * device_return_time : 300
         * order_amount : 0.01
         * coupon_amount : 0.00
         * cash_amount : 0.01
         * icon : /static/images/consumption/faucet.png
         */

        private int order_item_id;
        private String order_sn;
        private String order_state;
        private String device_key;
        private String controller_key;
        private long start_time;
        private long end_time;
        private String device_type_key;
        private int total_used_time;
        private int device_return_time;
        private String order_amount;
        private String coupon_amount;
        private String cash_amount;
        private String icon;

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

        public String getDevice_type_key() {
            return device_type_key;
        }

        public void setDevice_type_key(String device_type_key) {
            this.device_type_key = device_type_key;
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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
