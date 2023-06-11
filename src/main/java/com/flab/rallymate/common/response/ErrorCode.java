package com.flab.rallymate.common.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "password not match"),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "User email duplicated."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User is not found."),
	EMPTY_USER(HttpStatus.NOT_FOUND, "User is empty."),
	NOT_AUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "Not Authorized access"),
	INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "Invalid Authentication"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token"),
	INVALID_TOKEN_ALGORITHM(HttpStatus.UNAUTHORIZED, "Invalid Token Algorithm"),
	DUMMY(HttpStatus.CONFLICT, "");

	private final HttpStatus httpStatus;
	private final String result;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		if (httpStatus.is4xxClientError()) {
			this.result = BaseHttpResponse.FAIL;
		} else {
			this.result = BaseHttpResponse.ERROR;
		}
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getStatus() {
		return result;
	}

	public String getMessage() {
		return message;
	}
}
