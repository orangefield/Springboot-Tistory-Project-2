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
```

### 3. 주소 요청
```txt
localhost:8080/ (메인페이지 - 글 있는 곳 아님)
localhost:8080/user/{userId}/post
localhost:8080/user/{userId}/post/{postId}
localhost:8080/user/{userId}/category/{title}
```

### 4. 모델링
```sql
User
id
username
password
createDate
updateDate

Post
id
title
content
thumbnail
userId
categoryId
createDate
updateDate

Visit
id
userId
totalCount
createDate
updateDate

Love
id
postId
userId
createDate
updateDate

Category
id
title
userId
createDate
updateDate
```

### Gradle depenency update
```txt
./gradlew --refresh-dependencies
```