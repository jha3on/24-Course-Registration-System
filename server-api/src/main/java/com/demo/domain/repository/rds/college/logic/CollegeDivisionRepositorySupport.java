package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.payload.response.CollegeDivisionQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface CollegeDivisionRepositorySupport {

    /**
     * 대학 학부 중복을 확인한다.
     */
    Boolean existsBy(String collegeDivisionName, String collegeDivisionNumber);

    /**
     * 대학 학부를 조회한다.
     */
    Optional<CollegeDivision> getBy(Long collegeDivisionId);

    /**
     * 대학 학부를 조회한다.
     */
    Optional<CollegeDivision> getBy(String collegeDivisionName, String collegeDivisionNumber);

    /**
     * 대학 학부 목록을 조회한다.
     */
    List<CollegeDivisionQueryResultDto> getProjectionsBy(Long collegeId);
}