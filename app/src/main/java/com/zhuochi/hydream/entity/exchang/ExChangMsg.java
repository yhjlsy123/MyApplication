package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/28
 */

public class ExChangMsg {

    /**
     * id : 0
     * type : userState
     * content : {"userState":"normal","queueInfo":"","deviceInfo":"","remainTime":0,"allowRemoteControl":0}
     */

    private int id;
    private String type;
    private Object content;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
