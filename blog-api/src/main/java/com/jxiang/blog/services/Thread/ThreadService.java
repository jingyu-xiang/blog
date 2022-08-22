package com.jxiang.blog.services.Thread;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jxiang.blog.dao.ArticleMapper;
import com.jxiang.blog.dao.SysUserMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.SysUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor") // put the following task in a configured thread pool, without affecting main thread
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        // manual delay ...
        try {
            Thread.sleep(1500);
            System.out.println("update is done !");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
    public void updateLastLogin(SysUser sysUser, SysUserMapper sysUserMapper) {
        // manual delay ...
        try {
            Thread.sleep(1500);
            System.out.println("update is done !");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sysUser.setLastLogin(System.currentTimeMillis());
        sysUserMapper.updateById(sysUser);
    }

}
