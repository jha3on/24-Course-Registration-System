package com.demo.domain.api;

import com.demo.domain.payload.response.UserQueryResultDto;
import com.demo.domain.service.user.UserService;
import com.demo.global.common.api.ApiResponse;
import com.demo.global.configuration.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @Operation(summary = "API 상태 확인", description = "API 상태를 확인한다.")
    @GetMapping(value = {"/api/users/status"})
    public ResponseEntity<?> getStatus() {
        return ApiResponse.ok();
    }

    @Operation(summary = "사용자 조회", description = "사용자를 조회한다.")
    @GetMapping(value = {"/api/users/me"})
    public ResponseEntity<?> getUser(
        // ...
    ) {
        UserQueryResultDto apiResult = userService.getUserDto(SecurityUtils.getUserId());

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회한다.")
    @GetMapping(value = {"/api/users"})
    public ResponseEntity<?> getUsers(
        // ...
    ) {
        List<UserQueryResultDto> apiResult = userService.getUserDtos();

        return ApiResponse.ok(apiResult);
    }
}