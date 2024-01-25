package org.example.common.result.exception;

import org.example.common.result.CommonServerResult;

/**
 * @author lihui
 * @since 2024/1/23
 */
public class SystemException extends CommonException {
    public SystemException(String message) {
        super(CommonServerResult.SYSTEM_EXCEPTION_CODE, message);
    }
}