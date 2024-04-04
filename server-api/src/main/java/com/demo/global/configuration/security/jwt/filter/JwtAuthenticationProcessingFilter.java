package com.demo.global.configuration.security.jwt.filter;

import com.demo.domain.payload.request.UserLoginDto;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private static final String DEFAULT_USER_LOGIN_REQUEST_URL = "/api/users/login";
    private static final RequestMatcher DEFAULT_USER_LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_USER_LOGIN_REQUEST_URL, HttpMethod.POST.name());

    public JwtAuthenticationProcessingFilter(ObjectMapper objectMapper) {
        super(DEFAULT_USER_LOGIN_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException, IOException {
        if (!StringUtils.hasText(request.getContentType())) {
            throw ApiException.of400(ApiExceptionType.COMMON_CONTENT_TYPE_NOT_VALID);
        }

        if (!request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw ApiException.of400(ApiExceptionType.COMMON_CONTENT_TYPE_NOT_SUPPORTED);
        }

        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        UserLoginDto userLoginDto = objectMapper.readValue(requestBody, UserLoginDto.class);
        UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(userLoginDto.getUserNumber(), userLoginDto.getUserPassword());

        return this.getAuthenticationManager().authenticate(userAuthentication);
    }
}