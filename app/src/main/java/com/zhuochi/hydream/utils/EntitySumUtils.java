package com.zhuochi.hydream.utils;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zhuochi.hydream.activity.HomeActivity;
import com.zhuochi.hydream.activity.SchoolList;
import com.zhuochi.hydream.entity.RegisterEntity;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.http.GsonUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Cuixc
 * @date on  2018/5/14
 */

public class EntitySumUtils {

    /**
     * 登录提交数据 执行操作
     *
     * @param result
     * @param context 快捷登录、登录
     */
    public static void LoginSubmit(SonBaseEntity result, Context context) {
        Map map = (Map) result.getData().getData();
        try {
            String gson = GsonUtils.parseMapToJson(map);
            RegisterEntity entity = new Gson().fromJson(gson, RegisterEntity.class);
            UserUtils.setDataNull();

            //保存一下
            SPUtils.saveString(Constants.TOKEN_ID, entity.getToken()); //token 唯一标识
            SPUtils.saveInt("user_type",entity.getUser_type());
            SPUtils.saveInt(Constants.USER_ID, entity.getId());
            SPUtils.saveInt(Constants.USER_STATUE, entity.getUser_status());
            SPUtils.saveString(Constants.MOBILE_PHONE, entity.getMobile());
            SPUtils.saveString("nickName", entity.getUser_nickname());//昵称
            SPUtils.saveString("Avatar", entity.getAvatar());//头像
            SPUtils.saveInt(Constants.ORG_ID, entity.getDefault_org_id());//学校ID
            SPUtils.saveInt(Constants.ORG_AREA_ID, entity.getDefault_org_area_id());//校区ID
            SPUtils.saveInt(Constants.BUILDING_ID, entity.getDefault_building_id());//楼层ID
            SPUtils.saveInt(Constants.DEVICE_AREA_ID, entity.getDefault_device_area_id());//当前绑定区域（浴室）
            SPUtils.saveInt(Constants.USER_OAUTH, entity.getUser_oauth());//用户授权状态
            Set<String> set = new HashSet<String>();
            String mobile_Alias = "mobile_" + entity.getMobile();//绑定手机号 别名 Alias
            set.add("orgArea_" + entity.getDefault_org_area_id());//绑定 校区（Tag）
            JPushInterface.addTags(context, 0, set);
            JPushInterface.setAlias(context, 0, mobile_Alias);
            if (entity.getDefault_org_id() == 0 && entity.getDefault_org_area_id() == 0) {//判断学校ID和校区是否为0
                context.startActivity(new Intent(context, SchoolList.class));
            } else if (entity.getDefault_building_id() != 0) {
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadingAnimDialog.dismissLoadingAnimDialog();

    }
}