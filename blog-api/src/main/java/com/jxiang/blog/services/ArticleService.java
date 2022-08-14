package com.jxiang.blog.services;

import com.jxiang.blog.vo.Result;
import com.jxiang.blog.vo.params.PageParams;
import org.springframework.stereotype.Service;

public interface ArticleService {

    /**
     * list all the articles,
     * each article contains its tags and author nickname, and all other information of an article
     *
     * @param pageParams
     * @return
     */
    Result listArticles(PageParams pageParams);

}
