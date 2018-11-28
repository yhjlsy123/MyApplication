package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/9/7
 */

public class ActivityEntity {

    /**
     * activity_id : 37
     * activity_name : 考虑图
     * org_id : 43
     * org_area_id : 28
     * org_name : 齐鲁理工学院
     * org_area_name : 济南校区
     * gender : 男女均可
     * start_time : 2017-09-07 00:00:00
     * end_time : 2024-09-07 00:00:00
     */

    private int activity_id;
    private String activity_name;
    private int org_id;
    private int org_area_id;
    private String org_name;
    private String org_area_name;
    private String gender;
    private String start_time;
    private String end_time;

    public static ActivityEntity objectFromData(String str) {

        return new Gson().fromJson(str, ActivityEntity.class);
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getOrg_area_id() {
        return org_area_id;
    }

    public void setOrg_area_id(int org_area_id) {
        this.org_area_id = org_area_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_area_name() {
        return org_area_name;
    }

    public void setOrg_area_name(String org_area_name) {
        this.org_area_name = org_area_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
