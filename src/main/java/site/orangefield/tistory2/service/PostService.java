package site.orangefield.tistory2.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.post.PostRepository;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.handler.ex.CustomException;
import site.orangefield.tistory2.util.UtilFileUpload;
import site.orangefield.tistory2.web.dto.post.PostRespDto;
import site.orangefield.tistory2.web.dto.post.PostWriteReqDto;

@RequiredArgsConstructor
@Service
public class PostService {

    @Value("${file.path}")
    private String uploadFolder;

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

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

    public PostRespDto 게시글목록보기(Integer userId, Pageable pageable) {
        Page<Post> postsEntity = postRepository.findByUserId(userId, pageable);
        List<Category> categoriesEntity = categoryRepository.findByUserId(userId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity);

        return postRespDto;
    }

    public PostRespDto 게시글카테고리별보기(Integer userId, Integer categoryId, Pageable pageable) {
        Page<Post> postsEntity = postRepository.findByUserIdAndCategoryId(userId, categoryId, pageable);
        List<Category> categoriesEntity = categoryRepository.findByUserId(userId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity);

        return postRespDto;
    }
}
