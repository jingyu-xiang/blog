package com.jxiang.blog.configs;

import com.jxiang.blog.handlers.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry // Todo
            .addInterceptor(authInterceptor)
            .addPathPatterns("/api/users/me") // get current user
            .addPathPatterns("/api/upload") // upload image for article
            .addPathPatterns("/api/tags/create") // create tag
            .addPathPatterns("/api/categories/create") // create category
            .addPathPatterns("/api/comments") // create comment
            .addPathPatterns("/api/comments/*") // delete comment
            .addPathPatterns("/api/articles/publish") // create article
            .addPathPatterns("/api/articles/delete/*") // delete article
        ;
    }

}
