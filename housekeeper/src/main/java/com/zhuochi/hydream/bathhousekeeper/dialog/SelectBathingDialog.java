package com.zhuochi.hydream.bathhousekeeper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.ComnTextAdapter;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.ResponseListener;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.view.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectBathingDialog extends Dialog {
    @BindView(R.id.sh_area)
    ListView shArea;
    @BindView(R.id.sh_room)
    ListView shRoom;
    @BindView(R.id.sh_deivice)
    ListView shDeivice;
    private Context context;
    private ComnTextAdapter shAreaAdapter;
    private ComnTextAdapter scRoomAdapter;
    private ComnTextAdapter adDeviceAdapter;
    private XiRequestParams params;
    private ComnTextAdapter adapter;
    private ComnTextAdapter adapter1;
    List<SchoolListThridBean.OrgAreaBean> data;
    private ComnTextAdapter adapter2;
    private ComnTextAdapter adapter3;

    public SelectBathingDialog(@NonNull Context context, List<SchoolListThridBean.OrgAreaBean> data) {
        super(context,R.style.custom_dialog2);
        this.context = context;
        this.data = data;

    }

    public SelectBathingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public SelectBathingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bathing_select_dialog);
        ButterKnife.bind(this);
        adapter1 = new ComnTextAdapter(context);
        adapter1.setScArea(data);
        shArea.setAdapter(adapter1);
        adapter2 = new ComnTextAdapter(context);
        shRoom.setAdapter(adapter2);
        shArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter2.setScRoom(data.get(position).getBathroom());
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter3 = new ComnTextAdapter(context);
        shDeivice.setAdapter(adapter3);
        shRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean> ddd = data.get(position).getBathroom();
                SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean dd = (SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean) parent.getSelectedItem();
                adapter3.setScDevice(dd.getDevice());
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
