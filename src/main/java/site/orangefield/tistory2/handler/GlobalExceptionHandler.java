package site.orangefield.tistory2.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import site.orangefield.tistory2.handler.ex.CustomApiException;
import site.orangefield.tistory2.handler.ex.CustomException;
import site.orangefield.tistory2.util.Script;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(Exception e) { // fetch 요청시 발동
        log.error("에러 발생 : " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public String htmlException(Exception e) { // Get(a태그), Post(form태그) 요청 (일반적 요청)
        log.error("에러 발생 : " + e.getMessage());
        return Script.back(e.getMessage());
    }
}
