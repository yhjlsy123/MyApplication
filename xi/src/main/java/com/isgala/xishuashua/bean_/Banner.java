package com.isgala.xishuashua.bean_;

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

    private String status;
    private String msg;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : http://test.isgala.com/w3g/WebView/market?id=20160816
         * image : http://7xrlwm.com2.z0.glb.qiniucdn.com/57b2b92fb21a4348223.png
         */

        private String url;
        private String image;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
