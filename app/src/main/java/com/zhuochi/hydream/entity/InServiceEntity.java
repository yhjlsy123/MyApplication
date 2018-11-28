package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/6/14
 */

public class InServiceEntity implements Serializable{

    /**
     * request_id :
     * args : []
     * action : turn_on
     * sub_devices : ["faucet_01"]
     */

    private String request_id;
    private String action;
    private List<?> args;
    private List<String> sub_devices;

    public static InServiceEntity objectFromData(String str) {

        return new Gson().fromJson(str, InServiceEntity.class);
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<?> getArgs() {
        return args;
    }

    public void setArgs(List<?> args) {
        this.args = args;
    }

    public List<String> getSub_devices() {
        return sub_devices;
    }

    public void setSub_devices(List<String> sub_devices) {
        this.sub_devices = sub_devices;
    }
}
