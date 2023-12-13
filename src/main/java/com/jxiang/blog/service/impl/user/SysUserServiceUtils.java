package com.jxiang.blog.service.impl.user;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.AuthorVo;

@Service
public class SysUserServiceUtils {

  AuthorVo copyAuthor(final SysUser sysUser) {
    final AuthorVo authorVo = AuthorVo.builder()
        .id(String.valueOf(sysUser.getId()))
        .build();
    BeanUtils.copyProperties(sysUser, authorVo); // copy properties of sysUser to sysUserVo
    return authorVo;
  }

}
