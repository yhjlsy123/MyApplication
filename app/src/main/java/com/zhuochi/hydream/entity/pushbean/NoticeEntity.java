package com.zhuochi.hydream.entity.pushbean;

/**
 * @author Cuixc
 * @date on  2018/7/24
 */

public class NoticeEntity {
    private int id;
    private int org_area_id;
    private String title;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrg_area_id() {
        return org_area_id;
    }

    public void setOrg_area_id(int org_area_id) {
        this.org_area_id = org_area_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
