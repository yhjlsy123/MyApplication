package com.xinglincloud.www.newsuperbath.Base;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Box extends DataSupport {

    @Column(unique = true)              //是否唯一
    private int boxId;//箱柜的编号
    private String passwrod; //使用箱柜的用户的密码


    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }

}
