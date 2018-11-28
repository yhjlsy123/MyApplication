package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/5/7
 */

public class BaseEntity {

    /**
     * head : {"msg_id":"1525675634_453897","uuid":"SERVER","length":103,"verify":"f4acc9d6201100c07a2593e90fe7d2a00398f2cc"}
     * data : YEWDNmoye95kwX3JQIXXCUmkKY51qIakLiU//Rugl163WIEVysZXZSqYEk7dy04t/crS6Faie0paCLDsQYPF6MhRmcuhxwjefp+P403AKFLqEveyHCGy/YnVyQHOUkULdiCR3ZGi3+U=
     */

    private HeadBean head;
    private String data;

    public static BaseEntity objectFromData(String str) {

        return new Gson().fromJson(str, BaseEntity.class);
    }

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class HeadBean {
        /**
         * msg_id : 1525675634_453897
         * uuid : SERVER
         * length : 103
         * verify : f4acc9d6201100c07a2593e90fe7d2a00398f2cc
         */

        private String msg_id;
        private String uuid;
        private int length;
        private String verify;

        public static HeadBean objectFromData(String str) {

            return new Gson().fromJson(str, HeadBean.class);
        }

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }
    }
}
