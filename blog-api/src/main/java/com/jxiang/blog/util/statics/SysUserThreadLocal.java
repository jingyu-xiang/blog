package com.jxiang.blog.util.statics;

import com.jxiang.common.pojo.SysUser;

public class SysUserThreadLocal {

  private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

  // a local storage for each thread specifically
  private SysUserThreadLocal() {
  }

  public static void put(SysUser sysUser) {
    LOCAL.set(sysUser);
  }

  public static SysUser get() {
    return LOCAL.get();
  }

  public static void remove() {
    LOCAL.remove();
  }

}
