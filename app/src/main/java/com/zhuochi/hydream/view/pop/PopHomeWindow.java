package com.zhuochi.hydream.view.pop;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.klcxkj.zqxy.ui.MainUserActivity;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.activity.BlowerActivity;
import com.zhuochi.hydream.api.Neturl;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.http.ResponseListener;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.utils.ToastUtils;


public class PopHomeWindow extends PopupWindow implements OnClickListener {

    private String TAG = PopHomeWindow.class.getSimpleName();
    Activity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private Handler mHandler = new Handler();
    private static RelativeLayout layout;

    public PopHomeWindow(Activity context) {
        mContext = context;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;

        setWidth(mWidth);
        setHeight(mHeight);
    }

    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        long startMs = System.currentTimeMillis();

        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;//ͼƬ���ű�����
        float radius = 10;//ģ���̶�
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        Log.i(TAG, "blur time is:" + (System.currentTimeMillis() - startMs));
        return overlay;
    }

    private Animation showAnimation1(final View view, int fromY, int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
        go.setDuration(300);
        TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
        go1.setDuration(100);
        go1.setStartOffset(250);
        set.addAnimation(go1);
        set.addAnimation(go);

        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
        return set;
    }


    public void showMoreWindow(View anchor, int bottomMargin) {
        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_center_window, null);
        setContentView(layout);

        ImageView close = (ImageView) layout.findViewById(R.id.center_music_window_close);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.bottomMargin = bottomMargin;
        params.addRule(RelativeLayout.BELOW, R.id.more_window_local);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.more_window_local);
        params.topMargin = 200;
        params.leftMargin = 40;
        close.setLayoutParams(params);

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(layout);
                }
            }

        });

        showAnimation(layout);
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.center_music_window_close) {
                continue;
            }
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(300);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }

    private void closeAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.center_music_window_close) {
                continue;
            }
            child.setOnClickListener(this);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            }, (layout.getChildCount() - i - 1) * 30);

            if (child.getId() == R.id.more_window_local) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, (layout.getChildCount() - i) * 30 + 80);
            }
        }

    }

    private XiRequestParams params = new XiRequestParams(mContext);
    ResponseListener listener = new ResponseListener() {
        @Override
        public void onRequestSuccess(String tag, SonBaseEntity result) {

            switch (tag) {
                case "syncAmount":
                    ToastUtils.show(result.getData().getMsg());
                    Intent intent = new Intent();
                    intent.setClass(mContext, MainUserActivity.class);
                    intent.putExtra("tellPhone", SPUtils.getString(Constants.MOBILE_PHONE, ""));
                    intent.putExtra("PrjID", "0");
                    intent.putExtra("app_url", Neturl.WATER_URL);
                    mContext.startActivity(intent);
                    break;
            }
        }

        @Override
        public void onRequestFailure(String tag, Object s) {

        }
    };

    /**
     * 同步余额
     */
    private void syncAmount() {
        params.addCallBack(listener);
        params.syncAmount();
    }


    //同步余额
    private void synchroAmount() {
        syncAmount();
//        VolleySingleton.post(SYNCHROAMOUNT, "synchroAmount", null, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                Result re = JsonUtils.parseJsonToBean(result, Result.class);
//                if (re.data != null && re.data.flag == 1) {
//                    String userinfo = SPUtils.getString(Constants.USER_INFO, "");
//                    if (!TextUtils.isEmpty(userinfo)) {
//                        UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(userinfo, UserInfoEntity.class);
//                        if (userInfoEntity != null && userInfoEntity.data != null) {
////                            if (userInfoEntity.data.is_update.equals("1")) {//管理员
//                                //管理员测试洗澡
//                                Intent intent = new Intent();
//                                intent.setClass(mContext, MainAdminActivity.class);
//                                intent.putExtra("tellPhone", SPUtils.getString(Constants.PHONE_NUMBER, ""));
//                                intent.putExtra("PrjID", "0");
//                                intent.putExtra("app_url", Neturl.WATER_URL);
//                                mContext.startActivity(intent);
//                            } else {
//                                //用户洗澡

//                            }
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_window_local://洗澡
                synchroAmount();
                if (isShowing()) {
                    closeAnimation(layout);
                }

                break;
            case R.id.more_window_delete://吹风机
//                if (isShowing()) {//关闭当前布局
//                    closeAnimation(layout);
//                }
//                Intent intent1 = new Intent(mContext, BlowerActivity.class);
//                mContext.startActivity(intent1);
                break;

            default:
                break;
        }
    }

    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

}
