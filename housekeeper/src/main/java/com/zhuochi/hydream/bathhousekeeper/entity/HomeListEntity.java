package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/8/27
 */

public class HomeListEntity {

    /**
     * regUserNumAll : 70.0
     * regUserNumToday : 0.0
     * consumeMoneyAll : 47.86
     * consumeMoneyToday : 0.0
     * validUserNumTody : 0.0
     * orderNumAll : 408.0
     * orderNumToday : 0.0
     */

    private int regUserNumAll;
    private int regUserNumToday;
    private String consumeMoneyAll;
    private String consumeMoneyToday;
    private int validUserNumTody;
    private int orderNumAll;
    private int orderNumToday;

    public static HomeListEntity objectFromData(String str) {

        return new Gson().fromJson(str, HomeListEntity.class);
    }

    public int getRegUserNumAll() {
        return regUserNumAll;
    }

    public void setRegUserNumAll(int regUserNumAll) {
        this.regUserNumAll = regUserNumAll;
    }

    public int getRegUserNumToday() {
        return regUserNumToday;
    }

    public void setRegUserNumToday(int regUserNumToday) {
        this.regUserNumToday = regUserNumToday;
    }

    public String getConsumeMoneyAll() {
        return consumeMoneyAll;
    }

    public void setConsumeMoneyAll(String consumeMoneyAll) {
        this.consumeMoneyAll = consumeMoneyAll;
    }

    public String getConsumeMoneyToday() {
        return consumeMoneyToday;
    }

    public void setConsumeMoneyToday(String consumeMoneyToday) {
        this.consumeMoneyToday = consumeMoneyToday;
    }

    public int getValidUserNumTody() {
        return validUserNumTody;
    }

    public void setValidUserNumTody(int validUserNumTody) {
        this.validUserNumTody = validUserNumTody;
    }

    public int getOrderNumAll() {
        return orderNumAll;
    }

    public void setOrderNumAll(int orderNumAll) {
        this.orderNumAll = orderNumAll;
    }

    public int getOrderNumToday() {
        return orderNumToday;
    }

    public void setOrderNumToday(int orderNumToday) {
        this.orderNumToday = orderNumToday;
    }
}
