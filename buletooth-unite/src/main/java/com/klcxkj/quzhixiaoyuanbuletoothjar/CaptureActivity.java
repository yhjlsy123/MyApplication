/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klcxkj.quzhixiaoyuanbuletoothjar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.klcxkj.jxing.OnScannerCompletionListener;
import com.klcxkj.jxing.ScannerView;
import com.klcxkj.jxing.common.Scanner;
import com.klcxkj.quzhixiaoyuanbuletooth_unite.R;
import com.klcxkj.quzhixiaoyuanbuletoothjar.util.Common;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.NiftyDialogBuilder;



/**
 * autor:OFFICE-ADMIN
 * time:2018/4/17
 * email:yinjuan@klcxkj.com
 * description: 二维码扫码类
 */
public  class CaptureActivity extends AppCompatActivity implements OnScannerCompletionListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();





    private ImageView flash_img;
   private int ifOpenLight = 0; // 判断是否开启闪光灯






   private int capture_type;
    private ScannerView mScannerView;


    protected NiftyDialogBuilder dialogBuilder;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_capture);

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        //二维码设置
        setdecode();


        flash_img = (ImageView) findViewById(R.id.flash_img);
        openLight();
        flash_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ifOpenLight++;
                openLight();
            }
        });
        //隐藏设备列表
         initdata();


        setAdmin();
    }
    private void setAdmin() {
        if (ContextCompat.checkSelfPermission(CaptureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(CaptureActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
        }
    }
    // 是否开启闪光灯
    private void openLight() {
        switch (ifOpenLight % 2) {
            case 0:
                // 关闭
                flash_img.setSelected(false);
                mScannerView.toggleLight(false);
                break;

            case 1:
                // 打开
                flash_img.setSelected(true);
                mScannerView.toggleLight(true); // 开闪光灯
                break;
            default:
                break;
        }

    }



    private void setdecode() {
        mScannerView = (ScannerView) findViewById(R.id.capture_preview);
        mScannerView.setOnScannerCompletionListener(this);


        int laserMode = 0;
        int scanMode = 0;

        mScannerView.setMediaResId(R.raw.beep);//设置扫描成功的声音

       // mScannerView.setLaserFrameBoundColor(R.color.base_color);
        mScannerView.setLaserFrameSize(240,240);
        if (scanMode == 1) {
            //二维码
            mScannerView.setScanMode(Scanner.ScanMode.QR_CODE_MODE);
        } else if (scanMode == 2) {
            //一维码
            mScannerView.setScanMode(Scanner.ScanMode.PRODUCT_MODE);
        }
        //显示扫描成功后的缩略图
        mScannerView.isShowResThumbnail(true);
        //全屏识别
        mScannerView.isScanFullScreen(false);
        //隐藏扫描框
        mScannerView.isHideLaserFrame(false);
//        mScannerView.isScanInvert(true);//扫描反色二维码
//        mScannerView.setCameraFacing(CameraFacing.FRONT);
//        mScannerView.setLaserMoveSpeed(1);//速度

        mScannerView.setLaserFrameTopMargin(120);//扫描框与屏幕上方距离
//        mScannerView.setLaserFrameSize(400, 400);//扫描框大小
        mScannerView.setLaserFrameCornerLength(25);//设置4角长度
//        mScannerView.setLaserLineHeight(5);//设置扫描线高度
//        mScannerView.setLaserFrameCornerWidth(5);

        mScannerView.setLaserLineResId(R.drawable.scan_line);//线图
    }

    private void initdata() {



    }

    private void restartPreviewAfterDelay(long delayMS) {
        mScannerView.restartPreviewAfterDelay(delayMS);
        resetStatusView();
    }
    private Result mLastResult;
    private void resetStatusView() {
        mLastResult = null;
    }
    @Override
    protected void onResume() {
        mScannerView.onResume();
        resetStatusView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mLastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        if (rawResult ==null){
            showErrorQR("无效的二维码");
            return;
        }

        String result = rawResult.getText();
        Log.d(TAG, result);
        try {
            String[] okString = Common.getSubString(result, ",");
            if (okString != null && okString.length == 3) {
                if (!okString[0].equals("KLCXKJ-Water")) {
                    showErrorQR(getString(R.string.error_qr));

                    return;
                }
                Intent intent =new Intent(CaptureActivity.this,MainActivity.class);
                if (!okString[2].contains(":")) {
                    intent.putExtra("deciveMac", Common.getMacMode(okString[2]));

                } else {
                    intent.putExtra("deciveMac", okString[2]);
                }



                startActivity(intent);
                finish();



            } else {
                showErrorQR(getString(R.string.error_qr));

            }

        } catch (Exception e) {
            showErrorQR(getString(R.string.error_qr));

        }
    }



    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();

    }






    private void showErrorQR(String string) {
        Toast.makeText(CaptureActivity.this, string,Toast.LENGTH_SHORT);
        restartPreviewAfterDelay(1000);
    }



}