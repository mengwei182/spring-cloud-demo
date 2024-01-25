package org.example.common.usercontext;

import lombok.Data;
import org.example.common.entity.system.vo.UserVo;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Data
public final class UserContext implements Serializable {
    private static final ThreadLocal<UserVo> USER_CONTEXTS = new ThreadLocal<>();

    private UserContext() {
    }

    public static UserVo get() {
        return USER_CONTEXTS.get();
    }

    public static void set(UserVo data) {
        USER_CONTEXTS.set(data);
    }

    public static void remove() {
        USER_CONTEXTS.remove();
    }
}