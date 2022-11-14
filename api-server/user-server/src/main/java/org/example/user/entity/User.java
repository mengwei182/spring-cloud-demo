package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private String username;
    private String password;
    private String icon;
    private String phone;
    private String email;
    private String name;
    private String description;
    private Date loginTime;
    // 0.禁用，1.正常，2.需要手机验证码，3.需要图形验证码，4.需要手机验证码和图像验证码
    private Integer status;
}