package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_tag")
public class Tag {

  private Long id;

  private String avatar;

  private String tagName;

  @TableLogic
  private Boolean deleted;

}