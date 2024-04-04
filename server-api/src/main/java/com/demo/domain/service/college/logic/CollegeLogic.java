package com.demo.domain.service.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.repository.rds.college.CollegeRepository;
import com.demo.domain.payload.request.CollegeDto;
import com.demo.domain.payload.response.CollegeQueryResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollegeLogic {
    private final CollegeRepository collegeRepository;

    /**
     * 대학 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String collegeName, String collegeNumber) {
        return collegeRepository.existsBy(collegeName, collegeNumber);
    }

    /**
     * 대학을 생성한다.
     */
    @Transactional
    public College create(CollegeDto collegeDto) {
        College college = College.createBy(collegeDto.getCollegeName(), collegeDto.getCollegeNumber());

        return collegeRepository.save(college);
    }

    /**
     * 대학을 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<College> get(String collegeName, String collegeNumber) {
        return collegeRepository.getBy(collegeName, collegeNumber);
    }

    /**
     * 대학 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CollegeQueryResultDto> getDtos() {
        return collegeRepository.getProjectionsBy();
    }
}