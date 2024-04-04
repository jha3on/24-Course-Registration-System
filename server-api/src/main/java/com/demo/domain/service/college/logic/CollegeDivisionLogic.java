package com.demo.domain.service.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.payload.request.CollegeDivisionDto;
import com.demo.domain.payload.response.CollegeDivisionQueryResultDto;
import com.demo.domain.repository.rds.college.CollegeDivisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollegeDivisionLogic {
    private final CollegeDivisionRepository collegeDivisionRepository;

    /**
     * 대학 학부 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String collegeDivisionName, String collegeDivisionNumber) {
        return collegeDivisionRepository.existsBy(collegeDivisionName, collegeDivisionNumber);
    }

    /**
     * 대학 학부를 생성한다.
     */
    @Transactional
    public CollegeDivision create(College college, CollegeDivisionDto collegeDivisionDto) {
        CollegeDivision collegeDivision = CollegeDivision.createBy(college, collegeDivisionDto.getCollegeDivisionName(), collegeDivisionDto.getCollegeDivisionNumber());

        return collegeDivisionRepository.save(collegeDivision);
    }

    /**
     * 대학 학부를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<CollegeDivision> get(String collegeDivisionName, String collegeDivisionNumber) {
        return collegeDivisionRepository.getBy(collegeDivisionName, collegeDivisionNumber);
    }

    /**
     * 대학 학부 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CollegeDivisionQueryResultDto> getDtos(Long collegeId) {
        return collegeDivisionRepository.getProjectionsBy(collegeId);
    }
}