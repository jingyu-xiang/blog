package com.jxiang.blog.vo.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

  private boolean success;

  private int code;

  private String msg;

  private Object data;

  public static Result success(final Object data) {
    return new Result(true, 200, "success", data);
  }

  public static Result failure(final int code, final String msg) {
    return new Result(false, code, msg, null);
  }

}
