package com.zhuochi.hydream.bathhousekeeper.bean;

public class AreaDeviceItemBean {


    /**
     * device_key : 2781aefa-e7c9-401d-b2f8-8f17d8d4630f
     * bathroom : 1号楼1层
     * uuid : 2781aefa-e7c9-401d-b2f8-8f17d8d4630f
     * gender : 男女均可
     * status_text : 正常
     * status : ready
     */

    private String device_key;
    private String bathroom;
    private String uuid;
    private String gender;
    private String status_text;
    private String status;

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
