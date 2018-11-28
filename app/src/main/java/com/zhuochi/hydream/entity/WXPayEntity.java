package com.zhuochi.hydream.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author Cuixc
 * @date on  2018/6/27
 */

public class WXPayEntity {

    /**
     * appid : wx051a129ad1554652
     * partnerid : 1508444581
     * prepayid : wx2714514033715951e482eca63223821431
     * package : Sign=WXPay
     * noncestr : ccycso1g2uwjmbk08wlnivn1dgxhbn41
     * timestamp : 1530082301
     * sign : 53C2BE01D71BF8ED4831F526702686DF
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private int timestamp;
    private String sign;

    public static WXPayEntity objectFromData(String str) {

        return new Gson().fromJson(str, WXPayEntity.class);
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
