# blog

SpringBoot Blog System

## Techstack

* SpringBoot
* Redis
* MySQL

## Frontend

* React.js


## tech points:
1. use ThreadLocal instances to persist user information in controller methods' execution scope, and remove it from thread local after controllers finish execution:
    * each thread has a ThreadLocalMap, with keys to be week referenced ThreadLocal instances, values to be strong referenced copies of thread variables
    * ThreadLocal instance (key) could be grabage-collected. But the value will not. So a manual threadLocal.remove() is called to remove the key-val pair to prevent memory leak
    
![Screen Shot 2022-08-20 at 12 55 09 PM (2)](https://user-images.githubusercontent.com/46456200/185758133-64601d6d-38f1-48be-bc8e-80bdf40ae25d.png)
