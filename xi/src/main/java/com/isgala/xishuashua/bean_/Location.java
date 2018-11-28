package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by and on 2016/11/7.
 */

public class Location extends Base {


    public Data data;

    public static class Data  implements Serializable {

        public String status;//状态码 0 正常 1 排队中 2 待服务/服务中
        public String id;// 浴位的ID/排队的id
        public String did;
        public String uuid;
        public City location;
        public ArrayList<City> list;
        public City default_city;
        public ArrayList<City> plan;
        public String callcenter;
        public Version version;
        public String kefu;
    }

//    /**
//     * 优惠券信息
//     */
//    public class Coupon {
//        public String coupon_id;
//        public String title;
//        public String image;
//        public String width;
//        public String height;
//        public String click;
//        public String url;
//    }

    public static class City implements Serializable{
        public int area_id;
        public String area_name;
        public int studio;
        public String scalar;
    }

    public static class Version implements Serializable{

        public String now;
        public String code;
        public String url;
        public String min_code;
        public List<String> update;
    }
}
