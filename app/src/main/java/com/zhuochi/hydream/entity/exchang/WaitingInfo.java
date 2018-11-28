package com.zhuochi.hydream.entity.exchang;

import java.io.Serializable;

/**
 * @author Cuixc
 * @date on  2018/7/16
 */

public class WaitingInfo implements Serializable{
    private String message;//提示
    private String queuingNumber;//排队人数
    private int expectedWaitingTime;//预期等待时间

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQueuingNumber() {
        return queuingNumber;
    }

    public void setQueuingNumber(String queuingNumber) {
        this.queuingNumber = queuingNumber;
    }

    public int getExpectedWaitingTime() {
        return expectedWaitingTime;
    }

    public void setExpectedWaitingTime(int expectedWaitingTime) {
        this.expectedWaitingTime = expectedWaitingTime;
    }
}
