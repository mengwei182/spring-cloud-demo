package org.example.common.core.configuration;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.common.core.exception.CommonException;
import org.example.common.core.result.CommonResult;
import org.example.common.core.result.CommonServerResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * 全局Controller异常处理器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        printErrorInformation(e, request);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String msg = fieldErrors.stream().findFirst().map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage())).orElse(null);
        return CommonResult.error(msg);
    }

    @ExceptionHandler(CommonException.class)
    public CommonResult<String> handleCommonException(CommonException e, HttpServletRequest request) {
        printErrorInformation(e, request);
        return CommonResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e, HttpServletRequest request) {
        printErrorInformation(e, request);
        return CommonResult.error(CommonServerResult.SYSTEM_ERROR);
    }

    private void printErrorInformation(Exception e, HttpServletRequest request) {
        log.error("异常信息:", e);
        log.error("请求信息:{}", buildRequestInformation(request));
    }

    private String buildRequestInformation(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request == null) {
            return sb.toString();
        }
        sb.append(System.lineSeparator());
        sb.append(request.getRequestURL().toString());
        sb.append(System.lineSeparator());
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null && headerNames.hasMoreElements()) {
            sb.append("request headers:");
            sb.append(System.lineSeparator());
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (StrUtil.isEmpty(headerName)) {
                    sb.append(":");
                } else {
                    sb.append(headerName).append(":");
                }
                String header = request.getHeader(headerName);
                if (StrUtil.isNotEmpty(header)) {
                    sb.append(header);
                }
                sb.append(System.lineSeparator());
            }
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames != null && parameterNames.hasMoreElements()) {
            sb.append("request parameters:");
            sb.append(System.lineSeparator());
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                if (StrUtil.isEmpty(parameterName)) {
                    sb.append(":");
                } else {
                    sb.append(parameterName).append(":");
                }
                String parameter = request.getParameter(parameterName);
                if (StrUtil.isNotEmpty(parameter)) {
                    sb.append(parameter);
                }
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}