package com.jxiang.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice // AOP for all controller methods
@Slf4j
public class GeneralExceptionHandler {

  @ExceptionHandler(Exception.class)
  // handle all exceptions occurred during controller methods' execution
  @ResponseBody // return JSON response
  public Result handleException(final Exception ex) {
    GeneralExceptionHandler.log.error(ex.toString());
    return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
  }

}
