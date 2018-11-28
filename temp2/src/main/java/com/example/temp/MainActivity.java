package com.example.temp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView rvMain;
    private ArrayList<MultiItemEntity> multiList = new ArrayList<>();
    private MyAdapter adapter;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initData();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PahoExampleActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {

        send = (Button) findViewById(R.id.send);
        rvMain = (RecyclerView) findViewById(R.id.rv_main);

    }

    private void initData() {

        multiList = generateData();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new MyAdapter(multiList);
        rvMain.setLayoutManager(manager);
        rvMain.setAdapter(adapter);

        // 使一级列表默认展开
        for (int i = multiList.size() - 1; i >= 0; i--) {
            adapter.expand(i, false, false);
        }

    }


    private ArrayList<MultiItemEntity> generateData() {

        int levelOne = 10;
        int levelTwo = 3;

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        for (int i = 0; i < levelOne; i++) {

            LevelOne lv1 = new LevelOne("一级列表" + i);

            for (int j = 0; j < levelTwo; j++) {

                LevelTwo lv2 = new LevelTwo("二级列表：" + j);

                lv2.addSubItem(new LevelThree("三级列表" + j, "德玛西亚：" + j
                ));

                lv1.addSubItem(lv2);
            }
            res.add(lv1);
        }
        return res;
    }
}