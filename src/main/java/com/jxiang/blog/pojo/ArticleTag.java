package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ms_article_tag")
public class ArticleTag {

  private Long id;

  private Long articleId;

  private Long tagId;

}
