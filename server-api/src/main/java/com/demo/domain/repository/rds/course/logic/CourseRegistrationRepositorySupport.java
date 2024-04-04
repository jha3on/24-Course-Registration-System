package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.course.CourseRegistration;
import com.demo.domain.payload.response.CourseRegistrationQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationRepositorySupport {

    /**
     * 수강 신청 중복을 확인한다.
     */
    Boolean existsByUserCourse(Long userId, Long courseId);

    /**
     * 수강 신청 강의 중복을 확인한다.
     */
    Boolean existsByUserCollegeCourse(Long userId, Long collegeCourseId);

    /**
     * 수강 신청 개수를 조회한다.
     */
    Integer getCountByCourse(Long courseId);

    /**
     * 수강 신청을 조회한다.
     */
    Optional<CourseRegistration> getByUser(Long userId, Long courseRegistrationId);

    /**
     * 수강 신청을 조회한다.
     */
    Optional<CourseRegistration> getByUserCourse(Long userId, Long courseId);

    /**
     * 수강 신청 목록을 조회한다.
     */
    List<CourseRegistration> getsByUser(Long userId);

    /**
     * 수강 신청 목록을 조회한다.
     */
    List<CourseRegistrationQueryResultDto> getProjectionsByUser(Long userId);
}