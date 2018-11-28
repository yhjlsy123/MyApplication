package com.zhuochi.hydream.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuochi.hydream.entity.BaseEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.utils.NetworkUtil;
import com.zhuochi.hydream.utils.ToastUtils;


/**
 * HTTP请求控制器
 *
 * @author Cuixc
 * @date on  2018/5/11
 */

class RequestController {

    /**
     * 数据请求
     *
     * @param url        请求路径
     * @param Tag        方法标识
     * @param jsonObject 参数拼接
     * @param mCallBack  回调
     */
    static void RequestHttp(final String url, final String Tag, JSONObject jsonObject, final ResponseListener mCallBack) {
        if (NetworkUtil.isNetworkAvailable())
            VolleySingleton.requestPost(url, Tag, jsonObject, new VolleySingleton.CBack() {
                @Override
                public void onRequestSuccess(String result) {
                    BaseEntity baseEntity = new Gson().fromJson(result, BaseEntity.class);
                    try {
                        String analyData = DESCryptogRaphy.decode(baseEntity.getData());
                        SonBaseEntity array = GsonUtils.parseJsonToBean(analyData, SonBaseEntity.class);
                        Log.d("cxc", url);
                        Log.d("cxc", JSON.toJSONString(array));
                        if (array == null)
                            return;
                        if (array.getData().getCode() == 200) {
                            mCallBack.onRequestSuccess(Tag, array);
                        } else if (array.getData().getCode() == 100) {
                            mCallBack.onRequestSuccess(Tag, array);
                        } else {
                            ToastUtils.show(array.getData().getMsg());
                            mCallBack.onRequestFailure(Tag, array.getData());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, new VolleySingleton.ErrorBack() {
                @Override
                public void onRequestError(String result) {
                    mCallBack.onRequestFailure(Tag, result);
                }
            });
    }
}
