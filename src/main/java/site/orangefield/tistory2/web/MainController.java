package site.orangefield.tistory2.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import site.orangefield.tistory2.config.auth.LoginUser;

@Controller
public class MainController {

    @GetMapping({ "/" })
    public String main(@AuthenticationPrincipal LoginUser loginUser) {

        // System.out.println(loginUser.getUsername());
        // System.out.println(loginUser.getUser().getUsername());

        // LoginUser lu = (LoginUser)
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // System.out.println(lu.getUser().getEmail());

        return "main";
    }
}
