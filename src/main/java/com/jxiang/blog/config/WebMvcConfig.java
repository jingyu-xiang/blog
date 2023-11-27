package com.jxiang.blog.config;

import com.jxiang.blog.handler.AuthInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  final private AuthInterceptor authInterceptor;

  public WebMvcConfig(final AuthInterceptor authInterceptor) {
    this.authInterceptor = authInterceptor;
  }

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("http://localhost:3000");
  }

  @Override
  public void addInterceptors(@NotNull final InterceptorRegistry registry) {
    WebMvcConfigurer.super.addInterceptors(registry);
    registry
        // users
        .addInterceptor(authInterceptor)
        .addPathPatterns("/api/users/me") // get current user
        .addPathPatterns("/api/comments/create") // create comment
        .addPathPatterns("/api/comments/delete/*") // delete comment
        .addPathPatterns("/api/upload") // upload image for article
        .addPathPatterns("/api/articles/create") // create article
        .addPathPatterns("/api/articles/delete/*") // delete article
        .addPathPatterns("/api/articles/update/body/*") // update article body
        .addPathPatterns("/api/articles/update/*") // update article title & summary

        // admins
        .addPathPatterns("/api/tags/create") // create tag
        .addPathPatterns("/api/tags/delete/*") // delete tag
        .addPathPatterns("/api/categories/create") // create category
        .addPathPatterns("/api/categories/delete/*") // delete category

        // both
        .addPathPatterns("/api/articles/delete/*") // delete article
    ;
  }

}
