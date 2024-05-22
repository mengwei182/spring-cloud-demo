package org.example.common.core.exception;

import org.example.common.core.result.CommonServerResult;

/**
 * @author lihui
 * @since 2024/5/21
 */
public class AuthenticationException extends CommonException {
    public AuthenticationException(String message) {
        super(CommonServerResult.AUTHENTICATION_EXCEPTION_CODE, message);
    }
}