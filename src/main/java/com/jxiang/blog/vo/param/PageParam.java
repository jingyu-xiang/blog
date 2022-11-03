package com.jxiang.blog.vo.param;

import lombok.Data;

@Data
public class PageParam {

  private int page = 1; // default first page

  private int pageSize = 5; // default 5 items per page

  private String categoryId;

  private String tagId;

  private String userId;

}
