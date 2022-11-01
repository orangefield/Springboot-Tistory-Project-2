package site.orangefield.tistory2.web;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.config.auth.LoginUser;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.service.CategoryService;
import site.orangefield.tistory2.util.Script;
import site.orangefield.tistory2.util.UtilValid;
import site.orangefield.tistory2.web.dto.category.CategoryWriteReqDto;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final HttpSession session;
    private final CategoryService categoryService;

    @GetMapping("/s/category/writeForm")
    public String writeForm() {
        return "/category/writeForm";
    }

    @PostMapping("/s/category")
    public @ResponseBody String write(@Valid CategoryWriteReqDto categoryWriteReqDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        UtilValid.요청에러처리(bindingResult);

        // User principal = (User) session.getAttribute("principal");
        User principal = loginUser.getUser();
        // LoginUser loginU = (LoginUser)
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // User principal = loginU.getUser();

        Category category = categoryWriteReqDto.toEntity(principal);

        // 핵심로직
        categoryService.카테고리등록(category);

        return Script.href("/s/category/writeForm", "카테고리 등록 완료");
    }
}
