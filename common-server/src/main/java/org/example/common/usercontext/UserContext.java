package org.example.common.usercontext;

import lombok.Data;
import org.example.common.entity.vo.UserInfoVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Data
public final class UserContext implements Serializable {
    private static final ThreadLocal<UserContext> USER_CONTEXTS = new ThreadLocal<>();
    private static final UserContext EMPTY = new UserContext();
    private static final List<Class<?>> CLASSES = new ArrayList<>();
    private String userId;
    private String username;
    private UserInfoVo userInfoVo;

    private UserContext() {
    }

    public UserContext(String userId, String username, UserInfoVo userInfoVo) {
        this.userId = userId;
        this.username = username;
        this.userInfoVo = userInfoVo;
    }

    public static UserContext get() {
        UserContext userContext = USER_CONTEXTS.get();
        return userContext == null ? EMPTY : userContext;
    }

    public static UserContext create(String userId, String username, UserInfoVo userInfoVo) {
        return new UserContext(userId, username, userInfoVo);
    }

    public static void set(String userId, String username, UserInfoVo userInfoVo) {
        USER_CONTEXTS.set(create(userId, username, userInfoVo));
    }

    public static void remove() {
        USER_CONTEXTS.remove();
    }
}