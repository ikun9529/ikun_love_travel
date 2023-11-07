package com.ikun.common.constant;


public interface Code {
    int SUCCESS = 0;
    int ERROR = 1;

    /**
     * 用户未登录
     */
    Integer USER_NOT_LOGIN = 10000;

    /**
     * 请求参数错误
     */
    Integer PARAMS_ERROR = 10001;

    /**
     * 系统异常
     */
    Integer SYSTEM_ERROR = 10002;

    /**
     * 没有权限
     */
    Integer NO_AUTH = 10003;


}

