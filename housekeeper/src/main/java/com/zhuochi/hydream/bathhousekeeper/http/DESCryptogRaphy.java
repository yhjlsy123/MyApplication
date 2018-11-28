package com.zhuochi.hydream.bathhousekeeper.http;


import android.annotation.TargetApi;

import com.zhuochi.hydream.bathhousekeeper.utils.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * des加密  解密
 * SHA加密  解密
 * Created by 唯暮 on 2018/4/19.
 */

public class DESCryptogRaphy {
    private static String secretKey = "5yoOxt9w";
    // 向量
    private final static String iv = "11111111";
    private final static String encoding = "utf-8";


    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) {

        Key deskey = null;
        byte[] encryptData = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            deskey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

            encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.encode(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Key deskey = null;

        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
        deskey = keyfactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }

    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    /**
     * 字符串转换unicode
     */
    String code="下雨了！；。wxn;,.";
    public static String string2Unicode(String codeResult) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(codeResult);
        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < codeResult.length(); i++) {

            // 取出每一个字符
            char c = codeResult.charAt(i);
            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(String.valueOf(c));
            //判断是否为  中文
            if (m.matches()) {
                // 将汉字转换为unicode
                unicode.append("\\u" + Integer.toHexString(c));
                //判断是否为中文标点符号
            } else if (isChinesePunctuation(c)) {
                // 将中文标点符号转换为unicode
                unicode.append("\\u" + Integer.toHexString(c));
            } else {
                unicode.append(c);
            }

        }

        return unicode.toString();
    }

    // 根据UnicodeBlock方法判断中文标点符号  || ub == Character.UnicodeBlock.VERTICAL_FORMS
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub =  Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
               ) {
            return true;
        } else {
            return false;
        }
    }
}
