package com.flab.rallymate.common.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseHttpResponse<T> {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String ERROR = "error";

    private String status;
    private T data;
    private String message;

    public static <T> BaseHttpResponse<T> success(T data) {
        return new BaseHttpResponse<>(SUCCESS, data, null);
    }

    public static BaseHttpResponse<?> successWithNoContent() {
        return new BaseHttpResponse<>(SUCCESS, null, null);
    }

    public static BaseHttpResponse<?> fail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new BaseHttpResponse<>(FAIL, errors, null);
    }

    public static BaseHttpResponse<?> error(String message) {
        return new BaseHttpResponse<>(ERROR, null, message);
    }

    private BaseHttpResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

}
