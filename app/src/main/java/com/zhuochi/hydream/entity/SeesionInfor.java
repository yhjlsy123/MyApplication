package com.zhuochi.hydream.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SeesionInfor implements Parcelable {


    /**
     * sessionName : PHPSESSID
     * sessionId : 94mo90mi7frso50f9asqs1om2i
     * sessionData : {"id":"16080","wallet_id":"26326","mobile":"15628834660","user_login":"15628834660","user_nickname":"同学834660","user_type":"2","user_oauth":"2","user_status":"1","user_pass":"###8fb0a47d9220e66acd8964c7d3e96a55","grade_current":"1","grade_length":"4","sex":"1","last_login_time":"1542766201","credit_limit":"0.00","create_time":"1535970050","idcard":"0","idtype":"1","is_auth_id":"0","user_email":"","user_url":"","real_name":"","avatar":"http://p8uyenppn.bkt.clouddn.com/20181121084634758.jpg","signature":"","last_login_ip":"222.173.29.94","user_activation_key":"","device_pwd":"u2MnJk8fY2FfSD4PUXNYQg==","default_org_id":"2","default_org_area_id":"23","default_building_id":"99","default_device_area_id":"220","store_id":"0","pay_pwd":"","direct_pay_amount":"100.00"}
     */

    private String sessionName;
    private String sessionId;
    private SessionDataBean sessionData;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionDataBean getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionDataBean sessionData) {
        this.sessionData = sessionData;
    }

    public static class SessionDataBean implements Parcelable {
        /**
         * id : 16080
         * wallet_id : 26326
         * mobile : 15628834660
         * user_login : 15628834660
         * user_nickname : 同学834660
         * user_type : 2
         * user_oauth : 2
         * user_status : 1
         * user_pass : ###8fb0a47d9220e66acd8964c7d3e96a55
         * grade_current : 1
         * grade_length : 4
         * sex : 1
         * last_login_time : 1542766201
         * credit_limit : 0.00
         * create_time : 1535970050
         * idcard : 0
         * idtype : 1
         * is_auth_id : 0
         * user_email :
         * user_url :
         * real_name :
         * avatar : http://p8uyenppn.bkt.clouddn.com/20181121084634758.jpg
         * signature :
         * last_login_ip : 222.173.29.94
         * user_activation_key :
         * device_pwd : u2MnJk8fY2FfSD4PUXNYQg==
         * default_org_id : 2
         * default_org_area_id : 23
         * default_building_id : 99
         * default_device_area_id : 220
         * store_id : 0
         * pay_pwd :
         * direct_pay_amount : 100.00
         */

        private String id;
        private String wallet_id;
        private String mobile;
        private String user_login;
        private String user_nickname;
        private String user_type;
        private String user_oauth;
        private String user_status;
        private String user_pass;
        private String grade_current;
        private String grade_length;
        private String sex;
        private String last_login_time;
        private String credit_limit;
        private String create_time;
        private String idcard;
        private String idtype;
        private String is_auth_id;
        private String user_email;
        private String user_url;
        private String real_name;
        private String avatar;
        private String signature;
        private String last_login_ip;
        private String user_activation_key;
        private String device_pwd;
        private String default_org_id;
        private String default_org_area_id;
        private String default_building_id;
        private String default_device_area_id;
        private String store_id;
        private String pay_pwd;
        private String direct_pay_amount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWallet_id() {
            return wallet_id;
        }

        public void setWallet_id(String wallet_id) {
            this.wallet_id = wallet_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_oauth() {
            return user_oauth;
        }

        public void setUser_oauth(String user_oauth) {
            this.user_oauth = user_oauth;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_pass() {
            return user_pass;
        }

        public void setUser_pass(String user_pass) {
            this.user_pass = user_pass;
        }

        public String getGrade_current() {
            return grade_current;
        }

        public void setGrade_current(String grade_current) {
            this.grade_current = grade_current;
        }

        public String getGrade_length() {
            return grade_length;
        }

        public void setGrade_length(String grade_length) {
            this.grade_length = grade_length;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getCredit_limit() {
            return credit_limit;
        }

        public void setCredit_limit(String credit_limit) {
            this.credit_limit = credit_limit;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }

        public String getIs_auth_id() {
            return is_auth_id;
        }

        public void setIs_auth_id(String is_auth_id) {
            this.is_auth_id = is_auth_id;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_url() {
            return user_url;
        }

        public void setUser_url(String user_url) {
            this.user_url = user_url;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getUser_activation_key() {
            return user_activation_key;
        }

        public void setUser_activation_key(String user_activation_key) {
            this.user_activation_key = user_activation_key;
        }

        public String getDevice_pwd() {
            return device_pwd;
        }

        public void setDevice_pwd(String device_pwd) {
            this.device_pwd = device_pwd;
        }

        public String getDefault_org_id() {
            return default_org_id;
        }

        public void setDefault_org_id(String default_org_id) {
            this.default_org_id = default_org_id;
        }

        public String getDefault_org_area_id() {
            return default_org_area_id;
        }

        public void setDefault_org_area_id(String default_org_area_id) {
            this.default_org_area_id = default_org_area_id;
        }

        public String getDefault_building_id() {
            return default_building_id;
        }

        public void setDefault_building_id(String default_building_id) {
            this.default_building_id = default_building_id;
        }

        public String getDefault_device_area_id() {
            return default_device_area_id;
        }

        public void setDefault_device_area_id(String default_device_area_id) {
            this.default_device_area_id = default_device_area_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getPay_pwd() {
            return pay_pwd;
        }

        public void setPay_pwd(String pay_pwd) {
            this.pay_pwd = pay_pwd;
        }

        public String getDirect_pay_amount() {
            return direct_pay_amount;
        }

        public void setDirect_pay_amount(String direct_pay_amount) {
            this.direct_pay_amount = direct_pay_amount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.wallet_id);
            dest.writeString(this.mobile);
            dest.writeString(this.user_login);
            dest.writeString(this.user_nickname);
            dest.writeString(this.user_type);
            dest.writeString(this.user_oauth);
            dest.writeString(this.user_status);
            dest.writeString(this.user_pass);
            dest.writeString(this.grade_current);
            dest.writeString(this.grade_length);
            dest.writeString(this.sex);
            dest.writeString(this.last_login_time);
            dest.writeString(this.credit_limit);
            dest.writeString(this.create_time);
            dest.writeString(this.idcard);
            dest.writeString(this.idtype);
            dest.writeString(this.is_auth_id);
            dest.writeString(this.user_email);
            dest.writeString(this.user_url);
            dest.writeString(this.real_name);
            dest.writeString(this.avatar);
            dest.writeString(this.signature);
            dest.writeString(this.last_login_ip);
            dest.writeString(this.user_activation_key);
            dest.writeString(this.device_pwd);
            dest.writeString(this.default_org_id);
            dest.writeString(this.default_org_area_id);
            dest.writeString(this.default_building_id);
            dest.writeString(this.default_device_area_id);
            dest.writeString(this.store_id);
            dest.writeString(this.pay_pwd);
            dest.writeString(this.direct_pay_amount);
        }

        public SessionDataBean() {
        }

        protected SessionDataBean(Parcel in) {
            this.id = in.readString();
            this.wallet_id = in.readString();
            this.mobile = in.readString();
            this.user_login = in.readString();
            this.user_nickname = in.readString();
            this.user_type = in.readString();
            this.user_oauth = in.readString();
            this.user_status = in.readString();
            this.user_pass = in.readString();
            this.grade_current = in.readString();
            this.grade_length = in.readString();
            this.sex = in.readString();
            this.last_login_time = in.readString();
            this.credit_limit = in.readString();
            this.create_time = in.readString();
            this.idcard = in.readString();
            this.idtype = in.readString();
            this.is_auth_id = in.readString();
            this.user_email = in.readString();
            this.user_url = in.readString();
            this.real_name = in.readString();
            this.avatar = in.readString();
            this.signature = in.readString();
            this.last_login_ip = in.readString();
            this.user_activation_key = in.readString();
            this.device_pwd = in.readString();
            this.default_org_id = in.readString();
            this.default_org_area_id = in.readString();
            this.default_building_id = in.readString();
            this.default_device_area_id = in.readString();
            this.store_id = in.readString();
            this.pay_pwd = in.readString();
            this.direct_pay_amount = in.readString();
        }

        public static final Creator<SessionDataBean> CREATOR = new Creator<SessionDataBean>() {
            @Override
            public SessionDataBean createFromParcel(Parcel source) {
                return new SessionDataBean(source);
            }

            @Override
            public SessionDataBean[] newArray(int size) {
                return new SessionDataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sessionName);
        dest.writeString(this.sessionId);
        dest.writeParcelable(this.sessionData, flags);
    }

    public SeesionInfor() {
    }

    protected SeesionInfor(Parcel in) {
        this.sessionName = in.readString();
        this.sessionId = in.readString();
        this.sessionData = in.readParcelable(SessionDataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<SeesionInfor> CREATOR = new Parcelable.Creator<SeesionInfor>() {
        @Override
        public SeesionInfor createFromParcel(Parcel source) {
            return new SeesionInfor(source);
        }

        @Override
        public SeesionInfor[] newArray(int size) {
            return new SeesionInfor[size];
        }
    };
}
