package com.jxiang.blog.aop.admin;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.util.SysUserThreadLocal;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
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

  @Around("pt()")
  public Object around(final ProceedingJoinPoint pjp) {
    try {
      final SysUser requestUser = SysUserThreadLocal.get();
      if (requestUser.getAdmin() != 1) {
        return Result.failure(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
      }
      return pjp.proceed();
    } catch (final Throwable e) {
      e.printStackTrace();
      return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }

  }

}
