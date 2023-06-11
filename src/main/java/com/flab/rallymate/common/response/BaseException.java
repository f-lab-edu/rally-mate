package com.flab.rallymate.common.response;

public class BaseException extends RuntimeException {

    private final ErrorCode error;

    public BaseException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }
}
