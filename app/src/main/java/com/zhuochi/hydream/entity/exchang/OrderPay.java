package com.zhuochi.hydream.entity.exchang;

/**
 * @author Cuixc
 * @date on  2018/7/5
 */

public class OrderPay {
    private String result;
    private long payTime;
    private String payArgs;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getPayArgs() {
        return payArgs;
    }

    public void setPayArgs(String payArgs) {
        this.payArgs = payArgs;
    }
}
