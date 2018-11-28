package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/9
 */

public class CampusInformationEntity {

    /**
     * default_org_id : 11.0
     * default_org_area_id : 19.0
     * default_building_id : 88.0
     * default_device_area_id : 2.0
     * user_type : 2.0
     * org_name : 高品大学
     * org_area_name : 长青校区
     * _building_no : 2
     * device_area_name : 2号楼1层
     */

    private int default_org_id;
    private int default_org_area_id;
    private int default_building_id;
    private int default_device_area_id;
    private int user_type;
    private String org_name;
    private String org_area_name;
    private String _building_no;
    private String device_area_name;
    private int user_oauth;

    public static CampusInformationEntity objectFromData(String str) {

        return new Gson().fromJson(str, CampusInformationEntity.class);
    }

    public int getUser_oauth() {
        return user_oauth;
    }

    public void setUser_oauth(int user_oauth) {
        this.user_oauth = user_oauth;
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

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
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

    public String get_building_no() {
        return _building_no;
    }

    public void set_building_no(String _building_no) {
        this._building_no = _building_no;
    }

    public String getDevice_area_name() {
        return device_area_name;
    }

    public void setDevice_area_name(String device_area_name) {
        this.device_area_name = device_area_name;
    }
}
