package com.reddit.reddit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message) {
        super(message);
    }

    public SpringRedditException(String message, Throwable cause) {
        super(message, cause);
    }
}