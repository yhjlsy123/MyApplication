package com.isgala.xishuashua.view.pulltorefresh2;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isgala.xishuashua.R;


public class HeaderView extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;// 初始状态

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;// 没状态
    public final static int STATE_READY = 1;// 准备刷新
    public final static int STATE_REFRESHING = 2;// 刷新中
    public final static int STATE_FINISH = 3;// 刷新完成(结果页)

    public HeaderView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 0);
        // 时间TextView
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        // 找到头部页面里的控件
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    // 设置状态
    public void setState(int state) {
        if (state == mState)
            return;
        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {// 当状态是准备的时候，显示动画
                    mArrowImageView.startAnimation(mRotateDownAnim);
                } else if (mState == STATE_REFRESHING) {// 当状态显示进度条的时候，清除动画
                    mArrowImageView.clearAnimation();
                } else if (mState == STATE_FINISH) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText("下拉刷新");// 文字提示：下拉刷新
                mArrowImageView.setVisibility(View.VISIBLE);// 箭头可见
                break;
            case STATE_READY:
                if (mState != STATE_READY) {// 随意可以刷新
                    mArrowImageView.clearAnimation();
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);// 松开刷新数据
                    mArrowImageView.startAnimation(mRotateUpAnim);
                }
                break;
            case STATE_REFRESHING:// 显示进度(刷新中)
                mArrowImageView.clearAnimation();
                mHintTextView.setText(R.string.xlistview_header_hint_loading);// 刷新中
                mArrowImageView.setVisibility(View.INVISIBLE);// 不显示箭头图片
                break;
            case STATE_FINISH:// 刷新完成结果页
                mHintTextView.setText(R.string.xlistview_header_hint_finish);// 刷新完成(结果页)
                break;
            default:
        }
        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
