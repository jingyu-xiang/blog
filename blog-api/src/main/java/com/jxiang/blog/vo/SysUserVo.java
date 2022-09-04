package com.jxiang.blog.vo;

import lombok.Data;

@Data
public class SysUserVo {

    private Long id;

    private String account;

    private String nickname;

    private String avatar;

    private String lastLogin;

    private String email;

}
