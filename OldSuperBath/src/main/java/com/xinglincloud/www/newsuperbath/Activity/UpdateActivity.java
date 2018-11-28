package com.xinglincloud.www.newsuperbath.Activity;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import com.xinglincloud.www.newsuperbath.MqttService.MyMqttService;
import com.xinglincloud.www.newsuperbath.R;
import com.xinglincloud.www.newsuperbath.Util.JsonUtil;
import com.xinglincloud.www.newsuperbath.Util.LogUtil;
import com.xinglincloud.www.newsuperbath.Util.Md5Utils;
import com.xinglincloud.www.newsuperbath.Util.RootUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class UpdateActivity extends FullScreenActivity {
    private static final String TAG = "UpdateActivity";
    private Context mContext = UpdateActivity.this;
    private String updateUrl = "default";
    private String md5 = "default";
    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SuperBathUpdate";
    private boolean isTrue = false;
    private boolean downloadState = false;
    private TextView tvPre;
    private SmdtManager smdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        smdt = SmdtManager.create(this);

        SharedPreferences sp = getSharedPreferences("update", MODE_PRIVATE);
        updateUrl = sp.getString("path", "default");
        md5 = sp.getString("md5", "default");

        LogUtil.i(TAG, "onCreate: 下载的路径为：" + updateUrl);
        isTrue = sp.getBoolean("updateState", false);
        initView();
        tvPre = findViewById(R.id.tv_pre);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                finish();
                LogUtil.i(TAG, "handleMessage: guanbi   guanbi");
            } else {
                tvPre.setText(msg.what + "%");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!updateUrl.equals("default") && httpVerify(updateUrl)) {
            if (!isTrue) {
                LogUtil.i(TAG, "onResume: 校验通过，开始准备下载和安装");
                SharedPreferences.Editor editor = getSharedPreferences("update", MODE_PRIVATE).edit();
                editor.putBoolean("updateState", true);
                editor.apply();

                if (RootUtils.isRooted()) {
                    LogUtil.i(TAG, "onResume: 获取权限成功");
                    installFromNet();
                } else {
                    LogUtil.e(TAG, "onResume: 没有获取到ROOT权限");
                }
            }
        } else {
            //路径有误
            LogUtil.i(TAG, "onResume: 路径有误");
            MyMqttService.publishMessage("dev_update_response", JsonUtil.UpdateRsponseString("3"));
            finish();
        }
    }

    private boolean httpVerify(String Url) {
        return true;
    }

    /**
     * 从网络中下载并安装
     */
    public void installFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                install();
            }
        }).start();
    }

    /**
     * 从Url中下载  下载至 Environment.getExternalStorageDirectory() 目录的update.apk文件 并安装
     */
    private void install() {
        if (silenceInstall()) {
            LogUtil.i(TAG, "install: 安装成功");
        } else {
            handler.sendEmptyMessage(-1);
        }
    }


    /**
     * 传统安装
     */
    public void normalInstall(Context context) {
        File file = downLoadFile(updateUrl, rootPath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
//        startApp(context);
    }

    /**
     * 静默安装 并启动
     */
    public boolean silenceInstall() {
        // installFromNet() 从网络获取文件下载安装
        File file = downLoadFile(updateUrl, rootPath);
        LogUtil.i(TAG, "silenceInstall: 文件路径为："+rootPath+"/update.apk");
        LogUtil.d(TAG, "silenceInstall: 下载完成后，进行MD5校验后安装！");
        if (!Md5Utils.getFileMD5(rootPath+"/update.apk").equals(md5)) {
            LogUtil.i(TAG, "silenceInstall: 安装失败，MD5校验未通过");
            return false;
        }
        boolean result = false;
        Process process;
        OutputStream out;
        LogUtil.i(TAG, "file.getPath()：" + file.getPath());
        if (file.exists()) {
            System.out.println(file.getPath() + "==");
            try {
                 smdt.smdtSilentInstall(rootPath+"/update.apk", getApplicationContext());
                 result = true;
            } catch (Exception e) {
                e.printStackTrace();
                return  false;
            }
            if (!result) {
                LogUtil.e(TAG, "静默安装失败，");
//                Toast.makeText(this, "升级失败，正在关闭升级系统！", Toast.LENGTH_SHORT).show();
                result = false;
            }
        }else{
            LogUtil.e(TAG, "silenceInstall: 文件不存在" );
        }
        return result;
    }

    /**
     * 下载至 Environment.getExternalStorageDirectory().getPath() + "/update.apk"
     *
     * @param httpUrl
     * @return
     */
    private File downLoadFile(String httpUrl, String filePath) {
        if (TextUtils.isEmpty(httpUrl)) throw new IllegalArgumentException();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            LogUtil.i(TAG, "downLoadFile: 文件存在，删除文件");
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        file = new File(filePath + File.separator + "update.apk");
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://" + httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            int currentLength = connection.getContentLength();
            float completeSize = 0;
            int progress = 0;

            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
                completeSize += len;
                int limit = 0;

                if (completeSize < currentLength) {
                    LogUtil.e("tag", "completeSize=" + completeSize);
                    LogUtil.e("tag", "currentLength=" + currentLength);
                    progress = (int) (Float.parseFloat(getTwoPointFloatStr(completeSize / currentLength)) * 100);
                    LogUtil.e("tag", "下载进度：" + progress);
                    if (limit % 100 == 0 && progress <= 100) {//隔30次更新一次notification
                        //在此更新进度
                        handler.sendEmptyMessage(progress);
                    }
                    limit++;
                }
            }
            downloadState = true;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
        return file;
    }

    /**
     * 格式化数字的
     */
    private String getTwoPointFloatStr(float value) {
        DecimalFormat df = new DecimalFormat("0.00000000000");
        return df.format(value);

    }

    /**
     * 安装后自启动apk
     */
    private static void startApp(Context context) {
        execRootShellCmd("am start -S  " + context.getPackageName() + "/"
                + MainActivity.class.getCanonicalName() + " \n");
    }

    /**
     * 执行shell命令
     */
    private static boolean execRootShellCmd(String... cmds) {
        if (cmds == null || cmds.length == 0) {
            return false;
        }
        DataOutputStream dos = null;
        InputStream dis = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());

            for (int i = 0; i < cmds.length; i++) {
                dos.writeBytes(cmds[i] + " \n");
            }
            dos.writeBytes("exit \n");

            int code = p.waitFor();

            return code == 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (p != null) {
                    p.destroy();
                    p = null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return false;
    }


    private void initView() {
        tvPre = (TextView) findViewById(R.id.tv_pre);
    }
}
