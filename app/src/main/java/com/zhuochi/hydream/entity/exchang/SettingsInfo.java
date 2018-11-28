package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/7/16
 */

public class SettingsInfo {

    /**
     * companyName : 山东弘毅节能服务有限公司
     * servicePhone : 0531-55761060
     * refreshInterval : 5
     * imageBaseUrl : http://zaotang.94feel.com/
     */

    private String companyName;
    private String servicePhone;
    private int refreshInterval;
    private String imageBaseUrl;

    public static SettingsInfo objectFromData(String str) {

        return new Gson().fromJson(str, SettingsInfo.class);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }
}
