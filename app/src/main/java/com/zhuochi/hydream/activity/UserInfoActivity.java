package com.zhuochi.hydream.activity;

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
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhuochi.hydream.R;
import com.zhuochi.hydream.base.BaseAutoActivity;
import com.zhuochi.hydream.dialog.ModifyPhotoDialog;
import com.zhuochi.hydream.entity.SonBaseEntity;
import com.zhuochi.hydream.entity.UserInfoEntity;
import com.zhuochi.hydream.entity.UserUpdateEntity;
import com.zhuochi.hydream.http.GsonUtils;
import com.zhuochi.hydream.http.XiRequestParams;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.ImageLoadUtils;
import com.zhuochi.hydream.utils.PhotoUtils;
import com.zhuochi.hydream.utils.ToastUtils;
import com.zhuochi.hydream.utils.UserUtils;
import com.zhuochi.hydream.view.RoundedImageView;
import com.zhuochi.hydream.view.pickerView.TimePickerView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 个人信息界面
 *
 * @author Cuixc
 * @date on  2018/5/31
 */

public class UserInfoActivity extends BaseAutoActivity {

    @BindView(R.id.userinfo_cancle)
    ImageView userinfoCancle;
    @BindView(R.id.userinfo_photo)
    RoundedImageView userinfoPhoto;
    @BindView(R.id.info_authe)
    ImageView infoAuthe;
    @BindView(R.id.line)
    RelativeLayout line;
    @BindView(R.id.fix_user_photo)
    TextView fixUserPhoto;
    @BindView(R.id.p_fix_user_photo)
    RelativeLayout pFixUserPhoto;
    @BindView(R.id.man_Button)
    RadioButton manButton;
    @BindView(R.id.woman_Button)
    RadioButton womanButton;
    @BindView(R.id.sex_radiogroup)
    RadioGroup sexRadiogroup;
    @BindView(R.id.line_sex)
    LinearLayout lineSex;
    @BindView(R.id.userinfo_age)
    TextView userinfoAge;
    @BindView(R.id.p_userinfo_numberid)
    LinearLayout pUserinfoNumberid;
    @BindView(R.id.userinfo_save)
    Button userinfoSave;
    @BindView(R.id.userinfo_nikename)
    EditText userinfoNikename;
    @BindView(R.id.userinfo_numberid)
    EditText userinfoNumberid;
    @BindView(R.id.dorm_number)
    TextView dormNumber;
    @BindView(R.id.Academic_year)
    TextView AcademicYear;
//    private String ACADEMICYEAR;

    private String DORM;
    private String YEAR;

    private int sex;
    //定义时间选择器
    private TimePickerView pvTime;
    private XiRequestParams params;
    private UserUpdateEntity entity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        intiTextView();
        params = new XiRequestParams(this);
        InitView();

    }

    public void intiTextView() {
//        tvMarquee = (Util) findViewById(R.id.tv_marquee);
//        tvMarquee.run();
//        tvMarquee.setSingleLine(true);//设置单行显示
//        tvMarquee.setHorizontallyScrolling(true);//设置水平滚动效果
//        tvMarquee.setMarqueeRepeatLimit(-1);//设置滚动次数，-1为无限滚动，1
    }

    private void InitView() {//性别;0:保密,1:男,2:女
//        verticalTextview.startFor0();
        mMap = new HashMap();
        String userInfo = getIntent().getStringExtra("TokenEntity");
        UserInfoEntity entity = new Gson().fromJson(userInfo, UserInfoEntity.class);
        try {
            if (!entity.getAvatar().isEmpty()) {
                ImageLoadUtils.loadImage(this, userinfoPhoto, entity.getAvatar());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        userinfoNikename.setText(entity.getUser_nickname());
        userinfoNikename.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(30)});
        userinfoNumberid.setText(entity.getSignature());
        userinfoNumberid.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(100)});
        userinfoAge.setText(entity.getBirthday());
        if (entity.getSex() == 1) {
            manButton.setChecked(true);
            sex = 1;
        } else if (entity.getSex() == 2) {
            womanButton.setChecked(true);
            sex = 2;
        }
        if (entity.getGrade_current() == 1) {//年级
            AcademicYear.setText("1年级");
            YEAR = "1";
        } else if (entity.getGrade_current() == 2) {//年级
            AcademicYear.setText("2年级");
            YEAR = "2";
        } else if (entity.getGrade_current() == 3) {//年级
            AcademicYear.setText("3年级");
            YEAR = "3";
        } else if (entity.getGrade_current() == 4) {//年级
            AcademicYear.setText("4年级");
            YEAR = "4";
        } else if (entity.getGrade_current() == 5) {//年级
            AcademicYear.setText("5年级");
            YEAR = "5";
        }
        if (entity.getGrade_length() == 3) {//学制
            dormNumber.setText("3年");
            DORM = "3";
        } else if (entity.getGrade_length() == 4) {
            dormNumber.setText("4年");
            DORM = "4";
        } else if (entity.getGrade_length() == 5) {
            dormNumber.setText("5年");
            DORM = "5";
        }
//        sexRadiogroup.setClickable(false);
        sexRadiogroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("性别不能修改！");
            }
        });
        disableRadioGroup(sexRadiogroup);
//        sexRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (R.id.man_Button == checkedId) {
//                    sex = 1;
//                } else if (R.id.woman_Button == checkedId) {
//                    sex = 2;
//                }
//            }
//        });

        initPopBirthday();
    }

    /**
     * 设置该Radiogroup不能点击
     * @param testRadioGroup
     */
    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * 设置Radiogroup可点击
     * @param testRadioGroup
     */
    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }

    InputFilter inputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                //                    Toast.makeText(MainActivity.this,"不支持输入表情", 0).show();
                ToastUtils.show("不支持输入表情等特殊字符");
                return "";
            }
            return null;
        }
    };

    @OnClick({R.id.userinfo_cancle, R.id.userinfo_save, R.id.fix_user_photo, R.id.userinfo_age, R.id.Academic_year, R.id.dorm_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_cancle:
                finish();
                break;
            case R.id.userinfo_save://确认提交

                String birthday = userinfoAge.getText().toString();
                String userAge = userinfoNumberid.getText().toString();
                String nike = userinfoNikename.getText().toString();
//                String xuezhi = dormNumber.getText().toString();
//                String nianji = AcademicYear.getText().toString();
                params.addCallBack(this);
                params.changBaseInfo(UserUtils.getInstance(this).getMobilePhone(), birthday, sex, userAge, nike, YEAR, DORM);
                break;
            case R.id.fix_user_photo://修改头像
                showModifyPhotoDialog();
                break;
            case R.id.userinfo_age://弹出日期选择器
                pvTime.show();
                break;
            case R.id.Academic_year://年级  12345
                Intent intent = new Intent(this, SelectGradeActivity.class);
                startActivityForResult(intent, 102);
                break;
            case R.id.dorm_number://学制  3/4/5
                Intent intent2 = new Intent(this, MoneyGiveTypeActivity.class);
                startActivityForResult(intent2, 101);
                break;
        }
    }


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "changBaseInfo"://修改个人信息
                ToastUtils.show(result.getData().getMsg());
                Map map = (Map) result.getData().getData();
                try {
                    String gson = GsonUtils.parseMapToJson(map);
                    entity = new Gson().fromJson(gson, UserUpdateEntity.class);
                    Intent intent = getIntent();
                    intent.putExtra("entity", new Gson().toJson(entity));
                    setResult(101, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "getUploadFileToken"://获取  七牛Token
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
     * 初始化出生年月的弹窗
     */
    public void initPopBirthday() {
        final String birthday = userinfoAge.getText().toString();
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        if (TextUtils.isEmpty(birthday)) {
            pvTime.setTime(new Date());
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(birthday);
                pvTime.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setTitle(getString(R.string.personal_birthday));
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                userinfoAge.setText(Common.getTime(date));
            }
        });
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            String name = data.getStringExtra("name");
            DORM = data.getStringExtra("id");
            dormNumber.setText(name);
        }
        if (requestCode == 102 && data != null) {
            String name = data.getStringExtra("name");
            YEAR = data.getStringExtra("id");
            AcademicYear.setText(name);
        }
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
                    userinfoPhoto.setImageResource(R.mipmap.personal_photo_loading);
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
                    userinfoPhoto.setImageResource(R.mipmap.personal_photo_loading);
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
     * 获取七牛Token
     *
     * @param data
     */
    private void getQiNiuToken(final byte[] data) {
        b = data;
        params.addCallBack(this);
        params.getUploadFileToken(Common.getAppProcessName(this));
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
                    ImageLoadUtils.loadImage(UserInfoActivity.this, userinfoPhoto, mMap.get("photo"));
                    //上传并设置头像
                    params.addCallBack(UserInfoActivity.this);
                    params.setAvatar(UserUtils.getInstance(UserInfoActivity.this).getMobilePhone(), mMap.get("photo"));
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
