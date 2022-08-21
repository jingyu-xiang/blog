package com.jxiang.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentVo {

    private Long id;

    private SysUserVo author;

    private String content;

    private List<CommentVo> children;

    private String createDate;

    private Integer level;

    private SysUserVo toUser;

}