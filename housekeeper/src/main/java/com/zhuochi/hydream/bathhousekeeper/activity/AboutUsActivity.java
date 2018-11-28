package com.zhuochi.hydream.bathhousekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.utils.APKVersionCodeUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*关于我们*/
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.apk_name)
    TextView apkName;
    @BindView(R.id.apk_version)
    TextView apkVersion;
    @BindView(R.id.user_img)
    RoundedImageView userImg;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
//        highApiEffects();/*沉浸式+适配底部虚拟按键*/
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        apkVersion.setText(APKVersionCodeUtils.getVerName(this));
        toolbarTitle.setText("关于我们");
        String Avatar = SPUtils.getString("Avatar", "");
        ImageLoadUtils.loadImage(this, Avatar, R.mipmap.about_logo,userImg);
    }


    @OnClick({
            R.id.toolbar_back,
            R.id.checkversion, R.id.new_function, R.id.abouy_company})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.checkversion:

                break;
            case R.id.new_function:

                break;
            case R.id.abouy_company:

                break;
        }
    }
}
