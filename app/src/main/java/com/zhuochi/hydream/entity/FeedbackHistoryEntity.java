package com.zhuochi.hydream.entity;

/**
 * @author Cuixc
 * @date on  2018/6/6
 */

public class FeedbackHistoryEntity {
    /**
     *  "id" -> "1.0"
     "relation_su_id" -> "1.0"
     "type_id" -> "1.0"
     "content" -> "哈哈哈哈我的微博我就不知道了，我"
     "source_type" -> "user"
     "is_main" -> "1.0"
     "is_read" -> "0.0"
     "is_reply" -> "0.0"
     "imgs" -> "http://p8uyenppn.bkt.clouddn.com/34AF00DA-16C9-4D15-9EAC-EC2E33554A6E,http://p8uyenppn.bkt.clouddn.com/5E286D72-858E-469F-A57E-95578BCC5C27"
     */
    private String id;
    private String irelation_su_idd;
    private String type_id;
    private String content;
    private String source_type;
    private String is_main;
    private String is_read;
    private String is_reply;
    private String imgs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIrelation_su_idd() {
        return irelation_su_idd;
    }

    public void setIrelation_su_idd(String irelation_su_idd) {
        this.irelation_su_idd = irelation_su_idd;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getIs_main() {
        return is_main;
    }

    public void setIs_main(String is_main) {
        this.is_main = is_main;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getIs_reply() {
        return is_reply;
    }

    public void setIs_reply(String is_reply) {
        this.is_reply = is_reply;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
}
