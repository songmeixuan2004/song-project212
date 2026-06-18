package com.example.shop.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public com.example.shop.common.Result<?> handleRuntimeException(RuntimeException e) {
        return com.example.shop.common.Result.error(e.getMessage());
    }
}