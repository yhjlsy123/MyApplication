package com.zhuochi.hydream.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.ResponseListener;

import java.io.Serializable;

/**
 * Created by and on 2016/11/3.
 */

public class BaseFragment extends Fragment implements  Serializable,ResponseListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData();
    }
    public void initView(){

    }
    public void loadData(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {

    }

    @Override
    public void onRequestFailure(String tag, Object s) {

    }
}
