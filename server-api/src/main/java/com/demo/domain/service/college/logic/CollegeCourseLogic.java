package com.demo.domain.service.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.payload.request.CollegeCourseDto;
import com.demo.domain.payload.response.CollegeCourseQueryResultDto;
import com.demo.domain.repository.rds.college.CollegeCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollegeCourseLogic {
    private final CollegeCourseRepository collegeCourseRepository;

    /**
     * 대학 강의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String collegeCourseName, String collegeCourseNumber) {
        return collegeCourseRepository.existsBy(collegeCourseName, collegeCourseNumber);
    }

    /**
     * 대학 강의를 생성한다.
     */
    @Transactional
    public CollegeCourse create(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourseDto collegeCourseDto) {
        CollegeCourse collegeCourse = CollegeCourse.createBy(college, collegeDivision, collegeDepartment, collegeCourseDto.getCollegeCourseName(), collegeCourseDto.getCollegeCourseNumber());

        return collegeCourseRepository.save(collegeCourse);
    }

    /**
     * 대학 강의를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<CollegeCourse> get(String collegeCourseName, String collegeCourseNumber) {
        return collegeCourseRepository.getBy(collegeCourseName, collegeCourseNumber);
    }

    /**
     * 대학 강의 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CollegeCourseQueryResultDto> getDtos(Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        return collegeCourseRepository.getProjectionsBy(collegeId, collegeDivisionId, collegeDepartmentId);
    }
}