package org.example.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token<T> implements Serializable {
    /**
     * 过期时间，单位天
     */
    public static final Integer EXPIRATION_DAY = 7;
    private Object id;
    // 签名时间
    private Date signDate;
    // 过期时间，单位秒
    private Date expirationDate;
    // token数据
    private T data;
}