package com.zhuochi.hydream.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 可以拖动的ScrollView
 */
public class CustomerScrollView extends ScrollView {
    /**
     * 设置拖动距离与实际移动距离的比
     */
    private static final int size = 2;
    private View inner;
    private float y;
    private Rect normal = new Rect();
    /**
     * 到顶时,仍然可以向上拖动
     */
    private boolean canRemove = true;

    public CustomerScrollView(Context context) {
        this(context, null);
    }

    public CustomerScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerScrollView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        setScrollBarSize(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    /**
     * 设置置顶时,是否仍然可以向下拉
     *
     * @param canRemove true:可以
     */
    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            if (canRemove)
                commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY) / size;
                // 滚动
                // 			 scrollBy(0, deltaY);

                y = nowY;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        return;
                    }
                    // 移动布局
                    inner.layout(inner.getLeft(), inner.getTop() - deltaY, inner.getRight(), inner.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    private OnScrollListener scrollViewListener = null;

    public void setOnScrollChangeListener(OnScrollListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null)
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
    }

    public interface OnScrollListener {
        /**
         * 监听ScrollView状态改变
         *
         * @param scrollView
         * @param x
         * @param y
         * @param oldx
         * @param oldy
         */
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
