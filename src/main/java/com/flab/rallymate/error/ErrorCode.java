package com.flab.rallymate.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
	NOT_FOUND_KAKAO_USER_INFO(HttpStatus.NOT_FOUND, "요청하신 카카오 유저 정보를 찾을 수 없습니다."),
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

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
