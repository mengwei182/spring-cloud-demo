package org.example.common.usercontext;

import lombok.Data;
import org.example.common.entity.base.vo.UserInfoVo;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Data
public final class UserContext implements Serializable {
    private static final ThreadLocal<UserInfoVo> USER_CONTEXTS = new ThreadLocal<>();

    private UserContext() {
    }

    public static UserInfoVo get() {
        return USER_CONTEXTS.get();
    }

    public static void set(UserInfoVo data) {
        USER_CONTEXTS.set(data);
    }

    public static void remove() {
        USER_CONTEXTS.remove();
    }
}