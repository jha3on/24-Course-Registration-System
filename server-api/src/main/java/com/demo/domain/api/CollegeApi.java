package com.demo.domain.api;

import com.demo.domain.payload.response.CollegeCourseQueryResultDto;
import com.demo.domain.payload.response.CollegeDepartmentQueryResultDto;
import com.demo.domain.payload.response.CollegeDivisionQueryResultDto;
import com.demo.domain.payload.response.CollegeQueryResultDto;
import com.demo.domain.service.college.CollegeService;
import com.demo.global.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CollegeApi {
    private final CollegeService collegeService;

    @Operation(summary = "API 상태 확인", description = "API 상태를 확인한다.")
    @GetMapping(value = {"/api/colleges/status"})
    public ResponseEntity<?> getStatus() {
        return ApiResponse.ok();
    }

    @Operation(summary = "대학 목록 조회", description = "대학 목록을 조회한다.")
    @GetMapping(value = {"/api/colleges"})
    public ResponseEntity<?> getColleges(
        // ...
    ) {
        List<CollegeQueryResultDto> apiResult = collegeService.getCollegeDtos();

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "대학 학부 목록 조회", description = "대학에 속한 학부 목록을 조회한다.")
    @GetMapping(value = {"/api/colleges/{collegeId}/divisions"})
    public ResponseEntity<?> getCollegeDivisions(
        @PathVariable(value = "collegeId", required = true) Long collegeId
    ) {
        List<CollegeDivisionQueryResultDto> apiResult = collegeService.getCollegeDivisionDtos(collegeId);

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "대학 학과 목록 조회", description = "대학에 속한 학과 목록을 조회한다.")
    @GetMapping(value = {"/api/colleges/{collegeId}/departments"})
    public ResponseEntity<?> getCollegeDepartments(
        @PathVariable(value = "collegeId", required = true) Long collegeId
    ) {
        List<CollegeDepartmentQueryResultDto> apiResult = collegeService.getCollegeDepartmentDtos(collegeId);

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "대학 강의 목록 조회", description = "대학(학부, 학과)에 속한 강의 목록을 조회한다.")
    @GetMapping(value = {"/api/colleges/{collegeId}/courses"})
    public ResponseEntity<?> getCollegeCourses(
        @PathVariable(value = "collegeId", required = true) Long collegeId,
        @RequestParam(value = "collegeDivisionId", required = false) Long collegeDivisionId,
        @RequestParam(value = "collegeDepartmentId", required = false) Long collegeDepartmentId
    ) {
        List<CollegeCourseQueryResultDto> apiResult = collegeService.getCollegeCourseDtos(collegeId, collegeDivisionId, collegeDepartmentId);

        return ApiResponse.ok(apiResult);
    }
}