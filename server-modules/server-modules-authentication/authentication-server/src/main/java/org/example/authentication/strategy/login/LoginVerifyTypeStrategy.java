package org.example.authentication.strategy.login;

import lombok.Data;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Data
public abstract class LoginVerifyTypeStrategy {
    private static Map<Integer, LoginVerifyTypeStrategy> loginVerifyStrategyMap = new HashMap<>();

    public LoginVerifyTypeStrategy(int type) {
        loginVerifyStrategyMap.put(type, this);
    }

    public static void verify(int type, UserLoginVO userLoginVO, User user) {
        LoginVerifyTypeStrategy loginVerifyTypeStrategy = loginVerifyStrategyMap.get(type);
        if (loginVerifyTypeStrategy == null) {
            return;
        }
        loginVerifyTypeStrategy.strategy(userLoginVO, user);
    }

    /**
     * 登录策略，由具体类型实现
     *
     * @param userLoginVO 用户登录信息实体
     * @param objects 其他需要的参数
     */
    public abstract void strategy(UserLoginVO userLoginVO, Object... objects);
}