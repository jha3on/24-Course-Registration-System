package com.demo.global.common.api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiResponseType {
    // -----------------------------------------------------------------------------------------------------------------
    API_OK("+100", "요청을 수행하였습니다."),
    API_ERROR("-100", "요청을 수행할 수 없습니다."),
    // -----------------------------------------------------------------------------------------------------------------
    ;

    private final String code;
    private final String message;
}