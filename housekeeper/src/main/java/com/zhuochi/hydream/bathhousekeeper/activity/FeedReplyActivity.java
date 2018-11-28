package com.zhuochi.hydream.bathhousekeeper.activity;


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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.StringUtils;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.adapter.FeedBackTypeAdapter;
import com.zhuochi.hydream.bathhousekeeper.base.BaseAutoActivity;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.dialog.ModifyPhotoDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.FeedbackTypeEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.Common;
import com.zhuochi.hydream.bathhousekeeper.utils.DensityUtil;
import com.zhuochi.hydream.bathhousekeeper.utils.ImageLoadUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.PhotoUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.UserUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuochi.hydream.bathhousekeeper.activity.UserInfoActivity.getCropImageIntent;


/**
 * 意见反馈管理主页面
 * Created by and on 2018/9/12.
 */
public class FeedReplyActivity extends BaseAutoActivity implements TextWatcher {
    @BindView(R.id.feedback_input)
    EditText feedbackInput;
    @BindView(R.id.feedback_tip)
    TextView feedbackTip;

    @BindView(R.id.scrollview_container)
    LinearLayout scrollView_container;
    @BindView(R.id.hor_scrollview)
    HorizontalScrollView scrollView;
    @BindView(R.id.img_feed)
    ImageView imgFeed;
    @BindView(R.id.feedback_commit)
    Button feedbackCommit;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private ArrayList<String> mImgList = new ArrayList<>(Constants.FEED_BACK_PHOTO_NUM + 1);
    private ArrayList<ViewHolder> mImgViewList = new ArrayList<>(Constants.FEED_BACK_PHOTO_NUM + 1);
    private int mImgWidth = 100;
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
    private List list;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_history);
        ButterKnife.bind(this);
        params = new XiRequestParams(this);

        list = new ArrayList();
        mMap = new HashMap();
        toolbarTitle.setText("意见反馈");
        id = getIntent().getStringExtra("id");
        feedbackInput.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(300)});
    }


    private ArrayList<FeedbackTypeEntity> platformLis = new ArrayList<>();


    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        switch (tag) {
            case "FeedBack/sendReply"://提交反馈信息
                if (result.getData().getCode() == 200) {
                    ToastUtils.show(result.getData().getMsg());
                    finish();
                }


                break;
            case "getUploadFileToken"://获取  七牛Token
                mKey = ((LinkedTreeMap) result.getData().getData()).get("uploadHost").toString();
                mToken = ((LinkedTreeMap) result.getData().getData()).get("token").toString();
                upload();
                break;
        }
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

    @Override
    protected void onStart() {
        super.onStart();
    }
    //用户点击选择图片按钮

    private void checkImgFeedVisibility() {
        imgFeed.setVisibility(mImgList.size() >= Constants.FEED_BACK_PHOTO_NUM ? View.GONE : View.VISIBLE);
    }

    private void refreshImageView() {
        scrollView_container.removeAllViews();
        for (int i = 0; i < mImgList.size(); i++) {
            String path = mImgList.get(i);
            ViewHolder holder;
            if (i >= mImgViewList.size()) {
                holder = createImageView(path, i);
            } else {
                holder = mImgViewList.get(i);
                if (holder == null) {
                    mImgViewList.remove(i);
                    holder = createImageView(path, i);
                }
            }
            //            holder.path = path;
            LoadingAnimDialog.dismissLoadingAnimDialog();
            ImageLoadUtils.loadImage(this, path, holder.img);
            scrollView_container.addView(holder.layout);
        }
        scrollView.requestLayout();
        scrollView.invalidate();
    }

    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mImgList.remove((int) v.getTag());
            refreshImageView();
            checkImgFeedVisibility();
        }
    };

    private ViewHolder createImageView(String path, int index) {
        RelativeLayout item = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_feedback_image, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        if (index != 0) {
            layoutParams.leftMargin = DensityUtil.dip2px(this, 5);
        }

        ImageView imageView = (ImageView) item.findViewById(R.id.img_feed_item);
        ImageView imgDelete = (ImageView) item.findViewById(R.id.img_feed_item_delete);
        RelativeLayout.LayoutParams deleteLayoutParams = (RelativeLayout.LayoutParams) imgDelete.getLayoutParams();
        deleteLayoutParams.width = mImgWidth / 5;
        deleteLayoutParams.height = mImgWidth / 5;
        imgDelete.setTag(index);
        imgDelete.setOnClickListener(deleteClickListener);
        item.setLayoutParams(layoutParams);
        //        item.setTag(R.layout.item_feedback_image, index);
        //        item.setTag(R.id.img_feed_item, imageView);
        //        item.setTag(R.id.img_feed_item_delete, imgDelete);
        ViewHolder holder = new ViewHolder();
        //        holder.index = index;
        //        holder.path = path;
        holder.layout = item;
        holder.img = imageView;
        holder.delete = imgDelete;
        mImgViewList.add(holder);
        return holder;
    }

    class ViewHolder {
        //        int index;
        //        String path;
        RelativeLayout layout;
        ImageView img;
        ImageView delete;

        @Override
        public String toString() {
            StringBuilder stringBuffer = new StringBuilder();
            //            int i = path.lastIndexOf("/");
            //            String p = path.substring(i);
            //            stringBuffer.append("[index=").append(index).append("/");
            //            stringBuffer.append("path=").append(p).append("/");
            stringBuffer.append("layout=").append(layout.hashCode()).append("/");
            stringBuffer.append("img=").append(img.hashCode()).append("/");
            stringBuffer.append("delete=").append(delete.hashCode()).append("]\n");
            return stringBuffer.toString();
        }
    }


    private FeedBackTypeAdapter feedBackTypeAdapter;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            switch (requestCode) {
                case CAMERA_WITH_DATA:
                    // 相机拍照后裁剪图片
                    doCropPhoto(mCurrentPhotoFile);
                    break;
                case PHOTO_PICKED_WITH_DATA:
                    startPhotoZoom(data.getData(), 400);
                    break;
                case CAMERA_CROP_RESULT:
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


    @OnClick({R.id.feedback_commit, R.id.img_feed, R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.feedback_commit:
                commit();
                break;
            case R.id.img_feed:
                showModifyPhotoDialog();
                break;
            case R.id.toolbar_back:
                finish();
                break;
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

    /**
     * 提交反馈
     */
    private void commit() {
        String content = feedbackInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show("请填写您的宝贵意见");
            return;
        }
        Map<String, Object> pram = Common.intancePram();
        Map<String, Object> feedbackInfo = new HashMap<String, Object>();
        pram.put("user_id", UserUtils.getInstance(this).getUserID());
        pram.put("suggestion_id", this.id);
        feedbackInfo.put("content", content);
        if (null != mImgList && mImgList.size() > 0) {
            String[] dd = mImgList.toArray(new String[mImgList.size()]);
            if (dd instanceof String[]) {
                feedbackInfo.put("imgs", StringUtils.join(dd, ","));
            }

        } else {
            feedbackInfo.put("imgs", "");
        }

        pram.put("feedbackInfo", feedbackInfo);
        params.addCallBack(this);
        params.comnRequest("FeedBack/sendReply", pram);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        feedbackTip.setText("还可以输入" + (300 - s.length() + "个字符"));
    }

    @Override
    public void afterTextChanged(Editable s) {

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

    private Uri uritempFile;

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

    private void upload() {
        LoadingAnimDialog.showLoadingAnimDialog(this);
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(b, PhotoUtils.getRandomFileName() + ".jpg", mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    String url = (String) response.get("key");
                    mMap.put("photo", mKey + "/" + url);
//                    userinfoPhoto.setImageBitmap(mImageBitmap);
                    list.add(mImageBitmap);
                    //上传并设置头像
//                    params.addCallBack(FeedBackActivity.this);
//                    params.setAvatar(UserUtils.getInstance(FeedBackActivity.this).getMobilePhone(), mMap.get("photo"));
                    mImgList.add(mMap.get("photo"));
                    refreshImageView();
                    checkImgFeedVisibility();
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
     * 裁剪图片
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


}
