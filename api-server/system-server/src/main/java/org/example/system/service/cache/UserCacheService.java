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
     * @param captcha
     * @param timeout
     */
    void setPhoneCaptcha(String phone, String captcha, Long timeout);

    /**
     * 从redis获取手机验证码
     *
     * @param phone
     * @return
     */
    String getPhoneCaptcha(String phone);

    /**
     * 从redis删除手机验证码
     *
     * @param phone
     */
    void deletePhoneCaptcha(String phone);

    /**
     * 设置图片验证码到redis
     *
     * @param account
     * @param captcha
     * @param timeout
     */
    void setImageCaptcha(String account, String captcha, Long timeout);

    /**
     * 从redis获取图片验证码
     *
     * @param account
     */
    String getImageCaptcha(String account);

    /**
     * 从redis删除图片验证码
     *
     * @param account
     */
    void deleteImageCaptcha(String account);
}