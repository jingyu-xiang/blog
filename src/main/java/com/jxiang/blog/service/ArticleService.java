package com.jxiang.blog.service;

import com.jxiang.blog.vo.param.ArticleBodyParam;
import com.jxiang.blog.vo.param.ArticleParam;
import com.jxiang.blog.vo.param.ArticleUpdateParam;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.param.PageParam;
import com.jxiang.blog.vo.result.Result;

public interface ArticleService {

  /**
   * list all the articles, each article contains its tags and author nickname, and all other
   * information of an article
   *
   * @param pageParam {page, pageSize. categoryId, tagId, userId}
   * @return list of articles
   */
  Result listArticles(PageParam pageParam);

  /**
   * list most viewed articles
   *
   * @param limitParam object of LIMIT variable
   * @return list of articles
   */
  Result listHotArticles(LimitParam limitParam);

  /**
   * list newest articles
   *
   * @param limitParam object of LIMIT variable
   * @return list of articles
   */
  Result listNewArticles(LimitParam limitParam);

  /**
   * article archives list monthly archive of articles and show the count during each month
   *
   * @return list of articles
   */
  Result listArchiveSummary();

  /**
   * get single article by its id
   *
   * @return Result
   * @Param articleId  id
   */
  Result findArticleById(Long articleId);

  /**
   * Create an article
   *
   * @param articleParam {content, contentHtml}
   * @return Result
   */
  Result createArticle(ArticleParam articleParam);

  /**
   * delete an article by its author delete all it's comments
   *
   * @param articleId article id
   * @return Result
   */
  Result deleteArticleById(Long articleId);

  /**
   * update an article body by its author
   *
   * @param articleId   article id
   * @param articleBody {content, contentHtml}
   * @return Result
   */
  Result updateArticleBodyById(Long articleId, ArticleBodyParam articleBody);

  /**
   * update an article's title or summary by its author
   *
   * @param articleUpdateParam {title, summary}
   * @return Result
   */
  Result updateArticleById(Long articleId,
      ArticleUpdateParam articleUpdateParam);

  /**
   * list articles, filtered by a piece of text on title and summary
   *
   * @param queryString a piece of text for fulltext index search
   * @return Result
   */
  Result listSearchedArticles(String queryString, PageParam pageParam);

}