package com.jxiang.blog.service.thread;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.SysUserMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.SysUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ThreadService {

  private final String THREAD_POOL_ID = "taskExecutor";

  private final RedisTemplate<String, String> redisTemplate;

  @Async(THREAD_POOL_ID)
  public void updateArticleViewCount(final ArticleMapper articleMapper, final Article article) {
    final int viewCount = article.getViewCounts();
    final Article toUpdate = Article.builder().build();
    toUpdate.setViewCounts(viewCount + 1);

    final LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(Article::getId, article.getId())
        .eq(Article::getViewCounts, viewCount); // optimistic lock

    articleMapper.update(toUpdate, updateWrapper);
  }

  @Async(THREAD_POOL_ID)
  public void updateCommentCount(final ArticleMapper articleMapper, final Article article) {
    final int commentCount = article.getCommentCounts();
    final Article toUpdate = Article.builder().build();
    toUpdate.setCommentCounts(commentCount + 1);

    final LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(Article::getId, article.getId())
        .eq(Article::getCommentCounts, commentCount); // optimistic lock

    articleMapper.update(toUpdate, updateWrapper);
  }

  @Async(THREAD_POOL_ID)
  public void updateCommentCount(
      final ArticleMapper articleMapper,
      final Long articleId,
      final boolean add,
      final int count) {
    final Article article = articleMapper.selectById(articleId);
    final int commentCount = article.getCommentCounts();
    final Article toUpdate = Article.builder().build();

    if (add) {
      toUpdate.setCommentCounts(commentCount + 1);
    } else {
      toUpdate.setCommentCounts(Math.max(commentCount - count, 0));
    }

    final LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(Article::getId, article.getId())
        .eq(Article::getCommentCounts, commentCount); // optimistic lock

    articleMapper.update(toUpdate, updateWrapper);
  }

  @Async(THREAD_POOL_ID)
  public void updateLastLogin(final SysUser sysUser, final String token, final SysUserMapper sysUserMapper) {
    sysUser.setLastLogin(System.currentTimeMillis());
    sysUserMapper.updateById(sysUser);

    // update token in redis {TOKEN_ey21e123f24=SysUser} with updated lastLogin
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
  }

}
