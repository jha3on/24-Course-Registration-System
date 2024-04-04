package com.demo.global.configuration.security;

import com.demo.global.configuration.security.jwt.JwtAuthenticationService;
import com.demo.global.configuration.security.jwt.filter.JwtAuthenticationFilter;
import com.demo.global.configuration.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.demo.global.configuration.security.login.handler.UserLoginFailureHandler;
import com.demo.global.configuration.security.login.handler.UserLoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final AuthenticationConfiguration authenticationConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder(4); // 기본값 10 (= -1)
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig) throws Exception {
        return authenticationConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtAuthenticationService);
        return filter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() throws Exception {
        JwtAuthenticationProcessingFilter filter = new JwtAuthenticationProcessingFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager(authenticationConfig));
        filter.setAuthenticationSuccessHandler(userLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(userLoginFailureHandler());
        return filter;
    }

    @Bean
    public UserLoginSuccessHandler userLoginSuccessHandler() {
        return new UserLoginSuccessHandler(objectMapper, jwtAuthenticationService);
    }

    @Bean
    public UserLoginFailureHandler userLoginFailureHandler() {
        return new UserLoginFailureHandler(objectMapper);
    }

    @Bean
    public SecurityUnauthorizedHandler securityUnauthorizedHandler() {
        return new SecurityUnauthorizedHandler(objectMapper);
    }

    @Bean
    public SecurityUnauthenticatedHandler securityUnauthenticatedHandler() {
        return new SecurityUnauthenticatedHandler(objectMapper);
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.sessionManagement(e -> e
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(e -> e
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers("/ws/**", "/ws-sub/**").permitAll()
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/**", "/api/colleges/**", "/api/courses/**").permitAll()
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated()
        );

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(securityUnauthenticatedHandler())
                .accessDeniedHandler(securityUnauthorizedHandler()));

        http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class); // 1. LogoutFilter -> 2. JwtAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), JwtAuthenticationProcessingFilter.class); // 2. JwtAuthenticationFilter -> 3. JwtAuthenticationProcessingFilter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOriginPatterns(Arrays.asList("http://localhost:8080", "http://localhost:63342"));
        corsConfig.setAllowedMethods(Arrays.asList("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Content-Length", "Cache-Control"));
        corsConfig.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Content-Length", "Cache-Control"));

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }
}