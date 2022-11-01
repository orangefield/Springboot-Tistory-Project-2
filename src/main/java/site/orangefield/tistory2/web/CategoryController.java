package site.orangefield.tistory2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import site.orangefield.tistory2.util.Script;

@Controller
public class CategoryController {

    @GetMapping("/s/category/writeForm")
    public String writeForm() {
        return "/category/writeForm";
    }

    @PostMapping("/s/category")
    public @ResponseBody String write() {
        return Script.href("/s/category/writeForm", "카테고리 등록 완료");
    }
}
