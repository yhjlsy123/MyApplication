package com.zhuochi.hydream.bathhousekeeper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.ModifyPhotoDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.RegisterEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.PhotoUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;
import com.zhuochi.hydream.bathhousekeeper.view.RoundedImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*个人信息*/
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.user_img)
    RoundedImageView userImg;
    @BindView(R.id.user_info_account)
    TextView userInfoAccount;
    @BindView(R.id.user_info_name)
    TextView userInfoName;
    @BindView(R.id.user_info_phone)
    TextView userInfoPhone;
    @BindView(R.id.user_info_company)
    TextView userInfoCompany;
    @BindView(R.id.user_info_company_phone)
    TextView userInfoCompanyPhone;
    @BindView(R.id.tv_updateImg)
    TextView tvUpdateImg;
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbarMenuTv;
    @BindView(R.id.user_img_edit)
    ImageView userImgEdit;
    @BindView(R.id.user_info_button)
    Button userInfoButton;
    @BindView(R.id.activity_user_info)
    LinearLayout activityUserInfo;
    private XiRequestParams params;
    private String mKey = "";// 七牛key
    private String mToken;// 七牛token
    private File mCurrentPhotoFile;
    private Bitmap mImageBitmap;
    private static final int CAMERA_WITH_DATA = 0;// 系统相机
    private static final int PHOTO_PICKED_WITH_DATA = 1;// 系统相册
    private static final int CAMERA_CROP_RESULT = 2;// 系统相机裁剪
    private static final int PHOTO_CROP_RESULT = 3;// 系统相册裁剪
    private static final int ICON_SIZE = 100; // 裁剪尺寸
    private Map<String, String> mMap;
    private boolean isCancelled = false;
    private byte[] b;
    private Uri uritempFile;
    private String userInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
//        highApiEffects();
        params = new XiRequestParams(this);
        initData();

    }

    private void initData() {
        toolbarTitle.setText("个人信息");
        toolbarMenuTv.setVisibility(View.VISIBLE);
        toolbarMenuTv.setText("退出");
        userInfo = getIntent().getStringExtra("entity");
        if (!TextUtils.isEmpty(userInfo)) {
            RegisterEntity entity = JSON.parseObject(userInfo, RegisterEntity.class);
            userInfoAccount.setText(entity.getUser_login());
            userInfoName.setText(entity.getUser_nickname());
            userInfoPhone.setText(entity.getMobile());
            userInfoCompany.setText(entity.getStore().getOrg_name());
            userInfoCompanyPhone.setText(entity.getStore().getOrg_phone());
            ImageLoadUtils.loadImage(UserInfoActivity.this, entity.getAvatar(), R.mipmap.about_logo, userImg);
        }
        mMap = new HashMap();
    }

    @OnClick({R.id.toolbar_back, R.id.tv_updateImg, R.id.toolbar_menu_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.tv_updateImg://修改頭像
                showModifyPhotoDialog();
                break;
            case R.id.toolbar_menu_tv://退出登录
                showDialog();
                break;
        }
    }

    private void showDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
//            normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("确认退出吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO JG 解除极光推送绑定
                        Set<String> set = new HashSet<String>();
//                        int str=UserUtils.getInstance(this).getOrgAreaID();
//
//                        set.add("orgArea_" + str);//绑定 校区（Tag）
//                        JPushInterface.deleteTags(SettingActivity.this, 0, set);
//                        JPushInterface.deleteAlias(SettingActivity.this, 0);
                        UserUtils.setDataNull();

                        SPUtils.saveString("nickName", "");
//                        SPUtils.saveInt(Constants.ORG_ID, 0);//学校ID
//                        SPUtils.saveInt(Constants.ORG_AREA_ID, 0);//校区ID
//                        SPUtils.saveInt(Constants.BUILDING_ID, 0);//楼层ID
//                        SPUtils.saveInt(Constants.DEVICE_AREA_ID, 0);//当前绑定区域（浴室）
                        if (SPUtils.celerData()) {

                            startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                            finish();
                        }

                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {

            case "getUploadFileToken":
                mKey = ((LinkedTreeMap) result.getData().getData()).get("uploadHost").toString();
                mToken = ((LinkedTreeMap) result.getData().getData()).get("token").toString();
                upload();
                break;
            case "setAvatar":
                ToastUtils.show(result.getData().getMsg());
                break;
        }

    }

    /**
     * 上传图片
     */
    private void upload() {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(b, PhotoUtils.getRandomFileName() + ".jpg", mToken, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    String url = (String) response.get("key");
                    mMap.put("photo", mKey + "/" + url);
//                    userinfoPhoto.setImageBitmap(mImageBitmap);
                    ImageLoadUtils.loadImage(UserInfoActivity.this, userImg, mMap.get("photo"));
                    //上传并设置头像
                    params.addCallBack(UserInfoActivity.this);
                    params.setAvatar(UserUtils.getInstance(UserInfoActivity.this).getUserID(), mMap.get("photo"));
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

    /**
     * 获取七牛Token
     *
     * @param data
     */
    private void getQiNiuToken(final byte[] data) {
        b = data;
        params.addCallBack(this);
        params.getUploadFileToken(Common.getAppProcessName(this));
    }

    @Override
    public void onRequestFailure(String tag, Object s) {
        super.onRequestFailure(tag, s);
    }

    /**
     * 弹出修改头像的对话框
     */
    private void showModifyPhotoDialog() {
        ModifyPhotoDialog.Builder builder = new ModifyPhotoDialog.Builder(this);
        builder.setCameraButton("拍照", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 拍照获取
                doTakePhoto();
            }

        });
        builder.setAlbumButton("从相册选择", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 从相册获取
                doPickPhotoFromGallery();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            switch (requestCode) {
                case CAMERA_WITH_DATA:
                    // 相机拍照后裁剪图片
                    doCropPhoto(mCurrentPhotoFile);
                    break;
                case PHOTO_PICKED_WITH_DATA:
//                    startPhotoZoom(data.getData());
                    startPhotoZoom(data.getData(), 400);
                    break;
                case CAMERA_CROP_RESULT:
//                    userImg.setImageResource(R.mipmap.personal_photo_loading);
//                    mImageBitmap = data.getParcelableExtra("data");
//                    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    getQiNiuToken(Common.Bitmap2Bytes(mImageBitmap));
                    try {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            getQiNiuToken(Common.Bitmap2Bytes(bitmap));
                        } else {
                            mImageBitmap = data.getParcelableExtra("data");
                            mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            getQiNiuToken(Common.Bitmap2Bytes(mImageBitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PHOTO_CROP_RESULT:
//                    userImg.setImageResource(R.mipmap.personal_photo_loading);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        getQiNiuToken(Common.Bitmap2Bytes(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 相机剪切图片
     */
    protected void doCropPhoto(File f) {
        try {
            MediaScannerConnection.scanFile(this, new String[]{}, new String[]{null}, null);
            final Intent intent = getCropImageIntent(Uri.fromFile(f));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
                intent.setDataAndType(Uri.fromFile(f), "image/*");
                // crop为true是设置在开启的intent中设置显示的view可以剪裁
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                // outputX,outputY 是剪裁图片的宽高
                intent.putExtra("outputX", 500);
                intent.putExtra("outputY", 500);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            }
            startActivityForResult(intent, CAMERA_CROP_RESULT);
        } catch (Exception e) {
            ToastUtils.show("手机中无可用的图片");
        }
    }

    /**
     * 获取系统剪裁图片的Intent.
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 相册裁剪图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        /*
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);
        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_CROP_RESULT);
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
            final Intent intent = PhotoUtils.getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            ToastUtils.show("手机中无可用的图片或尝试开启拍照权限");
        }
    }

    /**
     * 从相册选择图片
     */
    protected void doPickPhotoFromGallery() {
        try {
            final Intent intent = PhotoUtils.getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            ToastUtils.show("手机中无可用的图片");
        }
    }
}
