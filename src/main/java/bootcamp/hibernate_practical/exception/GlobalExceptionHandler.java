package bootcamp.hibernate_practical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public Map<String, Object> handleBookNotFound(BookNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("message", e.getMessage());

        return body;
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public Map<String, Object> handleBookAlreadyBorrowed(BookAlreadyBorrowedException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 409);
        body.put("message", e.getMessage());

        return body;
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BookNotBorrowedException.class)
    public Map<String, Object> handleBookNotBorrowed(BookNotBorrowedException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 409);
        body.put("message", e.getMessage());

        return body;
    }
}
