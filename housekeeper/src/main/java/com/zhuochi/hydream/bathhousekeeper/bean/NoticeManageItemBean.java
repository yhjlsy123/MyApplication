package com.zhuochi.hydream.bathhousekeeper.bean;

public class NoticeManageItemBean {


    /**
     * msg_id : 287
     * msg_content_id : 34
     * title : 测试
     * describe : 推送文章
     * create_time : 1.528108702E9
     * href :
     * owner_type : all
     * owner_parent_id : 0
     * owner_id : 0
     * org_name : 全体用户
     * org_area_name : 全体用户
     * content : 推送文章
     */

    private int msg_id;
    private int msg_content_id;
    private String title;
    private String describe;
    private double create_time;
    private String href;
    private String owner_type;
    private int owner_parent_id;
    private int owner_id;
    private String org_name;
    private String org_area_name;
    private String content;

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getMsg_content_id() {
        return msg_content_id;
    }

    public void setMsg_content_id(int msg_content_id) {
        this.msg_content_id = msg_content_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getCreate_time() {
        return create_time;
    }

    public void setCreate_time(double create_time) {
        this.create_time = create_time;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public int getOwner_parent_id() {
        return owner_parent_id;
    }

    public void setOwner_parent_id(int owner_parent_id) {
        this.owner_parent_id = owner_parent_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
