package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/28
 */

public class ExChangDeviceInfo {

    /**
     * end_time : 0
     * settlement_area_id : 20
     * uuid : 7a494403-b526-43e5-9b9f-277a0979d375_faucet_01
     * device_status : running
     * start_time : 1532950499
     * goods_id : 0
     * reserve_time : 1532950496
     * device_number : 01
     * deviceAreaName : 测试服务区域
     * v_device_type_key : dev_controller_1
     * user_device_password : 159357258
     * max_wait_time : 480
     * org_id : 10
     * device_type_key : faucet
     * last_operate_time : 0
     * building_number :
     * creator_id : 4
     * longitude : 117.1297480
     * controller_key : 7a494403-b526-43e5-9b9f-277a0979d375
     * org_area_id : 15
     * stop_reason :
     * device_key : 7a494403-b526-43e5-9b9f-277a0979d375_faucet_01
     * order_sn : 609010
     * need_reserve : 1
     * building_id : 90
     * is_controller : 0
     * device_model_key : faucet_v1
     * device_name : 水控机01
     * latitude : 36.6872410
     * device_area_id : 17
     * device_user_id :
     * user_id : 25
     * user_device_pwd : byj1fJdKJBgk87FO4XlrrQ\u003d\u003d
     * create_time : 1532686970
     */

    private int id;
    private String end_time;
    private String settlement_area_id;
    private String uuid;
    private String device_status;
    private String start_time;
    private String goods_id;
    private String reserve_time;
    private String device_number;
    private String deviceAreaName;
    private String v_device_type_key;
    private String user_device_password;
    private int max_wait_time;
    private String org_id;
    private String device_type_key;
    private String last_operate_time;
    private String building_number;
    private String creator_id;
    private String longitude;
    private String controller_key;
    private String org_area_id;
    private String stop_reason;
    private String device_key;
    private String order_sn;
    private String need_reserve;
    private String building_id;
    private String is_controller;
    private String device_model_key;
    private String device_name;
    private String latitude;
    private String device_area_id;
    private String device_user_id;
    private String user_id;
    private String user_device_pwd;
    private String create_time;

    public static ExChangDeviceInfo objectFromData(String str) {

        return new Gson().fromJson(str, ExChangDeviceInfo.class);
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getSettlement_area_id() {
        return settlement_area_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSettlement_area_id(String settlement_area_id) {
        this.settlement_area_id = settlement_area_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice_status() {
        return device_status;
    }

    public void setDevice_status(String device_status) {
        this.device_status = device_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getDeviceAreaName() {
        return deviceAreaName;
    }

    public void setDeviceAreaName(String deviceAreaName) {
        this.deviceAreaName = deviceAreaName;
    }

    public String getV_device_type_key() {
        return v_device_type_key;
    }

    public void setV_device_type_key(String v_device_type_key) {
        this.v_device_type_key = v_device_type_key;
    }

    public String getUser_device_password() {
        return user_device_password;
    }

    public void setUser_device_password(String user_device_password) {
        this.user_device_password = user_device_password;
    }

    public int getMax_wait_time() {
        return max_wait_time;
    }

    public void setMax_wait_time(int max_wait_time) {
        this.max_wait_time = max_wait_time;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getDevice_type_key() {
        return device_type_key;
    }

    public void setDevice_type_key(String device_type_key) {
        this.device_type_key = device_type_key;
    }

    public String getLast_operate_time() {
        return last_operate_time;
    }

    public void setLast_operate_time(String last_operate_time) {
        this.last_operate_time = last_operate_time;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getController_key() {
        return controller_key;
    }

    public void setController_key(String controller_key) {
        this.controller_key = controller_key;
    }

    public String getOrg_area_id() {
        return org_area_id;
    }

    public void setOrg_area_id(String org_area_id) {
        this.org_area_id = org_area_id;
    }

    public String getStop_reason() {
        return stop_reason;
    }

    public void setStop_reason(String stop_reason) {
        this.stop_reason = stop_reason;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getNeed_reserve() {
        return need_reserve;
    }

    public void setNeed_reserve(String need_reserve) {
        this.need_reserve = need_reserve;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getIs_controller() {
        return is_controller;
    }

    public void setIs_controller(String is_controller) {
        this.is_controller = is_controller;
    }

    public String getDevice_model_key() {
        return device_model_key;
    }

    public void setDevice_model_key(String device_model_key) {
        this.device_model_key = device_model_key;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDevice_area_id() {
        return device_area_id;
    }

    public void setDevice_area_id(String device_area_id) {
        this.device_area_id = device_area_id;
    }

    public String getDevice_user_id() {
        return device_user_id;
    }

    public void setDevice_user_id(String device_user_id) {
        this.device_user_id = device_user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_device_pwd() {
        return user_device_pwd;
    }

    public void setUser_device_pwd(String user_device_pwd) {
        this.user_device_pwd = user_device_pwd;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
