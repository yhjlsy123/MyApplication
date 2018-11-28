package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/5/11
 */

public class SonHeader {


    /**
     * token : 12121232323232333322222222222222
     */

    private String token;

    public static SonHeader objectFromData(String str) {

        return new Gson().fromJson(str, SonHeader.class);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
