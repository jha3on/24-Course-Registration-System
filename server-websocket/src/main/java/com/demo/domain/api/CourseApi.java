package com.demo.domain.api;

import com.demo.global.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseApi {

    @Operation(summary = "API 상태 확인", description = "API 상태를 확인한다.")
    @GetMapping(value = {"/api/courses/status"})
    public ResponseEntity<?> getStatus() {
        return ApiResponse.ok();
    }
}