package com.zhuochi.hydream.entity;

/**
 * @author Cuixc
 * @date on  2018/6/7
 */

public class MessageDataEntity {
    /**
     "title" -> "大庆乙烯9月生产情况通报"
     "create_time" -> "1.528109501E9"
     "content" -> "<p>大庆乙烯9月生产情况通报大庆乙烯9月生产情况通报大庆乙烯9月生产情况通报大庆乙烯9月生产情况通报</p>"
     "sender_name" -> "lbl"
     */
    private String title;
    private long create_time;
    private String content;
    private String sender_name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }
}
