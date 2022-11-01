package site.orangefield.tistory2.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.config.auth.LoginUser;
import site.orangefield.tistory2.service.PostService;
import site.orangefield.tistory2.web.dto.post.PostRespDto;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/user/{id}/post")
    public String postList(@PathVariable Integer id, @AuthenticationPrincipal LoginUser loginUser, Model model) {

        // SELECT * FROM category WHERE userId = :id
        // 카테고리도 같이 가져가기
        PostRespDto postRespDto = postService.게시글목록보기(id);
        model.addAttribute("postRespDto", postRespDto);

        return "/post/list";
    }
}
