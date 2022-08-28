package com.jxiang.blog.aop.log;

import java.lang.annotation.*;

@Target({ElementType.METHOD,}) // apply on methods
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";

}
