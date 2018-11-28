package com.zhuochi.hydream.bathhousekeeper.bean;

public class OrderFlowItemBean {


    /**
     * order_year : 2018
     * order_month : 201807
     * order_date : 2.0180729E7
     * num : 5
     * money : 6.00
     */

    private int order_year;
    private String order_month;
    private double order_date;
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

    public double getOrder_date() {
        return order_date;
    }

    public void setOrder_date(double order_date) {
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
}
