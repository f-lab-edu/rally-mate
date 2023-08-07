package com.flab.rallymate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseHttpResponse<?>> apiExceptionHandler(final BaseException ex) {
        return ResponseEntity.status(ex.getError().getHttpStatus()).body(BaseHttpResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseHttpResponse<?>> internalServerExceptionHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseHttpResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseHttpResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var baseHttpResponse = e.getBindingResult().getAllErrors().stream().findFirst()
                .map(objectError -> BaseHttpResponse.error(objectError.getDefaultMessage()))
                .orElseGet(() -> BaseHttpResponse.error(e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseHttpResponse);
    }

}