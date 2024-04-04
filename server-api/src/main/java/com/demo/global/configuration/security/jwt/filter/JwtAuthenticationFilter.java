package com.demo.global.configuration.security.jwt.filter;

import com.demo.global.configuration.security.jwt.JwtAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationService jwtAuthenticationService;
    private static final String DEFAULT_USER_TOKEN_TYPE = "Bearer";
    private static final String DEFAULT_USER_LOGIN_REQUEST_URL = "/api/users/login";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getRequestURI().equals(DEFAULT_USER_LOGIN_REQUEST_URL)) {
            filterChain.doFilter(request, response);
            return; // -> JwtAuthenticationProcessingFilter
        }

        String token = getAuthenticationToken(request);

        if (StringUtils.hasText(token) && jwtAuthenticationService.validate(token)) {
            Authentication authentication = jwtAuthenticationService.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(token)) {
            if (token.startsWith(DEFAULT_USER_TOKEN_TYPE)) {
                return token.replace(DEFAULT_USER_TOKEN_TYPE, "").trim();
            }
        }

        return null;
    }
}