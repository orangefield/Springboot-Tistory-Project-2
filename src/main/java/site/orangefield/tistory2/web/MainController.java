package site.orangefield.tistory2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({ "/" })
    public String main() {
        return "main";
    }
}