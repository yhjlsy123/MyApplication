package com.zhuochi.hydream.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 兑换中心
 *
 * @author Cuixc
 * @date on  2018/5/31
 */

public class ExchangeContentActivity extends BaseAutoActivity {

    @BindView(R.id.userinfo_cancle)
    ImageView userinfoCancle;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.Hint)
    TextView Hint;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.tv_hin)
    TextView tvHin;
    @BindView(R.id.Hint1)
    TextView Hint1;
    @BindView(R.id.tv_hin2)
    TextView tvHin2;
    @BindView(R.id.confrim)
    Button confrim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        InitView();
    }
    private void InitView(){
        userinfoCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
