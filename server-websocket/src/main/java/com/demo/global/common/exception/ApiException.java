package com.demo.global.common.exception;

import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final ApiExceptionType exceptionType;

    protected ApiException(HttpStatus status, ApiExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.status = status;
        this.exceptionType = exceptionType;
    }

    public static ApiException of(HttpStatus status, ApiExceptionType exceptionType) {
        return new ApiException(status, exceptionType);
    }

    public static ApiException of400(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.BAD_REQUEST, exceptionType);
    }

    public static ApiException of401(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.UNAUTHORIZED, exceptionType);
    }

    public static ApiException of403(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.FORBIDDEN, exceptionType);
    }

    public static ApiException of404(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.NOT_FOUND, exceptionType);
    }

    public static ApiException of409(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.CONFLICT, exceptionType);
    }

    public static ApiException of500(ApiExceptionType exceptionType) {
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionType);
    }
}