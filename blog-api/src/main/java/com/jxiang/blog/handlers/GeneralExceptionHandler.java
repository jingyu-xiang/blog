package com.jxiang.blog.handlers;

import com.jxiang.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // AOP for all controller methods
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class) // handle all exceptions occurred during controller methods' execution
    @ResponseBody // return JSON response
    public Result handleException(Exception ex) {
        ex.printStackTrace();
        return Result.failure(500, "System Error");
    }

}
