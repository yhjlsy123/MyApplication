package com.zhuochi.hydream.entity;

import java.util.List;

/**
 * Created by ztech on 2018/7/5.
 */
/*排名数据*/
public class RankEntity {

    /**
     * myRanking : {"sum_used_time":"0","number":1}
     * rankingList : [{"sum_used_time":"92","buyer_id":"14","_num":1,"buyer_name":"我有胡萝卜不给你吃","avatar":"http://p8uyenppn.bkt.clouddn.com/Fntzs7xKXXE8Bt3f-rCD9Kwi8AuO"},{"sum_used_time":"0","buyer_id":"51","_num":2,"buyer_name":"哈哈哈哈哈","avatar":"http://p8uyenppn.bkt.clouddn.com/20180712175018249.jpg"},{"sum_used_time":"0","buyer_id":"25","_num":3,"buyer_name":"momo","avatar":"http://p8uyenppn.bkt.clouddn.com/20180611170503293.jpg"}]
     */

    private String FAQ="";
    private MyRankingBean myRanking;
    private List<RankingListBean> rankingList;

    public String getFAQ() {
        return FAQ;
    }

    public void setFAQ(String FAQ) {
        this.FAQ = FAQ;
    }

    public MyRankingBean getMyRanking() {
        return myRanking;
    }

    public void setMyRanking(MyRankingBean myRanking) {
        this.myRanking = myRanking;
    }

    public List<RankingListBean> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<RankingListBean> rankingList) {
        this.rankingList = rankingList;
    }

    public static class MyRankingBean {
        /**
         * sum_used_time : 0
         * number : 1
         */

        private String sum_used_time;
        private int number;

        public String getSum_used_time() {
            return sum_used_time;
        }

        public void setSum_used_time(String sum_used_time) {
            this.sum_used_time = sum_used_time;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class RankingListBean {
        /**
         * sum_used_time : 92
         * buyer_id : 14
         * _num : 1
         * buyer_name : 我有胡萝卜不给你吃
         * avatar : http://p8uyenppn.bkt.clouddn.com/Fntzs7xKXXE8Bt3f-rCD9Kwi8AuO
         */

        private String sum_used_time;
        private String buyer_id;
        private int _num;
        private String buyer_name;
        private String avatar;

        public String getSum_used_time() {
            return sum_used_time;
        }

        public void setSum_used_time(String sum_used_time) {
            this.sum_used_time = sum_used_time;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
        }

        public int get_num() {
            return _num;
        }

        public void set_num(int _num) {
            this._num = _num;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
