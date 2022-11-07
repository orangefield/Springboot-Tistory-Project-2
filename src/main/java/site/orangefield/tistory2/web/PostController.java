package site.orangefield.tistory2.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.config.auth.LoginUser;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.love.Love;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.handler.ex.CustomException;
import site.orangefield.tistory2.service.PostService;
import site.orangefield.tistory2.web.dto.post.PostDetailRespDto;
import site.orangefield.tistory2.web.dto.post.PostRespDto;
import site.orangefield.tistory2.web.dto.post.PostWriteReqDto;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @PostMapping("/s/api/post/{postId}/love")
    public ResponseEntity<?> love(@PathVariable Integer postId, @AuthenticationPrincipal LoginUser loginUser) {
        Love loveEntity = postService.좋아요(postId, loginUser.getUser());
        return new ResponseEntity<>(loveEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/s/api/post/{postId}/love")
    public ResponseEntity<?> unLove(@PathVariable Integer postId, @AuthenticationPrincipal LoginUser loginUser) {
        // (로그인한 유저의 userId) vs (Love 테이블의 userId)
        postService.좋아요취소(postId, loginUser.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/s/api/post/{id}")
    public ResponseEntity<?> postDelete(@PathVariable Integer id, @AuthenticationPrincipal LoginUser loginUser) {
        User principal = loginUser.getUser();

        postService.게시글삭제(id, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable Integer id, Model model, @AuthenticationPrincipal LoginUser loginUser) {

        PostDetailRespDto postDetailRespDto = null;

        if (loginUser == null) {
            postDetailRespDto = postService.게시글상세보기(id);
        } else {
            postDetailRespDto = postService.게시글상세보기(id, loginUser.getUser());
        }

        model.addAttribute("data", postDetailRespDto);

        return "/post/detail";
    }

    @PostMapping("/s/post")
    public String write(PostWriteReqDto postWriteReqDto, @AuthenticationPrincipal LoginUser loginUser) {

        postService.게시글쓰기(postWriteReqDto, loginUser.getUser());

        return "redirect:/user/" + loginUser.getUser().getId() + "/post";
    }

    @GetMapping("/s/post/write-form")
    public String writeForm(@AuthenticationPrincipal LoginUser loginUser, Model model) {
        List<Category> categories = postService.게시글쓰기화면(loginUser.getUser());

        if (categories.size() == 0) {
            throw new CustomException("카테고리 등록이 필요해요");
        }

        model.addAttribute("categories", categories);
        return "/post/writeForm";
    }

    @GetMapping("/user/{pageOwnerId}/post")
    public String postList(Integer categoryId, @PathVariable Integer pageOwnerId,
            @AuthenticationPrincipal LoginUser loginUser,
            Model model, @PageableDefault(size = 3) Pageable pageable) {

        // SELECT * FROM category WHERE userId = :id
        // 카테고리도 같이 가져가기
        PostRespDto postRespDto = null;

        if (categoryId == null) {
            postRespDto = postService.게시글목록보기(pageOwnerId, pageable);
        } else {
            postRespDto = postService.게시글카테고리별보기(pageOwnerId, categoryId, pageable);
        }

        model.addAttribute("postRespDto", postRespDto);

        return "/post/list";
    }
}
