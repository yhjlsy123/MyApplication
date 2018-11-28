package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/7
 */

public class MessageTypeEntity {

    /**
     * id : 3
     * icon : http://newdev.gaopintech.cn/static/images/defaultMsgIcon.png
     * type_name : 朕的奏折
     * type_no : 1
     * create_time : 1.527679057E9
     * status : 1
     */

    private int id;
    private String icon;
    private String type_name;
    private int type_no;
    private double create_time;
    private int status;

    public static MessageTypeEntity objectFromData(String str) {

        return new Gson().fromJson(str, MessageTypeEntity.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getType_no() {
        return type_no;
    }

    public void setType_no(int type_no) {
        this.type_no = type_no;
    }

    public double getCreate_time() {
        return create_time;
    }

    public void setCreate_time(double create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
