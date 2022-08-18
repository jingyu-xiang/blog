package com.jxiang.blog.vo.params;

import lombok.Data;
import lombok.Value;

@Data
public class RegisterParams {

    private String account;

    private String password;

    private String nickname;

    private String email;

}
