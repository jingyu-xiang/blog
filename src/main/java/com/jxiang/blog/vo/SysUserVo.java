package com.jxiang.blog.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysUserVo {

  private String id;

  private String account;

  private String nickname;

  private String lastLogin;

  private String github;

}
