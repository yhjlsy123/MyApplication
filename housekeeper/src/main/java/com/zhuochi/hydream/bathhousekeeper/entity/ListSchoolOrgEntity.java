package com.zhuochi.hydream.bathhousekeeper.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/9/10
 */

public class ListSchoolOrgEntity {

    /**
     * org_area_id : 23
     * org_id : 2
     * org_area_name : 中心校区
     * org_area_address :
     * is_hq : 0
     * status : 1
     * create_time : 1527324853
     * notice_start : 0
     * notice_end : 0
     * notice_tip :
     * bus_cache_times : 0
     * queue_driver : DeviceQueueModel
     */

    private int org_area_id;
    private int org_id;
    private String org_area_name;
    private String org_area_address;
    private int is_hq;
    private int status;
    private long create_time;
    private int notice_start;
    private int notice_end;
    private String notice_tip;
    private int bus_cache_times;
    private String queue_driver;

    public static ListSchoolOrgEntity objectFromData(String str) {

        return new Gson().fromJson(str, ListSchoolOrgEntity.class);
    }

    public int getOrg_area_id() {
        return org_area_id;
    }

    public void setOrg_area_id(int org_area_id) {
        this.org_area_id = org_area_id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getOrg_area_name() {
        return org_area_name;
    }

    public void setOrg_area_name(String org_area_name) {
        this.org_area_name = org_area_name;
    }

    public String getOrg_area_address() {
        return org_area_address;
    }

    public void setOrg_area_address(String org_area_address) {
        this.org_area_address = org_area_address;
    }

    public int getIs_hq() {
        return is_hq;
    }

    public void setIs_hq(int is_hq) {
        this.is_hq = is_hq;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getNotice_start() {
        return notice_start;
    }

    public void setNotice_start(int notice_start) {
        this.notice_start = notice_start;
    }

    public int getNotice_end() {
        return notice_end;
    }

    public void setNotice_end(int notice_end) {
        this.notice_end = notice_end;
    }

    public String getNotice_tip() {
        return notice_tip;
    }

    public void setNotice_tip(String notice_tip) {
        this.notice_tip = notice_tip;
    }

    public int getBus_cache_times() {
        return bus_cache_times;
    }

    public void setBus_cache_times(int bus_cache_times) {
        this.bus_cache_times = bus_cache_times;
    }

    public String getQueue_driver() {
        return queue_driver;
    }

    public void setQueue_driver(String queue_driver) {
        this.queue_driver = queue_driver;
    }
}
