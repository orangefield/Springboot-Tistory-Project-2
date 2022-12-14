package site.orangefield.tistory2.web;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.orangefield.tistory2.config.auth.LoginUser;
import site.orangefield.tistory2.service.UserService;
import site.orangefield.tistory2.util.UtilValid;
import site.orangefield.tistory2.web.dto.user.JoinReqDto;
import site.orangefield.tistory2.web.dto.user.PasswordResetReqDto;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    // DI
    private final UserService userService;
    private final HttpSession session;

    private static final String TAG = "UserController : ";

    @Value("${file.path}")
    private String uploadFolder;

    @PutMapping("/s/api/user/profile-img")
    public ResponseEntity<?> profileImgUpdate(@AuthenticationPrincipal LoginUser loginUser,
            MultipartFile profileImgFile) {

        userService.프로파일이미지변경(loginUser.getUser(), profileImgFile, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "/user/joinForm";
    }

    @GetMapping("/s/user/{id}")
    public String updateForm(@PathVariable Integer id) {
        return "/user/updateForm";
    }

    @GetMapping("/user/password-reset-form")
    public String passwordResetForm() {
        return "/user/passwordResetForm";
    }

    @PostMapping("/user/password-reset")
    public String passwordReset(@Valid PasswordResetReqDto passwordResetReqDto, BindingResult bindingResult) {

        UtilValid.요청에러처리(bindingResult);

        // 핵심로직
        userService.패스워드초기화(passwordResetReqDto);

        return "redirect:/login-form";
    }

    @GetMapping("/api/user/username-same-check")
    public ResponseEntity<?> usernameSameCheck(String username) {
        boolean isNotSame = userService.유저네임중복체크(username); // true (같지 않다)
        return new ResponseEntity<>(isNotSame, HttpStatus.OK);
    }

    @PostMapping("/join")
    public String join(@Valid JoinReqDto joinReqDto, BindingResult bindingResult) {

        UtilValid.요청에러처리(bindingResult);

        // 핵심로직
        userService.회원가입(joinReqDto.toEntity());

        return "redirect:/login-form";
    }

}
