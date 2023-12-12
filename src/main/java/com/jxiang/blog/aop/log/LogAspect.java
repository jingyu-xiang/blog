package com.jxiang.blog.aop.log;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.util.NetworkUtils;

import lombok.extern.slf4j.Slf4j;

// aop 定义一个切面， 切面定义切点和通知的关系
@Aspect
@Component
@Slf4j
public class LogAspect {

  @Pointcut("@annotation(com.jxiang.blog.aop.log.Log)") // 切点
  public void pt() {
  }

  @Around("pt()") // 通知（环绕）
  public Object logInfo(final ProceedingJoinPoint joinPoint) throws Throwable {
    final long beginTime = System.currentTimeMillis();

    final Object result = joinPoint.proceed();

    final Long time = System.currentTimeMillis() - beginTime;

    recordLog(joinPoint, time);
    return result;
  }

  private void recordLog(final ProceedingJoinPoint joinPoint, final Long time) {
    // retrieve the invoked method properties (annotation in this case)
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Log log = method.getAnnotation(Log.class);

    LogAspect.log.info("=====================log start================================");
    LogAspect.log.info("module:{}", log.module());
    LogAspect.log.info("operation:{}", log.operator());

    // method name
    final String className = joinPoint.getTarget().getClass().getName();
    final String methodName = signature.getName();
    LogAspect.log.info("request method:{}", className + "." + methodName + "()");

    // parameters
    final Object[] args = joinPoint.getArgs();
    final String params = JSON.toJSONString(args[0]);
    LogAspect.log.info("params:{}", params);

    // retrieve request info such as ip address
    LogAspect.log.info("ip:{}", NetworkUtils.getIpAddress(NetworkUtils.getHttpServletRequest()));

    LogAspect.log.info("execute time : {} ms", time);
    LogAspect.log.info("=====================log end================================");
  }

}
