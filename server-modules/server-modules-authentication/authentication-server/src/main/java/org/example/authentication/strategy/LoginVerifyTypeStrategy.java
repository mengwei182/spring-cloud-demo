package org.example.authentication.strategy;

import lombok.Data;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;

import javax.servlet.http.HttpServletRequest;
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

    public static void verify(int type, HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        LoginVerifyTypeStrategy loginVerifyTypeStrategy = loginVerifyStrategyMap.get(type);
        if (loginVerifyTypeStrategy == null) {
            return;
        }
        loginVerifyTypeStrategy.strategy(request, userLoginVO, user);
    }

    public abstract void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user);
}