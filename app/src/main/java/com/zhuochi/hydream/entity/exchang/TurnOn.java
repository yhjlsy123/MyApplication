package com.zhuochi.hydream.entity.exchang;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/29
 */

public class TurnOn {

    /**
     * uuid : 9019df64-ff05-433f-a8c0-51ce63d36265_faucet_05
     * startTime : 1.530243887E9
     * orderSn : 91014
     */

    private String uuid;
    private String startTime;
    private String orderSn;



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
