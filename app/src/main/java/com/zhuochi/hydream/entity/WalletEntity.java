package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/10
 */

public class WalletEntity {

    /**
     * wallet_id : 25
     * wallet_user_name :
     * wallet_pwd :
     * wallet_type : user
     * create_time : 0
     * score : 0
     * coin : 0
     * balance : 9777.80
     * deposit : 0.00
     * available_balance : 9777.8
     * freeze_balance : 0.00
     * given_balance : 889.00
     * available_credit : 0.00
     * device_pwd : byj1fJdKJBgk87FO4XlrrQ\u003d\u003d
     * pay_pwd :
     */

    private String wallet_id;
    private String wallet_user_name;
    private String wallet_pwd;
    private String wallet_type;
    private String create_time;
    private String score;
    private String coin;
    private String balance;
    private String deposit;
    private double available_balance;
    private String freeze_balance;
    private String given_balance;
    private String available_credit;
    private String device_pwd;
    private String pay_pwd;

    public static WalletEntity objectFromData(String str) {

        return new Gson().fromJson(str, WalletEntity.class);
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getWallet_user_name() {
        return wallet_user_name;
    }

    public void setWallet_user_name(String wallet_user_name) {
        this.wallet_user_name = wallet_user_name;
    }

    public String getWallet_pwd() {
        return wallet_pwd;
    }

    public void setWallet_pwd(String wallet_pwd) {
        this.wallet_pwd = wallet_pwd;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public double getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(double available_balance) {
        this.available_balance = available_balance;
    }

    public String getFreeze_balance() {
        return freeze_balance;
    }

    public void setFreeze_balance(String freeze_balance) {
        this.freeze_balance = freeze_balance;
    }

    public String getGiven_balance() {
        return given_balance;
    }

    public void setGiven_balance(String given_balance) {
        this.given_balance = given_balance;
    }

    public String getAvailable_credit() {
        return available_credit;
    }

    public void setAvailable_credit(String available_credit) {
        this.available_credit = available_credit;
    }

    public String getDevice_pwd() {
        return device_pwd;
    }

    public void setDevice_pwd(String device_pwd) {
        this.device_pwd = device_pwd;
    }

    public String getPay_pwd() {
        return pay_pwd;
    }

    public void setPay_pwd(String pay_pwd) {
        this.pay_pwd = pay_pwd;
    }
}
