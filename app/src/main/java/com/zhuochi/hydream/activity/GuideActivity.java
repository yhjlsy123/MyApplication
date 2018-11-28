package com.zhuochi.hydream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseAutoActivity {

    private ViewPager mViewPager;
    private List<View> mImageViews; // 滑动的图片集合
    private int[] mImageIds; // 图片ID
    private int currentItem = 0; // 当前图片的索引号
    private GestureDetector gestureDetector; // 用户滑动
    private int flaggingWidth;// 互动翻页所需滑动的长度是当前屏幕宽度的1/3


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        gestureDetector = new GestureDetector(this, new GuideViewTouch());
        // 获取屏幕分辨率
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        flaggingWidth = displayMetrics.widthPixels / 3;

        mImageIds = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

        mImageViews = new ArrayList<View>();

        for (int i = 0; i < mImageIds.length; i++) {
            View view = View.inflate(this, R.layout.item_guide, null);
            RelativeLayout image = (RelativeLayout) view.findViewById(R.id.ll_item_guide_image);
            image.setBackgroundResource(mImageIds[i]);
            if (i == mImageIds.length - 1) {
                image.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        next();
                    }
                });
            }
            mImageViews.add(view);
        }

        mViewPager = (ViewPager) findViewById(R.id.vp_activity_guide_viewpager);
        // 给ViewPager填充内容，设置Adapter
        mViewPager.setAdapter(new GuideAdapter());
        // 给ViewPager设置界面切换监听
        mViewPager.setOnPageChangeListener(new GuideViewPageChangeListener());
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author gong
     */
    private class GuideViewPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当界面切换完成的时候调用
         */
        @Override
        public void onPageSelected(int position) {
            currentItem = position;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gestureDetector.onTouchEvent(ev)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }

    public class GuideAdapter extends PagerAdapter {

        /**
         * 设置条目的个数
         */
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        /**
         * 判断ViewPager中的一个view对象是否和instantiateItem返回的object一致
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 给ViewPager添加条目，只能添加view对象，返回添加的view对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 根据条目的位置获取相应的view
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        /**
         * 销毁条目
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class GuideViewTouch extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (currentItem == mImageIds.length - 1) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
                        && (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        next();
                        return true;
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 下一步操作
     */
    public void next() {

//        String s_id = SPUtils.getString(Constants.S_ID, "");
        int ORG_ID = UserUtils.getInstance(this).getOrgID();//学校ID
//        String BUILDING_ID = SPUtils.getString(Constants.BUILDING_ID, "");//楼层ID
        int userID = UserUtils.getInstance(this).getUserID();
        String phone = UserUtils.getInstance(this).getMobilePhone();
        Class nextClass;
        if (userID != 0 && !phone.isEmpty()) {

            if (ORG_ID == 0) {//3.判断是否有设置过学校信息
                nextClass = SchoolList.class;
            } else {
                nextClass = HomeActivity.class;
            }
//        if (SPUtils.getBoolean(Constants.IS_LOGIN, false)) {//1.判断是否登录
//            String campus_id = SPUtils.getString(Constants.CAMPUS, "");
//            if (TextUtils.isEmpty(s_id) || TextUtils.equals(s_id, "0")) {//2.判断是否有设置过学校信息
//                nextClass = SchoolList.class;
//            } else if (TextUtils.isEmpty(campus_id) || TextUtils.equals(campus_id, "0")) {//3.判断是否设置过校区信息
//                nextClass = SchoolAreaList.class;
//            } else {
//                nextClass = HomeActivity.class;
//            }
        } else {
            nextClass = LoginActivity.class;
        }
        Intent intent = new Intent(this, nextClass);
//        intent.putExtra("s_id", s_id);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            next();
        }
        return super.onKeyDown(keyCode, event);
    }
}
