package site.orangefield.tistory2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping({ "/" })
    public String main() {
        return "main";
    }
}
