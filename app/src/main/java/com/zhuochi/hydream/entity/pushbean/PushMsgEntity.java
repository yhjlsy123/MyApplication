package com.zhuochi.hydream.entity.pushbean;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/24
 */

public class PushMsgEntity {

    private String type;//busRefresh刷新总线，notice公告跳转H5,reserveSuccess预约成功 声音
    private Object content;
    private String create_time;
    private String sound;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
