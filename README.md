# blog

SpringBoot Blog System

## Techstack

* SpringBoot
* Redis
* MySQL

## Frontend

* React.js


## tech highlights:
1. use ThreadLocal instances to persist user information in controller methods' execution scope, and remove it from thread local after controllers finish execution:
    * each thread has a ThreadLocalMap, with keys to be week referenced ThreadLocal instances, values to be strong referenced copies of thread variables
    * ThreadLocal instance (key) could be grabage-collected. But the value will not. So a manual threadLocal.remove() is called to remove the key-val pair to prevent memory leak
    
<img src="https://user-images.githubusercontent.com/46456200/185763633-2979512e-7fb3-4aaf-8ff6-94514a7afb33.png" alt="MarineGEO circle logo" style="height: 250px; width:550px;"/>

2. use thread pool to update view counts for articles:
   * update view counts takes time and may trigger exceptions. But it should not affect user viewing the article
   * apply optimistic when upating viewcount to achieve thread safty
