package com.example.demo.exception;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.*;

@ControllerAdvice//class ka global context provide karta hai, isse hum globally exception handle kar sakte hai, iska use tab hota hai jab hume multiple controller me same exception handle karna hota hai, isse hum code ko reuse kar sakte hai aur code ko clean rakh sakte hai.
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)//why calss as an argument , because we handle perticalar exception here of a particular class
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {//object of  a class
        Map<String, String> errors = new HashMap<>();
        // <?> optional in java is an empty container which signifies the value key , if value is present then it will return the value otherwise it will return empty container, it is used to avoid null pointer exception and to handle the case when value is not present in a better way.
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
