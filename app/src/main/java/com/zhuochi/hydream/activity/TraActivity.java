package com.zhuochi.hydream.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.utils.ToastUtils;


/**
 * 透明的activity
 * Created by and on 2016/11/23.
 */

public class TraActivity extends BaseAutoActivity {


    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getDecorView().setBackgroundDrawable(null);
        ToastUtils.show("TraActivity");
        remind();
    }

    private Vibrator vibrator;

    private void remind() {
        // 使用来电铃声的铃声路径
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        if (mMediaPlayer == null)
//            mMediaPlayer = new MediaPlayer();
//        try {
//            mMediaPlayer.setDataSource("file:///android_asset/");
////            mMediaPlayer.setDataSource(context, uri);
//            mMediaPlayer.prepare();
//            mMediaPlayer.setLooping(true); //循环播放
//            mMediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环
        vibrator.vibrate(new long[]{3000, 3000}, 0);
        vibrator.cancel();
    }

    private void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (vibrator != null && vibrator.hasVibrator())
            vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
    }
}
