package org.example.common.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoVo extends BaseEntity {
    // 用户名
    @NotNull(message = "用户名不能为空")
    private String username;
    // 密码
    @NotNull(message = "密码不能为空")
    private String password;
    // 头像
    private String icon;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 姓名
    private String name;
    // 描述
    private String description;
    // 登录时间
    private Date loginTime;
    // 0禁用，1正常，2需要手机验证码，3需要图形验证码，4需要手机验证码和图像验证码
    private Integer status;
}