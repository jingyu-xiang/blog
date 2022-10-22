package com.jxiang.blog.service;

import com.jxiang.common.vo.TagVo;
import com.jxiang.common.vo.param.LimitParam;
import com.jxiang.common.vo.result.Result;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TagService {

  /**
   * query ms_article_tag, group by tag_id, find the tag_id with most article_ids return the first
   * {limit} records, and order by the number of article_ids
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
  Result createTag(String tagName, MultipartFile file);

  /**
   * retrieve a tag vo by its id
   *
   * @param id tag id
   * @return Result
   */
  Result findTagVoById(Long id);

  /**
   * delete a tag, given it's id
   *
   * @param id
   * @return Result
   */
  Result deleteTagById(String id);
}
