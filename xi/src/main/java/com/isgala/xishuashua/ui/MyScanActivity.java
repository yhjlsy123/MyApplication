package com.isgala.xishuashua.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.klcxkj.zqxy.zxing.zxing.activity.ScanActivity;

/**
 * 大白洗衣机的二维码扫描
 * Intent intent = new Intent(getContext(), MyScanActivity.class);
 * intent.putExtra("capture_type", 5);
 * startActivity(intent);
 */
public class MyScanActivity extends ScanActivity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG).show();
        Intent data = new Intent();
        data.putExtra("rqcode", rawResult.getText());
        setResult(222, data);
        finish();
    }
}
