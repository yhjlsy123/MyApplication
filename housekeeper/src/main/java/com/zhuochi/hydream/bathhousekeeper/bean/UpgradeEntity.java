package com.zhuochi.hydream.bathhousekeeper.bean;

/**
 * Created by Administrator on 2018/3/16.
 */

public class UpgradeEntity {

        /**
         * status : 1
         * new_version : \u6d4b\u8bd51.8
         * url : \/static\/app\/device\/f0\/1600b580b620126e8874e071bb9546.apk
         * is_force : 0
         * intro : 1.8
         */

        private int status;
        private String new_version;
        private String url;
        private int is_force;//0，正常  1强制
        private String intro;


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNew_version() {
            return new_version;
        }

        public void setNew_version(String new_version) {
            this.new_version = new_version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIs_force() {
            return is_force;
        }

        public void setIs_force(int is_force) {
            this.is_force = is_force;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
    }
