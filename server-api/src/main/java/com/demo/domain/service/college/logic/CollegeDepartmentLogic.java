package com.demo.domain.service.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.payload.request.CollegeDepartmentDto;
import com.demo.domain.payload.response.CollegeDepartmentQueryResultDto;
import com.demo.domain.repository.rds.college.CollegeDepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollegeDepartmentLogic {
    private final CollegeDepartmentRepository collegeDepartmentRepository;

    /**
     * 대학 학과 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String collegeDepartmentName, String collegeDepartmentNumber) {
        return collegeDepartmentRepository.existsBy(collegeDepartmentName, collegeDepartmentNumber);
    }

    /**
     * 대학 학과를 생성한다.
     */
    @Transactional
    public CollegeDepartment create(College college, CollegeDepartmentDto collegeDepartmentDto) {
        CollegeDepartment collegeDepartment = CollegeDepartment.createBy(college, collegeDepartmentDto.getCollegeDepartmentName(), collegeDepartmentDto.getCollegeDepartmentNumber());

        return collegeDepartmentRepository.save(collegeDepartment);
    }

    /**
     * 대학 학과를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<CollegeDepartment> get(String collegeDepartmentName, String collegeDepartmentNumber) {
        return collegeDepartmentRepository.getBy(collegeDepartmentName, collegeDepartmentNumber);
    }

    /**
     * 대학 학과 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CollegeDepartmentQueryResultDto> getDtos(Long collegeId) {
        return collegeDepartmentRepository.getProjectionsBy(collegeId);
    }
}
