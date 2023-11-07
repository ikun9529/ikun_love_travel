package com.ikun.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private T data; //数据
    private String message; //错误信息

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.message="ok";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        result.message="ok";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.message = msg;
        result.code = 0;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result result = new Result();
        result.message = msg;
        result.code = code;
        return result;
    }

}