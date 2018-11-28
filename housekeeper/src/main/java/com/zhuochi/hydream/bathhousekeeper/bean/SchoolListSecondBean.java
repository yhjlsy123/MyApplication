package com.zhuochi.hydream.bathhousekeeper.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhuochi.hydream.bathhousekeeper.adapter.SchoolListSecondAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */

public class SchoolListSecondBean extends AbstractExpandableItem<SchoolListSecondBean.OrgAreaBean> implements MultiItemEntity {

    /**
     * org_id : 2
     * org_code : 2
     * org_name : 中瑞
     * org_short_name : 中瑞
     * org_type : school
     * is_investor : 0
     * creator_id : 0
     * create_time : 1.477560082E9
     * province_id : 370000
     * city_id : 370100
     * area_id : 370102
     * org_address : 山东济南市高新区龙翔大厦A座
     * longitude : 116.2300000
     * latitude : 39.5500000
     * org_phone :
     * org_email :
     * org_state : 1
     * org_hidden : 0
     * org_thumb : 1
     * org_logo : 1
     * org_video : 1
     * org_license : 1
     * org_license_group : 1
     * enable_map : 0
     * store_id : 0
     * recommend : 0
     * auth : 0
     * org_area : [{"org_area_id":1,"org_id":0,"org_area_name":"测试","org_area_address":"","is_hq":0,"status":1,"create_time":0},{"org_area_id":15,"org_id":10,"org_area_name":"济南校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.526026553E9},{"org_area_id":16,"org_id":10,"org_area_name":"青岛校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.526026483E9},{"org_area_id":18,"org_id":11,"org_area_name":"章丘校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.526279278E9},{"org_area_id":19,"org_id":11,"org_area_name":"长青校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.526279265E9},{"org_area_id":22,"org_id":1,"org_area_name":"济南校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.5273248E9},{"org_area_id":23,"org_id":2,"org_area_name":"中心校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.527324853E9},{"org_area_id":24,"org_id":1,"org_area_name":"淄博校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.527325022E9},{"org_area_id":25,"org_id":2,"org_area_name":"西校区","org_area_address":"","is_hq":0,"status":1,"create_time":1.527325112E9},{"org_area_id":26,"org_id":22,"org_area_name":"测试组织机构的园区1","org_area_address":"","is_hq":0,"status":1,"create_time":1.528180005E9},{"org_area_id":27,"org_id":41,"org_area_name":"园区1","org_area_address":"","is_hq":0,"status":1,"create_time":1.532686359E9}]
     */

    private int org_id;
    private String org_code;
    private String org_name;
    private String org_short_name;
    private String org_type;
    private int is_investor;
    private int creator_id;
    private double create_time;
    private int province_id;
    private int city_id;
    private int area_id;
    private String org_address;
    private String longitude;
    private String latitude;
    private String org_phone;
    private String org_email;
    private int org_state;
    private int org_hidden;
    private String org_thumb;
    private String org_logo;
    private String org_video;
    private String org_license;
    private String org_license_group;
    private int enable_map;
    private int store_id;
    private int recommend;
    private int auth;
    /**
     * org_area_id : 1
     * org_id : 0
     * org_area_name : 测试
     * org_area_address :
     * is_hq : 0
     * status : 1
     * create_time : 0
     */

    private List<OrgAreaBean> org_area;

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_short_name() {
        return org_short_name;
    }

    public void setOrg_short_name(String org_short_name) {
        this.org_short_name = org_short_name;
    }

    public String getOrg_type() {
        return org_type;
    }

    public void setOrg_type(String org_type) {
        this.org_type = org_type;
    }

    public int getIs_investor() {
        return is_investor;
    }

    public void setIs_investor(int is_investor) {
        this.is_investor = is_investor;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public double getCreate_time() {
        return create_time;
    }

    public void setCreate_time(double create_time) {
        this.create_time = create_time;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getOrg_address() {
        return org_address;
    }

    public void setOrg_address(String org_address) {
        this.org_address = org_address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOrg_phone() {
        return org_phone;
    }

    public void setOrg_phone(String org_phone) {
        this.org_phone = org_phone;
    }

    public String getOrg_email() {
        return org_email;
    }

    public void setOrg_email(String org_email) {
        this.org_email = org_email;
    }

    public int getOrg_state() {
        return org_state;
    }

    public void setOrg_state(int org_state) {
        this.org_state = org_state;
    }

    public int getOrg_hidden() {
        return org_hidden;
    }

    public void setOrg_hidden(int org_hidden) {
        this.org_hidden = org_hidden;
    }

    public String getOrg_thumb() {
        return org_thumb;
    }

    public void setOrg_thumb(String org_thumb) {
        this.org_thumb = org_thumb;
    }

    public String getOrg_logo() {
        return org_logo;
    }

    public void setOrg_logo(String org_logo) {
        this.org_logo = org_logo;
    }

    public String getOrg_video() {
        return org_video;
    }

    public void setOrg_video(String org_video) {
        this.org_video = org_video;
    }

    public String getOrg_license() {
        return org_license;
    }

    public void setOrg_license(String org_license) {
        this.org_license = org_license;
    }

    public String getOrg_license_group() {
        return org_license_group;
    }

    public void setOrg_license_group(String org_license_group) {
        this.org_license_group = org_license_group;
    }

    public int getEnable_map() {
        return enable_map;
    }

    public void setEnable_map(int enable_map) {
        this.enable_map = enable_map;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public List<OrgAreaBean> getOrg_area() {
        return org_area;
    }

    public void setOrg_area(List<OrgAreaBean> org_area) {
        this.org_area = org_area;
    }

    @Override
    public int getLevel() {
       ; return 0;
    }

    @Override
    public int getItemType() {
        return SchoolListSecondAdapter.TYPE_LEVEL_0;
    }

    public static class OrgAreaBean implements MultiItemEntity {
        private int org_area_id;
        private int org_id;
        private String org_area_name;
        private String org_area_address;
        private int is_hq;
        private int status;
        private int create_time;

        public int getOrg_area_id() {
            return org_area_id;
        }

        public void setOrg_area_id(int org_area_id) {
            this.org_area_id = org_area_id;
        }

        public int getOrg_id() {
            return org_id;
        }

        public void setOrg_id(int org_id) {
            this.org_id = org_id;
        }

        public String getOrg_area_name() {
            return org_area_name;
        }

        public void setOrg_area_name(String org_area_name) {
            this.org_area_name = org_area_name;
        }

        public String getOrg_area_address() {
            return org_area_address;
        }

        public void setOrg_area_address(String org_area_address) {
            this.org_area_address = org_area_address;
        }

        public int getIs_hq() {
            return is_hq;
        }

        public void setIs_hq(int is_hq) {
            this.is_hq = is_hq;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        @Override
        public int getItemType() {
            return SchoolListSecondAdapter.TYPE_LEVEL_1;
        }
    }
}
