package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.manager.BarChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankFragment extends BaseFragment {

    @BindView(R.id.rank_barchart)
    BarChart rankBarchart;
    @BindView(R.id.rank_img)
    ImageView rankImg;
    @BindView(R.id.rank_tv)
    TextView rankTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {


        BarChartManager bar_chart1 = new BarChartManager(rankBarchart);


        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j <= 10; j++) {
                yValue.add((float) (Math.random() * 80));
            }
            yValues.add(yValue);
        }

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.CYAN);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("折线一");
        names.add("折线二");
        names.add("折线三");
        names.add("折线四");

        //创建多条折线的图表
        bar_chart1.showBarChart(xValues, yValues.get(0), "", colours.get(3));
//        barChartManager2.showBarChart(xValues, yValues, names, colours);
//        barChartManager2.setXAxis(11f, 0f, 11);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
