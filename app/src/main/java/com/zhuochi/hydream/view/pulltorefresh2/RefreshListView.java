package com.zhuochi.hydream.view.pulltorefresh2;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zhuochi.hydream.R;


public class RefreshListView extends ListView implements OnScrollListener {
    private float mLastY = -1; // save event y
    private Scroller mScroller; // 用于回滚
    private OnScrollListener mScrollListener; // 回滚监听
    // 触发刷新和加载更多接口.
    private IXListViewListener mListViewListener;
    // -- 头部的View
    private HeaderView mHeaderView;
    // 查看头部的内容，用它计算头部高度，和隐藏它
    // 当禁用的时候刷新
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight; // 头部View的高
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // 是否正在刷新.
    // -- 底部的View
    private FooterView mFooterView;
    private boolean mEnablePushLoad;
    private boolean mPushLoading;// 上推加载更多
    private boolean mIsFooterReady = false;
    // 总列表项，用于检测列表视图的底部
    private int mTotalItemCount;

    // for mScroller, 滚动页眉或者页脚
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;// 顶部
    private final static int SCROLLBACK_FOOTER = 1;// 下部

    private final static int SCROLL_DURATION = 300; // 滚动回时间
    private final static int PULL_LOAD_MORE_DELTA = 10; // 当大于10PX的时候，加载更多

    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.
    private LinearLayout linearLayout;

    /**
     * @param context
     */
    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // 初始化头部View
        mHeaderView = new HeaderView(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(mHeaderView);
        addHeaderView(linearLayout);// 把头部这个视图添加进去
        // 初始化底部的View
        mFooterView = new FooterView(context);
        mFooterView.setOnClickListener(null);
        // 初始化头部高度
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 限定增加一个头布局
     *
     * @param view
     */
    public void addHeadView(View view) {
        if (linearLayout.getChildCount() < 2) {
            linearLayout.addView(view);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // 确定XListViewFooter是最后底部的View, 并且只有一次
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * 启用或禁用下拉刷新功能.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // 禁用,隐藏内容
            mHeaderViewContent.setVisibility(View.INVISIBLE);// 如果为false则隐藏下拉刷新功能
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);// 否则就显示下拉刷新功能
        }
    }

    /**
     * 获取脚布局是否可见
     *
     * @return
     */
    public boolean footerViewIsVisible() {
        if (mFooterView != null) {
            if (getChildAt(getLastVisiblePosition()) instanceof FooterView) {
                return true;
            }
        }
        return false;
    }

    /**
     * 启用或禁用加载更多的功能.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePushLoad = enable;
        if (!mEnablePushLoad) {
            mFooterView.hide();
            mFooterView.setState(FooterView.STATE_NULL);
            hander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFooterView.hide();// 隐藏
                    mFooterView.setOnClickListener(null);
                }
            }, 1500);

        } else {
            mPushLoading = false;
            mFooterView.show();// 显示
            mFooterView.setState(FooterView.STATE_NORMAL);
        }
    }

    /**
     * 停止刷新, 重置头视图.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            long sendTime = System.currentTimeMillis() - startTime;
            hander.postDelayed(new Runnable() {
                public void run() {
                    mPullRefreshing = false;
                    resetHeaderHeight();
                }
            }, (sendTime < 800 ? (800 - sendTime) : 0));

        }
    }

    /**
     * 立即重置视图
     */
    public void immediatelyStop() {
        mPullRefreshing = false;
        int height = mHeaderView.getVisiableHeight();
        int finalHeight = 0; // 默认：滚动回头.
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, 1);
        mPushLoading = false;
        // 触发刷新
        invalidate();
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPushLoading  && mEnablePushLoad) {
            mPushLoading = false;
            mFooterView.setState(FooterView.STATE_NONE);
        }
    }

    /**
     * 数据加载出错
     */
    public void errLoadMore() {
        if (mPushLoading) {
            mPushLoading = false;
            mFooterView.setState(FooterView.STATE_ERR);
        }
    }

    /**
     * stop load more, no more,cannot push
     */
    public void showNOMore() {
        mPushLoading = false;
        mFooterView.setState(FooterView.STATE_NO_MORE);
//        mEnablePushLoad = false;
    }


    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        // 当头部不可见时,设置为没有做任何事
        if (mHeaderView.getVisiableHeight() == 0) {
            nothing = true;
        }
        int height = (int) delta + mHeaderView.getVisiableHeight();
        mHeaderView.setVisiableHeight(height);
        if (mEnablePullRefresh && nothing) { // 未处于刷新或刷新结果状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(HeaderView.STATE_READY);
            } else {
                mHeaderView.setState(HeaderView.STATE_NORMAL);
            }
        }
        setSelection(0); // 滚动回顶部
    }

    /**
     * 重置头视图的高度
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // 头部可见的高度=0不显示.
            return;
        // 不显示刷新和标题的时候，什么都不显示
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // 默认：滚动回头.
        // 当滚动回显示所有头标题时候，刷新
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // 触发刷新
        invalidate();
    }

    // 改变底部视图高度
    private void updateFooterHeight(float delta) {

        float height = mFooterView.getBottomMargin() + delta;
        if (mEnablePushLoad && !mPushLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // 高度足以调用加载更多
                mFooterView.setState(FooterView.STATE_READY);
            } else {
                mFooterView.setState(FooterView.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin((int) height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    // 开始加载更多
    private void startLoadMore() {
        mPushLoading = true;
        mFooterView.setState(FooterView.STATE_LOADING);
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mListViewListener != null) {
                    mListViewListener.onLoadMore();
                }
            }
        }, 500);
    }

    private float moveY;

    // 触发事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                moveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                moveY += deltaY;
                // if (scrollListener != null)
                // scrollListener.onScrollChanged(this, moveY);
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // 第一项显示,标题显示或拉下来.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // 最后一页，已停止或者想拉起
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }

                break;
            default:
                moveY = 0;
                mLastY = -1; // 重置
                if (getFirstVisiblePosition() == 0) {
                    // 调用刷新,如果头部视图高度大于设定高度。
                    if (mEnablePullRefresh && nothing && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        nothing = false;
                        mPullRefreshing = true;// 那么刷新
                        resetHeaderHeight();
                        mHeaderView.setState(HeaderView.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            startTime = System.currentTimeMillis();
                            mListViewListener.onRefresh();
                        }
                    } else {// 没有超过指定的刷新高度,重置头布局
                        resetHeaderHeight();
                    }
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // 调用加载更多.getLastVisiblePosition()-->
                    Log.e("tagtag", mEnablePushLoad + "  " + (mFooterView.getBottomMargin()) + "  " + PULL_LOAD_MORE_DELTA + "  " + (!mPushLoading));

                    if (mEnablePushLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPushLoading) {
                        startLoadMore();// 如果底部视图高度大于可以加载高度，那么就开始加载
                    }
                    resetFooterHeight();// 重置加载更多视图高度
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private long startTime;// 刷新 用时
    /**
     * 不是刷新中或者已完成状态
     */
    private boolean nothing = true;

    /**
     * 完成下拉刷新
     */
    public void finishRefresh() {
        long sendTime = System.currentTimeMillis() - startTime;
        hander.postDelayed(new Runnable() {
            public void run() {
                mHeaderView.setState(HeaderView.STATE_FINISH);
                hander.sendEmptyMessageDelayed(100, 600);
            }
        }, (sendTime < 800 ? (800 - sendTime) : 0));
    }

    Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mPullRefreshing = false;
            resetHeaderHeight();
            hander.removeMessages(100);
        }
    };

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 发送到用户的监听器
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null && mFooterView.getBottomMargin() < 1) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * 你可以监听到列表视图，OnScrollListener 或者这个. 他将会被调用 , 当头部或底部触发的时候
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * 实现这个接口来刷新/负载更多的事件
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public enum State {
        PULL, // 下拉刷新
        NONE, // 没做事
        PUSH;// 上拉加载更多
    }
}

