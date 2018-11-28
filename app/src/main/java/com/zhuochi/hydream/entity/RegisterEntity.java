package com.zhuochi.hydream.entity;


import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/5/12
 */

public class RegisterEntity {


    /**
     * id : 25
     * wallet_id : 25
     * user_type : 2
     * sex : 2
     * birthday : 2000-07-01
     * last_login_time : 1531186419
     * credit_limit : 0.00
     * create_time : 1526546800
     * user_status : 2
     * user_login : 18854117407
     * user_pass : ###269cceb2fbef5a2260ed62aa353f07a9
     * user_nickname : momo
     * user_email :
     * user_url :
     * avatar : http:\/\/p8uyenppn.bkt.clouddn.com\/20180611170503293.jpg
     * signature : \u4f60\u7231\u6216\u8005\u4e0d\u7231\u6211
     * last_login_ip : 222.173.29.94
     * user_activation_key :
     * mobile : 18854117407
     * more : null
     * device_pwd : byj1fJdKJBgk87FO4XlrrQ==
     * default_org_id : 10
     * default_org_area_id : 15
     * default_building_id : 91
     * default_device_area_id : 4
     * store_id : 0
     * pay_pwd :
     * direct_pay_amount : 100.00
     * token : 7f3ded0bd1cb9cf0af9437ca723d5b947f3ded0bd1cb9cf0af9437ca723d5b94
     */

    private int id;
    private int wallet_id;
    private int user_type;
    private int sex;
    private String birthday;
    private int last_login_time;
    private String credit_limit;
    private int create_time;
    private int user_status;
    private int user_oauth;
    private String user_login;
    private String user_pass;
    private String user_nickname;
    private String user_email;
    private String user_url;
    private String avatar;
    private String signature;
    private String last_login_ip;
    private String user_activation_key;
    private String mobile;
    private Object more;
    private String device_pwd;
    private int default_org_id;
    private int default_org_area_id;
    private int default_building_id;
    private int default_device_area_id;
    private int store_id;
    private String pay_pwd;
    private String direct_pay_amount;
    private String token;

    public static RegisterEntity objectFromData(String str) {

        return new Gson().fromJson(str, RegisterEntity.class);
    }

    public void setUser_oauth(int id) {
        this.user_oauth = user_oauth;
    }

    public int getUser_oauth() {
        return user_oauth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
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

    public int getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(int last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(String credit_limit) {
        this.credit_limit = credit_limit;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
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

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getUser_activation_key() {
        return user_activation_key;
    }

    public void setUser_activation_key(String user_activation_key) {
        this.user_activation_key = user_activation_key;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getMore() {
        return more;
    }

    public void setMore(Object more) {
        this.more = more;
    }

    public String getDevice_pwd() {
        return device_pwd;
    }

    public void setDevice_pwd(String device_pwd) {
        this.device_pwd = device_pwd;
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

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getPay_pwd() {
        return pay_pwd;
    }

    public void setPay_pwd(String pay_pwd) {
        this.pay_pwd = pay_pwd;
    }

    public String getDirect_pay_amount() {
        return direct_pay_amount;
    }

    public void setDirect_pay_amount(String direct_pay_amount) {
        this.direct_pay_amount = direct_pay_amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
