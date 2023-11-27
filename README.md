# blog

SpringBoot Blog System

## Techstack

- Spring Boot
- Redis
- MySQL (MyBatis, MyBatisPlus)

## Frontend (planned)

- React

## Tech highlights:

1. Used ThreadLocal instances to thread-safely persist user information in
   controller methods'
   execution scope, instead
   of re-retrieving it from redis every time. Also, it is removed from thread
   local after
   controllers finish execution:
   - Each thread has a ThreadLocalMap, with keys to be week referenced
     ThreadLocal instances,
     values to be strong
     referenced copies of thread variables.
   - ThreadLocal instance (key) could be grabage-collected. But the value will
     not. So a manual
     threadLocal.remove() is
     called to remove the key-val pair to prevent memory leak.

<img src="https://user-images.githubusercontent.com/46456200/185763633-2979512e-7fb3-4aaf-8ff6-94514a7afb33.png" alt="MarineGEO circle logo" style="height: 250px; width:550px;"/>

<br/>

2. Used thread pool to update view counts for articles:

   - Side effects such as update view counts and comment counts involves
     database transactions, and may trigger exceptions.
     But it should not
     affect users viewing the
     article.
   - Applied optimistic lock when updating view count to achieve thread safety:

   ```
   update ms_article set view_count=100 where view_count={viewCount} and id={article.getId()}
   ```

3. MySql highlights:
   - Used junction tables to represent many-to-many relationships such as
     article-tag. Therefore, 1
     tag can be
     associated with many articles and 1 article can be associated with many
     tags.
   - Applied indexes on commonly-queried columns to boost query speed.
   - Applied fulltext index on article title and summary for better user
     experience on searching articles
     .
4. Redis:

   - Methods caching: cached service method calls by creating custom
     annotations and utilising Java reflection mechanism & Spring AOP. Stored
     a unique combination of class name, method name, and encoded arguments of
     specified method calls as the redis key and, the JSON representation of
     the returned value as the redis value.
   - Authentication & authorization: used JWT for user authentication. Stored
     the JWT token as redis key to store user auth information.

5. Code quality & organization:
   - Seperated the codebase into 3 Maven modules: blog-admin for the admin-side
     services, blog-api for the user-side services, and blog-common for
     commonly used entities such as the POJOs (server-database connection) and
     VOs (server-client connection).
   - Widely utilized custom annotations and Spring AOP to organize common code
     and improve readability.

## App features:

### User features:

1. View posts.
2. Share posts consisting of plain text, Markdown and images.
3. Comment on others' posts.

### Admin features:

1. Manage posts.
2. Manage posts categories and tags.
3. Manage Users.

### API Documentation:

https://documenter.getpostman.com/view/21301848/2s8YKGifv1

### Deployment:

https://jxiang-blog.herokuapp.com/
