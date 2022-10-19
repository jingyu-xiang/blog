package com.jxiang.common.vo.result;

public enum ErrorCode {

  ACCOUNT_EXISTS(400, "This account has already registered"),
  PARAMS_ERROR(400, "Invalid parameters"),
  ACCOUNT_PWD_NOT_EXIST(404, "Username or password does not exist"),
  NO_PERMISSION(404, "No permission"),
  SESSION_TIME_OUT(408, "Request time out"),
  NO_LOGIN(401, "Require log in"),
  TOKEN_INVALID(401, "Invalid jwt token "),
  SYSTEM_ERROR(500, "System error"),
  NOT_FOUND(404, "Item not found"),
  FILE_UPLOAD_FAILURE(500, "Failed to upload image to Qiniu cloud"),
  ITEM_ALREADY_EXISTS(409, "Item already exists");

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