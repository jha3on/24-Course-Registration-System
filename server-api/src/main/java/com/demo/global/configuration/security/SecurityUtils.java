package com.demo.global.configuration.security;

import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.demo.global.configuration.security.login.UserLoginDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {
    public static Long getUserId() {
        return getUserDetails().getId();
    }

    public static String getUserNumber() {
        return getUserDetails().getNumber();
    }

    public static UserLoginDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            throw ApiException.of401(ApiExceptionType.USER_AUTHENTICATION_NOT_VALID);
        }

        return (UserLoginDetails) authentication.getPrincipal();
    }
}