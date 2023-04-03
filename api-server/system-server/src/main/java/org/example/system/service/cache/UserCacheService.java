package org.example.system.service.cache;

public interface UserCacheService {
    void setPhoneVerifyCode(String phone, String verifyCode, Long timeout);

    String getPhoneVerifyCode(String phone);

    void deletePhoneVerifyCode(String phone);

    void setImageVerifyCode(String account, String verifyCode, Long timeout);

    String getImageVerifyCode(String account);

    void deleteImageVerifyCode(String account);
}