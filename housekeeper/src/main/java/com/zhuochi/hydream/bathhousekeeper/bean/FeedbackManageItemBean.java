package com.zhuochi.hydream.bathhousekeeper.bean;

public class FeedbackManageItemBean {

    /**
     * suggestion_id : 128
     * org_name : 卓驰大学济南校区
     * user_mobile : 18364172723
     * suggestion_type : 设备问题
     * create_time : 2018-08-02 20:45:56
     * suggestion_content : 11111
     * has_repaly:yes or no 是否回复
     */

    private int suggestion_id;
    private String org_name;
    private String user_mobile;
    private String suggestion_type;
    private String create_time;
    private String suggestion_content;
    private String has_reply;
    private String order_sn;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getHas_reply() {
        return has_reply;
    }

    public void setHas_reply(String has_reply) {
        this.has_reply = has_reply;
    }

    public int getSuggestion_id() {
        return suggestion_id;
    }

    public void setSuggestion_id(int suggestion_id) {
        this.suggestion_id = suggestion_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getSuggestion_type() {
        return suggestion_type;
    }

    public void setSuggestion_type(String suggestion_type) {
        this.suggestion_type = suggestion_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSuggestion_content() {
        return suggestion_content;
    }

    public void setSuggestion_content(String suggestion_content) {
        this.suggestion_content = suggestion_content;
    }
}
