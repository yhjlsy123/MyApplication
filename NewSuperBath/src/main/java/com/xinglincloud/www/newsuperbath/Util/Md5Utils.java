package com.xinglincloud.www.newsuperbath.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 针对字符串做的md5加密，以及涉及md5操作的工具类
 * @author lenovo
 *
 */
public class Md5Utils {
    /**
     * 返回文件的md5值
     * @param path
     * 		要加密的文件的路径
     * @return
     * 		文件的md5值
     */
    public static String getFileMD5(String path){
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            //获取MD5加密器
            MessageDigest md = MessageDigest.getInstance("md5");
            //类似读取文件
            byte[] bytes = new byte[10240];//一次读取写入10k
            int len = 0;
            while((len = fis.read(bytes))!=-1){//从原目的地读取数据
                //把数据写到md加密器，类比fos.write(bytes, 0, len);
                md.update(bytes, 0, len);
            }
            //读完整个文件数据，并写到md加密器中
            byte[] digest = md.digest();//完成加密，得到md5值，但是是byte类型的。还要做最后的转换
            for (byte b : digest) {//遍历字节，把每个字节拼接起来
                //把每个字节转换成16进制数
                int d = b & 0xff;//只保留后两位数
                String herString = Integer.toHexString(d);//把int类型数据转为16进制字符串表示
                //如果只有一位，则在前面补0.让其也是两位
                if(herString.length()==1){//字节高4位为0
                    herString = "0"+herString;//拼接字符串，拼成两位表示
                }
                sb.append(herString);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 对传递过来的字符串进行md5加密
     * @param str
     * 		待加密的字符串
     * @return
     * 		字符串Md5加密后的结果
     */
    public static String md5(String str){
        StringBuilder sb = new StringBuilder();//字符串容器
        try {
            //获取md5加密器.public static MessageDigest getInstance(String algorithm)返回实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();//把要加密的字符串转换成字节数组
            byte[] digest = md.digest(bytes);//使用指定的 【byte 数组】对摘要进行最后更新，然后完成摘要计算。即完成md5的加密

            for (byte b : digest) {
                //把每个字节转换成16进制数
                int d = b & 0xff;//只保留后两位数
                String herString = Integer.toHexString(d);//把int类型数据转为16进制字符串表示
                //如果只有一位，则在前面补0.让其也是两位
                if(herString.length()==1){//字节高4位为0
                    herString = "0"+herString;//拼接字符串，拼成两位表示
                }
                sb.append(herString);
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }
}