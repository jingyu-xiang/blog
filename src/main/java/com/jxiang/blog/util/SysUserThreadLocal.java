package com.jxiang.blog.util;

import com.jxiang.blog.pojo.SysUser;

public class SysUserThreadLocal {

  private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

  // a local storage for each thread specifically
  private SysUserThreadLocal() {
  }

  public static void put(final SysUser sysUser) {
    SysUserThreadLocal.LOCAL.set(sysUser);
  }

  public static SysUser get() {
    return SysUserThreadLocal.LOCAL.get();
  }

  public static void remove() {
    SysUserThreadLocal.LOCAL.remove();
  }

}
