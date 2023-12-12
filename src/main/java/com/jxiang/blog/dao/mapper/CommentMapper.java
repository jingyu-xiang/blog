package com.jxiang.blog.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxiang.blog.pojo.Comment;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

  void deleteChildComments(@Param("commentIds") List<Long> commentIds);

  void deleteArticleComments(@Param("articleId") Long articleId);

}
