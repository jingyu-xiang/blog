# blog

SpringBoot Blog System

## Techstack

* Spring Boot
* Spring Security
* Redis
* MySQL (MyBatis, MyBatisPlus)

## Frontend (planned)

* React.js
* Redux

## Tech highlights:

1. used ThreadLocal instances to thread-safely persist user information in controller methods' execution scope, instead
   of re-retrieving it from redis every time. Also, it is removed from thread local after controllers finish execution:
    * each thread has a ThreadLocalMap, with keys to be week referenced ThreadLocal instances, values to be strong
      referenced copies of thread variables
    * ThreadLocal instance (key) could be grabage-collected. But the value will not. So a manual threadLocal.remove() is
      called to remove the key-val pair to prevent memory leak

<img src="https://user-images.githubusercontent.com/46456200/185763633-2979512e-7fb3-4aaf-8ff6-94514a7afb33.png" alt="MarineGEO circle logo" style="height: 250px; width:550px;"/>

<br/>

2. used thread pool to update view counts for articles:
    * update view counts would be time-consuming, and may trigger exceptions. But it should not affect users viewing the
      article
    * applied optimistic lock when updating view count to achieve thread safety
   ```
   update ms_article set view_count=100 where view_count={viewCount} and id={article.getId()}
   ```

3. MySql highlights:
    * used junction tables to represent many-to-many relationships such as article-tag. Therefore, 1 tag can be
      associated with many articles and 1 article can be associated with many tags.
    * applied indexes on commonly-queried columns.
