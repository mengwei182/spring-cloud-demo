package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 用户信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String icon;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 姓名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 状态：0禁用，1正常
     */
    private Integer status;
    /**
     * 其他验证状态，可以组合
     * 0.关闭
     * 1.图片验证码
     * 2.短信验证码
     * 3.公私钥验证
     * 4.人脸验证
     * 5.指纹验证
     * 6.手势验证
     * 7.邮箱验证
     */
    private String verifyStatus;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * token过期时间
     */
    private Date tokenExpireTime;
}