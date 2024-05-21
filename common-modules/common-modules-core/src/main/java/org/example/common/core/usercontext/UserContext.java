package org.example.common.core.usercontext;

import lombok.Data;
import org.example.common.core.domain.UserContextEntity;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Data
public final class UserContext implements Serializable {
    private static final ThreadLocal<UserContextEntity> USER_CONTEXTS = new ThreadLocal<>();

    private UserContext() {
    }

    public static UserContextEntity get() {
        return USER_CONTEXTS.get();
    }

    public static void set(UserContextEntity userVO) {
        USER_CONTEXTS.set(userVO);
    }

    public static void remove() {
        USER_CONTEXTS.remove();
    }
}