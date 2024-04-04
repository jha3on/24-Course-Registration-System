package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.course.Course;
import com.demo.domain.payload.response.CourseQueryResultDto;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CourseRepositorySupport {

    /**
     * 강의 중복을 확인한다.
     */
    Boolean existsBy(String courseNumber);

    /**
     * 강의를 조회한다.
     */
    Optional<Course> getBy(Long courseId);

    /**
     * 강의를 조회한다.
     */
    Optional<Course> getBy(String courseNumber);

    /**
     * 강의를 조회한다.
     */
    Optional<Course> getBy(Long courseId, LockModeType lockModeType);

    /**
     * 수강 신청 인원이 있는 강의 목록을 조회한다.
     * (여러 명의 사용자가 동일한 강의를 수강 신청한 경우는 제외한다.)
     */
    List<Course> getsBy();

    /**
     * 강의 목록을 조회한다.
     */
    List<CourseQueryResultDto> getProjectionsBy(String courseYear, String courseSemester, String courseNumber);

    /**
     * 강의 목록을 조회한다.
     */
    List<CourseQueryResultDto> getProjectionsBy(String courseYear, String courseSemester, String courseType, Long collegeId, Long collegeDivisionId, Long collegeDepartmentId);
}