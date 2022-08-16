package com.jxiang.blog.services;

import com.jxiang.blog.vo.results.Result;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.PageParams;

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
    Result listArchives();

}
