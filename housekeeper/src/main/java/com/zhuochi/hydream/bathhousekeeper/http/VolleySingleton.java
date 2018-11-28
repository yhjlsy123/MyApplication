package com.zhuochi.hydream.bathhousekeeper.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.LruCache;
import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.utils.LogUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.NetworkUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;


import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Volley单例工具类
 */
public class VolleySingleton {
    private static final String TAG = "VolleySingleton";
    private static VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    /**
     * 运行在主线程 回调
     */
    public interface CBack {
        /**
         * 回调
         *
         * @param result
         */
        void onRequestSuccess(String result);
    }

    public interface ErrorBack {
        void onRequestError(String result);
    }

    private VolleySingleton() {
        mContext = BathHouseApplication.mApplicationContext;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
    }

    public static synchronized VolleySingleton getVolleySingleton() {
        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton();
        }
        return volleySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(mContext);
            mRequestQueue = Volley.newRequestQueue(mContext, new OkHttpStack(new OkHttpClient()));

        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    /**
     * POST请求
     *
     * @param url   url
     * @param tag   标记
     * @param call  成功回调
     * @param call2 失败回调
     */
    public static void requestPost(final String url, final String tag, final JSONObject jsonObject, final CBack call, final ErrorBack call2
    ) {
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll(tag);
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "response: " + response);
                }
                if (call != null)
//                    ResponseListener.onRequestSuccess(response)
                    call.onRequestSuccess(response);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, error);
                if (call2 != null) {
                    call2.onRequestError(error.toString());
                }
                try {
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                    LoadingAnimDialog.dismissLoadingAnimDialog();
                } catch (Exception e) {

                }
                if (NetworkUtil.isNetworkAvailable()) {
                    ToastUtils.show("网络不稳定,请稍后再试");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "url: " + url);
//                    LogUtils.e(tag, "para: " + hashMap.toString());
                }
                return JsonSplicingUtil.JsonSplicingSon(tag, jsonObject, mContext);
            }
        };
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getVolleySingleton().addToRequestQueue(request);
    }
}
