package com.jxiang.blog.util.statics;

import com.jxiang.common.pojo.SysUser;

public class PermissionUtils {

  public static boolean isAdmin(SysUser sysUser) {
    return sysUser.getAdmin() == 1;
  }
  
}
