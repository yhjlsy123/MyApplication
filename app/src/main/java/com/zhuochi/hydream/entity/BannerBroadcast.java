package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author Cuixc
 * @date on  2018/8/11
 */

public class BannerBroadcast {

    /**
     * hasAd : 1.0
     * picList : ["http://newdev.gaopintech.cn/static/images/ad/orgArea28_ad1.png","http://newdev.gaopintech.cn/static/images/ad/orgArea28_ad2.png","http://newdev.gaopintech.cn/static/images/ad/orgArea28_ad3.png"]
     */

    private int hasAd;
    private Object picList;

    public Object getPicList() {
        return picList;
    }

    public void setPicList(Object picList) {
        this.picList = picList;
    }

    public static BannerBroadcast objectFromData(String str) {

        return new Gson().fromJson(str, BannerBroadcast.class);
    }

    public int getHasAd() {
        return hasAd;
    }

    public void setHasAd(int hasAd) {
        this.hasAd = hasAd;
    }


}
