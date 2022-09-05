package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ms_article")
// can not use primitive types in mp data modals. Because primitive types have default values, and mp will update any non-null fields
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private Integer commentCounts;

    private Long createDate;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight;

    private Long authorId;

    private Long bodyId;

    private Long categoryId;

    @TableLogic
    private Boolean deleted;

}