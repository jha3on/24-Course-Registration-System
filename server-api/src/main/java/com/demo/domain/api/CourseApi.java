package com.demo.domain.api;

import com.demo.domain.payload.response.CourseQueryResultDto;
import com.demo.domain.payload.response.CourseRegistrationCartQueryResultDto;
import com.demo.domain.payload.response.CourseRegistrationQueryResultDto;
import com.demo.domain.service.course.CourseService;
import com.demo.global.common.api.ApiResponse;
import com.demo.global.configuration.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseApi {
    private final CourseService courseService;

    @Operation(summary = "API 상태 확인", description = "API 상태를 확인한다.")
    @GetMapping(value = {"/api/courses/status"})
    public ResponseEntity<?> getStatus() {
        return ApiResponse.ok();
    }

    @Operation(summary = "강의 목록 조회", description = "강의 목록을 조회한다.")
    @GetMapping(value = {"/api/courses"})
    public ResponseEntity<?> getCourses(
        @RequestParam(name = "courseYear", required = true) String courseYear,
        @RequestParam(name = "courseSemester", required = true) String courseSemester,
        @RequestParam(name = "courseType", required = true) String courseType,
        @RequestParam(name = "collegeId", required = true) Long collegeId,
        @RequestParam(name = "collegeDivisionId", required = false) Long collegeDivisionId,
        @RequestParam(name = "collegeDepartmentId", required = false) Long collegeDepartmentId
    ) {
        List<CourseQueryResultDto>  apiResult = courseService.getCourseDtos(courseYear, courseSemester, courseType, collegeId, collegeDivisionId, collegeDepartmentId);

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "강의 목록 조회", description = "강의 검색 목록을 조회한다.")
    @GetMapping(value = {"/api/courses/search"})
    public ResponseEntity<?> getCourses(
        @RequestParam(name = "courseYear", required = true) String courseYear,
        @RequestParam(name = "courseSemester", required = true) String courseSemester,
        @RequestParam(name = "courseNumber", required = true) String courseNumber
    ) {
        List<CourseQueryResultDto> apiResult = courseService.getCourseDtos(courseYear, courseSemester, courseNumber);

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "수강 신청 목록 조회", description = "수강 신청 목록을 조회한다.")
    @GetMapping(value = {"/api/courses/registrations"})
    public ResponseEntity<?> getCourseRegistrations(
        // ...
    ) {
        List<CourseRegistrationQueryResultDto> apiResult = courseService.getCourseRegistrationDtos(SecurityUtils.getUserId());

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "수강 신청 생성", description = "수강 신청을 생성한다.")
    @PostMapping(value = {"/api/courses/registrations"})
    public ResponseEntity<?> createCourseRegistration(
        @RequestParam(name = "courseId", required = true) Long courseId
    ) {
        courseService.createCourseRegistration(SecurityUtils.getUserId(), courseId);

        return ApiResponse.ok();
    }

    @Operation(summary = "수강 신청 삭제", description = "수강 신청을 삭제한다.")
    @DeleteMapping(value = {"/api/courses/registrations"})
    public ResponseEntity<?> deleteCourseRegistration(
        @RequestParam(name = "courseId", required = true) Long courseId
    ) {
        courseService.deleteCourseRegistration(SecurityUtils.getUserId(), courseId);

        return ApiResponse.ok();
    }

    @Operation(summary = "예비 수강 신청 목록 조회", description = "예비 수강 신청 목록을 조회한다.")
    @GetMapping(value = {"/api/courses/registrations/carts"})
    public ResponseEntity<?> getCourseRegistrationCarts(
        // ...
    ) {
        List<CourseRegistrationCartQueryResultDto> apiResult = courseService.getCourseRegistrationCartDtos(SecurityUtils.getUserId());

        return ApiResponse.ok(apiResult);
    }

    @Operation(summary = "예비 수강 신청 생성", description = "예비 수강 신청을 생성한다.")
    @PostMapping(value = {"/api/courses/registrations/carts"})
    public ResponseEntity<?> createCourseRegistrationCart(
        @RequestParam(name = "courseId", required = true) Long courseId
    ) {
        courseService.createCourseRegistrationCart(SecurityUtils.getUserId(), courseId);

        return ApiResponse.ok();
    }

    @Operation(summary = "예비 수강 신청 삭제", description = "예비 수강 신청을 삭제한다.")
    @DeleteMapping(value = {"/api/courses/registrations/carts"})
    public ResponseEntity<?> deleteCourseRegistrationCart(
        @RequestParam(name = "courseId", required = true) Long courseId
    ) {
        courseService.deleteCourseRegistrationCart(SecurityUtils.getUserId(), courseId);

        return ApiResponse.ok();
    }
}