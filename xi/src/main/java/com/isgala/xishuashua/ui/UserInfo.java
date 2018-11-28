package com.isgala.xishuashua.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isgala.xishuashua.R;
import com.isgala.xishuashua.api.Neturl;
import com.isgala.xishuashua.base.BaseAutoActivity;
import com.isgala.xishuashua.bean_.QiNiuToken;
import com.isgala.xishuashua.bean_.Result;
import com.isgala.xishuashua.bean_.UserInfoEntity;
import com.isgala.xishuashua.config.Constants;
import com.isgala.xishuashua.dialog.LoadingTrAnimDialog;
import com.isgala.xishuashua.dialog.ModifyPhotoDialog;
import com.isgala.xishuashua.dialog.OneCardSolutionPWD;
import com.isgala.xishuashua.utils.Common;
import com.isgala.xishuashua.utils.Des3;
import com.isgala.xishuashua.utils.ImageLoadUtils;
import com.isgala.xishuashua.utils.JsonUtils;
import com.isgala.xishuashua.utils.PhotoUtils;
import com.isgala.xishuashua.utils.SPUtils;
import com.isgala.xishuashua.utils.ToastUtils;
import com.isgala.xishuashua.utils.VolleySingleton;
import com.isgala.xishuashua.view.RoundedImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.isgala.xishuashua.R.id.userinfo_photo;
import static com.isgala.xishuashua.api.Neturl.GET_ONECARDSOLUTIONAUTH;
import static com.isgala.xishuashua.utils.ToastUtils.show;

/**
 * Created by and on 2016/11/8.
 */
public class UserInfo extends BaseAutoActivity {
    private static final int CAMERA_WITH_DATA = 0;// 系统相机
    private static final int PHOTO_PICKED_WITH_DATA = 1;// 系统相册
    private static final int CAMERA_CROP_RESULT = 2;// 系统相机裁剪
    private static final int PHOTO_CROP_RESULT = 3;// 系统相册裁剪
    private static final int ICON_SIZE = 100; // 裁剪尺寸
    private static final int UPDATE_PHONE = 101; // 修改手机号码

    @BindView(R.id.info_authe)
    ImageView infoAuthe;
    private String mKey;// 七牛key
    private String mToken;// 七牛token
    private File mCurrentPhotoFile;
    private Bitmap mImageBitmap;
    private String mUpdateNumber = "";
    private String TAG = "UserInfoEntity";
    @BindView(userinfo_photo)
    RoundedImageView userinfoPhoto;
    @BindView(R.id.userinfo_nikename)
    EditText userinfoNikename;
    @BindView(R.id.userinfo_age)
    TextView userinfoAge;
    @BindView(R.id.userinfo_numberid)
    TextView userinfoNumberid;
    @BindView(R.id.userinfo_phone)
    TextView userinfoPhone;
    @BindView(R.id.personal_gender)
    TextView personal_gender;
    @BindView(R.id.userinfo_school)
    TextView userinfo_school;
    @BindView(R.id.userinfo_campus)
    TextView userinfo_campus;
    @BindView(R.id.userinfo_bathroom)
    TextView userinfo_bathroom;
    @BindView(R.id.p_userinfo_numberid)
    RelativeLayout p_userinfo_numberid;
    @BindView(R.id.line_one_card_solution)
    RelativeLayout line_one_card_solution;
    @BindView(R.id.btn_one_card_solution)
    Button btn_one_card_solution;
    @BindView(R.id.card_number)
    TextView card_number;
    private Intent resultIntent;
    private Map<String, String> map;
    private int showType = 0;//0显示load接口里头像，1不去重新load

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        resultIntent = new Intent();
        map = new HashMap<>();
        iniData();
    }


    /**
     * 先取本地存储的个人信息数据
     */
    private void iniData() {
        LoadingTrAnimDialog.showLoadingAnimDialog(this);
        String userinfo = SPUtils.getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userinfo)) {
            UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(userinfo, UserInfoEntity.class);
            if (userInfoEntity != null && userInfoEntity.data != null) {
                updateView(userInfoEntity.data);
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        }
        update(false);
    }

    /**
     * 修改个人信息
     */
    private void modify() {
        map.put("nickname", getViewText(userinfoNikename));
        map.put("age", getViewText(userinfoAge));
        map.put("student_number", getViewText(userinfoNumberid));
        if (!mUpdateNumber.isEmpty()) {
            map.put("phone", getViewText(userinfoAge));
        }
        VolleySingleton.post(Neturl.MODIFY_USER_INFO, "modify", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                Result result1 = JsonUtils.parseJsonToBean(result, Result.class);
                if (result1 != null && result1.data != null) {
                    ToastUtils.show(result1.data.msg);
                    resultIntent.putExtra("change", true);
                    resultIntent.putExtra("photo", map.get("photo"));
                    resultIntent.putExtra("nikename", map.get("nickname"));
                }
                if (!mUpdateNumber.isEmpty()) {//判断是否修改的手机号
                    update(false);//是的话 就不关闭当前页面
                } else {
                    update(true);//不是 就是在当前页面修改的个人信息
                }
            }
        });
    }

    private String s_id;

    /**
     * 获取最新的个人信息
     *
     * @param finish 是否关闭本activity
     */
    private void update(final boolean finish) {
        VolleySingleton.post(Neturl.USER_INFO, "userinfo", map, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(result, UserInfoEntity.class);
                if (userInfoEntity != null && userInfoEntity.data != null) {
                    SPUtils.saveString(Constants.USER_INFO, result);
                    //判断显示一卡通绑定界面
                    if (userInfoEntity.data.if_allow_one_card_solution == 1) {
                        line_one_card_solution.setVisibility(View.VISIBLE);
                    } else {
                        line_one_card_solution.setVisibility(View.GONE);
                    }
                    if (userInfoEntity.data.is_checked.equals("0")) {
                        btn_one_card_solution.setText("未认证");
                    } else if (userInfoEntity.data.is_checked.equals("1")) {
                        btn_one_card_solution.setVisibility(View.GONE);
                        card_number.setText(userInfoEntity.data.card_no);
                        card_number.setClickable(false);
                    }
                    if (!finish)
                        updateView(userInfoEntity.data);

                }
                if (finish) {
                    close();
                }
                LoadingTrAnimDialog.dismissLoadingAnimDialog();
            }
        });
    }

    public void close() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userinfoPhoto.getWindowToken(), 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo.this.setResult(200, resultIntent);
                finish();
            }
        }, 50);
    }

    private String getViewText(TextView textView) {
        CharSequence text = textView.getText();
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        return text.toString().trim();
    }

    /**
     * 更新数据显示和请求参数
     *
     * @param info
     */
    private void updateView(UserInfoEntity.UserData info) {
        s_id = info.s_id;
        try {
            if (showType == 0) {
                map.put("photo", info.photo);
                ImageLoadUtils.loadImage(this, userinfoPhoto, info.photo);
            }
        } catch (Exception e) {

        }
        if (TextUtils.equals("1", info.auth)) {
            infoAuthe.setVisibility(View.VISIBLE);
            p_userinfo_numberid.setVisibility(View.VISIBLE);
        }
        userinfoNikename.setText(info.nickname);
        userinfoAge.setText(info.age);
        userinfoNumberid.setText(info.student_number);
//        if (TextUtils.equals("1", info.sex)) {
//            personal_gender.setText("男");
//        } else {
//            personal_gender.setText("女");
//        }
        userinfo_school.setText(info.school_name);
        userinfo_campus.setText(info.campus_name);
        userinfo_bathroom.setText(info.bathroom_name);
        userinfoPhone.setText(info.phone);
    }


    @OnClick({R.id.userinfo_save, R.id.userinfo_cancle, R.id.p_fix_user_photo, R.id.p_userinfo_school, R.id.btn_update_number, R.id.btn_one_card_solution, R.id.card_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.p_fix_user_photo:
                showModifyPhotoDialog();
                break;
            case R.id.userinfo_cancle:
                //隐藏软键盘,如果有.
                onBackPressed();
                finish();
                break;
            case R.id.userinfo_save:
                modify();
                break;
            case R.id.p_userinfo_school://修改学校  提示：有欠款或者已经交过押金的不能修改学校
                LoadingTrAnimDialog.showLoadingAnimDialog(this);
                VolleySingleton.post(Neturl.USER_INFO, "userinfo", map, new VolleySingleton.CBack() {
                    @Override
                    public void runUI(String result) {
                        UserInfoEntity userInfoEntity = JsonUtils.parseJsonToBean(result, UserInfoEntity.class);
                        if (userInfoEntity != null && TextUtils.equals("1", userInfoEntity.status)) {
                            SPUtils.saveString(Constants.USER_INFO, result);
                            if (TextUtils.equals("1", userInfoEntity.data.is_update)) {
                                Intent intent = new Intent(UserInfo.this, SchoolList.class);
                                intent.putExtra("from", "UserInfoEntity");
                                startActivity(intent);
                            } else {
                                ToastUtils.show(userInfoEntity.data.is_update_tip);
                            }
                        }
//                        if (TextUtils.equals("0", userInfoEntity.data.is_update)) {
//                            Intent intent = new Intent(UserInfoEntity.this, SchoolList.class);
//                            intent.putExtra("from", "UserInfoEntity");
//                            startActivity(intent);
//                        }
                        LoadingTrAnimDialog.dismissLoadingAnimDialog();

                    }
                });
                break;
            case R.id.btn_update_number://更换手机号
                Intent intent = new Intent(this, UpdatePhoneNumberActivity.class);
                startActivityForResult(intent, UPDATE_PHONE);
                break;
            case R.id.btn_one_card_solution://认证
                String card_no = card_number.getText().toString();
                if (card_no.isEmpty()) {
                    showOneCardInput();
                }
                break;
            case R.id.card_number:
                if (card_number.getText().toString().isEmpty()) {
                    showOneCardInput();
                }
                break;
        }
    }

    private void showOneCardInput() {


        OneCardSolutionPWD.Builder builder = new OneCardSolutionPWD.Builder(this);
        builder.create().show();
        builder.setConfirm(new OneCardSolutionPWD.OnConfirmListener() {
            @Override
            public void confirm(String pwd, String onecard) {
                try {
                    String onecardpwd = Des3.encode(pwd);
                    getoneCardSolutionAuth(onecard, onecardpwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 一卡通认证
     */
    private void getoneCardSolutionAuth(String card_no, String card_pwd) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("card_no", card_no);
        params.addBodyParameter("card_pwd", card_pwd);
        params.addBodyParameter("oauth_token", SPUtils.getString(Constants.OAUTH_TOKEN, ""));
        params.addBodyParameter("oauth_token_secret", SPUtils.getString(Constants.OAUTH_TOKEN_SECRET, ""));
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GET_ONECARDSOLUTIONAUTH, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(com.lidroid.xutils.http.ResponseInfo<String> responseInfo) {
                Result onCard = JsonUtils.parseJsonToBean(responseInfo.result, Result.class);
                if (onCard.data != null) {
                    if (onCard.data.status.equals("0")) {
                        ToastUtils.show(onCard.data.msg);
                    }
                    if (onCard.data.status.equals("1")) {
                        ToastUtils.show(onCard.data.msg);
                        card_number.setText(onCard.data.card_no);
                        btn_one_card_solution.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show("一卡通认证失败，请稍后重试！");
                btn_one_card_solution.setVisibility(View.GONE);
            }
        });
//        Map<String, String> map = new HashMap<>();
//        map.put("card_no", card_no);//一卡通账号（必须）
//        map.put("card_pwd", card_pwd);//一卡通密码（必须）
//        VolleySingleton.post(GET_ONECARDSOLUTIONAUTH, "oneCardSolutionAuth", map, new VolleySingleton.CBack() {
//            @Override
//            public void runUI(String result) {
//                Result onCard = JsonUtils.parseJsonToBean(result, Result.class);
//                if (onCard.data != null) {
//                    if (onCard.data.status.equals("0")){
//                        ToastUtils.show(onCard.data.msg);
//                    }
//                    if (onCard.data.status.equals("1")){
//                        ToastUtils.show(onCard.data.msg);
//                        card_number.setText(onCard.data.card_no);
//                        btn_one_card_solution.setVisibility(View.GONE);
//                    }
//
//                }
//
//            }
//        });
    }

    private static Uri photoURI;

    /**
     * 弹出修改头像的对话框
     */
    private void showModifyPhotoDialog() {
        ModifyPhotoDialog.Builder builder = new ModifyPhotoDialog.Builder(this);
        builder.setCameraButton("拍照", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                if (Common.isAbove22SDKVersion()) {
//                    // 跳转到系统照相机
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    UserInfo.this.setIntent(new Intent());
//                    if (cameraIntent.resolveActivity(UserInfo.this.getPackageManager()) != null) {
//                        // 设置系统相机拍照后的输出路径
//                        // 创建临时文件
//                        mCurrentPhotoFile = createFile(UserInfo.this);
//
//                        try {
//                            photoURI = FileProvider.getUriForFile(UserInfo.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
//                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                            startActivityForResult(cameraIntent, CAMERA_WITH_DATA);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "没有找到摄像头", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
                doTakePhoto();
//                }
            }

        });
        builder.setAlbumButton("从相册选择", new DialogInterface.OnClickListener()

        {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 从相册获取
                doPickPhotoFromGallery();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()

        {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().

                show();

    }

    private static File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String imageFileName = "PIC-" + timestamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        String uploadFilePath = image.getAbsolutePath();
        return image;
    }

    /**
     * 从相册选择图片
     */
    protected void doPickPhotoFromGallery() {
        try {
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            ToastUtils.show("手机中无可用的图片");
        }
    }

    public static File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp);
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp);
        }
        return file;
    }

    /**
     * 获取调用相册的Intent
     */
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            // 启动相机获取图片
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtils.getRandomFileName());
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            ToastUtils.show("手机中无可用的图片或尝试开启拍照权限");
        }
    }

    /**
     * 创建一个intent对象来拍照，并保存在临时文件
     *
     * @param file
     * @return
     */
    private Intent getTakePickIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            showType = 0;
            if (requestCode == CAMERA_CROP_RESULT || requestCode == PHOTO_CROP_RESULT) {
                showType = 1;
            }
            switch (requestCode) {
                case CAMERA_WITH_DATA:
                    // 相机拍照后裁剪图片
//                    if (Common.isAbove22SDKVersion()) {
//
//                        Uri mImageCaptureUri = data.getData();
//                        mImageBitmap = null;
//                        if (mImageCaptureUri != null) {
//                            try{
//                                mImageBitmap = MediaStore.Images.Media.getBitmap(UserInfo.this.getContentResolver(), mImageCaptureUri);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        userinfoPhoto.setImageResource(R.mipmap.personal_photo_loading);
////                        mImageBitmap = data.getParcelableExtra("data");
//                        mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        uploadImageToQiNiu(Bitmap2Bytes(mImageBitmap));
//                    } else {
//                        doCropPhoto(mCurrentPhotoFile);
//                    }
                    doTakePhoto();
//                    startPhotoZoom(data.getData());


                    break;

                case PHOTO_PICKED_WITH_DATA:
                    startPhotoZoom(data.getData());

                    break;

                case CAMERA_CROP_RESULT:
                    userinfoPhoto.setImageResource(R.mipmap.personal_photo_loading);
                    mImageBitmap = data.getParcelableExtra("data");
                    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    uploadImageToQiNiu(Bitmap2Bytes(mImageBitmap));
                    break;

                case PHOTO_CROP_RESULT:
                    userinfoPhoto.setImageResource(R.mipmap.personal_photo_loading);
//                    userinfoPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.personal_photo_loading));
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        mImageBitmap = extras.getParcelable("data");
                        mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        uploadImageToQiNiu(Bitmap2Bytes(mImageBitmap));
                    }
                    break;
                case UPDATE_PHONE://修改手机号码
                    mUpdateNumber = data.getStringExtra("newPhone");
                    modify();
                    break;
            }

        }
    }

    /**
     * 上传图片到七牛云
     *
     * @param data
     */


    private void uploadImageToQiNiu(byte[] data) {
        // 获取七牛token
        getQiNiuToken(data);
    }

    /**
     * 获取七牛Token
     *
     * @param data
     */
    private void getQiNiuToken(final byte[] data) {
        // 七牛Token没有
        VolleySingleton.post(Neturl.QINIU_TOKEN, "getqiniu", null, new VolleySingleton.CBack() {
            @Override
            public void runUI(String result) {
                QiNiuToken mQiNiuToken = JsonUtils.parseJsonToBean(result, QiNiuToken.class);
                mKey = mQiNiuToken.data.domain;
                mToken = mQiNiuToken.data.uptoken;
                upload(data);
            }
        });
    }

    private boolean isCancelled = false;

    /**
     * 上传图片
     *
     * @param data
     */
    private void upload(byte[] data) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(data, PhotoUtils.getRandomFileName() + ".jpg", mToken, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    String url = (String) response.get("key");
                    map.put("photo", mKey + url);
                    userinfoPhoto.setImageBitmap(mImageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {

            }
        }, new UpCancellationSignal() {
            public boolean isCancelled() {
                return isCancelled;
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCancelled = true;
    }

    /**
     * 将bitmap写成字节数组
     *
     * @param bm
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 相机剪切图片
     */
    protected void doCropPhoto(File f) {
        try {
//            MediaScannerConnection.scanFile(this, new String[]{}, new String[]{null}, null);
//            final Intent intent = getCropImageIntent(Uri.fromFile(f));
//            ToastUtils.show(Uri.fromFile(f).toString());
//            startActivityForResult(intent, CAMERA_CROP_RESULT);
            cropPhoto(Uri.fromFile(f));
        } catch (Exception e) {
            show("手机中无可用的图片");
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, CAMERA_CROP_RESULT);
    }


    /**
     * 获取系统剪裁图片的Intent.
     */
    public static Intent getCropImageIntent(Uri photoUris) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(photoUris, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        return intent;
    }

    /**
     * 相册裁剪图片
     *
     * @param photoUri
     */
    private void startPhotoZoom(Uri photoUri) {
        // 调用系统自带的一个图片裁剪页面
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CROP_RESULT);
    }

    public void onResume() {
        super.onResume();
        update(false);
        MobclickAgent.onPageStart("UserInfoEntity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("UserInfoEntity");
        MobclickAgent.onPause(this);
    }
}
