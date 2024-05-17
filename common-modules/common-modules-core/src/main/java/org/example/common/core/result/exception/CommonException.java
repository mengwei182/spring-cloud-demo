package org.example.common.core.result.exception;

import org.example.common.core.result.CommonServerResult;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
public class CommonException extends RuntimeException implements Serializable {
    private final String code;

    public CommonException(String message) {
        super(message);
        this.code = CommonServerResult.COMMON_EXCEPTION_CODE;
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }
}