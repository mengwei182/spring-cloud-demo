package org.example.common.entity.base.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
public class TokenVo<T> {
    private Object id;
    // 签名时间
    private Date signTime;
    // 过期时间，单位秒
    private Long expiration;
    // token数据
    private T data;

    public TokenVo() {
    }

    public TokenVo(Object id, Date signTime, Long expiration, T data) {
        this.id = id;
        this.signTime = signTime;
        this.expiration = expiration;
        this.data = data;
    }
}