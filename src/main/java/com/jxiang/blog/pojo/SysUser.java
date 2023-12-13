package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_sys_user")
public class SysUser {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String account;

  private Integer admin;

  private Long createDate;

  private Integer deleted;

  private String github;

  private Long lastLogin;

  private String nickname;

  private String password;

}