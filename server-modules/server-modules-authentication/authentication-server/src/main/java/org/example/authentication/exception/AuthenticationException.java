package org.example.authentication.exception;

import org.example.common.core.exception.ServerException;

/**
 * @author lihui
 * @since 2024/5/21
 */
public class AuthenticationException extends ServerException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Integer code, String message) {
        super(code, message);
    }

    public AuthenticationException(Integer code, String message, Object data) {
        super(code, message, data);
    }
}