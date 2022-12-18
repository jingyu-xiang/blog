package com.jxiang.blog.vo.param;

import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

  private ArticleBodyParam body;

  private String categoryId;

  private String summary;

  private List<String> tagIds;

  private String title;

}
