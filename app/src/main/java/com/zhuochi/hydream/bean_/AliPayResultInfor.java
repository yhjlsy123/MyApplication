package com.zhuochi.hydream.bean_;

import android.os.Parcel;
import android.os.Parcelable;

public class AliPayResultInfor implements Parcelable {


    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2018082461124483","auth_app_id":"2018082461124483","charset":"UTF-8","timestamp":"2018-10-24 15:28:24","out_trade_no":"P1001355519010","total_amount":"0.01","trade_no":"2018102422001429351005682890","seller_id":"2088231458536946"}
     * sign : Hkw0BlWPJwmQ8plxo+P/p+zXDnUuAJEEwZr+9TGZo2FMmRIIylxk7bRFi62ajtnNkr1UZ9dUS1hK/8ZmTB/aZZwPB+4neQQCzqgtWU1C1iyAyCmho4TNz+j3+/GNX8UDrOBwra9QtKk4g21Zc4BqMlx8GvY2vOHWZtC20Vys66CXTCC1GLfFK7kez38envk4J0bilNepv9aejzTQA/u/2r+1oKUX3igFvOa1AaORPcPipimlhREZ5z5SYHHQ/RkuLqdE8DZj1JD6Ycsh+Dzy/xrgDlA8bv0Grl+c39avLNR2plX3ZtdRpTvKIZX1UqjifzfxNmV7aDxhtGWIaAJ3IA==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean implements Parcelable {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2018082461124483
         * auth_app_id : 2018082461124483
         * charset : UTF-8
         * timestamp : 2018-10-24 15:28:24
         * out_trade_no : P1001355519010
         * total_amount : 0.01
         * trade_no : 2018102422001429351005682890
         * seller_id : 2088231458536946
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String out_trade_no;
        private String total_amount;
        private String trade_no;
        private String seller_id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.code);
            dest.writeString(this.msg);
            dest.writeString(this.app_id);
            dest.writeString(this.auth_app_id);
            dest.writeString(this.charset);
            dest.writeString(this.timestamp);
            dest.writeString(this.out_trade_no);
            dest.writeString(this.total_amount);
            dest.writeString(this.trade_no);
            dest.writeString(this.seller_id);
        }

        public AlipayTradeAppPayResponseBean() {
        }

        protected AlipayTradeAppPayResponseBean(Parcel in) {
            this.code = in.readString();
            this.msg = in.readString();
            this.app_id = in.readString();
            this.auth_app_id = in.readString();
            this.charset = in.readString();
            this.timestamp = in.readString();
            this.out_trade_no = in.readString();
            this.total_amount = in.readString();
            this.trade_no = in.readString();
            this.seller_id = in.readString();
        }

        public static final Creator<AlipayTradeAppPayResponseBean> CREATOR = new Creator<AlipayTradeAppPayResponseBean>() {
            @Override
            public AlipayTradeAppPayResponseBean createFromParcel(Parcel source) {
                return new AlipayTradeAppPayResponseBean(source);
            }

            @Override
            public AlipayTradeAppPayResponseBean[] newArray(int size) {
                return new AlipayTradeAppPayResponseBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.alipay_trade_app_pay_response, flags);
        dest.writeString(this.sign);
        dest.writeString(this.sign_type);
    }

    public AliPayResultInfor() {
    }

    protected AliPayResultInfor(Parcel in) {
        this.alipay_trade_app_pay_response = in.readParcelable(AlipayTradeAppPayResponseBean.class.getClassLoader());
        this.sign = in.readString();
        this.sign_type = in.readString();
    }

    public static final Parcelable.Creator<AliPayResultInfor> CREATOR = new Parcelable.Creator<AliPayResultInfor>() {
        @Override
        public AliPayResultInfor createFromParcel(Parcel source) {
            return new AliPayResultInfor(source);
        }

        @Override
        public AliPayResultInfor[] newArray(int size) {
            return new AliPayResultInfor[size];
        }
    };
}
