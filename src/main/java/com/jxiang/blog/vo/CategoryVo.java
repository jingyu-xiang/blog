package com.jxiang.blog.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryVo {

  private String id;

  private String avatar;

  private String categoryName;

  private String description;

}
