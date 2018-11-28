package com.isgala.xishuashua.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件操作类
 */
public class FileUtils {

    private static final String APP_NAME = "car";
    private static final String LOG_DIR = "log";
    private static final String LOG_NAME = APP_NAME + "_" + LOG_DIR;
    private static final String SKIN_DIR = "skin";
    private static final String SKIN_NAME = APP_NAME + "_" + SKIN_DIR + ".skin";

    /**
     * 图片保存路径
     * ./file/image/
     *
     * @param context
     * @param name    图片名称 要有时间戳，尽量不要用jpg等后缀名 会显示图库里面
     * @return
     */
    public static File getImgFile(Context context, String name){
        return new File(getFileDir(context, "image"), name);
    }

    /**
     * 删除指定文件夹下所有子文件
     * <p>
     * true 删除成功
     * false 删除失败
     */
    public static boolean deleteAll(Context context, String name){
        //删除之前的文件
        File file = new File(getFileDir(context, name));
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (!file1.delete()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 资源包保存路径
     * ./file/skin/
     *
     * @param context
     * @return
     */
    public static File getSkinFile(Context context){
        File skin = new File(getFileDir(context, "skin"), SKIN_NAME);
        createDir(skin);
        return skin;
    }

    /**
     * 更新包下载路径
     * ./download/
     *
     * @param context
     * @return
     */
    public static File getUpdataApk(Context context){
        File apk = new File(getDownloadDir(context), APP_NAME + ".apk");
        createDir(apk);
        return apk;
    }

    /**
     * 日志保存路径
     * ./file/log/
     *
     * @param context
     * @return
     */
    public static String getLogDir(Context context){
        return getFileDir(context, LOG_DIR);
    }

    /**
     * 日志文件名称
     *
     * @param context
     * @return
     */
    public static File getLogFile(Context context){
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".log";
        File file = new File(getLogDir(context), LOG_NAME + "_" + fileName);
        createFile(file);
        return file;
    }

    /**
     * 图库临时图片文件名称
     *
     * @param context
     * @param name
     * @return
     */
    public static File getPhotoTempFile(Context context, String name){
        String timeStamp = String.valueOf(new Date().getTime());
        String path = getCacheDir(context.getApplicationContext(), "photo");
        File file = new File(path, TextUtils.isEmpty(name) ? timeStamp : name + ".jpg");
        createFile(file);
        return file;
    }

    /**
     * 拍照临时图片文件名称
     *
     * @param context
     * @param name
     * @return
     */
    public static File getPhotoSaveFile(Context context, String name){
        String timeStamp = String.valueOf(new Date().getTime());
        String path = getCacheDir(context.getApplicationContext(), "photo");
        File file = new File(path, TextUtils.isEmpty(name) ? timeStamp : name + ".jpg");
        createFile(file);
        return file;
    }

    public static boolean isExternalStorageAvailable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * ../data/packagename/cache
     */
    public static String getCacheDir(Context context, String subDir){
        String cachePath = null;
        File cacheDir;
        if (isExternalStorageAvailable()) {
            cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                cachePath = cacheDir.getPath();
            }
        }
        if (cachePath == null) {
            cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }
        return TextUtils.isEmpty(subDir) ? cachePath : cachePath + File.separator + subDir;
    }

    /**
     * ../data/packagename/file
     */
    public static String getFileDir(Context context, String subDir){
        String filePath = null;
        File fileDir;
        if (isExternalStorageAvailable()) {
            fileDir = context.getExternalCacheDir();
            if (fileDir != null) {
                filePath = fileDir.getParent() + File.separator + "file";
            }
        }
        if (filePath == null) {
            fileDir = context.getFilesDir();
            if (fileDir != null && fileDir.exists()) {
                filePath = fileDir.getPath();
            }
        }
        return TextUtils.isEmpty(subDir) ? filePath : filePath + File.separator + subDir;
    }

    public static String getDownloadDir(Context context){
        //防止6.0以上不让使用非app内部路径
        //if (isExternalStorageAvailable()) {
        //    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        //            .getAbsolutePath() + File.separator + APP_NAME;
        //} else {
        return getFileDir(context, "download");
        //}
    }

    public static void createFile(File file){
        if (file != null && !file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDir(File file){
        if (file != null) {
            file.getParentFile().mkdirs();
        }
    }

    public static void deleteFile(File file){
        if (file != null && file.exists()) {
            if (file.isDirectory()) {// 如果下面还有文件
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i].getAbsolutePath());
                }
            }
            file.delete();
        }
    }

    public static void deleteFile(String file){
        if (!TextUtils.isEmpty(file)) {
            deleteFile(new File(file));
        }
    }

    public static long getFileSize(File f){
        if (f == null || !f.exists()) {
            return 0;
        }
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = formatFileSize((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = formatFileSize((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = formatFileSize((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = formatFileSize((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /* 格式化文件大小 */
    public static String formatFileSize(double d) {
        return new DecimalFormat("0.##").format(d);
    }


}
