package com.xinglincloud.www.newsuperbath.Util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileUtil {
    private static final String TAG = "FileUtil";
    public static boolean saveUserInfo(String json){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"SuperBathInfo.txt"); //context.getFilesDir()帮助我们返回一个目录 /date/date/包名/files   "Info.txt"文件名
            if(!file.exists()){
                LogUtil.i(TAG, "saveUserInfo: 文件结果："+file.exists()+",文件不存在，写入uuid");
                FileOutputStream fos = new FileOutputStream(file);// 创建输出流对象
                fos.write((json).getBytes());// 向文件中写入信息
                fos.close();  //关闭输出流对象
                return true;   //true保存成功
            }else {
                LogUtil.i(TAG, "saveUserInfo: 文件结果："+file.exists()+",文件已存在，不重新写入uuid");
                return false;   //true保存成功
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;//false 保存失败
        }
    }
    /*
     * 获取保存的数据
     */
    public static String getSavedUserInfo(){
        File file = new File(Environment.getExternalStorageDirectory(),"SuperBathInfo.txt");
        FileInputStream fis = null;
        BufferedReader br= null;
        try{
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();//从这个缓存中读取一行的内容
            return str;
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }finally {
            LogUtil.i(TAG, "getSavedUserInfo: 关闭文件流！！");
            try {
                if(!fis.equals(null)){
                    fis.close();
                }
                if(!br.equals(null)){
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
