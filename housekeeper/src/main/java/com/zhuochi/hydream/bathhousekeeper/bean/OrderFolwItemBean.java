package com.zhuochi.hydream.bathhousekeeper.bean;

import java.util.List;

public class OrderFolwItemBean extends MyMultipleItem {


    /**
     * date : 201807
     * order : [{"order_year":2018,"order_month":"201807","order_date":2.0180729E7,"num":"5","money":"6.00"},{"order_year":2018,"order_month":"201807","order_date":2.0180728E7,"num":"2","money":"2.40"},{"order_year":2018,"order_month":"201807","order_date":2.0180727E7,"num":"6","money":"7.20"},{"order_year":2018,"order_month":"201807","order_date":2.0180726E7,"num":"2","money":"2.40"},{"order_year":2018,"order_month":"201807","order_date":2.0180725E7,"num":"9","money":"10.80"},{"order_year":2018,"order_month":"201807","order_date":2.0180723E7,"num":"17","money":"17.82"},{"order_year":2018,"order_month":"201807","order_date":2.0180721E7,"num":"10","money":"0.10"},{"order_year":2018,"order_month":"201807","order_date":2.018072E7,"num":"26","money":"0.25"},{"order_year":2018,"order_month":"201807","order_date":2.0180719E7,"num":"89","money":"0.89"}]
     * num : 166
     * money : 47.86
     */

    private String date;
    private int num;
    private String money;
    private List<OrderBean> order;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }

    @Override
    public int getItemType() {
        return MyMultipleItem.FIRST_TYPE;
    }

    public static class OrderBean extends MyMultipleItem {
        /**
         * order_year : 2018
         * order_month : 201807
         * order_date : 20180729E7
         * num : 5
         * money : 6.00
         */

        private int order_year;
        private String order_month;
        private Long order_date;
        private String num;
        private String money;

        public int getOrder_year() {
            return order_year;
        }

        public void setOrder_year(int order_year) {
            this.order_year = order_year;
        }

        public String getOrder_month() {
            return order_month;
        }

        public void setOrder_month(String order_month) {
            this.order_month = order_month;
        }

        public Long getOrder_date() {
            return order_date;
        }

        public void setOrder_date(Long order_date) {
            this.order_date = order_date;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Override
        public int getItemType() {
            return MyMultipleItem.SECOND_TYPE;
        }
    }
}
