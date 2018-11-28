package com.isgala.xishuashua.bean_;

/**
 * Created by Administrator on 2018/3/16.
 */

public class UpgradeEntity extends Base{
    public UpgradeEntityData data;
    public static class UpgradeEntityData{

        /**
         * status : 1
         * now : 1.1.5-debug
         * url : http://www.isgala.com/version/Unicorn1.0.7.apk
         * update : 更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦，更新啦
         */

        private int status;
        private String now;
        private String url;
        private String update;
        private String is_force;

        public String getIs_force() {
            return is_force;
        }

        public void setIs_force(String is_force) {
            this.is_force = is_force;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNow() {
            return now;
        }

        public void setNow(String now) {
            this.now = now;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }
    }
}
