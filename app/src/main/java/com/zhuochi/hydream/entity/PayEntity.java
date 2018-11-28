package com.zhuochi.hydream.entity;

import com.google.gson.Gson;

/**
 * @author Cuixc
 * @date on  2018/6/27
 */

public class PayEntity {

    /**
     * code : 200
     * msg : \u64cd\u4f5c\u6210\u529f
     * data : app_id=2018062660479035&biz_content=%7B%22body%22%3A%22%E4%BD%99%E9%A2%9D%E5%85%85%E5%80%BC%22%2C%22subject%22%3A%222018-06-27+11%3A54%E4%BD%99%E9%A2%9D%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%22201806271154_1_2_4227%22%2C%22total_amount%22%3A%2210.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22goods_type%22%3A%221%22%2C%22passback_params%22%3A%222%22%2C%22timeout_express%22%3A%2210m%22%7D&charset=UTF-8&format=JSON&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fzaotang.94feel.com%2Fpayment%2Fnotify%2Fhandlecallback%2FdeviceAreaId%2F2%2Fmode%2Falipay.html&sign_type=RSA2×tamp=2018-06-27+11%3A54%3A04&version=1.0&sign=WpXMtUWFwoMoJ%2FzeQ0MFUJXOfS9SdQRYaLaUcqzqcTrYqXGl6KIQDyq%2Bjo8BQ4Mj1667g7ih8M9V5JznhFi6OKJh8MQFfXSVOMj43bVqRaJwp01K%2F7pEKf3Q7C4zfxzGKnkjRa%2B9L7qtCGUuItLK4VSvFAaHKYRwerGCwotwj1HxYQRuTDjzaSU8ncX4m%2FhyF1rBBop8bVtIljGgba09A%2Fp%2BlRpptaqVjJ0Ppi9XvFPMNfErFbn1uv41TdUIa3MRuDsbQyELZP7HuoK5Xq0779eJ5810LAPNyzX2Ws%2FA9EJjbPke67%2BRlHvc8dIxKhvkaRiqGyqtgmw6830t2WwnJg%3D%3D
     * time : 1530071643
     */

    private int code;
    private String msg;
    private String data;
    private int time;

    public static PayEntity objectFromData(String str) {

        return new Gson().fromJson(str, PayEntity.class);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
