package com.isgala.xishuashua.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/**
 * 通过画布移动实现视觉上的位移
 * @author and
 *
 */
public class AnimationFrameLayout extends FrameLayout {
	private float dynamicHeight;
	public AnimationFrameLayout(Context context) {
		super(context);
	}

	public AnimationFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AnimationFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	public void  setDynamicHeight(float dynamicHeight){
		if(dynamicHeight<0)
			dynamicHeight=0;
		this.dynamicHeight=dynamicHeight;
		invalidate();
	}
	public float getDynamicHeight(){
		return this.dynamicHeight;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.restore();
		canvas.translate(0, -dynamicHeight);
		super.onDraw(canvas);
		canvas.save();
	}
}
