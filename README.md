# blog

SpringBoot Blog System

## Techstack

* SpringBoot
* Redis
* MySQL

## Frontend

* React.js


## tech highlights:
1. used ThreadLocal instances to persist user information in controller methods' execution scope, and remove it from thread local after controllers finish execution:
    * each thread has a ThreadLocalMap, with keys to be week referenced ThreadLocal instances, values to be strong referenced copies of thread variables
    * ThreadLocal instance (key) could be grabage-collected. But the value will not. So a manual threadLocal.remove() is called to remove the key-val pair to prevent memory leak
    
<img src="https://user-images.githubusercontent.com/46456200/185763633-2979512e-7fb3-4aaf-8ff6-94514a7afb33.png" alt="MarineGEO circle logo" style="height: 250px; width:550px;"/>

<br/>

2. used thread pool to update view counts for articles:
   * update view counts would be time-consuming, and may trigger exceptions. But it should not affect users viewing the article
   * applied optimistic lock when upating viewcount to achieve thread safty
   ```
   update ms_article set view_count=100 where view_count={viewCount} and id={article.getId()}
   ```
