package com.jxiang.blog.vo.results;

public enum ErrorCode {

    PARAMS_ERROR(400, "invalid parameters"),
    ACCOUNT_PWD_NOT_EXIST(404, "username or password does not exist"),
    NO_PERMISSION(404, "no permission"),
    SESSION_TIME_OUT(90001, "session time out"),
    NO_LOGIN(401, "unauthorized"),
    ;

    private int code;

    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}