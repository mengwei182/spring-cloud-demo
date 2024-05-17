package org.example.common.core.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author lihui
 * @since 2023/8/4
 */
@Slf4j
public class RSAEncryptUtils {
    private static final ThreadLocal<String> PUBLIC_KEY_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<String> PRIVATE_KEY_THREAD_LOCAL = new ThreadLocal<>();

    private RSAEncryptUtils() {
    }

    /**
     * 生成一个公私钥队
     *
     * @return 0位置为公钥，1位置为私钥
     * @throws NoSuchAlgorithmException
     */
    public static String[] generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString((privateKey.getEncoded()));
        PUBLIC_KEY_THREAD_LOCAL.set(publicKeyString);
        PRIVATE_KEY_THREAD_LOCAL.set(privateKeyString);
        return new String[]{publicKeyString, privateKeyString};
    }

    /**
     * 获取公钥，同一线程多次调用返回的密钥值相同
     *
     * @return Base64加密后的公钥字符串
     * @throws NoSuchAlgorithmException
     */
    public static String getPublicKey() throws NoSuchAlgorithmException {
        String publicKeyString = PUBLIC_KEY_THREAD_LOCAL.get();
        if (publicKeyString == null) {
            generateKeyPair();
        }
        return PUBLIC_KEY_THREAD_LOCAL.get();
    }

    /**
     * 获取私钥，同一线程多次调用返回的密钥值相同
     *
     * @return Base64加密后的私钥字符串
     * @throws NoSuchAlgorithmException
     */
    public static String getPrivateKey() throws NoSuchAlgorithmException {
        String privateKeyString = PRIVATE_KEY_THREAD_LOCAL.get();
        if (privateKeyString == null) {
            generateKeyPair();
        }
        return PRIVATE_KEY_THREAD_LOCAL.get();
    }

    /**
     * RSA公钥加密
     *
     * @param string 加密字符串
     * @param publicKey 公钥
     * @return 加密后的密文
     */
    public static String encrypt(String string, String publicKey) {
        try {
            byte[] decode = Base64.getDecoder().decode(publicKey.getBytes());
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decode));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * RSA私钥解密
     *
     * @param string 加密字符串
     * @param privateKey 私钥
     * @return 解密的明文
     */
    public static String decrypt(String string, String privateKey) {
        try {
            byte[] decode = Base64.getDecoder().decode(privateKey.getBytes());
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decode));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string.getBytes())));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}