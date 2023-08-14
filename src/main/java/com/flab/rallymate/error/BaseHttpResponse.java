package com.flab.rallymate.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseHttpResponse<T> {

    private T data;
    private String message;

    public static <T> BaseHttpResponse<T> success(T data) {
        return new BaseHttpResponse<>(data, null);
    }

    public static <T> BaseHttpResponse<T> successWithNoContent() {
        return new BaseHttpResponse<>(null, null);
    }

    public static <T> BaseHttpResponse<T> successWithNoContent(String message) {
        return new BaseHttpResponse<>(null, message);
    }

    public static <T> BaseHttpResponse<T> error(String message) {
        return new BaseHttpResponse<>(null, message);
    }

    private BaseHttpResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

}