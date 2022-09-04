package com.jxiang.blog.services;

import com.jxiang.blog.vo.TagVo;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.results.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TagService {

    /**
     * query ms_article_tag, group by tag_id, find the tag_id with most article_ids
     * return the first {limit} records, and order by the number of article_ids
     *
     * @param limitParam object of page and pageSize
     * @return list of tags
     */
    Result listHotTags(LimitParam limitParam);

    /**
     * list all tags
     *
     * @return Result
     */
    Result getAllTags();

    /**
     * list tags, given the articleId of the article that they are tagged with
     *
     * @param articleId article's id
     * @return Result
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * create a tag. Admin only
     *
     * @param tagName tag name
     * @param file    tag image file
     * @return Result
     */
    @Transactional
    Result createTag(String tagName, MultipartFile file);

}
