package com.isgala.xishuashua.bean_;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by and on 2016/11/9.
 */

public class Message extends Base {
    public MessageData data;

    public static class MessageData implements Serializable {
        public String count;
        public String nowPage;
        public String totalPage;
        public List<MessageItem> result;
    }

    public static class MessageItem implements Serializable {
        /**
         * "ms_id":"1",
         * "title":"新订单提醒",
         * "content":"您有一个预约时间为2016-07-18 19:00的新订单",
         * "href":"",
         * "created":"10月28日 10:12"
         */
        public String ms_id;
        public String title;
        public String content;
        public String created;
        public String href;
    }
}
