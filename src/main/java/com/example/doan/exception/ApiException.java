package com.example.doan.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class ApiException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }
}
