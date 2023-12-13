package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_category")
public class Category {

  private Long id;

  private String avatar;

  private String categoryName;

  private String description;

  @TableLogic
  private Boolean deleted;

}
