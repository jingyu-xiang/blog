package com.jxiang.blog.services;

import com.jxiang.blog.vo.results.Result;
import org.springframework.stereotype.Service;

public interface CommentService {

    /**
     * given article id, find its comments and the corresponding creators
     * if level = 2, find the level2 comments on the level1 comment, and their creators
     *
     * @param id article id
     * @return comments of the article with article id to be id
     */
    Result getCommentsByArticleId(String id);

}
