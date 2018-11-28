package com.isgala.xishuashua.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.Serializable;

import static com.isgala.xishuashua.api.Neturl.S;

/**
 * Created by and on 2016/11/3.
 */

public class BaseFragment extends Fragment implements  Serializable{

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
}
