package com.ikun.exception;


/**
 * 自定义异常 业务异常
 */
public class BussinessException extends RuntimeException {
    private final Integer code;
    private final String description;

    public BussinessException(Integer code, String description) {
        this.code = code;
        this.description = description;
    }


    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
