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

        final List<ArticleVo> articleVoList = copyList(articlePage.getRecords());

        return articleVoList.size() != 0
            ? Result.success(articleVoList)
            : Result.success("No article yet");
    }

    private List<ArticleVo> copyList(List<Article> records) {
        boolean tagsRequired = true;
        boolean authorRequired = true;

        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            // author and tags are required
            articleVoList.add(copy(record, tagsRequired, authorRequired));
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
