package org.example.common.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ServerException extends RuntimeException implements Serializable {
    private final Integer code;
    private Object data;

    public ServerException(String message) {
        super(message);
        this.code = ExceptionInformation.EXCEPTION_1000.getCode();
    }

    public ServerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServerException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }
}