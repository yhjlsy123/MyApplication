package com.xinglincloud.www.newsuperbath.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.ActivityUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAdminActivity extends FullScreenActivity {

    @BindView(R.id.et_admin_account)
    EditText etAdminAccount;
    @BindView(R.id.et_admin_password)
    EditText etAdminPassword;
    @BindView(R.id.bt_admin_sure)
    Button btAdminSure;
    @BindView(R.id.bt_admin_back)
    Button btAdminBack;
    private static final String TAG = "LoginAdminActivity";
    private int time = 0;

    private boolean isTrue = true;
    private Thread threadtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        ButterKnife.bind(this);
        timerclock();
        viewInit();
    }

    private void viewInit() {
        etAdminAccount.setText("admin");
        etAdminPassword.setFocusable(true);
        etAdminPassword.setFocusableInTouchMode(true);
        etAdminPassword.requestFocus();
        etAdminAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                time = 0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etAdminPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                time = 0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @OnClick({R.id.bt_admin_sure, R.id.bt_admin_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_admin_sure:
                closeKeybord(etAdminAccount, getApplicationContext());
                time = 0;
                if (etAdminAccount.getText().toString().equals("root") && etAdminPassword.getText().toString().equals("h25kq9wmczd8")) {
                    ActivityUtil.startActivity(LoginAdminActivity.this, AdminActivity.class);
                    finish();
                    time = 10;
                } else if (etAdminAccount.getText().toString().equals("admin")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    String password = sharedPreferences.getString("password", "admin123");
                    if (etAdminPassword.getText().toString().equals(password)) {
                        ActivityUtil.startActivity(LoginAdminActivity.this, AdminActivity.class);
                        finish();
                        time = 10;
                    } else {
                        Toast.makeText(getApplicationContext(), "账号或密码错误!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "账号或密码错误!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_admin_back:
                closeKeybord(etAdminAccount, getApplicationContext());
                isTrue = false;
                finish();
                break;
        }
    }

    private void timerclock() {
        threadtime = new Thread() {
            @Override
            public void run() {
                while (isTrue) {
                    if (time > 10) {
                        closeKeybord(etAdminAccount, getApplicationContext());
                        finish();
                        break;
                    } else {
                        time++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                LogUtil.i(TAG, "run: 线程结束");

            }
        };
        threadtime.start();
    }

    public void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
