package com.isgala.xishuashua.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by and on 2016/11/19.
 */
public class BalanceLogDetailData implements Serializable {
    /*
   "operate":"1","icon":"weixin","text":"\u5fae\u4fe1\u5145\u503c","price":"1.00","status":"\u4ea4\u6613\u6210\u529f",
   "content":[{"title":"\u4ed8\u6b3e\u65b9\u5f0f","info":"\u5fae\u4fe1"},
    {"title":"\u4ea4\u6613\u5355\u53f7","info":""},{"title":"\u521b\u5efa\u65f6\u95f4","info":"2016-11-10 15:08"}]}
     */
    public String operate;
    public String icon;
    public String text;
    public String price;
    public String status;
    public List<Content> content;
    public Info info;

    public static class Content implements Serializable{
        public String title;
        public String info;
        public String content;
    }
    public static class Info implements Serializable{
        public String title;
        public List<Content> content;
    }
}
