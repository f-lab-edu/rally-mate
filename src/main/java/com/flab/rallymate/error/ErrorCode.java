package com.flab.rallymate.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다"),
	NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "해당 유저를 찾을 수 없습니다"),
	PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다"),


	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");

	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
