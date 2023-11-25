package com.reddit.reddit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public record ApiException(String message, HttpStatus httpStatus, ZonedDateTime timestamps) {
}
