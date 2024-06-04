package org.example.common.core.exception;

import lombok.Data;

/**
 * 异常类型常量，ExceptionType的code值大小和业务没关联，先到先得靠前的code值
 *
 * @author lihui
 * @since 2024/6/3
 */
public interface ExceptionInformation {
    // 1000开头：非强业务的异常类型，如一些通用校验规则等
    ExceptionType EXCEPTION_1000 = new ExceptionType(1000, "系统错误");
    ExceptionType EXCEPTION_1001 = new ExceptionType(1001, "对象不存在");

    // 2000开头：authentication（权限管理）用异常类型
    ExceptionType AUTHENTICATION_2001 = new ExceptionType(2001, "访问无权限");
    ExceptionType AUTHENTICATION_2002 = new ExceptionType(2002, "访问被禁止");
    ExceptionType AUTHENTICATION_2003 = new ExceptionType(2003, "令牌过期");
    ExceptionType AUTHENTICATION_2004 = new ExceptionType(2004, "令牌刷新失败");
    ExceptionType AUTHENTICATION_2005 = new ExceptionType(2005, "令牌无效");
    ExceptionType AUTHENTICATION_2006 = new ExceptionType(2006, "令牌校验失败");
    ExceptionType AUTHENTICATION_2007 = new ExceptionType(2007, "用户名为空");
    ExceptionType AUTHENTICATION_2008 = new ExceptionType(2008, "密码为空");
    ExceptionType AUTHENTICATION_2009 = new ExceptionType(2009, "登录验证类型错误");
    ExceptionType AUTHENTICATION_2010 = new ExceptionType(2010, "用户已存在");
    ExceptionType AUTHENTICATION_2011 = new ExceptionType(2011, "用户不存在");
    ExceptionType AUTHENTICATION_2012 = new ExceptionType(2012, "密码错误");
    ExceptionType AUTHENTICATION_2013 = new ExceptionType(2013, "原密码错误");
    ExceptionType AUTHENTICATION_2014 = new ExceptionType(2014, "验证码错误");
    ExceptionType AUTHENTICATION_2015 = new ExceptionType(2015, "验证码过期");
    ExceptionType AUTHENTICATION_2016 = new ExceptionType(2016, "令牌超时");

    // 3000开头：system（系统管理）用异常类型
    ExceptionType SYSTEM_3001 = new ExceptionType(3001, "存在同名菜单");
    ExceptionType SYSTEM_3002 = new ExceptionType(3002, "父级对象不存在");
    ExceptionType SYSTEM_3003 = new ExceptionType(3003, "分类不存在");
    ExceptionType SYSTEM_3004 = new ExceptionType(3004, "分类已存在");
    ExceptionType SYSTEM_3005 = new ExceptionType(3005, "资源为空");
    ExceptionType SYSTEM_3006 = new ExceptionType(3006, "分类下存在资源");
    ExceptionType SYSTEM_3007 = new ExceptionType(3007, "资源名称重复");
    ExceptionType SYSTEM_3008 = new ExceptionType(3008, "资源URL不能为空");
    ExceptionType SYSTEM_3009 = new ExceptionType(3009, "资源URL长度不能超过255个字符");
    ExceptionType SYSTEM_3010 = new ExceptionType(3010, "资源分类不能为空");
    ExceptionType SYSTEM_3011 = new ExceptionType(3011, "资源分类主键不能超过32个字符");
    ExceptionType SYSTEM_3012 = new ExceptionType(3012, "存在同名角色");
    ExceptionType SYSTEM_3013 = new ExceptionType(3013, "存在同名字典");
    ExceptionType SYSTEM_3014 = new ExceptionType(3014, "存在相同编码字典");
    ExceptionType SYSTEM_3015 = new ExceptionType(3015, "存在子字典");
    ExceptionType SYSTEM_3016 = new ExceptionType(3016, "角色不能为空");
    ExceptionType SYSTEM_3017 = new ExceptionType(3017, "不允许设定为超级管理员");

    @Data
    class ExceptionType {
        private Integer code;
        private String message;

        public ExceptionType(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}