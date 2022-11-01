package site.orangefield.tistory2.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.post.PostRepository;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.web.dto.post.PostRespDto;
import site.orangefield.tistory2.web.dto.post.PostWriteReqDto;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public List<Category> 게시글쓰기화면(User principal) {
        return categoryRepository.findByUserId(principal.getId());
    }

    @Transactional
    public void 게시글쓰기(PostWriteReqDto postWriteReqDto) {
        // 1번 이미지 파일 저장 (UUID로 변경해서 저장)
        // 2번 이미지 파일명을 Post 오브젝트에 thumbnail 옮기기
        // 3번 title, content도 Post 오브젝트에 옮기고
        // 4번 userId도 Post 오브젝트에 옮기기
        // 5번 categoryId도 Post 오브젝트에 옮기기
        // 6번 save하면 끝
    }

    public PostRespDto 게시글목록보기(int userId) {
        List<Post> postsEntity = postRepository.findByUserId(userId);
        List<Category> categoriesEntity = categoryRepository.findByUserId(userId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity);
        return postRespDto;
    }
}
