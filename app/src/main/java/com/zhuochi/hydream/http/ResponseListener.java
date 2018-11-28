package com.zhuochi.hydream.http;

import com.zhuochi.hydream.entity.SonBaseEntity;

/**
 * 请求回调
 * 返回值代表是否消费 需要判断super方法返回值
 *
 * @author po.
 * @date 2016/5/4.
 */
public interface ResponseListener {

    /**
     * 请求成功 数据正确
     */
    void onRequestSuccess(String tag, SonBaseEntity result);

    /**
     * 请求失败 网络异常
     */
    void onRequestFailure(String tag, Object s);

}
