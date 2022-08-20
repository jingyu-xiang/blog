package com.jxiang.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;

    private String createDate;

    private String author; // nickname of author

    private List<TagVo> tags; // list of user-tags

    private CategoryVo category;

    private ArticleBodyVo body;

}
