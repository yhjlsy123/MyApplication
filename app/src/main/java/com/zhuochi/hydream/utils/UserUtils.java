package com.zhuochi.hydream.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zhuochi.hydream.config.Constants;

/**
 * @author CuiXC
 * @date on 2017/4/10.
 */

public class UserUtils {

    private static UserUtils instance;
    public static String mTOKEN_ID = "";         //token 唯一标识
    public static int mUSER_ID = 0;         //用户ID
    public static String mMOBILE_PHONE = "";    //手机号
    public static int mORG_ID = 0;          //组织机构ID（学校ID）
    public static int mORG_AREA_ID = 0;          //组织机构ID（校区ID）
    public static int mDEVICE_AREA_ID = 0;          //当前绑定区域（浴室）

    public static int mBUILDING_ID = 0;     //组织区域下属的建筑物ID（楼层ID）
    public static int mUSER_STATUE = 0;     //用户状态;0:禁用,1:正常,2:未验证


    private Context mContext;

    public static void setDataNull() {
        mTOKEN_ID = "";
        mUSER_ID = 0;
        mMOBILE_PHONE = "";
        mORG_ID = 0;
        mBUILDING_ID = 0;
        mUSER_STATUE = 0;
        mORG_AREA_ID = 0;
    }

    public static void setSchoolbase() {
        mORG_ID = 0;
        mBUILDING_ID = 0;
        mORG_AREA_ID = 0;
        mDEVICE_AREA_ID = 0;
    }

    private UserUtils(Context context) {
        mContext = context.getApplicationContext();

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static UserUtils getInstance(Context context) {

        if (instance == null) {
            instance = new UserUtils(context);
        }

        return instance;
    }


    public String getTokenID() {
        if (TextUtils.isEmpty(mTOKEN_ID)) {
            mTOKEN_ID = SPUtils.getString(Constants.TOKEN_ID, "");
        }
        return mTOKEN_ID;
    }

    public int getUserID() {
//        if (mUSER_ID == 0) {
            mUSER_ID = SPUtils.getInt(Constants.USER_ID, 0);
//        }
        return mUSER_ID;
    }

    public String getMobilePhone() {
        if (TextUtils.isEmpty(mMOBILE_PHONE)) {
            mMOBILE_PHONE = SPUtils.getString(Constants.MOBILE_PHONE, "");
        }
        return mMOBILE_PHONE;
    }

    public int getOrgAreaID() {
//        if (mORG_AREA_ID == 0) {
            mORG_AREA_ID = SPUtils.getInt(Constants.ORG_AREA_ID, 0);
//        }
        return mORG_AREA_ID;
    }

    public int getOrgID() {
//        if (mORG_ID == 0) {
            mORG_ID = SPUtils.getInt(Constants.ORG_ID, 0);
//        }
        return mORG_ID;
    }

    public int getBuildingID() {
        if (mBUILDING_ID == 0) {
            mBUILDING_ID = SPUtils.getInt(Constants.BUILDING_ID, 0);
        }
        return mBUILDING_ID;
    }

    public int getDevice_Area_id() {
//        if (mDEVICE_AREA_ID == 0) {
            mDEVICE_AREA_ID = SPUtils.getInt(Constants.DEVICE_AREA_ID, 0);
//        }
        return mDEVICE_AREA_ID;
    }

    public int getUserStatue() {
        if (mUSER_STATUE == 0) {
            mUSER_STATUE = SPUtils.getInt(Constants.USER_STATUE, 0);
        }
        return mUSER_STATUE;
    }

}
