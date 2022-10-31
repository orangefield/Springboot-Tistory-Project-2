package site.orangefield.tistory2.handler.ex;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
