package com.jxiang.blog.aop.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE,})
@Retention(RetentionPolicy.RUNTIME)
public @interface MySpringCache {

  int THREE_MINUTE_IN_MILLISECONDS = 60 * 1000;

  long expire() default THREE_MINUTE_IN_MILLISECONDS;

  String name() default "";
}
