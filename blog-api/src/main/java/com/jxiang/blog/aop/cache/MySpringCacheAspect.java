package com.jxiang.blog.aop.cache;


import com.alibaba.fastjson.JSON;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import java.lang.reflect.Method;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

// aop 定义一个切面， 切面定义切点和通知的关系
@Aspect
@Component
@Slf4j
public class MySpringCacheAspect {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Pointcut("@annotation(com.jxiang.blog.aop.cache.MySpringCache)") // 切点
  public void pt() {
  }

  @Around("pt()") // 通知（环绕）
  public Object around(ProceedingJoinPoint pjp) {
    try {
      Signature signature = pjp.getSignature();
      String methodName = signature.getName();
      String className = pjp.getTarget().getClass().getSimpleName();

      Class<?>[] argTypes = new Class[pjp.getArgs().length];
      Object[] args = pjp.getArgs();

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
      if (StringUtils.isNotEmpty(argsString.toString())) {
        // encode argsString
        argsString = new StringBuilder(DigestUtils.md5Hex(argsString.toString()));
      }

      // get annotation info
      Class<?> pjpClass = pjp.getSignature().getDeclaringType();
      Method method = pjpClass.getMethod(methodName, argTypes);
      MySpringCache annotation = method.getAnnotation(MySpringCache.class);
      long annotatedExpire = annotation.expire();
      String annotatedName = annotation.name();

      String redisKey = annotatedName + "::" + className + "::" + methodName + "::" + argsString;
      String redisValue = redisTemplate.opsForValue().get(redisKey);

      if (StringUtils.isNotEmpty(redisValue)) {
        log.info("hit cache~~~ {}, {}", className, methodName);
        return JSON.parseObject(redisValue, Result.class);
      }

      Object result = pjp.proceed();

      redisTemplate.opsForValue()
          .set(redisKey, JSON.toJSONString(result), Duration.ofMillis(annotatedExpire));
      log.info("cache~~~ {}, {}", className, methodName);

      return result;
    } catch (Throwable e) {
      e.printStackTrace();
      return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }
  }
}
