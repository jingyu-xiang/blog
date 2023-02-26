package com.jxiang.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // enable thread pool
public class ThreadPoolConfig {

  @Bean("taskExecutor")
  public Executor asyncServiceExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(Integer.MAX_VALUE);
    executor.setKeepAliveSeconds(60);
    executor.setThreadNamePrefix("blog-api");
    executor.setWaitForTasksToCompleteOnShutdown(true); // close thread pool after task done

    return executor;
  }

}
