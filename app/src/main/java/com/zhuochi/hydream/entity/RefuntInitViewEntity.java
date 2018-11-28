package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/7
 */

public class RefuntInitViewEntity {

    /**
     * id : 25
     * sex : 2
     * birthday : 1010505600
     * user_nickname : \u8d85\u7ea7\u6211
     * avatar : http:\/\/p8uyenppn.bkt.clouddn.com\/20180611170503293.jpg
     * signature : \u5982\u679c\u6ca1\u6709\u5982\u679c\uff0c\u4ee5\u540e\u4e5f\u4e0d\u4f1a\u6709\u4ee5\u540e
     * user_login : 18854117407
     * mobile : 18854117407
     * default_org_id : 11
     * default_org_area_id : 19
     * default_building_id : 88
     * default_device_area_id : 2
     * direct_pay_amount : 100.00
     * more : null
     * user_status : 2
     * device_pwd : byj1fJdKJBgk87FO4XlrrQ==
     * wallet_id : 25
     * device_pwd_source : 159357258
     * default_org_name : \u9ad8\u54c1\u5927\u5b66
     * default_org_area_name : \u957f\u9752\u6821\u533a
     * default_building_name : 2
     * default_device_area_name : 2\u53f7\u697c1\u5c42
     * wallet_user_name :
     * wallet_type : user
     * score : 0
     * coin : 0
     * balance : 9777.80
     * available_balance : 9777.80
     * freeze_balance : 0.00
     * given_balance : 0.00
     * wallet_pwd :
     * deposit : 0.00
     * tip : \u4f59\u989d\u9000\u8fd8\u65f6\u95f4\u4e3a5-7\u4e2a\u5de5\u4f5c\u65e5,\u5145\u503c\u6ee1\u8d60\u6d3b\u52a8\u53d1\u751f\u9000\u6b3e,\u8d60\u9001\u7684\u6d3b\u52a8\u91d1\u989d\u5c06\u56de\u6536,\u656c\u8bf7\u8c05\u89e3
     * handle_tip : \u5c0f\u4e3b,\u60a8\u771f\u7684\u4e0b\u5b9a\u51b3\u5fc3\u8981\u9000\u6b3e\u4e86\u5417?
     * refund_balance : 9777.80
     * handle_type : \u4f59\u989d
     * total_balance : 9777.80
     * handle_remark
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
    private Object more;
    private int user_status;
    private String device_pwd;
    private int wallet_id;
    private String device_pwd_source;
    private String default_org_name;
    private String default_org_area_name;
    private String default_building_name;
    private String default_device_area_name;
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
    private String tip;
    private String handle_tip;
    private String refund_balance;
    private String handle_type;
    private String total_balance;
    private int handle_status = 0;//是否正在退款
    private int refund_id = 0;//退款ID
    private String handle_remark="";
    private String handle_status_text="";

    public String getHandle_remark() {
        return handle_remark;
    }

    public void setHandle_remark(String handle_remark) {
        this.handle_remark = handle_remark;
    }

    public int getHandle_status() {
        return handle_status;
    }

    public int getRefund_id() {
        return refund_id;
    }

    public String getHandle_status_text() {
        return handle_status_text;
    }

    public void setHandle_status_text(String handle_status_text) {
        this.handle_status_text = handle_status_text;
    }

    public void setRefund_id(int refund_id) {
        this.refund_id = refund_id;
    }

    public void setHandle_status(int handle_status) {
        this.handle_status = handle_status;
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

    public Object getMore() {
        return more;
    }

    public void setMore(Object more) {
        this.more = more;
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getHandle_tip() {
        return handle_tip;
    }

    public void setHandle_tip(String handle_tip) {
        this.handle_tip = handle_tip;
    }

    public String getRefund_balance() {
        return refund_balance;
    }

    public void setRefund_balance(String refund_balance) {
        this.refund_balance = refund_balance;
    }

    public String getHandle_type() {
        return handle_type;
    }

    public void setHandle_type(String handle_type) {
        this.handle_type = handle_type;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

}
