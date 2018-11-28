package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/5/9
 */

public class SonBaseEntity {

    /**
     * header : {"auth":"00002"}
     * data : {"code":102,"msg":"很抱歉，手机号或密码不正确!","data":[]}
     */

    private HeaderBean header;
    private DataBean data;

    public static SonBaseEntity objectFromData(String str) {

        return new Gson().fromJson(str, SonBaseEntity.class);
    }

    public HeaderBean getHeader() {
        return header;
    }



    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class HeaderBean {
        /**
         * auth : 00002
         */

        private String auth;
        private String error;
        private String errorMsg;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public static HeaderBean objectFromData(String str) {

            return new Gson().fromJson(str, HeaderBean.class);
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }
    }

    public static class DataBean {
        /**
         * code : 102
         * msg : 很抱歉，手机号或密码不正确!
         * data : []
         */

        private int code;
        private String msg;
        private Object data;
        private String time;

        public void setData(Object data) {
            this.data = data;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
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

        public Object getData() {
            return data;
        }

        public void setData(List<?> data) {
            this.data = data;
        }
    }
}
