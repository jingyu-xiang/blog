package com.jxiang.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthUserVo {

    private Long id;

    private String account;

    private String nickname;

    private String avatar;

}
