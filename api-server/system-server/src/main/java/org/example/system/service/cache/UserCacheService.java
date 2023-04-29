package org.example.system.service.cache;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface UserCacheService {
    /**
     * 设置手机验证码到redis
     *
     * @param phone
     * @param verifyCode
     * @param timeout
     */
    void setPhoneVerifyCode(String phone, String verifyCode, Long timeout);

    /**
     * 从redis获取手机验证码
     *
     * @param phone
     * @return
     */
    String getPhoneVerifyCode(String phone);

    /**
     * 从redis删除手机验证码
     *
     * @param phone
     */
    void deletePhoneVerifyCode(String phone);

    /**
     * 设置图片验证码到redis
     *
     * @param account
     * @param verifyCode
     * @param timeout
     */
    void setImageVerifyCode(String account, String verifyCode, Long timeout);

    /**
     * 从redis获取图片验证码
     *
     * @param account
     */
    String getImageVerifyCode(String account);

    /**
     * 从redis删除图片验证码
     *
     * @param account
     */
    void deleteImageVerifyCode(String account);
}