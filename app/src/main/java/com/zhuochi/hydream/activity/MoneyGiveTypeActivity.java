package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;

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
                intent.putExtra("name","3年");
                intent.putExtra("id","3");

                setResult(101,intent);
                finish();
                break;
            case R.id.deposit://押金
                Intent intent1=new Intent();
                intent1.putExtra("name","4年");
                intent1.putExtra("id","4");
                setResult(101,intent1);
                finish();
                break;
            case R.id.balance://活动余额
                Intent intent2=new Intent();
                intent2.putExtra("name","5年");
                intent2.putExtra("id","5");
                setResult(101,intent2);
                finish();
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }
}
