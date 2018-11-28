package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.base.BaseAutoActivity;

/**
 * 选择转增类型
 * Created by 唯暮 on 2018/3/27.
 */

public class MoneyGiveTypeActivity extends BaseAutoActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_type);
    }
    public void onClickListener(View view){
        switch (view.getId()){
            case R.id.Principal://本金账户
                Intent intent=new Intent();
                intent.putExtra("name","本金账户");
                intent.putExtra("id","2");
                setResult(101,intent);
                finish();
                break;
            case R.id.deposit://押金
                Intent intent1=new Intent();
                intent1.putExtra("name","押金");
                intent1.putExtra("id","1");
                setResult(101,intent1);
                finish();
                break;
            case R.id.balance://活动余额
                Intent intent2=new Intent();
                intent2.putExtra("name","活动余额");
                intent2.putExtra("id","3");
                setResult(101,intent2);
                finish();
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }
}
