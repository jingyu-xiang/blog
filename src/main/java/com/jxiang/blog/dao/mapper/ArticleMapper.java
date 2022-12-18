package com.jxiang.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.vo.ArchiveVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

  List<ArchiveVo> listArchiveSummary();

  Page<Article> queryFullText(Page<Article> page, @Param("queryString") String queryString);

}
