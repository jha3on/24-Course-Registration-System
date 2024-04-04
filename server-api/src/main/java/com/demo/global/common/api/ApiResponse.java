package com.demo.global.common.api;

import com.demo.global.common.api.enums.ApiResponseType;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T result;

    // -----------------------------------------------------------------------------------------------------------------

    public static ApiResponse<?> create() {
        return ApiResponse.builder()
                .code(ApiResponseType.API_OK.getCode())
                .message(ApiResponseType.API_OK.getMessage())
                .result(null)
                .build();
    }

    public static <T> ApiResponse<?> create(T result) {
        return ApiResponse.builder()
                .code(ApiResponseType.API_OK.getCode())
                .message(ApiResponseType.API_OK.getMessage())
                .result(result)
                .build();
    }

    public static <T> ApiResponse<?> create(String message, T result) {
        return ApiResponse.builder()
                .code(ApiResponseType.API_OK.getCode())
                .message(message)
                .result(result)
                .build();
    }

    public static ApiResponse<?> create(ApiExceptionType exceptionType) {
        return ApiResponse.builder()
                .code(exceptionType.getCode())
                .message(exceptionType.getMessage())
                .result(null)
                .build();
    }

    public static <T> ApiResponse<?> create(ApiExceptionType exceptionType, T result) {
        return ApiResponse.builder()
                .code(exceptionType.getCode())
                .message(exceptionType.getMessage())
                .result(result)
                .build();
    }

    public static <T> ApiResponse<?> create(ApiExceptionType exceptionType, String message, T result) {
        return ApiResponse.builder()
                .code(exceptionType.getCode())
                .message(message)
                .result(result)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static ResponseEntity<?> ok() {
        var response = ApiResponse.create();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<?> ok(T result) {
        var response = ApiResponse.create(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<?> ok(String message, T result) {
        var response = ApiResponse.create(message, result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<?> error(ApiException exception) {
        var response = create(exception.getExceptionType());

        return new ResponseEntity<>(response, exception.getStatus());
    }

    public static <T> ResponseEntity<?> error(ApiException exception, T result) {
        var response = ApiResponse.create(exception.getExceptionType(), result);

        return new ResponseEntity<>(response, exception.getStatus());
    }

    public static <T> ResponseEntity<?> error(ApiException exception, String message, T result) {
        var response = ApiResponse.create(exception.getExceptionType(), message, result);

        return new ResponseEntity<>(response, exception.getStatus());
    }

    // -----------------------------------------------------------------------------------------------------------------
}