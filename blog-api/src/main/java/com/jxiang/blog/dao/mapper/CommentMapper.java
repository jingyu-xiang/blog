package com.jxiang.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxiang.blog.pojo.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    @Update({
        "<script>",
        "update ms_comment c set c.deleted = true where c.id in",
        "<foreach item='commentIds' index='index' collection='list'",
        "open='(' separator=',' close=')'>",
        "#{commentIds}",
        "</foreach>",
        "</script>"
    })
    void deleteChildComments(List<Long> commentIds);

    @Delete("UPDATE ms_comment c SET c.deleted=true WHERE c.article_id = #{articleId}")
    void deleteArticleComments(Long articleId);

}
