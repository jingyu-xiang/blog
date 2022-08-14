package com.jxiang.blog;

import com.jxiang.blog.configs.MyBatisPlusConfig;
import com.jxiang.blog.configs.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
    MyBatisPlusConfig.class,
    WebMvcConfig.class,
})
@SpringBootApplication
public class BlogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApiApplication.class, args);
    }

}
