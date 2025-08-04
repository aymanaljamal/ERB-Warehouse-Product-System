package com.erb.demo.Exception;

import io.jsonwebtoken.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraint(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>("Email or password is incorrect", HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class,
            SignatureException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleInvalidToken(RuntimeException ex) {
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGeneric(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwt(ExpiredJwtException ex) {
        return new ResponseEntity<>("Token has expired. Please login again.", HttpStatus.UNAUTHORIZED);
    }
}
