package com.demo.global.common.exception.handler;

import com.demo.global.common.api.ApiResponse;
import com.demo.global.common.api.enums.ApiResponseType;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<?> handleApiException(ApiException e) {
        var status = e.getStatus();
        var statusType = e.getExceptionType();

        log.info("[INFO] status: {}, statusType: {}", status, statusType);
        return ApiResponse.error(e);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var statusType = ApiResponseType.API_ERROR;

        log.info("[INFO] status: {}, statusType: {}", status, statusType);
        return ApiResponse.error(ApiException.of(HttpStatus.INTERNAL_SERVER_ERROR, ApiExceptionType.COMMON));
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var statusType = ApiResponseType.API_ERROR;

        log.info("[INFO] status: {}, statusType: {}", status, statusType);
        return ApiResponse.error(ApiException.of(HttpStatus.INTERNAL_SERVER_ERROR, ApiExceptionType.COMMON));
    }
}