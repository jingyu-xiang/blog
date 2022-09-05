package com.jxiang.blog.services.Thread;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.SysUserMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ThreadService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Async("taskExecutor") // put the following task in a configured thread pool, without affecting main thread
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCount = article.getViewCounts();
        Article updated = new Article();
        updated.setViewCounts(viewCount + 1);

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper // update article set view_count=100 where view_count={viewCount} and id={article.getId()}
            .eq(Article::getId, article.getId())
            .eq(Article::getViewCounts, viewCount); // optimistic lock

        articleMapper.update(updated, updateWrapper);
    }

    @Async("taskExecutor")
    public void updateLastLogin(SysUser sysUser, String token, SysUserMapper sysUserMapper) {
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUserMapper.updateById(sysUser);

        // update token in redis {TOKEN_ey21e123f24=SysUser} with updated lastLogin
        redisTemplate
            .opsForValue()
            .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
    }

}
