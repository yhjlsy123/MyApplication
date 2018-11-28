package com.xinglincloud.www.newsuperbath.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.VersionUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BathActivity extends FullScreenActivity {


    @BindView(R.id.bt_old_start)
    Button btOldStart;
    @BindView(R.id.bt_old_box)
    Button btOldBox;
    @BindView(R.id.bt_old_end)
    Button btOldEnd;
    @BindView(R.id.bt_admin_login)
    Button btAdminLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.et_signkey)
    EditText etSignkey;

    private int num = 0;
    private boolean threadSign = false;
    static TextView tvNetState;
    private static final String TAG = "BathActivity";
    boolean sign = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bath);
        ButterKnife.bind(this);
        ActivityUtil.addActivityToList(this);
        tvNetState = findViewById(R.id.tv_net_state);
        tvNetState.setVisibility(View.GONE);
        setVersionTv(tvVersion);
        viewInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        num = 0;
        setEtSignkeyFocusable();
    }

    private void setEtSignkeyFocusable(){
        //用EditText监听外接键盘
        etSignkey.setFocusable(true);
        etSignkey.setFocusableInTouchMode(true);
        etSignkey.requestFocus();
    }
    private void viewInit() {
        setEtSignkeyFocusable();
        //设置监听
        etSignkey.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                LogUtil.i(TAG, "onKey: 主界面按键的值：" + i);
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP:
                    case KeyEvent.ACTION_DOWN:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void setVersionTv(TextView tv) {
        tv.setText(VersionUtil.getVersion(this));
    }

    public static TextView getTvNetState() {
        return tvNetState;
    }

    @OnClick({R.id.bt_old_start, R.id.bt_old_box, R.id.bt_old_end, R.id.bt_admin_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_old_start:
                changeActivity("page", "openwater");
                break;
            case R.id.bt_old_box:
                Toast.makeText(getApplicationContext(), "功能未开放！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_old_end:
                changeActivity("page", "closewater");
                break;
            case R.id.bt_admin_login:
                Threadcountdown();
                if (num > 6) {
                    ActivityUtil.startActivity(BathActivity.this, LoginAdminActivity.class);
                } else {
                    num++;
                }
                break;
        }
    }

    public void changeActivity(String key, String value) {
        ActivityUtil.startActivity(BathActivity.this, LoginActivity.class, key, value);

    }

    private void Threadcountdown() {
        if (!threadSign) {
            threadSign = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num = 0;
                    threadSign = false;
                }
            }).start();
        }
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
