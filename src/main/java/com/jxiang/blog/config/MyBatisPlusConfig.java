package com.jxiang.blog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@Configuration
@MapperScan("com.jxiang.blog.dao.mapper")
public class MyBatisPlusConfig {

  @Bean
  MybatisPlusInterceptor mybatisPlusInterceptor() {
    final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // pagination unit
    return interceptor;
  }

}
