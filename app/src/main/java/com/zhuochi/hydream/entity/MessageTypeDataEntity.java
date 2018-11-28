package com.zhuochi.hydream.entity;

/**
 * @author Cuixc
 * @date on  2018/6/7
 */

public class MessageTypeDataEntity {
    /**
     * "msg_id" -> "291.0"
     "msg_content_id" -> "36.0"
     "title" -> "大庆乙烯9月生产情况通报"
     "describe" -> "大庆乙烯9月生产情况通报"
     "create_time" -> "1.528109501E9"
     */
    private String msg_id;
    private String msg_content_id;
    private String title;
    private String describe;
    private long create_time;
    private String href;
    private String url;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public void setMsg_content_id(String msg_content_id) {
        this.msg_content_id = msg_content_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public String getMsg_content_id() {
        return msg_content_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public long getCreate_time() {
        return create_time;
    }
}
