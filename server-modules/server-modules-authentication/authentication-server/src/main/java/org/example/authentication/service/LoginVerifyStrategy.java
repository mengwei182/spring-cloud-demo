package org.example.authentication.service;

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
public abstract class LoginVerifyStrategy {
    private static Map<Integer, LoginVerifyStrategy> loginVerifyStrategyMap = new HashMap<>();

    public LoginVerifyStrategy(int type) {
        loginVerifyStrategyMap.put(type, this);
    }

    public static void verify(int type, HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        LoginVerifyStrategy loginVerifyStrategy = loginVerifyStrategyMap.get(type);
        if (loginVerifyStrategy == null) {
            return;
        }
        loginVerifyStrategy.strategy(request, userLoginVO, user);
    }

    public abstract void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user);
}