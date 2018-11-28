package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/5
 */

public class FeedbackMessageEntity {

    /**
     * id : 1
     * org_id : 19
     * creator_id : 14
     * type_id : 1
     * create_time : 1.528159804E9
     * content : 哈哈哈哈我的微博我就不知道了，我
     * phone : 13356659700
     * source_type : user
     * relation_su_id : 1
     * is_main : 1
     * is_read : 0
     * is_reply : 0
     * imgs : http://p8uyenppn.bkt.clouddn.com/34AF00DA-16C9-4D15-9EAC-EC2E33554A6E,http://p8uyenppn.bkt.clouddn.com/5E286D72-858E-469F-A57E-95578BCC5C27
     */

    private int id;
    private int org_id;
    private int creator_id;
    private int type_id;
    private long create_time;
    private String content;
    private String phone;
    private String source_type;
    private int relation_su_id;
    private int is_main;
    private int is_read;
    private int is_reply;
    private String imgs;

    public static FeedbackMessageEntity objectFromData(String str) {

        return new Gson().fromJson(str, FeedbackMessageEntity.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public int getRelation_su_id() {
        return relation_su_id;
    }

    public void setRelation_su_id(int relation_su_id) {
        this.relation_su_id = relation_su_id;
    }

    public int getIs_main() {
        return is_main;
    }

    public void setIs_main(int is_main) {
        this.is_main = is_main;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int getIs_reply() {
        return is_reply;
    }

    public void setIs_reply(int is_reply) {
        this.is_reply = is_reply;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
}
