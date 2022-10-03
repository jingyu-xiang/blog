package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ms_category")
public class Category {

  private Long id;

  private String avatar;

  private String categoryName;

  private String description;

  // TODO: finish the delete logics
  @TableLogic
  private Boolean deleted;

}
