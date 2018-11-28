package com.zhuochi.hydream.entity.exchang;

import java.io.Serializable;

/**
 * @author Cuixc
 * @date on  2018/7/17
 */

public class QueuingInfo implements Serializable {
    /**
     * id : 8
     * device_key :
     * controller_key :
     * device_type_key : faucet
     * v_device_type_key : 0
     * device_area_id : 4
     * device_area_name : 2号楼西浴室
     * settlement_area_id : 0
     * user_id : 25
     * create_time : 1529979971
     * state : waiting
     * finish_time : 0
     * message : 请关注您的排队位置变化
     以免错过洗浴时间
     * queuingNumber : 1
     * expectedWaitingTime : 300
     */

    private int id;
    private String device_key;
    private String controller_key;
    private String device_type_key;
    private String v_device_type_key;
    private int device_area_id;
    private String device_area_name;
    private String settlement_area_id;
    private String user_id;
    private String create_time;
    private String state;
    private String finish_time;
    private String message;
    private int queuingNumber;
    private int expectedWaitingTime;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getController_key() {
        return controller_key;
    }

    public void setController_key(String controller_key) {
        this.controller_key = controller_key;
    }

    public String getDevice_type_key() {
        return device_type_key;
    }

    public void setDevice_type_key(String device_type_key) {
        this.device_type_key = device_type_key;
    }

    public String getV_device_type_key() {
        return v_device_type_key;
    }

    public void setV_device_type_key(String v_device_type_key) {
        this.v_device_type_key = v_device_type_key;
    }

    public int getDevice_area_id() {
        return device_area_id;
    }

    public void setDevice_area_id(int device_area_id) {
        this.device_area_id = device_area_id;
    }

    public String getDevice_area_name() {
        return device_area_name;
    }

    public void setDevice_area_name(String device_area_name) {
        this.device_area_name = device_area_name;
    }

    public String getSettlement_area_id() {
        return settlement_area_id;
    }

    public void setSettlement_area_id(String settlement_area_id) {
        this.settlement_area_id = settlement_area_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQueuingNumber() {
        return queuingNumber;
    }

    public void setQueuingNumber(int queuingNumber) {
        this.queuingNumber = queuingNumber;
    }

    public int getExpectedWaitingTime() {
        return expectedWaitingTime;
    }

    public void setExpectedWaitingTime(int expectedWaitingTime) {
        this.expectedWaitingTime = expectedWaitingTime;
    }
}
