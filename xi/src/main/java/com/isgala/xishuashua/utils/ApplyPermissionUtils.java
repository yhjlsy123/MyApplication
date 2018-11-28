package com.isgala.xishuashua.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * 功能
 *
 * @author by Sunqk
 * @date 2017/4/1
 */
public class ApplyPermissionUtils extends Activity {

    //请求拍照权限
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    //请求读取内存权限
    private static final int REQUEST_WRITE_PERMISSION = 102;
    //请求定位权限
    private static final int REQUEST_LOCATION_PERMISSION = 103;
    private static ApplyPermissionCallBack mCallBack;

    /**
     * 申请定位权限
     */
    public boolean applyLocationPermission(ApplyPermissionCallBack callBack){
        //Android Api-22 及以下不需要检测权限
        if (!isAboveSDK22()) {
            return true;
        }
        int hasCameraPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            mCallBack = callBack;
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * 申请相机权限
     */
    public static boolean applyCameraPermission(Context context){
        //Android Api-22 及以下不需要检测权限
        if (!isAboveSDK22()) {
            return true;
        }
        int hasCameraPermission = context.checkSelfPermission(Manifest.permission.CAMERA);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ((Activity)context).requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * 申请相机照片存储权限
     */
    public static boolean applyWritePermission(Context context){
        //Android Api-22 及以下不需要检测权限
        if (!isAboveSDK22()) {
            return true;
        }
        int hasCameraPermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ((Activity)context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * 权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:     //拍照
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.show("开启相机失败，请打开拍照权限");
                    mCallBack.applyCameraResult(false);
                    return;
                }
                mCallBack.applyCameraResult(true);
                break;
            case REQUEST_WRITE_PERMISSION:   //读取内存
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.show("开启相机失败，请打开存储权限");
                    mCallBack.applyCameraResult(false);
                    return;
                }
                mCallBack.applyCameraResult(true);
                break;
            case REQUEST_LOCATION_PERMISSION:   //定位
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //new DialogUtil().showToastNormal(this, "开启定位失败，请打开定位权限");
                    mCallBack.applyCameraResult(false);
                    return;
                }
                mCallBack.applyCameraResult(true);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 版本号是否高于22
     * Android-23 = Android 6.0
     */
    private static boolean isAboveSDK22(){
        return Build.VERSION.SDK_INT > 22;
    }

    public static void addApplyPermissionCallBack(ApplyPermissionCallBack callBack){
        mCallBack = callBack;
    }

    public interface ApplyPermissionCallBack{
        void applyCameraResult(boolean bool);
    }
}
