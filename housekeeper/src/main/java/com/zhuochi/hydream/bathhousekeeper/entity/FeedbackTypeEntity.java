package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/5
 */

public class FeedbackTypeEntity {

    /**
     * id : 1
     * f_id : 0
     * name : 设备问题
     * create_time : 1527738087
     */

    private int id;
    private int f_id;
    private String name;
    private Object create_time;

    public static FeedbackTypeEntity objectFromData(String str) {

        return new Gson().fromJson(str, FeedbackTypeEntity.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Object create_time) {
        this.create_time = create_time;
    }
}
