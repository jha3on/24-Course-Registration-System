package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.payload.response.CollegeQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CollegeRepositorySupport {

    /**
     * 대학 중복을 확인한다.
     */
    Boolean existsBy(String collegeName, String collegeNumber);

    /**
     * 대학을 조회한다.
     */
    Optional<College> getBy(Long collegeId);

    /**
     * 대학을 조회한다.
     */
    Optional<College> getBy(String collegeName, String collegeNumber);

    /**
     * 대학 목록을 조회한다.
     */
    List<CollegeQueryResultDto> getProjectionsBy();
}