package com.isgala.xishuashua.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.isgala.xishuashua.config.BathHouseApplication;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingAnimDialog;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley单例工具类
 */
public class VolleySingleton {
    private static final String TAG = "VolleySingleton";
    private static VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    /**
     * 运行在主线程 回调
     */
    public interface CBack {
        /**
         * 回调
         *
         * @param result
         */
        void runUI(String result);
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
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

//    /**
//     * GET请求
//     *
//     * @param url  url
//     * @param tag  标记
//     * @param call 成功回调
//     */
//    public static void get(String url, String tag, final CBack call) {
//        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll(tag);
//        StringRequest request = new StringRequest(Method.GET, url, new Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                if (call != null)
//                    call.runUI(response);
//                popupWindowDismiss();
//            }
//
//        }, new ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                popupWindowDismiss();
//                if (NetworkUtil.isNetworkAvailable()) {
//                    ToastUtils.show("网络不稳定,请稍后再试");
//                }
//                Log.e(TAG, error.toString());
//            }
//
//        });
//        request.setTag(tag);
//        // 将StringRequest对象添加到RequestQueue请求队列中
//        VolleySingleton.getVolleySingleton().addToRequestQueue(request);
//    }


    /**
     * POST请求
     *
     * @param url  url
     * @param tag  标记
     * @param map  参数
     * @param call 成功回调
     */
    public static void post(final String url, final String tag, final Map<String, String> map, final CBack call) {
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll(tag);
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "response: " + response);
                }
                if (call != null)
                    call.runUI(response);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, error);
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
                Map<String, String> hashMap = new HashMap<String, String>();
                if (map != null)
                    hashMap.putAll(map);
                hashMap.put(Constants.OAUTH_TOKEN, SPUtils.getString(Constants.OAUTH_TOKEN, ""));
                hashMap.put(Constants.OAUTH_TOKEN_SECRET, SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
                hashMap.put(Constants.DEVICE_TOKEN, SPUtils.getString(Constants.DEVICE_TOKEN, ""));
                hashMap.put("code", String.valueOf(BathHouseApplication.VERSION_CODE));
                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "url: " + url);
                    LogUtils.e(tag, "para: " + hashMap.toString());
                }
                return hashMap;
            }
        };
        request.setTag(tag);
        VolleySingleton.getVolleySingleton().addToRequestQueue(request);
    }

    /**
     * POST请求
     *
     * @param url   url
     * @param tag   标记
     * @param map   参数
     * @param call  成功回调
     * @param call2 失败回调
     */
    public static void post(final String url, final String tag, final Map<String, String> map, final CBack call, final CBack call2
    ) {
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll(tag);
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "response: " + response);
                }
                if (call != null)
                    call.runUI(response);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, error);
                if (call2 != null) {
                    call2.runUI(error.toString());
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
                Map<String, String> hashMap = new HashMap<String, String>();
                if (map != null)
                    hashMap.putAll(map);
                hashMap.put(Constants.OAUTH_TOKEN, SPUtils.getString(Constants.OAUTH_TOKEN, ""));
                hashMap.put(Constants.OAUTH_TOKEN_SECRET, SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
                hashMap.put(Constants.DEVICE_TOKEN, SPUtils.getString(Constants.DEVICE_TOKEN, ""));
                hashMap.put("code", String.valueOf(BathHouseApplication.VERSION_CODE));
                if (BathHouseApplication.DEGUG) {
                    LogUtils.e(tag, "url: " + url);
                    LogUtils.e(tag, "para: " + hashMap.toString());
                }
                return hashMap;
            }
        };
        request.setTag(tag);
        VolleySingleton.getVolleySingleton().addToRequestQueue(request);
    }

    /**
     * POST请求( DEVICETOKEN)
     *
     * @param url   url
     * @param tag   标记
     * @param map   参数
     * @param call  成功回调
     * @param call2 失败回调
     */
    public static void postNoDEVICETOKEN(final String url, String tag, final Map<String, String> map, final CBack call, final CBack call2
    ) {
        VolleySingleton.getVolleySingleton().getRequestQueue().cancelAll(tag);
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("lsy", url + "\n" + response);
                if (call != null)
                    call.runUI(response);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, error);
                if (call2 != null) {
                    call2.runUI(error.toString());
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
                Map<String, String> hashMap = new HashMap<String, String>();
                if (map != null)
                    hashMap.putAll(map);
                hashMap.put(Constants.OAUTH_TOKEN, SPUtils.getString(Constants.OAUTH_TOKEN, ""));
                hashMap.put(Constants.OAUTH_TOKEN_SECRET, SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
                hashMap.put("code", String.valueOf(BathHouseApplication.VERSION_CODE));
                return hashMap;
            }
        };
        request.setTag(tag);
        VolleySingleton.getVolleySingleton().addToRequestQueue(request);
    }
}
