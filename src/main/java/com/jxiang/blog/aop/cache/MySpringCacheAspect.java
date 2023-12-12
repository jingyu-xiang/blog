package com.jxiang.blog.aop.cache;

import java.lang.reflect.Method;
import java.time.Duration;

import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// aop 定义一个切面， 切面定义切点和通知的关系
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MySpringCacheAspect {

  final private RedisTemplate<String, String> redisTemplate;

  @Pointcut("@annotation(com.jxiang.blog.aop.cache.MySpringCache)") // 切点
  public void pt() {
  }

  @Around("pt()") // 通知（环绕）
  public Object around(final ProceedingJoinPoint pjp) {
    try {
      final Signature signature = pjp.getSignature();
      final String methodName = signature.getName();
      final String className = pjp.getTarget().getClass().getSimpleName();

      final Class<?>[] argTypes = new Class[pjp.getArgs().length];
      final Object[] args = pjp.getArgs();

      // generate a string of arguments
      StringBuilder argsString = new StringBuilder();
      for (int i = 0; i < args.length; i++) {
        if (args[i] != null) {
          argsString.append(JSON.toJSONString(args[i]));
          argTypes[i] = args[i].getClass();
        } else {
          argTypes[i] = null;
        }
      }
      if (!argsString.toString().isEmpty()) {
        // encode argsString
        argsString = new StringBuilder(DigestUtils.md5Hex(argsString.toString()));
      }

      // get annotation info
      final Class<?> pjpClass = signature.getDeclaringType();
      final Method method = pjpClass.getMethod(methodName, argTypes);
      final MySpringCache annotation = method.getAnnotation(MySpringCache.class);
      final long annotatedExpire = annotation.expire();
      final String annotatedName = annotation.name();

      final String redisKey = annotatedName + "::" + className + "::" + methodName + "::" + argsString;
      final String redisValue = redisTemplate.opsForValue().get(redisKey);

      if (!redisValue.isEmpty()) {
        MySpringCacheAspect.log.info("hit cache: {}, {}", className, methodName);
        return JSON.parseObject(redisValue, Result.class);
      }

      final Object result = pjp.proceed();

      redisTemplate.opsForValue()
          .set(redisKey, JSON.toJSONString(result), Duration.ofMillis(annotatedExpire));
      MySpringCacheAspect.log.info("caching... {}, {}", className, methodName);

      return result;
    } catch (final Throwable e) {
      e.printStackTrace();
      return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }
  }

}
