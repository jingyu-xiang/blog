package com.jxiang.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.vo.ArchiveVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("" +
        "SELECT FROM_UNIXTIME(create_date/1000, '%y') AS year, FROM_UNIXTIME(create_date/1000, '%m') AS month, COUNT(*) as count " +
        "FROM ms_article " +
        "GROUP BY year, month;"
    )
    List<ArchiveVo> listArchiveSummary();

}
