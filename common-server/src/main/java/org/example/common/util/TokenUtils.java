package org.example.common.util;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.vo.TokenVo;
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
    private static final String SALT = "dc,.-!^%";
    private static final String ERROR_TOKEN = "ERROR_TOKEN";
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    static {
        try {
            SecretKey secretKey = generateKey();
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decryptCipher = Cipher.getInstance("DES");
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
     * @param tokenVo
     * @return
     */
    public static String sign(TokenVo<?> tokenVo) {
        try {
            byte[] bytes = encryptCipher.doFinal(CommonUtils.gson().toJson(tokenVo).getBytes());
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
    public static TokenVo<?> unsigned(String token) {
        try {
            byte[] bytes = decryptCipher.doFinal(Base64.getDecoder().decode(token.getBytes()));
            return CommonUtils.gson().fromJson(new String(bytes), TypeToken.get(TokenVo.class));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new TokenVo<>();
        }
    }

    /**
     * 解析token
     *
     * @param token
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> TokenVo<T> unsigned(String token, Class<T> clazz) {
        try {
            TokenVo<?> tokenVo = unsigned(token);
            Object data = tokenVo.getData();
            T t = CommonUtils.gson().fromJson(CommonUtils.gson().toJson(data), clazz);
            TokenVo<T> resultTokenVo = new TokenVo<>();
            BeanUtils.copyProperties(tokenVo, resultTokenVo);
            resultTokenVo.setData(t);
            return resultTokenVo;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new TokenVo<>();
        }
    }

    public static SecretKey generateKey() {
        return new SecretKeySpec(SALT.getBytes(), "DES");
    }
}