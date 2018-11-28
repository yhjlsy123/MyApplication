package com.zhuochi.hydream.entity.pushbean;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/27
 */

public class InitSettingEntity {

    /**
     * deviceTimeoutStartTip
     * companyAddress : 龙翔商务大厦
     * companyName : 山东弘毅节能服务有限公司
     * copyRight : Copyright © 2017 hydream.cn, 山东弘毅节能服务有限公司 版权所有, 保留所有权利
     * postCode : 250000
     * servicePhone : 0531-55761060
     * aboutUsUrl : http://zaotang.94feel.com/apipage/setting/aboutus/userId/25/mobile/18854117407.html
     * commonProblemUrl : http://zaotang.94feel.com/apipage/setting/commonproblem/userId/25/mobile/18854117407.html
     * registrationAgreementUrl : http://zaotang.94feel.com/apipage/setting/registrationagreement/userId/25/mobile/18854117407.html
     */
    private int deviceTimeout;//服务开始时间判定
    private String companyAddress;
    private String companyName;
    private String copyRight;
    private String postCode;
    private String servicePhone;
    private String aboutUsUrl;//关于我们
    private String commonProblemUrl;//常见问题
    private String registrationAgreementUrl;//注册协议
    private String refundAgreementUrl;//退款协议
    private String deviceTimeoutStartTip;
    private String deviceTimeoutEndTip;


    public String getRefundAgreementUrl() {
        return refundAgreementUrl;
    }

    public int getDeviceTimeout() {
        return deviceTimeout;
    }

    public void setDeviceTimeout(int deviceTimeout) {
        this.deviceTimeout = deviceTimeout;
    }

    public String getDeviceTimeoutStartTip() {
        return deviceTimeoutStartTip;
    }

    public void setDeviceTimeoutStartTip(String deviceTimeoutStartTip) {
        this.deviceTimeoutStartTip = deviceTimeoutStartTip;
    }

    public String getDeviceTimeoutEndTip() {
        return deviceTimeoutEndTip;
    }

    public void setDeviceTimeoutEndTip(String deviceTimeoutEndTip) {
        this.deviceTimeoutEndTip = deviceTimeoutEndTip;
    }

    public void setRefundAgreementUrl(String refundAgreementUrl) {
        this.refundAgreementUrl = refundAgreementUrl;
    }

    public static InitSettingEntity objectFromData(String str) {

        return new Gson().fromJson(str, InitSettingEntity.class);
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        this.aboutUsUrl = aboutUsUrl;
    }

    public String getCommonProblemUrl() {
        return commonProblemUrl;
    }

    public void setCommonProblemUrl(String commonProblemUrl) {
        this.commonProblemUrl = commonProblemUrl;
    }

    public String getRegistrationAgreementUrl() {
        return registrationAgreementUrl;
    }

    public void setRegistrationAgreementUrl(String registrationAgreementUrl) {
        this.registrationAgreementUrl = registrationAgreementUrl;
    }
}
