package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/9/10
 */

public class ListSchoolOrgAreaEntity {

    /**
     * device_area_id : 18
     * device_area_name : 4号楼1层
     * create_time : 1535828566
     * creator_id : 1
     * org_id : 43
     * org_area_id : 28
     * building_id : 113
     * floor : 1
     * settlement_area_id : 35
     * gender_limit : male
     * description :
     * longitude : 116.2300000
     * latitude : 39.5500000
     * controller_key : 6439701d-a2da-48fb-ac7d-f8a19162455e
     * external_device_type :
     * status : 1
     * maintenance_start : 1970
     * maintenance_end : 1970
     * maintenance_tip : 网络异常
     * service_time_1_id : 6
     * service_time_2_id : 0
     */

    private int device_area_id;
    private String device_area_name;
    private long create_time;
    private int creator_id;
    private int org_id;
    private int org_area_id;
    private int building_id;
    private int floor;
    private int settlement_area_id;
    private String gender_limit;
    private String description;
    private String longitude;
    private String latitude;
    private String controller_key;
    private String external_device_type;
    private int status;
    private int maintenance_start;
    private int maintenance_end;
    private String maintenance_tip;
    private int service_time_1_id;
    private int service_time_2_id;

    public static ListSchoolOrgAreaEntity objectFromData(String str) {

        return new Gson().fromJson(str, ListSchoolOrgAreaEntity.class);
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

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
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

    public int getSettlement_area_id() {
        return settlement_area_id;
    }

    public void setSettlement_area_id(int settlement_area_id) {
        this.settlement_area_id = settlement_area_id;
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

    public String getExternal_device_type() {
        return external_device_type;
    }

    public void setExternal_device_type(String external_device_type) {
        this.external_device_type = external_device_type;
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

    public int getService_time_1_id() {
        return service_time_1_id;
    }

    public void setService_time_1_id(int service_time_1_id) {
        this.service_time_1_id = service_time_1_id;
    }

    public int getService_time_2_id() {
        return service_time_2_id;
    }

    public void setService_time_2_id(int service_time_2_id) {
        this.service_time_2_id = service_time_2_id;
    }
}
