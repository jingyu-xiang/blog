package com.jxiang.common.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "ms_tag")
public class Tag {

  private Long id;

  private String avatar;

  private String tagName;
  
  @TableLogic
  private Boolean deleted;

}