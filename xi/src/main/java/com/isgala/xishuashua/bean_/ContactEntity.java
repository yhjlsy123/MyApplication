package com.isgala.xishuashua.bean_;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ContactEntity extends Base{
    public ContactBean data;

    public static class ContactBean  {

        /**
         * m_name : 李保路
         * m_tel : 13356659700
         * school_name : 山东交通学院
         */

        private String m_name;
        private String m_tel;
        private String school_name;

        public String getM_name() {
            return m_name;
        }

        public void setM_name(String m_name) {
            this.m_name = m_name;
        }

        public String getM_tel() {
            return m_tel;
        }

        public void setM_tel(String m_tel) {
            this.m_tel = m_tel;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }
    }
}
