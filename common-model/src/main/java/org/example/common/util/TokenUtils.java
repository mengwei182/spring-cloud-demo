package org.example.common.util;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.Token;
import org.springframework.beans.BeanUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * token工具类
 *
 * @author lihui
 * @since 2022/10/25
 */
@Slf4j
public class TokenUtils {
    private static final String TRANSFORMATION = "DES";
    private static final String SALT = "dc,.-!^%";
    private static final String ERROR_TOKEN = "ERROR_TOKEN";
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    static {
        try {
            SecretKey secretKey = generateKey();
            encryptCipher = Cipher.getInstance(TRANSFORMATION);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decryptCipher = Cipher.getInstance(TRANSFORMATION);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private TokenUtils() {
    }

    /**
     * 签名并生成token
     *
     * @param token
     * @return
     */
    public static String sign(Token<?> token) {
        try {
            byte[] bytes = encryptCipher.doFinal(GsonUtils.gson().toJson(token).getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ERROR_TOKEN;
        }
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static Token<?> unsigned(String token) {
        try {
            byte[] bytes = decryptCipher.doFinal(Base64.getDecoder().decode(token.getBytes()));
            return GsonUtils.gson().fromJson(new String(bytes), TypeToken.get(Token.class));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Token<>();
        }
    }

    /**
     * 解析token
     *
     * @param tokenString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Token<T> unsigned(String tokenString, Class<T> clazz) {
        try {
            Token<?> token = unsigned(tokenString);
            Object data = token.getData();
            T t = GsonUtils.gson().fromJson(GsonUtils.gson().toJson(data), clazz);
            Token<T> resultToken = new Token<>();
            BeanUtils.copyProperties(token, resultToken);
            resultToken.setData(t);
            return resultToken;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Token<>();
        }
    }

    public static SecretKey generateKey() {
        return new SecretKeySpec(SALT.getBytes(), TRANSFORMATION);
    }
}