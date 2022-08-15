package com.jxiang.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxiang.blog.dao.ArticleMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.services.ArticleService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.services.TagService;
import com.jxiang.blog.vo.ArticleVo;
import com.jxiang.blog.vo.Result;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    TagService tagService;

    @Autowired
    SysUserService sysUserService;

    @Override
    public Result listArticles(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper // order by create_date DESC & weight DESC
            .orderByDesc(Article::getCreateDate)
            .orderByDesc(Article::getWeight);

        final Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);

        final List<ArticleVo> articleVoList = copyList(articlePage.getRecords(), true, true);

        return articleVoList.size() != 0
            ? Result.success(articleVoList)
            : Result.success("No article yet");
    }

    @Override
    public Result listHotArticles(LimitParam limitParam) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper // select id, title, view_counts from ms_article order by view_counts desc limit {limit}
            .orderByDesc(Article::getViewCounts)
            .select(Article::getId, Article::getTitle, Article::getViewCounts)
            .last("LIMIT " + limitParam.getLimit());

        final List<Article> articles = articleMapper.selectList(queryWrapper);

        final List<ArticleVo> articleVoList = copyList(articles, false, false);

        return articleVoList.size() != 0
            ? Result.success(articleVoList)
            : Result.success("No article Yet");
    }

    @Override
    public Result listNewArticles(LimitParam limitParam) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper // select id, title, createDate from ms_article order by create_date desc limit {limit}
            .orderByDesc(Article::getCreateDate)
            .select(Article::getId, Article::getTitle, Article::getCreateDate)
            .last("LIMIT " + limitParam.getLimit());

        final List<Article> articles = articleMapper.selectList(queryWrapper);

        final List<ArticleVo> articleVoList = copyList(articles, false, false);

        return articleVoList.size() != 0
            ? Result.success(articleVoList)
            : Result.success("No article Yet");
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTagsRequired, boolean isAuthorRequired) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            // author and tags are required
            articleVoList.add(copy(record, isTagsRequired, isAuthorRequired));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTagsRequired, boolean isAuthorRequired) {
        ArticleVo articleVo = new ArticleVo();
        // copy properties of article to articleVo, set field of articleVo to null if it is not in article
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        if (isTagsRequired) {
            Long articleId = articleVo.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }

        if (isAuthorRequired) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        return articleVo;
    }

}
