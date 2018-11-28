package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/8/21
 */

public class LoginEntity {

    /**
     * id : 70
     * sex : 0
     * user_nickname :
     * avatar :
     * signature :
     * user_login : adtest1
     * mobile :
     * default_org_id : 0
     * default_org_area_id : 0
     * default_building_id : 0
     * default_device_area_id : 0
     * direct_pay_amount : 100.00
     * user_status : 1
     * device_pwd :
     * wallet_id : 0
     * user_pass : ###3c0499b5816a6024f29cb9016ef13af0
     * user_type : 1
     * store_id : 0
     * grade_current : 0
     * grade_length : 0
     * device_pwd_source : false
     * token : b77fffea4908ca034c9d6190e51e07b1b77fffea4908ca034c9d6190e51e07b1
     * store : superadmin
     */

    private int id;
    private int sex;
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
    private boolean device_pwd_source;
    private String token;
    private Object store;

    public static LoginEntity objectFromData(String str) {

        return new Gson().fromJson(str, LoginEntity.class);
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

    public boolean isDevice_pwd_source() {
        return device_pwd_source;
    }

    public void setDevice_pwd_source(boolean device_pwd_source) {
        this.device_pwd_source = device_pwd_source;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getStore() {
        return store;
    }

    public void setStore(Object store) {
        this.store = store;
    }
}
