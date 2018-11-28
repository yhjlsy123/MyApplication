package com.zhuochi.hydream.entity;

/**
 * Created by ztech on 2018/7/7.
 */

public class GiveEntity {


    /**
     * key : given_balance
     * value : 赠款
     * account : 0.00
     */

    private String key;
    private String value;
    private String account;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
