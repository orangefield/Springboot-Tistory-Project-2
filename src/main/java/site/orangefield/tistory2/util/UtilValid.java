package site.orangefield.tistory2.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import site.orangefield.tistory2.handler.ex.CustomException;

public class UtilValid {

    public static void 요청에러처리(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new CustomException(errorMap.toString());
        }
    }
}
