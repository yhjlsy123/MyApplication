package com.zhuochi.hydream.bean_;

import java.io.Serializable;

/**
 * 用户信息
 * Created by and on 2016/11/8.
 */

public class TokenEntity extends Base {
    public UserData data;

    public static class UserData implements Serializable {
        /**
         * "v_id":"20",
         * "nickname":"小阳测试",
         * "photo":"http://7xrlwm.com2.z0.glb.qiniucdn.com//FglPIplLTleHjPSLtMRkHbhj0H7L",
         * "sex":"0",
         * "age":"0",
         * "phone":"15810380093",
         * "student_number":""
         */
        public String v_id;
        public String auth;
        public String nickname;
        public String photo;
        public String sex;
        public String age;
        public String school_name;
        public String campus_name;
        public String bathroom_name;
        public String is_update;
        public String is_update_tip;
        public String phone;
        public String campus;
        public String student_number;
        public String s_id;//学校id
        public int if_allow_one_card_solution;
        public String is_checked;
        public String card_no;
    }
}
