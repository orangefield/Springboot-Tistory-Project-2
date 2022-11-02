package site.orangefield.tistory2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.post.PostRepository;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void 게시글삭제(Integer id, User principal) {

        Optional<Post> postOp = postRepository.findById(id);

        if (postOp.isPresent()) {
            Post postEntity = postOp.get();

            // 권한 체크
            if (principal.getId() == postEntity.getUser().getId()) {
                postRepository.deleteById(id);
            } else {
                throw new CustomApiException("삭제 권한이 없습니다");
            }
        } else {
            throw new CustomApiException("해당 게시글이 존재하지 않습니다");
        }

    }

    // 로그인x 방문자일 때
    @Transactional
    public PostDetailRespDto 게시글상세보기(Integer id) {
        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();

        Optional<Post> postOp = postRepository.findById(id);

        if (postOp.isPresent()) {
            Post postEntity = postOp.get();
            postDetailRespDto.setPost(postEntity);

            postDetailRespDto.setPageOwner(false);

            // 방문자 카운터 증가
            Optional<Visit> visitOp = visitRepository.findById(postEntity.getUser().getId());
            if (visitOp.isPresent()) {
                Visit visitEntity = visitOp.get();
                Long totalCount = visitEntity.getTotalCount();
                visitEntity.setTotalCount(totalCount + 1);
            } else {
                log.error("겁나 심각", "회원가입할때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
                throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
            }
            return postDetailRespDto;
        } else {
            throw new CustomException("해당 게시글을 찾을 수 없습니다");
        }
    }

    // 로그인O
    @Transactional
    public PostDetailRespDto 게시글상세보기(Integer id, User principal) {
        // 1. 권한체크
        // 2. 게시글 가져오기
        // 3. 방문자수 증가하기
        // 4. 리턴값 만들기

        PostDetailRespDto postDetailRespDto = new PostDetailRespDto();

        // 해당 페이지의 postId 찾아서
        // Integer postId = id;
        // 주인 userId를 찾기
        Integer pageOwnerId = null;
        // 로그인한 사용자의 userId
        Integer loginUserId = principal.getId();

        Optional<Post> postOp = postRepository.findById(id);

        if (postOp.isPresent()) {
            Post postEntity = postOp.get();
            postDetailRespDto.setPost(postEntity);

            pageOwnerId = postEntity.getUser().getId(); // 주인 userId 찾았다

            if (pageOwnerId == loginUserId) {
                postDetailRespDto.setPageOwner(true);
            } else {
                postDetailRespDto.setPageOwner(false);
            }

            // 방문자 카운터 증가
            Optional<Visit> visitOp = visitRepository.findById(postEntity.getUser().getId());
            if (visitOp.isPresent()) {
                Visit visitEntity = visitOp.get();
                Long totalCount = visitEntity.getTotalCount();
                visitEntity.setTotalCount(totalCount + 1);
            } else {
                log.error("겁나 심각", "회원가입할때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
                throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
            }
            return postDetailRespDto;
        } else {
            throw new CustomException("해당 게시글을 찾을 수 없습니다.");
        }
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

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,
                pageNumbers,
                0L);

        // 방문자 카운터 증가
        Optional<User> pageOwnerOp = userRepository.findById(pageOwnerId);

        if (pageOwnerOp.isPresent()) {
            User pageOwnerEntity = pageOwnerOp.get();
            Optional<Visit> visitOp = visitRepository.findById(pageOwnerEntity.getId());
            if (visitOp.isPresent()) {
                Visit visitEntity = visitOp.get();
                // Dto에 방문자수 담기 (request에서 ip주소 받아서 동일하면 증가 안시키는 로직 필요)
                postRespDto.setTotalCount(visitEntity.getTotalCount());
                Long totalCount = visitEntity.getTotalCount();
                visitEntity.setTotalCount(totalCount + 1);
            } else {
                log.error("겁나 심각", "회원가입할때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
                throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
            }
        } else {
            throw new CustomException("해당 블로그는 없는 페이지입니다.");
        }

        return postRespDto;
    }

    public PostRespDto 게시글카테고리별보기(Integer pageOwnerId, Integer categoryId, Pageable pageable) {
        Page<Post> postsEntity = postRepository.findByUserIdAndCategoryId(pageOwnerId, categoryId, pageable);
        List<Category> categoriesEntity = categoryRepository.findByUserId(pageOwnerId);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < postsEntity.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity,
                pageOwnerId,
                postsEntity.getNumber() - 1,
                postsEntity.getNumber() + 1,
                pageNumbers,
                0L);

        // 방문자 카운터 증가
        Optional<User> pageOwnerOp = userRepository.findById(pageOwnerId);

        if (pageOwnerOp.isPresent()) {
            User pageOwnerEntity = pageOwnerOp.get();
            Optional<Visit> visitOp = visitRepository.findById(pageOwnerEntity.getId());
            if (visitOp.isPresent()) {
                Visit visitEntity = visitOp.get();
                // Dto에 방문자수 담기 (request에서 ip주소 받아서 동일하면 증가 안시키는 로직 필요)
                postRespDto.setTotalCount(visitEntity.getTotalCount());
                Long totalCount = visitEntity.getTotalCount();
                visitEntity.setTotalCount(totalCount + 1);
            } else {
                log.error("겁나 심각", "회원가입할때 Visit이 안 만들어지는 심각한 오류가 있습니다.");
                throw new CustomException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
            }
        } else {
            throw new CustomException("해당 블로그는 없는 페이지입니다.");
        }

        return postRespDto;
    }
}
