package com.isgala.xishuashua.bean_;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 唯暮 on 2018/3/29.
 */

public class Banners extends Base {
    public BannersData data;

    public static class BannersData implements Serializable {

        /**
         * status : 1
         * msg : 数据获取成功!
         * banner : [{"url":"http://www.hydream.cn/","image":"http://ogd58ywup.bkt.clouddn.com/5abcbf9f867bb694713.png"},{"url":"http://www.hydream.cn/","image":"http://ogd58ywup.bkt.clouddn.com/5abcc2d1055c783058.png"}]
         * notice : [{"content":"超级澡堂正式上线了，欢迎同学们使用~"},{"content":"通知：由于学校自来水停水，现热水温度48℃左右，同学们预约洗澡时请小心烫伤，天气寒冷，注意保暖。"}]
         */

        private int status;
        private String msg;
        private List<BannerBean> banner;
        private List<NoticeBean> notice;
        public static BannersData objectFromData(String str) {

            return new Gson().fromJson(str, BannersData.class);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<NoticeBean> getNotice() {
            return notice;
        }

        public void setNotice(List<NoticeBean> notice) {
            this.notice = notice;
        }

        public static class BannerBean {
            /**
             * url : http://www.hydream.cn/
             * image : http://ogd58ywup.bkt.clouddn.com/5abcbf9f867bb694713.png
             */

            private String url;
            private String image;

            public static BannerBean objectFromData(String str) {

                return new Gson().fromJson(str, BannerBean.class);
            }

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

        public static class NoticeBean {
            /**
             * content : 超级澡堂正式上线了，欢迎同学们使用~
             */

            private String content;

            public static NoticeBean objectFromData(String str) {

                return new Gson().fromJson(str, NoticeBean.class);
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
