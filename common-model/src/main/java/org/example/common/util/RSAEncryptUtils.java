package org.example.common.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author lihui
 * @since 2023/8/4
 */
public class RSAEncryptUtils {
    public static String publicKey;
    public static String privateKey;

    private RSAEncryptUtils() {
    }

    /**
     * 生成RSA加密规则的公私密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void generatePublicPrivateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString((privateKey.getEncoded()));
        RSAEncryptUtils.publicKey = publicKeyString;
        RSAEncryptUtils.privateKey = privateKeyString;
    }

    /**
     * RSA公钥加密
     *
     * @param string 加密字符串
     * @param publicKey 公钥
     * @return 加密后的密文
     * @throws Exception
     */
    public static String encrypt(String string, String publicKey) throws Exception {
        byte[] decode = Base64.getDecoder().decode(publicKey.getBytes());
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decode));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes()));
    }

    /**
     * RSA私钥解密
     *
     * @param string 加密字符串
     * @param privateKey 私钥
     * @return 解密的明文
     * @throws Exception
     */
    public static String decrypt(String string, String privateKey) throws Exception {
        byte[] decode = Base64.getDecoder().decode(privateKey.getBytes());
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decode));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(string.getBytes())));
    }
}