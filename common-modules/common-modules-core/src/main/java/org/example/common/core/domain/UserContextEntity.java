package org.example.common.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author lihui
 * @since 2024/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserContextEntity extends BaseEntity {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 手机号
     */
    private String phone;
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
     * 0禁用，1正常
     */
    private Integer status;
    /**
     * 角色ID集合
     */
    private List<String> roleIds;
    /**
     * 部门ID集合
     */
    private List<String> departmentIds;
    /**
     * 菜单信息
     */
    private List<String> menuIds;
    /**
     * 资源信息
     */
    private List<String> resourceIds;
    /**
     * 其他验证状态
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
     * token过期时间
     */
    private String tokenExpireTime;
}