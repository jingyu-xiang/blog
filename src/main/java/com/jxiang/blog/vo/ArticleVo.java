package com.jxiang.blog.vo;

import java.util.List;

import lombok.Data;

@Data
public class ArticleVo {

  private String id;

  private String title;

  private String summary;

  private Integer commentCounts;

  private Integer viewCounts;

  private Integer weight;

  private String createDate;

  private AuthorVo author; // nickname of author

  private List<TagVo> tags; // list of user-tags

  private CategoryVo category;

  private ArticleBodyVo body;

}
