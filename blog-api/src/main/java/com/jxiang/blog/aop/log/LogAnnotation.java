package com.jxiang.blog.aop.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,}) // apply on methods
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

  String module() default "";

  String operator() default "";

}
