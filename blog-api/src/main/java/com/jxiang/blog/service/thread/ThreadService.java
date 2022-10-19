package com.jxiang.blog.service.thread;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.SysUserMapper;
import com.jxiang.common.pojo.Article;
import com.jxiang.common.pojo.SysUser;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

  final private String THREAD_POOL_ID = "taskExecutor";

  private final RedisTemplate<String, String> redisTemplate;

  @Autowired
  public ThreadService(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Async(THREAD_POOL_ID)
  // put the following task in a configured thread pool, without affecting main thread
  public void updateArticleViewCount(ArticleMapper articleMapper,
      Article article) {
    int viewCount = article.getViewCounts();
    Article toUpdate = new Article();
    toUpdate.setViewCounts(viewCount + 1);

    LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper // update article set view_count=100 where view_count={viewCount} and id={article.getId()}
        .eq(Article::getId, article.getId())
        .eq(Article::getViewCounts, viewCount); // optimistic lock

    articleMapper.update(toUpdate, updateWrapper);
  }

  @Async(THREAD_POOL_ID)
  public void updateCommentCount(ArticleMapper articleMapper, Article article) {
    int commentCount = article.getCommentCounts();
    Article toUpdate = new Article();
    toUpdate.setCommentCounts(commentCount + 1);

    LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(Article::getId, article.getId())
        .eq(Article::getCommentCounts, commentCount); // optimistic lock

    articleMapper.update(toUpdate, updateWrapper);
  }

  @Async(THREAD_POOL_ID)
  public void updateLastLogin(SysUser sysUser, String token,
      SysUserMapper sysUserMapper) {
    sysUser.setLastLogin(System.currentTimeMillis());
    sysUserMapper.updateById(sysUser);

    // update token in redis {TOKEN_ey21e123f24=SysUser} with updated lastLogin
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
  }

}
