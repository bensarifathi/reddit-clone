package com.reddit.reddit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {SpringRedditException.class})
    public ResponseEntity<Object> handleApiException(SpringRedditException e) {
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiException);
    }

    @ExceptionHandler(value = {UnauthorizedException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleUnauthorizedException(RuntimeException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiException);
    }
}
