package com.jxiang.blog.aop.admin;

import com.jxiang.blog.util.statics.SysUserThreadLocal;
import com.jxiang.common.pojo.SysUser;
import com.jxiang.common.vo.result.ErrorCode;
import com.jxiang.common.vo.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminOnlyAspect {

  @Pointcut("@annotation(com.jxiang.blog.aop.admin.AdminOnly)") // 切点
  public void pt() {
  }

  @Around("pt()") //@Before：前置通知，在原始方法运行之前执行
  public Object around(ProceedingJoinPoint pjp) {
    try {
      SysUser requestUser = SysUserThreadLocal.get();
      if (requestUser.getAdmin() != 1) {
        return Result.failure(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
      }
      return pjp.proceed();
    } catch (Throwable e) {
      e.printStackTrace();
      return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }

  }
}
