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

### 5. 기능정리
- 카테고리 등록
- 글쓰기
- 글목록보기
- 페이징
- 글상세보기
- 검색
- 글삭제
- 글수정
- 댓글 (라이브러리 사용)

- 프로필 사진 업로드 (회원가입시)
- 회원수정


### Gradle depenency update
```txt
./gradlew --refresh-dependencies
```

### 페이징 참고
```sql
-- currentPage, totalPages
SELECT TRUE last FROM dual;

SELECT 
true LAST,
false FIRST,
3 size, 
0 currentPage,
(SELECT COUNT(*) FROM post WHERE userId = 1) totalCount,
(SELECT CEIL(COUNT(*)/3) FROM post WHERE userId = 1) totalPages,
p.*
FROM post p
ORDER BY p.id DESC
LIMIT 0, 3;
-- LIMIT (0*3), 3;
```