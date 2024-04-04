package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.payload.response.CollegeCourseQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CollegeCourseRepositorySupport {

    /**
     * 대학 강의 중복을 확인한다.
     */
    Boolean existsBy(String collegeCourseName, String collegeCourseNumber);

    /**
     * 대학 강의를 조회한다.
     */
    Optional<CollegeCourse> getBy(Long collegeCourseId);

    /**
     * 대학 강의를 조회한다.
     */
    Optional<CollegeCourse> getBy(String collegeCourseName, String collegeCourseNumber);

    /**
     * 대학 강의 목록을 조회한다.
     */
    List<CollegeCourseQueryResultDto> getProjectionsBy(Long collegeId, Long collegeDivisionId, Long collegeDepartmentId);
}