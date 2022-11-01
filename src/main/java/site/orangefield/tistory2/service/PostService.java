package site.orangefield.tistory2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.post.PostRepository;
import site.orangefield.tistory2.web.dto.post.PostRespDto;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public PostRespDto 게시글목록보기(int userId) {
        List<Post> postsEntity = postRepository.findByUserId(userId);
        List<Category> categoriesEntity = categoryRepository.findByUserId(userId);

        PostRespDto postRespDto = new PostRespDto(
                postsEntity,
                categoriesEntity);
        return postRespDto;
    }
}
