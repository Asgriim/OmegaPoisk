package org.omega.omegapoisk.utils;

import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.exception.UserAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAEE(UserAlreadyExistsException exception, WebRequest request) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvaliUserOrPasswordException.class)
    public ResponseEntity<?> handleIUOPE() {
        return ResponseEntity.badRequest().body("Invalid user or password");
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<?> handleCCE() {
        return ResponseEntity.badRequest().body("You can't access this resource");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        // Collecting validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIAE(IllegalArgumentException exception, WebRequest request) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handMANVE(MethodArgumentNotValidException exception, WebRequest request) {
//        return ResponseEntity.badRequest().body(exception.getMessage());
//    }
}
