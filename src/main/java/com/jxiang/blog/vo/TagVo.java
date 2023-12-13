package com.jxiang.blog.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagVo {

  private String id;

  private String tagName;

  private String avatar;

}
