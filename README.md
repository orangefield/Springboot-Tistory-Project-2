# 스프링부트 JPA 블로그 Tistory Project

### 1. 의존성
- devtools
- spring web (mvc)
- mustache
- lombok
- jpa
- mariadb
- security
- validation

### 2. DB설정
```sql
CREATE USER 'orange'@'%' IDENTIFIED BY 'orange1234';
CREATE DATABASE orangedb;
GRANT ALL PRIVILEGES ON orangedb.* TO 'orange'@'%';