package com.zhuochi.hydream.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自适应高度的GridView
 *
 * @author and
 */
public class CustomGridView extends GridView {

    public CustomGridView(Context context) {
        this(context, null);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);// 设置上拉的效果
        setScrollBarSize(0);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
