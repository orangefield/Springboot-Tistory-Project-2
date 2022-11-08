package site.orangefield.tistory2.config.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import site.orangefield.tistory2.util.UtilValid;

@Aspect
@Component
public class BindingAdvice {

    // @Before("execution(* site.orangefield.tistory2.web.MainController.*(..))")
    public void before() {
        System.out.println("Before~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    // @After("execution(* site.orangefield.tistory2.web.MainController.*(..))")
    public void after() {
        System.out.println("After~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Around("execution(* site.orangefield.tistory2.web..*Controller.*(..))")
    public Object bindingValidation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("JoinPoint ===================================");

        // proceedingJoinPoint
        String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String method = proceedingJoinPoint.getSignature().getName();

        System.out.println(type);
        System.out.println(method);

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                UtilValid.요청에러처리(bindingResult);
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
