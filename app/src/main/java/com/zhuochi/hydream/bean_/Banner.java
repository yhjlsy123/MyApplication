package com.zhuochi.hydream.bean_;

import java.util.List;

/**
 * 轮播图片
 * Created by lzf on 2017/3/28.
 */

public class Banner {

    /**
     * status : 1
     * msg :
     * data : [{"url":"http://test.isgala.com/w3g/WebView/market?id=20160816","image":"http://7xrlwm.com2.z0.glb.qiniucdn.com/57b2b92fb21a4348223.png"},{"url":"http://test.isgala.com/w3g/WebView/market?id=new","image":"http://7xrlwm.com2.z0.glb.qiniucdn.com/57b2bbcc2ece5801674.png"}]
     */

    private String hasAd;
    private List<String> data;

    public String getHasAd() {
        return hasAd;
    }

    public void setHasAd(String hasAd) {
        this.hasAd = hasAd;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
