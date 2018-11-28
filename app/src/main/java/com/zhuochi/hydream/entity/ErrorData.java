package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/14
 */

public class ErrorData {

    /**
     * code : 132
     * msg : 您尚未设置设备密码
     * data : Exception
     */

    private int code;
    private String msg;
    private String data;

    public static ErrorData objectFromData(String str) {

        return new Gson().fromJson(str, ErrorData.class);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
