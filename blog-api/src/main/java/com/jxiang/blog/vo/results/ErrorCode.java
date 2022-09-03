package com.jxiang.blog.vo.results;

public enum ErrorCode {

    ACCOUNT_EXISTS(400, "this account has already registered"),
    PARAMS_ERROR(400, "invalid parameters"),
    ACCOUNT_PWD_NOT_EXIST(404, "username or password does not exist"),
    NO_PERMISSION(404, "no permission"),
    SESSION_TIME_OUT(90001, "session time out"),
    NO_LOGIN(401, "require log in"),
    TOKEN_INVALID(401, "invalid jwt token "),
    SYSTEM_ERROR(500, "System error"),
    NOT_FOUND(404, "Item not found"),
    FILE_UPLOAD_FAILURE(500, "failed to upload image to Qiniu cloud");

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