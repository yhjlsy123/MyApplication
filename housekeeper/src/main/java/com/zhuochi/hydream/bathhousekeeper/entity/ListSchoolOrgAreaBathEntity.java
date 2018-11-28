package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/9/10
 */

public class ListSchoolOrgAreaBathEntity {

    /**
     * device_key : 7a494403-b526-43e5-9b9f-277a0979d375_faucet_01
     * device_name : 洗浴01
     */

    private String device_key;
    private String device_name;

    public static ListSchoolOrgAreaBathEntity objectFromData(String str) {

        return new Gson().fromJson(str, ListSchoolOrgAreaBathEntity.class);
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
}
