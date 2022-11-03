package com.jxiang.blog.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
    return clientConfigurationBuilder -> {
      if (clientConfigurationBuilder.build().isUseSsl()) {
        clientConfigurationBuilder.useSsl().disablePeerVerification();
      }
    };
  }

}