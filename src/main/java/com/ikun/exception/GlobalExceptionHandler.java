package com.ikun.exception;

import com.ikun.common.constant.Code;
import com.ikun.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BussinessException.class)
    public Result businessExceptionHandler(BussinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return Result.error(e.getCode(), e.getDescription());
    }

    @ExceptionHandler(Exception.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return Result.error(Code.SYSTEM_ERROR, "系统异常");
    }
}
