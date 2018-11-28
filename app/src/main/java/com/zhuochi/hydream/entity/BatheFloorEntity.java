package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/13
 */

public class BatheFloorEntity {

    /**
     * device_area_id : 3
     * device_area_name : 1号楼东浴室
     * org_id : 10
     * org_area_id : 15
     * building_id : 90
     * floor : 1
     * gender_limit : male
     * description :
     * longitude : 117.1284660
     * latitude : 36.6814740
     * controller_key : 9989df54-ff05-431f-a8c2-11ce63d34249
     * status : 1
     * maintenance_start : 0
     * maintenance_end : 0
     * maintenance_tip : 临时检修，开放时间待通知
     * _num : 1
     * idleNum : 0
     * totalNum : 2
     * reminder
     */

    private int device_area_id;
    private String device_area_name;
    private int org_id;
    private int org_area_id;
    private int building_id;
    private int floor;
    private String gender_limit;
    private String description;
    private String longitude;
    private String latitude;
    private String controller_key;
    private int status;
    private int maintenance_start;
    private int maintenance_end;
    private String maintenance_tip;
    private int _num;
    private int idleNum;
    private int totalNum;
    private String reminder;

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public static BatheFloorEntity objectFromData(String str) {

        return new Gson().fromJson(str, BatheFloorEntity.class);
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

    public int getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getGender_limit() {
        return gender_limit;
    }

    public void setGender_limit(String gender_limit) {
        this.gender_limit = gender_limit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getController_key() {
        return controller_key;
    }

    public void setController_key(String controller_key) {
        this.controller_key = controller_key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaintenance_start() {
        return maintenance_start;
    }

    public void setMaintenance_start(int maintenance_start) {
        this.maintenance_start = maintenance_start;
    }

    public int getMaintenance_end() {
        return maintenance_end;
    }

    public void setMaintenance_end(int maintenance_end) {
        this.maintenance_end = maintenance_end;
    }

    public String getMaintenance_tip() {
        return maintenance_tip;
    }

    public void setMaintenance_tip(String maintenance_tip) {
        this.maintenance_tip = maintenance_tip;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public int getIdleNum() {
        return idleNum;
    }

    public void setIdleNum(int idleNum) {
        this.idleNum = idleNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
