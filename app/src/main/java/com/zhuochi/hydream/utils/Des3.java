package com.zhuochi.hydream.utils;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 一卡通密码加密
 * 3DES加密工具类
 */
public class Des3 {
    // 密钥
    private final static String secretKey = "everhigheverhigheverhigh";
    // 向量
    private final static String iv = null;
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText)  {
        Key deskey = null;
        byte[] encryptData=null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey,ips);

           encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.encode(encryptData);
        }catch (Exception e){
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
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }
}

