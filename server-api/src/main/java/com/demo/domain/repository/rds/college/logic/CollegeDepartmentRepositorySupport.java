package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.payload.response.CollegeDepartmentQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CollegeDepartmentRepositorySupport {

    /**
     * 대학 학과 중복을 확인한다.
     */
    Boolean existsBy(String collegeDepartmentName, String collegeDepartmentNumber);

    /**
     * 대학 학과를 조회한다.
     */
    Optional<CollegeDepartment> getBy(Long collegeDepartmentId);

    /**
     * 대학 학과를 조회한다.
     */
    Optional<CollegeDepartment> getBy(String collegeDepartmentName, String collegeDepartmentNumber);

    /**
     * 대학 학과 목록을 조회한다.
     */
    List<CollegeDepartmentQueryResultDto> getProjectionsBy(Long collegeId);
}