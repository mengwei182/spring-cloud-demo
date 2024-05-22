package org.example.common.core.util;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.common.core.domain.Token;
import org.springframework.beans.BeanUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
            byte[] bytes = encryptCipher.doFinal(JSON.toJSONString(token).getBytes());
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
    public static Token<?> unsigned(String token) throws IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = decryptCipher.doFinal(Base64.getDecoder().decode(token.getBytes()));
        return JSON.parseObject(new String(bytes), Token.class);
    }

    /**
     * 解析token
     *
     * @param tokenString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Token<T> unsigned(String tokenString, Class<T> clazz) throws IllegalBlockSizeException, BadPaddingException {
        Token<?> token = unsigned(tokenString);
        Object data = token.getData();
        T t = JSON.parseObject(JSON.toJSONString(data), clazz);
        Token<T> resultToken = new Token<>();
        BeanUtils.copyProperties(token, resultToken);
        resultToken.setData(t);
        return resultToken;
    }

    private static SecretKey generateKey() {
        return new SecretKeySpec(SALT.getBytes(), TRANSFORMATION);
    }
}