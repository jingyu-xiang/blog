package com.jxiang.blog.services;

import com.jxiang.blog.vo.params.*;
import com.jxiang.blog.vo.results.Result;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleService {

    /**
     * list all the articles,
     * each article contains its tags and author nickname, and all other information of an article
     *
     * @param pageParams object of page and pageSize
     * @return list of articles
     */
    Result listArticles(PageParams pageParams);

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
     * article archives
     * list monthly archive of articles and show the count during each month
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
     * delete an article by its author
     * delete all it's comments
     *
     * @param articleId article id
     * @return Result
     */
    @Transactional
    Result deleteArticleById(Long articleId);

    /**
     * update an article body by its author
     *
     * @param articleId   article id
     * @param articleBody {content, contentHtml}
     * @return Result
     */
    @Transactional
    Result updateArticleBodyById(Long articleId, ArticleBodyParam articleBody);

    /**
     * update an article's title or summary by its author
     *
     * @param articleUpdateParam {title, summary}
     * @return Result
     */
    @Transactional
    Result updateArticleById(Long articleId, ArticleUpdateParam articleUpdateParam);

}
