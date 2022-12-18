package com.jxiang.blog.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxiang.blog.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

  void deleteChildComments(@Param("commentIds") List<Long> commentIds);

  void deleteArticleComments(@Param("articleId") Long articleId);

}
