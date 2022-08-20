package com.jxiang.blog.services.Thread;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.jxiang.blog.dao.ArticleMapper;
import com.jxiang.blog.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor") // put the following task in a configured thread pool, without affecting main thread
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        // manual delay ...
        try {
            Thread.sleep(3000);
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

}
