package com.jxiang.blog.aop.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, })
@Retention(RetentionPolicy.RUNTIME)
public @interface MySpringCache {

  int MINUTE_IN_MILLISECONDS = 60 * 1000;

  long expire() default MySpringCache.MINUTE_IN_MILLISECONDS;

  String name() default "";

}
