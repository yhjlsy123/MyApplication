package com.zhuochi.hydream.bathhousekeeper.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.activity.SchoolSelectAcitvity;
import com.zhuochi.hydream.bathhousekeeper.config.BathHouseApplication;
import com.zhuochi.hydream.bathhousekeeper.dialog.SelectTimeDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.DevicePram;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.ResponseListener;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.JumpUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.view.datapicker.DialogAlertListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 筛选自定义view
 *
 * @author Cuixc
 * @date on  2018/8/15
 */

public class HeightSelectView extends LinearLayout implements ResponseListener {
    TextView tvMultistageQuery;
    TextView tvTitleQuery;
    TextView tvSelect;
    TextView tvStartTime;
    @BindView(R.id.view)
    View view;
    TextView tvEndTime;
    TextView tvSelected;
    private Context mContext;
    private SelcetCallBack mCallBack;
    private XiRequestParams params;
    //请求学校、校区的code 1
    // 请求学校、校区 浴室的code 2
    // 请求学校,校区,浴室,浴位的code 3
    private int mRequestCode = 1;
    private int org_id;
    private int org_area_id;
    private int boothroom_id;
    private String device_key; //浴位
    private String startTime;
    private String endTime;
    private String hinText = "";

    public HeightSelectView(Context context, String hintxt) {
        super(context);
        hinText = hintxt;
        init(context);
    }

    public HeightSelectView(Context context, SelcetCallBack callBack) {
        super(context);
        mCallBack = callBack;
        init(context);
    }

    public HeightSelectView(Context context, SelcetCallBack callBack, int requestCode, String hintxt) {
        super(context);
        mCallBack = callBack;
        hinText = hintxt;
        mRequestCode = requestCode;
        init(context);
    }

    public HeightSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeightSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void addCallBack(SelcetCallBack callBack) {
        mCallBack = callBack;
    }

    public void setmRequestCode(int mRequestCode) {
        this.mRequestCode = mRequestCode;
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_head_select, this, true);
        ButterKnife.bind(view);
        tvSelect = (TextView) view.findViewById(R.id.edit_select);
        tvSelect.setHint(hinText);
        tvMultistageQuery = (TextView) view.findViewById(R.id.tv_multistageQuery);
        tvTitleQuery = (TextView) view.findViewById(R.id.tv_titleQuery);
        tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
        tvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
        tvSelected = (TextView) view.findViewById(R.id.tv_select);
        params = new XiRequestParams(context);
    }

    @OnClick({R.id.tv_select, R.id.edit_select, R.id.tv_startTime, R.id.tv_endTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select://点击查询
            /*    if (TextUtils.isEmpty(tvSelect.getText().toString())) {
                    ToastUtils.show("校区查询栏不可为空！");
                    return;
                }
                if (TextUtils.isEmpty(tvStartTime.getText().toString()) || TextUtils.isEmpty(tvEndTime.getText())) {
                    ToastUtils.show("时间查询栏不可为空！");
                    return;
                }
*/
                if (mCallBack != null) {
                    mCallBack.CallBackSelect(org_id, org_area_id, boothroom_id, device_key, startTime, endTime);
                }

                break;
            case R.id.edit_select://选择跳转学校
                selectSchool();
                break;
            case R.id.tv_startTime://选择开始时间
                new SelectTimeDialog(mContext, new DialogAlertListener() {
                    @Override
                    public void onDialogOk(Dialog dlg, String date1, String date2) {
                        tvStartTime.setText(date2);
                        startTime = date2;
                        dlg.dismiss();
                    }

                    @Override
                    public void onDialogCancel(Dialog dlg) {

                    }
                }).show();
                break;
            case R.id.tv_endTime://选择结束时间
                new SelectTimeDialog(mContext, new DialogAlertListener() {
                    @Override
                    public void onDialogOk(Dialog dlg, String date1, String date2) {
                        tvEndTime.setText(date2);
                        endTime = date2;
                        dlg.dismiss();
                    }

                    @Override
                    public void onDialogCancel(Dialog dlg) {

                    }
                }).show();
                break;
        }
    }

    //选择跳转学校 mContext 是acitvity 用于保存要返回的页面，首页不可销毁
    private void selectSchool() {
        tvSelect.setText(null);
        BathHouseApplication.mHeightSelectView = this;
        //mContext.startActivity(new Intent(mContext,SchoolList.class));
        DevicePram dpr = new DevicePram();
        dpr.setActivity(mContext.getClass());
        dpr.setRequestCode(mRequestCode);
        JumpUtils.jumpAcitvityParce(mContext, SchoolSelectAcitvity.class, dpr);
/*
        SelectBathingDialog dialog = new SelectBathingDialog(getContext());
        dialog.show();
        DialogUtils.bottomMatchWidhtScreen(dialog);*/

    }

    //选择跳转学校的回调
    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

    }

    @Override
    public void onRequestFailure(String tag, Object s) {

    }

    // 选择回调
    public interface SelcetCallBack {
        //学校id，开始时间， 结束时间
        void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime);
    }

    //从列表中获取学校、校区、浴室的id
    public void get_ids(int org_id, int org_area_id, int boothroom_id, String device_key) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        this.boothroom_id = boothroom_id;
        this.device_key = device_key;
    }

    //设置选择返回的name
    public void setNameText(String org_name, String org_area_name, String boothroom_name, String device_key_name) {

        tvSelect.setText(org_name + "\t" + org_area_name + "\t" + boothroom_name + "\t" + device_key_name);
    }


}
