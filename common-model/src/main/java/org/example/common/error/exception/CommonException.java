package org.example.common.error.exception;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
 */
public class CommonException extends RuntimeException implements Serializable {
    public CommonException(String message) {
        super(message);
    }
}