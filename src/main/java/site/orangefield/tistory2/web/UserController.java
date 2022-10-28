package site.orangefield.tistory2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/login-form")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "/user/joinForm";
    }

}
