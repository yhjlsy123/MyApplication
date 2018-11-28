package com.zhuochi.hydream.utils;


        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.os.Handler;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;

/**
 * 自定义控件循环走马灯的实现
 *
 * @author cyf 继承自TextView
 */
public class Util extends TextView implements Runnable {
    private static final String TAG = "MarqueeTextView";
    // 设置跑马灯重复的次数，次数
    private int circleTimes = 3;
    //记录已经重复了多少遍
    private int hasCircled = 0;
    private int currentScrollPos = 0;
    // 跑马灯走一遍需要的时间（秒数）
    private int circleSpeed = 10;
    // 文字的宽度
    private int textWidth = 0;

    private boolean isMeasured = false;
    // Handler机制
    private Handler handler;
    private boolean flag = false;

    // 构造方法
    public Util(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.removeCallbacks(this);
        post(this);
    }
    /**
     * 画笔工具
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isMeasured) {
            getTextWidth();
            isMeasured = true;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        // 二次进入时初始化成员变量
        flag = false;
        isMeasured = false;
        this.hasCircled = 0;
        super.setVisibility(visibility);
    }

    @Override
    public void run() {
        // 起始滚动位置
        currentScrollPos += 1;
        scrollTo(currentScrollPos, 0);
        // Log.i(TAG, "pos"+currentScrollPos);
        // 判断滚动一次
        if (currentScrollPos >= textWidth) {
            // 从屏幕右侧开始出现
            currentScrollPos = -this.getWidth();
            //记录的滚动次数大设定的次数代表滚动完成，这个控件就可以隐藏了
            if (hasCircled >= this.circleTimes) {
                this.setVisibility(View.GONE);
                flag = true;
            }
            hasCircled += 1;
        }

        if (!flag) {
            // 滚动时间间隔
            postDelayed(this, circleSpeed);
        }
    }

    /**
     * 获取文本显示长度
     */

    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        Log.i(TAG, str);
        if (str == null) {
            textWidth = 0;
        }
        textWidth = (int) paint.measureText(str);
    }

    /**
     * 设置滚动次数，达到次数后设置不可见
     *
     * @param circleTimes
     */
    public void setCircleTimes(int circleTimes) {
        this.circleTimes = circleTimes;
    }

    public void setSpeed(int speed) {
        this.circleSpeed = speed;
    }

    public void startScrollShow() {
        if (this.getVisibility() == View.GONE)
            this.setVisibility(View.VISIBLE);
        this.removeCallbacks(this);
        post(this);
    }

    private void stopScroll() {
        handler.removeCallbacks(this);
    }
}