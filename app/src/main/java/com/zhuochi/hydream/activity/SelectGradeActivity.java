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

public class SelectGradeActivity extends BaseAutoActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_grade);
    }
    public void onClickListener(View view){
        switch (view.getId()){
            case R.id.Principalone://1年级
                Intent intentOne=new Intent();
                intentOne.putExtra("name","1年级");
                intentOne.putExtra("id","1");

                setResult(102,intentOne);
                finish();
                break;
            case R.id.Principaltow://2年级
                Intent intentTow=new Intent();
                intentTow.putExtra("name","2年级");
                intentTow.putExtra("id","2");
                setResult(102,intentTow);
                finish();
                break;
            case R.id.Principal:
                Intent intent=new Intent();
                intent.putExtra("name","3年级");
                intent.putExtra("id","3");
                setResult(102,intent);
                finish();
                break;
            case R.id.deposit://押金
                Intent intent1=new Intent();
                intent1.putExtra("name","4年级");
                intent1.putExtra("id","4");

                setResult(102,intent1);
                finish();
                break;
            case R.id.balance://活动余额
                Intent intent2=new Intent();
                intent2.putExtra("name","5年级");
                intent2.putExtra("id","5");
                setResult(102,intent2);
                finish();
                break;
            case R.id.pay_back:
                finish();
                break;
        }
    }
}
