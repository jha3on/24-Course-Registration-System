package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.course.CourseRegistrationCart;
import com.demo.domain.payload.response.CourseRegistrationCartQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationCartRepositorySupport {

    /**
     * 예비 수강 신청 중복을 확인한다.
     */
    Boolean existsByUserCourse(Long userId, Long courseId);

    /**
     * 예비 수강 신청을 조회한다.
     */
    Optional<CourseRegistrationCart> getBy(Long courseRegistrationCartId);

    /**
     * 예비 수강 신청을 조회한다.
     */
    Optional<CourseRegistrationCart> getByUser(Long userId, Long courseRegistrationCartId);

    /**
     * 예비 수강 신청을 조회한다.
     */
    Optional<CourseRegistrationCart> getByUserCourse(Long userId, Long courseId);

    /**
     * 예비 수강 신청 목록을 조회한다.
     */
    List<CourseRegistrationCartQueryResultDto> getProjectionsByUser(Long userId);
}