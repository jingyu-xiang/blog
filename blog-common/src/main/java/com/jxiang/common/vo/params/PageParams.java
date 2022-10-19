package com.jxiang.common.vo.params;

import lombok.Data;

@Data
public class PageParams {

  private int page = 1; // default first page

  private int pageSize = 5; // default 5 items per page

  private String categoryId;

  private String tagId;

}
