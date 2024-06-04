package org.example.system.exception;

import org.example.common.core.exception.ServerException;

/**
 * @author lihui
 * @since 2024/1/23
 */
public class SystemException extends ServerException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(Integer code, String message) {
        super(code, message);
    }

    public SystemException(Integer code, String message, Object data) {
        super(code, message, data);
    }
}