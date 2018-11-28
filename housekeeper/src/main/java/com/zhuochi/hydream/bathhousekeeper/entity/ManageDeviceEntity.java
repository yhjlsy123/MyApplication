package com.zhuochi.hydream.bathhousekeeper.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Cuixc
 * @date on  2018/8/17
 */

public class ManageDeviceEntity {


    /**
     * bathroom : 1号楼1层
     * org_name : 齐鲁理工学院
     * org_area_name : 济南校区
     * uuid : 4e3d9afe-4011-4cc0-9391-d528b23c64b5
     * device_key : 4e3d9afe-4011-4cc0-9391-d528b23c64b5
     * gender : 女
     * gender_key : female
     * status_text : 正常
     * status : 1.0
     * building_number : 1号楼
     * floor : 1.0
     * maintenance_start : 0.0
     * maintenance_end : 0.0
     * maintenance_tip :
     * service_time_1_id : {"id":6,"show_name":"齐鲁理工学院时间段1：5:30-23:30","start_time":"2018-08-26","end_time":"2022-08-30","daily_start_time":"05:30","daily_end_time":"23:30"}
     * service_time_2_id : {"id":6,"show_name":"齐鲁理工学院时间段1：5:30-23:30","start_time":"2018-08-26","end_time":"2022-08-30","daily_start_time":"05:30","daily_end_time":"23:30"}
     * num : 24.0
     */

    private String bathroom;
    private String org_name;
    private String org_area_name;
    private String uuid;
    private String device_key;
    private String gender;
    private String gender_key;
    private String status_text;
    private double status;
    private String building_number;
    private double floor;
    private double maintenance_start;
    private double maintenance_end;
    private String maintenance_tip;
    private ServiceTime1IdBean service_time_1_id;
    private ServiceTime2IdBean service_time_2_id;
    private double num;

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender_key() {
        return gender_key;
    }

    public void setGender_key(String gender_key) {
        this.gender_key = gender_key;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

    public double getMaintenance_start() {
        return maintenance_start;
    }

    public void setMaintenance_start(double maintenance_start) {
        this.maintenance_start = maintenance_start;
    }

    public double getMaintenance_end() {
        return maintenance_end;
    }

    public void setMaintenance_end(double maintenance_end) {
        this.maintenance_end = maintenance_end;
    }

    public String getMaintenance_tip() {
        return maintenance_tip;
    }

    public void setMaintenance_tip(String maintenance_tip) {
        this.maintenance_tip = maintenance_tip;
    }

    public ServiceTime1IdBean getService_time_1_id() {
        return service_time_1_id;
    }

    public void setService_time_1_id(Object service_time_1_id) {
        if (service_time_1_id instanceof JSONObject) {
            this.service_time_1_id = JSON.parseObject(((JSONObject) service_time_1_id).toJSONString(), ServiceTime1IdBean.class);
        } else {
            this.service_time_1_id = null;
        }

    }

    public ServiceTime2IdBean getService_time_2_id() {
        return service_time_2_id;
    }

    public void setService_time_2_id(Object service_time_2_id) {

        if (service_time_2_id instanceof JSONObject) {
            this.service_time_2_id = JSON.parseObject(((JSONObject) service_time_2_id).toJSONString(), ServiceTime2IdBean.class);
        } else {
            this.service_time_2_id = null;
        }


    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public static class ServiceTime1IdBean {
        /**
         * id : 6.0
         * show_name : 齐鲁理工学院时间段1：5:30-23:30
         * start_time : 2018-08-26
         * end_time : 2022-08-30
         * daily_start_time : 05:30
         * daily_end_time : 23:30
         */

        private double id;
        private String show_name;
        private String start_time;
        private String end_time;
        private String daily_start_time;
        private String daily_end_time;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getShow_name() {
            return show_name;
        }

        public void setShow_name(String show_name) {
            this.show_name = show_name;
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

        public String getDaily_start_time() {
            return daily_start_time;
        }

        public void setDaily_start_time(String daily_start_time) {
            this.daily_start_time = daily_start_time;
        }

        public String getDaily_end_time() {
            return daily_end_time;
        }

        public void setDaily_end_time(String daily_end_time) {
            this.daily_end_time = daily_end_time;
        }
    }

    public static class ServiceTime2IdBean {
        /**
         * id : 6.0
         * show_name : 齐鲁理工学院时间段1：5:30-23:30
         * start_time : 2018-08-26
         * end_time : 2022-08-30
         * daily_start_time : 05:30
         * daily_end_time : 23:30
         */

        private double id;
        private String show_name;
        private String start_time;
        private String end_time;
        private String daily_start_time;
        private String daily_end_time;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getShow_name() {
            return show_name;
        }

        public void setShow_name(String show_name) {
            this.show_name = show_name;
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

        public String getDaily_start_time() {
            return daily_start_time;
        }

        public void setDaily_start_time(String daily_start_time) {
            this.daily_start_time = daily_start_time;
        }

        public String getDaily_end_time() {
            return daily_end_time;
        }

        public void setDaily_end_time(String daily_end_time) {
            this.daily_end_time = daily_end_time;
        }
    }
}
