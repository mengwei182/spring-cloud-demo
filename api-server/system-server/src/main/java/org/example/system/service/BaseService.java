package org.example.system.service;

import org.example.common.entity.system.vo.UsernamePasswordVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface BaseService {
    /**
     * 登录
     *
     * @param usernamePasswordVo
     * @return
     */
    String login(UsernamePasswordVo usernamePasswordVo);

    /**
     * 登出
     *
     * @return
     */
    Boolean logout();

    /**
     * 获取图片验证码
     *
     * @param response
     * @throws IOException
     */
    void generateImageVerifyCode(HttpServletResponse response) throws IOException;
}