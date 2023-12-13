package com.jxiang.blog.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorVo {

  String id;

  String nickname;

  String github;

}
