package com.isgala.xishuashua.view.pulltorefresh2;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isgala.xishuashua.R;

public class FooterView extends LinearLayout {
	public final static int STATE_NORMAL = 0;//拉动的常态
	public final static int STATE_READY = 1;//达到加载更多的阀值
	public final static int STATE_LOADING = 2;//加载中
	public final static int STATE_SUCCESS=3;//加载成功
	public final static int STATE_NONE=4;//初始状态
	public final static  int STATE_NO_MORE=5;//没有更多数据
	public final static  int STATE_ERR=6;//加载出错
	public final static int STATE_NULL=7;//不显示任何提示
	private Context mContext;

	private View mContentView;
	// private View mProgressBar;
	private TextView mHintView;

	public FooterView(Context context) {
		this(context, null);
	}

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		// mProgressBar.setVisibility(View.INVISIBLE);
		// mHintView.setVisibility(View.INVISIBLE);
		if(state==STATE_NULL){
			mHintView.setText("");
		}else if (state == STATE_READY) {// 如果是第一页状态，那么“查看更多”显示
			mHintView.setText(R.string.xlistview_footer_hint_ready);// 松开显示更多
		} else if (state == STATE_LOADING) {// 当加载的时候
			// mProgressBar.setVisibility(View.VISIBLE);// 加载进度条显示
			mHintView.setText("加载中...");
		} else if(STATE_SUCCESS==state){
			mHintView.setText("");
			// mHintView.setText(R.string.xlistview_footer_hint_normal);// 查看更多
		}else if(state==STATE_NO_MORE){
			mHintView.setText("没有更多数据");
		}else if(state==STATE_NONE){
			mHintView.setText("上拉加载更多");
		}else if(state==STATE_ERR){
			mHintView.setText("数据加载出错,请稍候重试");
		}else{
			mHintView.setText("上拉加载更多");
		}
	}
	
	public void setBottomMargin(int height) {
		if (height < 0)
			height=0;
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		// mHintView.setVisibility(View.VISIBLE);
		// mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		// mHintView.setVisibility(View.GONE);
		// mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 当禁用拉加载更多隐藏底部视图
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * 显示底部视图
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	private int mFooterViewHeight;
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		moreView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mFooterViewHeight = FooterView.this.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				setBottomMargin(-mFooterViewHeight);
			}
		});
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView;
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
		mHintView.setText("上拉加载更多");
	}

}
