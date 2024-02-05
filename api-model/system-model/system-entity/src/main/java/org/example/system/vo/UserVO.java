package org.example.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BaseEntity {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
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
    @Email
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
     * 0禁用，1正常
     */
    private Integer status;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 角色ID集合
     */
    private List<String> roleIds;
    /**
     * 菜单信息
     */
    private List<MenuVO> menus;
    /**
     * 角色信息
     */
    private List<RoleVO> roles;
    /**
     * 资源信息
     */
    private List<ResourceVO> resources;
    /**
     * 部门信息
     */
    private List<DepartmentVO> departments;
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