package com.demo.global.configuration.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.demo.global.configuration.security.login.UserLoginDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final UserLoginDetailsService userLoginDetailsService;

    @Value(value = "${app.jwt.secret-key}")
    private String secretKey;

    public String create(String userNumber) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + (3 * 60 * 60 * 1000L)); // 3시간

        return JWT.create()
                .withSubject("token")
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .withClaim("user", userNumber)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public Boolean validate(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication authenticate(String token) {
        UserDetails userDetails = userLoginDetailsService.loadUserByUsername(getUserNumber(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUserNumber(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("user")
                    .asString();
        } catch (Exception e) {
            throw ApiException.of400(ApiExceptionType.USER_AUTHENTICATION_TOKEN_NOT_VALID);
        }
    }
}