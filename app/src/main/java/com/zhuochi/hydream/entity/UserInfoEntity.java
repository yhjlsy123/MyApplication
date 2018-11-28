package com.zhuochi.hydream.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/9
 */

public class UserInfoEntity implements Parcelable {


    /**
     * id : 25
     * sex : 1
     * birthday : 1998-07-01
     * user_nickname : momo
     * avatar : http://p8uyenppn.bkt.clouddn.com/20180811100851648.jpg
     * signature : 你爱或者不爱我……
     * user_login : 18854117407
     * mobile : 18854117407
     * default_org_id : 43
     * default_org_area_id : 28
     * default_building_id : 110
     * default_device_area_id : 19
     * direct_pay_amount : 100.00
     * user_status : 1
     * device_pwd : u2MnJk8fY2FvQbsb6KeAGQ\u003d\u003d
     * wallet_id : 25
     * user_pass : ###269cceb2fbef5a2260ed62aa353f07a9
     * user_type : 2
     * store_id : 0
     * grade_current : 1
     * grade_length : 4
     * device_pwd_source : 123456789
     * default_org_name : 齐鲁理工学院
     * default_org_area_name : 济南校区
     * default_building_name : 1
     * default_device_area_name : 1号楼1层服务器_02
     * external_device_type :
     * wallet_user_name :
     * wallet_type : user
     * score : 0
     * coin : 0
     * balance : 1010.12
     * available_balance : 1010.12
     * freeze_balance : 0.00
     * given_balance : 1.00
     * wallet_pwd :
     * deposit : 100.00
     */

    private int id;
    private int sex;
    private String birthday;
    private String user_nickname;
    private String avatar;
    private String signature;
    private String user_login;
    private String mobile;
    private int default_org_id;
    private int default_org_area_id;
    private int default_building_id;
    private int default_device_area_id;
    private String direct_pay_amount;
    private int user_status;
    private String device_pwd;
    private int wallet_id;
    private String user_pass;
    private int user_type;
    private int store_id;
    private int grade_current;
    private int grade_length;
    private String device_pwd_source;
    private String default_org_name;
    private String default_org_area_name;
    private String default_building_name;
    private String default_device_area_name;
    private String external_device_type;
    private String wallet_user_name;
    private String wallet_type;
    private int score;
    private int coin;
    private String balance;
    private String available_balance;
    private String freeze_balance;
    private String given_balance;
    private String wallet_pwd;
    private String deposit;

    public static UserInfoEntity objectFromData(String str) {

        return new Gson().fromJson(str, UserInfoEntity.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getDefault_org_id() {
        return default_org_id;
    }

    public void setDefault_org_id(int default_org_id) {
        this.default_org_id = default_org_id;
    }

    public int getDefault_org_area_id() {
        return default_org_area_id;
    }

    public void setDefault_org_area_id(int default_org_area_id) {
        this.default_org_area_id = default_org_area_id;
    }

    public int getDefault_building_id() {
        return default_building_id;
    }

    public void setDefault_building_id(int default_building_id) {
        this.default_building_id = default_building_id;
    }

    public int getDefault_device_area_id() {
        return default_device_area_id;
    }

    public void setDefault_device_area_id(int default_device_area_id) {
        this.default_device_area_id = default_device_area_id;
    }

    public String getDirect_pay_amount() {
        return direct_pay_amount;
    }

    public void setDirect_pay_amount(String direct_pay_amount) {
        this.direct_pay_amount = direct_pay_amount;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public String getDevice_pwd() {
        return device_pwd;
    }

    public void setDevice_pwd(String device_pwd) {
        this.device_pwd = device_pwd;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getGrade_current() {
        return grade_current;
    }

    public void setGrade_current(int grade_current) {
        this.grade_current = grade_current;
    }

    public int getGrade_length() {
        return grade_length;
    }

    public void setGrade_length(int grade_length) {
        this.grade_length = grade_length;
    }

    public String getDevice_pwd_source() {
        return device_pwd_source;
    }

    public void setDevice_pwd_source(String device_pwd_source) {
        this.device_pwd_source = device_pwd_source;
    }

    public String getDefault_org_name() {
        return default_org_name;
    }

    public void setDefault_org_name(String default_org_name) {
        this.default_org_name = default_org_name;
    }

    public String getDefault_org_area_name() {
        return default_org_area_name;
    }

    public void setDefault_org_area_name(String default_org_area_name) {
        this.default_org_area_name = default_org_area_name;
    }

    public String getDefault_building_name() {
        return default_building_name;
    }

    public void setDefault_building_name(String default_building_name) {
        this.default_building_name = default_building_name;
    }

    public String getDefault_device_area_name() {
        return default_device_area_name;
    }

    public void setDefault_device_area_name(String default_device_area_name) {
        this.default_device_area_name = default_device_area_name;
    }

    public String getExternal_device_type() {
        return external_device_type;
    }

    public void setExternal_device_type(String external_device_type) {
        this.external_device_type = external_device_type;
    }

    public String getWallet_user_name() {
        return wallet_user_name;
    }

    public void setWallet_user_name(String wallet_user_name) {
        this.wallet_user_name = wallet_user_name;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(String available_balance) {
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

    public String getWallet_pwd() {
        return wallet_pwd;
    }

    public void setWallet_pwd(String wallet_pwd) {
        this.wallet_pwd = wallet_pwd;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
