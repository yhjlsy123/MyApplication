package com.zhuochi.hydream.bathhousekeeper.bean;

public class RefundsManageItemBean {


    /**
     * id : 7
     * applicant_id : 60
     * org_id : 10
     * org_area_id : 15
     * apply_refund_money : 99.96
     * handle_status : 1
     * created : 1.533036058E9
     * org_name : 卓驰大学
     * org_area_name : 济南校区
     * user_nickname : 小猪佩奇
     * mobile : 18364172723
     * handle_status_text : 未处理
     */

    private int id;
    private int applicant_id;
    private int org_id;
    private int org_area_id;
    private String apply_refund_money;
    private int handle_status;
    private Long created;
    private String org_name;
    private String org_area_name;
    private String user_nickname;
    private String mobile;
    private String handle_status_text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicant_id() {
        return applicant_id;
    }

    public void setApplicant_id(int applicant_id) {
        this.applicant_id = applicant_id;
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

    public String getApply_refund_money() {
        return apply_refund_money;
    }

    public void setApply_refund_money(String apply_refund_money) {
        this.apply_refund_money = apply_refund_money;
    }

    public int getHandle_status() {
        return handle_status;
    }

    public void setHandle_status(int handle_status) {
        this.handle_status = handle_status;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
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

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHandle_status_text() {
        return handle_status_text;
    }

    public void setHandle_status_text(String handle_status_text) {
        this.handle_status_text = handle_status_text;
    }
}
