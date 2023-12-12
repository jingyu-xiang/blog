package com.jxiang.blog.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxiang.blog.pojo.Tag;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

  List<Tag> findTagsByArticleId(@Param("articleId") Long articleId);

  List<Long> findHotTagIds(@Param("limit") int limit);

}
