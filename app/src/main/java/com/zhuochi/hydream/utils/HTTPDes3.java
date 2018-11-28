package com.zhuochi.hydream.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密工具类
 */
public class HTTPDes3 {
    // 密钥
    private final static String secretKey = "5yoOxt9w";
    // 向量
    private final static String iv = "11111111";
    // 加解密统一使用的编码方式
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
}

