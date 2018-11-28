package com.isgala.xishuashua.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.bean_.BathRoomFilter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by and on 2016/11/11.
 */

public class ChoicePopup extends PopupWindow {

    private static final int AnimatorDuration = 250;
    private Activity mActiviry;
    public ChoicePopup(Activity activity) {
        mActiviry=activity;
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setAnimationStyle(R.style.Popup);
        setOutsideTouchable(false);
    }

    public static ChoicePopup pop = null;
    private static ListAdapter listAdapter;
    private static float origHeight;
//    private static AnimationFrameLayout animationFrameLayout;
    private static ListView listView;
    private static View view;

    public interface  OnitemClickListener{
        void onClick(String id,String name);
    }
    public static  OnitemClickListener onItemClickListener;
    public void setOnitemClickListener(OnitemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    /**
     * 初始化popwindow
     *
     * @param activity
     * @return
     */
    public static ChoicePopup getInstance(Activity activity) {
        if (pop == null) {
            pop = new ChoicePopup(activity);
            view = LayoutInflater.from(activity).inflate(R.layout.layout_choiceroom, null);
            listView = (ListView) view.findViewById(R.id.choiceroom_listview);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(onItemClickListener!=null)
                        onItemClickListener.onClick(list.get(position).b_id,list.get(position).name);
                    if (pop.isShowing())
                        pop.dismiss();
                }
            });
//            animationFrameLayout = new AnimationFrameLayout(activity);
//            animationFrameLayout.setBackgroundColor(Color.TRANSPARENT);
//            animationFrameLayout.addView(view);
        }
        pop.setContentView(view);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        return pop;
    }

    private static void measureOriHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        origHeight = (float) view.getMeasuredHeight();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Window window = mActiviry.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }

    private static  List<BathRoomFilter.Filter> list = new ArrayList<>();

    /**
     * 加载数据(或者是更新数据)
     */
    public void show(final View target, List<BathRoomFilter.Filter> list) {
        this.list.clear();
        this.list.addAll(list);
        if (list.size() < 1) {
            return;
        }
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);
//        measureOriHeight(view);
//        com.nineoldandroids.animation.ObjectAnimator startAnimator = com.nineoldandroids.animation.ObjectAnimator
//                .ofFloat(animationFrameLayout, "dynamicHeight", new float[]{origHeight, 0.0f})
//                .setDuration(AnimatorDuration);
//        startAnimator.addListener(new AnimatorListener() {
//            @Override
//            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
//                pop.showAsDropDown(target);
//            }
//
//            @Override
//            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
//
//            }
//        });
//        startAnimator.start();
        pop.showAsDropDown(target);
        pop.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    /**
     * 过滤列表的数据
     *
     * @author and
     */
    private class ListAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_bathroom, null);
            }
            BathRoomFilter.Filter filter = list.get(position);
            ((TextView) convertView.findViewById(R.id.filter_bathroom_name)).setText(filter.name);
            ((TextView) convertView.findViewById(R.id.filter_bathroom_ratio)).setText(filter.ratio);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
