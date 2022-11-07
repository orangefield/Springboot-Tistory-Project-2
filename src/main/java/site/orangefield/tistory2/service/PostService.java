package site.orangefield.tistory2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 어머어머 이거네

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.love.Love;
import site.orangefield.tistory2.domain.love.LoveRepository;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.post.PostRepository;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.visit.Visit;
import site.orangefield.tistory2.domain.visit.VisitRepository;
import site.orangefield.tistory2.handler.ex.CustomApiException;
import site.orangefield.tistory2.handler.ex.CustomException;
import site.orangefield.tistory2.util.UtilFileUpload;
import site.orangefield.tistory2.web.dto.post.PostDetailRespDto;
import site.orangefield.tistory2.web.dto.post.PostRespDto;
import site.orangefield.tistory2.web.dto.post.PostWriteReqDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    @Value("${file.path}")
    private String uploadFolder;

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final VisitRepository visitRepository;
    private final LoveRepository loveRepository;
    private final EntityManager em; // IoC 컨테이너에서 가져옴

    @Transactional(rollbackFor = CustomApiException.class)
    public void 게시글삭제(Integer id, User principal) {

        // 게시글 확인
        Post postEntity = basicFindById(id);

        // 권한 체크
        if (authCheck(postEntity.getUser().getId(), principal.getId())) {
            // 핵심로직
            postRepository.deleteById(id);
        } else {
            throw new CustomApiException("삭제 권한이 없습니다");
        }
    }

    // 로그인x 방문자일 때
    @Transactional
    public PostDetailRespDto 게시글상세보기(Integer id) {
        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();

        // 게시글 확인
        Post postEntity = basicFindById(id);

        // 방문자 카운터 증가
        visitIncrease(postEntity.getUser().getId());

        // 리턴값 만들기
        postDetailRespDto.setPost(postEntity);
        postDetailRespDto.setPageOwner(false);

        // 좋아요 유무 추가하기 (로그인한 사람이 해당 게시글을 좋아하는지)
        postDetailRespDto.setLove(false);

        return postDetailRespDto;
    }

    // 로그인O
    @Transactional
    public PostDetailRespDto 게시글상세보기(Integer id, User principal) {
        // 1. 권한체크
        // 2. 게시글 가져오기
        // 3. 방문자수 증가하기
        // 4. 리턴값 만들기

        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();

        // 게시글 확인
        Post postEntity = basicFindById(id);

        // 권한 체크
        boolean isAuth = authCheck(postEntity.getUser().getId(), principal.getId());

        // 방문자 카운터 증가
        visitIncrease(postEntity.getUser().getId());

        // 리턴값 만들기
        postDetailRespDto.setPost(postEntity);
        postDetailRespDto.setPageOwner(isAuth);

        // 좋아요 유무 추가하기 (로그인한 사람이 해당 게시글을 좋아하는지)
        // 로그인한 사람의 userId와 상세보기한 postId로 Love 테이블에서 select해서 row가 있으면 true
        Optional<Love> loveOp = loveRepository.mFindByUserIdAndPostId(principal.getId(), id);
        if (loveOp.isPresent()) {
            postDetailRespDto.setLove(true);
        } else {
            postDetailRespDto.setLove(false);
        }

        return postDetailRespDto;
    }

    public List<Category> 게시글쓰기화면(User principal) {
        return categoryRepository.findByUserId(principal.getId());
    }

    @Transactional
    public void 게시글쓰기(PostWriteReqDto postWriteReqDto, User principal) {

        // 1. UUID로 파일쓰고 경로 리턴 받기
        String thumbnail = null;
        if (!postWriteReqDto.getThumbnailFile().isEmpty()) {
            thumbnail = UtilFileUpload.write(uploadFolder, postWriteReqDto.getThumbnailFile());
        }
        // 2. 카테고리 있는지 확인
        Optional<Category> categoryOp = categoryRepository.findById(postWriteReqDto.getCategoryId());
        // 3. post DB에 저장
        if (categoryOp.isPresent()) {
            Post post = postWriteReqDto.toEntity(thumbnail, principal, categoryOp.get());
            postRepository.save(post);
        } else {
            throw new CustomException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    @Transactional
    public PostRespDto 게시글목록보기(Integer pageOwnerId, Pageable pageable) {
        Page<Post> postsEntity = postRepository.findByUserId(pageOwnerId, pageable);
        List<Category> categoriesEntity = categoryRepository.findByUserId(pageOwnerId);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        // 방문자 카운터 증가
        Visit visitEntity = visitIncrease(pageOwnerId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,
                pageNumbers,
                visitEntity.getTotalCount());

        return postRespDto;
    }

    public PostRespDto 게시글카테고리별보기(Integer pageOwnerId, Integer categoryId, Pageable pageable) {
        Page<Post> postsEntity = postRepository.findByUserIdAndCategoryId(pageOwnerId, categoryId, pageable);
        List<Category> categoriesEntity = categoryRepository.findByUserId(pageOwnerId);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        // 방문자 카운터 증가
        Visit visitEntity = visitIncrease(pageOwnerId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,
                pageNumbers,
                visitEntity.getTotalCount());

        return postRespDto;
    }

    // 게시글 한 건 찾기 메서드
    private Post basicFindById(Integer postId) {
        Optional<Post> postOp = postRepository.findById(postId);

        if (postOp.isPresent()) {
            Post postEntity = postOp.get();
            return postEntity;
        } else {
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }
    }

    // 로그인 유저가 게시글 주인인지 확인하는 메서드
    private boolean authCheck(Integer principalId, Integer pageOwnerId) {
        boolean isAuth = false;

        if (principalId == pageOwnerId) {
            isAuth = true;
        } else {
            isAuth = false;
        }

        return isAuth;
    }

    // 방문자 카운터 증가 메서드
    private Visit visitIncrease(Integer pageOwnerId) {
        Optional<Visit> visitOp = visitRepository.findById(pageOwnerId);

        if (visitOp.isPresent()) {
            Visit visitEntity = visitOp.get();
            Long totalCount = visitEntity.getTotalCount();
            visitEntity.setTotalCount(totalCount + 1);
            return visitEntity;
        } else {
            log.error("겁나 심각", "회원가입할 때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
            throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
        }
    }

    //////////////////////////////// 연습 해봄 /////////////////////////////////////
    // JPQL -> Java Persistence Query Langauge
    // 복잡한 쿼리(ex.통계쿼리), DTO로 받고 싶을 때!!
    public Post emTest1(int id) {
        em.getTransaction().begin(); // 트랜잭션 시작

        // 컴파일시점에 쿼리의 오류를 발견하기 위해 QueryDSL 사용
        String sql = null;
        if (id == 1) {
            sql = "SELECT * FROM post WHERE id = 1";
        } else {
            sql = "SELECT * FROM post WHERE id = 2";
        }

        TypedQuery<Post> query = em.createQuery(sql, Post.class);
        Post postEntity = query.getSingleResult();

        try {
            // insert()

            // update()
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
        }

        em.close(); // 트랜잭션 종료
        return postEntity;
    }

    // 영속화 비영속화
    public Love emTest2() {
        Love love = new Love();
        em.persist(love); // 영속화
        em.detach(love); // 비영속화
        em.merge(love); // 재영속화
        em.remove(love); // 영속성 삭제
        return love; // MessageConverter
    }
    //////////////////////////////// 연습 해봄 /////////////////////////////////////

}
