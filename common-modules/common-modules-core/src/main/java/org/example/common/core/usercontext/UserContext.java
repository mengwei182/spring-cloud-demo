package org.example.common.core.usercontext;

import lombok.Data;
import org.example.common.core.domain.LoginUser;

import java.io.Serializable;

/**
 * 用户上下文，每个线程都有自己的一份实例
 *
 * @author lihui
 * @since 2022/10/26
 */
@Data
public final class UserContext implements Serializable {
    private static final ThreadLocal<LoginUser> USER_CONTEXTS = new ThreadLocal<>();

    private UserContext() {
    }

    public static LoginUser get() {
        return USER_CONTEXTS.get();
    }

    public static void set(LoginUser loginUser) {
        USER_CONTEXTS.set(loginUser);
    }

    public static void remove() {
        USER_CONTEXTS.remove();
    }
}