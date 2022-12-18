package com.jxiang.blog.aop.log;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.util.statics.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

// aop 定义一个切面， 切面定义切点和通知的关系
@Aspect
@Component
@Slf4j
public class LogAspect {

  @Pointcut("@annotation(com.jxiang.blog.aop.log.Log)") // 切点
  public void pt() {
  }

  @Around("pt()") // 通知（环绕）
  public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {
    long beginTime = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    Long time = System.currentTimeMillis() - beginTime;

    recordLog(joinPoint, time);
    return result;
  }

  private void recordLog(ProceedingJoinPoint joinPoint, Long time) {
    // retrieve the invoked method properties (annotation in this case)
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    Log log = method.getAnnotation(Log.class);

    LogAspect.log.info("=====================log start================================");
    LogAspect.log.info("module:{}", log.module());
    LogAspect.log.info("operation:{}", log.operator());

    // method name
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = signature.getName();
    LogAspect.log.info("request method:{}", className + "." + methodName + "()");

    // parameters
    Object[] args = joinPoint.getArgs();
    String params = JSON.toJSONString(args[0]);
    LogAspect.log.info("params:{}", params);

    //retrieve request info such as ip address
    HttpServletRequest request = NetworkUtils.getHttpServletRequest();
    LogAspect.log.info("ip:{}", NetworkUtils.getIpAddress(request));

    LogAspect.log.info("execute time : {} ms", time);
    LogAspect.log.info("=====================log end================================");
  }
}
