package com.zhuochi.hydream.bean_;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/** 吹风机
 * Created by 唯暮 on 2018/3/25.
 */

public class BathendOrder extends Base{
    public BathendOrderData data;

    public static class BathendOrderData implements Serializable {

        /**
         * default_choice : {"s_id":"25","campus":"26","b_id":"1","position":"国际友人","uuid":"uuuuuuuuuuuuuuu"}
         * deviceLlist : [{"id":"1","num":"01","status":"1"},{"id":"2","num":"02","status":"1"}]
         */

        private DefaultChoiceBean default_choice;
        private List<DeviceLlistBean> deviceLlist;

        public static BathendOrderData objectFromData(String str) {

            return new Gson().fromJson(str, BathendOrderData.class);
        }

        public DefaultChoiceBean getDefault_choice() {
            return default_choice;
        }

        public void setDefault_choice(DefaultChoiceBean default_choice) {
            this.default_choice = default_choice;
        }

        public List<DeviceLlistBean> getDeviceLlist() {
            return deviceLlist;
        }

        public void setDeviceLlist(List<DeviceLlistBean> deviceLlist) {
            this.deviceLlist = deviceLlist;
        }

        public static class DefaultChoiceBean {
            /**
             * s_id : 25
             * campus : 26
             * b_id : 1
             * position : 国际友人
             * uuid : uuuuuuuuuuuuuuu
             * tips:提示语
             */

            private String s_id;
            private String campus;
            private String b_id;
            private String position;
            private String uuid;
            private String tips;

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public static DefaultChoiceBean objectFromData(String str) {

                return new Gson().fromJson(str, DefaultChoiceBean.class);
            }

            public String getS_id() {
                return s_id;
            }

            public void setS_id(String s_id) {
                this.s_id = s_id;
            }

            public String getCampus() {
                return campus;
            }

            public void setCampus(String campus) {
                this.campus = campus;
            }

            public String getB_id() {
                return b_id;
            }

            public void setB_id(String b_id) {
                this.b_id = b_id;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }
        }

        public static class DeviceLlistBean {
            /**
             * id : 1
             * num : 01
             * status : 1
             */

            private String id;
            private String num;
            private String status;

            public static DeviceLlistBean objectFromData(String str) {

                return new Gson().fromJson(str, DeviceLlistBean.class);
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
