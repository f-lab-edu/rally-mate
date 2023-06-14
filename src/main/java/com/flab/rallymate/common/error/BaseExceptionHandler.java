package com.flab.rallymate.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flab.rallymate.common.response.BaseException;
import com.flab.rallymate.common.response.BaseHttpResponse;

import lombok.extern.slf4j.Slf4j;

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


}

