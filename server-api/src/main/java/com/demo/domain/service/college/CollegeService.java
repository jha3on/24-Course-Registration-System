package com.demo.domain.service.college;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.service.college.logic.CollegeCourseLogic;
import com.demo.domain.service.college.logic.CollegeDepartmentLogic;
import com.demo.domain.service.college.logic.CollegeDivisionLogic;
import com.demo.domain.service.college.logic.CollegeLogic;
import com.demo.domain.payload.request.CollegeCourseDto;
import com.demo.domain.payload.request.CollegeDepartmentDto;
import com.demo.domain.payload.request.CollegeDivisionDto;
import com.demo.domain.payload.request.CollegeDto;
import com.demo.domain.payload.response.CollegeCourseQueryResultDto;
import com.demo.domain.payload.response.CollegeDepartmentQueryResultDto;
import com.demo.domain.payload.response.CollegeDivisionQueryResultDto;
import com.demo.domain.payload.response.CollegeQueryResultDto;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeLogic collegeLogic;
    private final CollegeDivisionLogic collegeDivisionLogic;
    private final CollegeDepartmentLogic collegeDepartmentLogic;
    private final CollegeCourseLogic collegeCourseLogic;

    /**
     * 대학을 생성한다.
     */
    public College createCollege(CollegeDto collegeDto) {
        if (collegeLogic.duplicated(collegeDto.getCollegeName(), collegeDto.getCollegeNumber())) {
            throw ApiException.of409(ApiExceptionType.COLLEGE_EXISTED);
        }

        return collegeLogic.create(collegeDto);
    }

    /**
     * 대학을 조회한다.
     */
    public Optional<College> getCollege(String collegeName, String collegeNumber) {
        return collegeLogic.get(collegeName, collegeNumber);
    }

    /**
     * 대학 목록을 조회한다.
     */
    public List<CollegeQueryResultDto> getCollegeDtos() {
        return collegeLogic.getDtos();
    }

    /**
     * 대학 학부를 생성한다.
     */
    public CollegeDivision createCollegeDivision(College college, CollegeDivisionDto collegeDivisionDto) {
        if (collegeDivisionLogic.duplicated(collegeDivisionDto.getCollegeDivisionName(), collegeDivisionDto.getCollegeDivisionNumber())) {
            throw ApiException.of409(ApiExceptionType.COLLEGE_DIVISION_EXISTED);
        }

        return collegeDivisionLogic.create(college, collegeDivisionDto);
    }

    /**
     * 대학 학부를 조회한다.
     */
    public Optional<CollegeDivision> getCollegeDivision(String collegeDivisionName, String collegeDivisionNumber) {
        return collegeDivisionLogic.get(collegeDivisionName, collegeDivisionNumber);
    }

    /**
     * 대학 학부 목록을 조회한다.
     */
    public List<CollegeDivisionQueryResultDto> getCollegeDivisionDtos(Long collegeId) {
        return collegeDivisionLogic.getDtos(collegeId);
    }

    /**
     * 대학 학과를 생성한다.
     */
    public CollegeDepartment createCollegeDepartment(College college, CollegeDepartmentDto collegeDepartmentDto) {
        if (collegeDepartmentLogic.duplicated(collegeDepartmentDto.getCollegeDepartmentName(), collegeDepartmentDto.getCollegeDepartmentNumber())) {
            throw ApiException.of409(ApiExceptionType.COLLEGE_DEPARTMENT_EXISTED);
        }

        return collegeDepartmentLogic.create(college, collegeDepartmentDto);
    }

    /**
     * 대학 학과를 조회한다.
     */
    public Optional<CollegeDepartment> getCollegeDepartment(String collegeDepartmentName, String collegeDepartmentNumber) {
        return collegeDepartmentLogic.get(collegeDepartmentName, collegeDepartmentNumber);
    }

    /**
     * 대학 학과 목록을 조회한다.
     */
    public List<CollegeDepartmentQueryResultDto> getCollegeDepartmentDtos(Long collegeId) {
        return collegeDepartmentLogic.getDtos(collegeId);
    }

    /**
     * 대학 강의를 생성한다.
     */
    public CollegeCourse createCollegeCourse(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourseDto collegeCourseDto) {
        if (collegeCourseLogic.duplicated(collegeCourseDto.getCollegeCourseName(), collegeCourseDto.getCollegeCourseNumber())) {
            throw ApiException.of409(ApiExceptionType.COLLEGE_COURSE_EXISTED);
        }

        return collegeCourseLogic.create(college, collegeDivision, collegeDepartment, collegeCourseDto);
    }

    /**
     * 대학 강의를 조회한다.
     */
    public Optional<CollegeCourse> getCollegeCourse(String collegeCourseName, String collegeCourseNumber) {
        return collegeCourseLogic.get(collegeCourseName, collegeCourseNumber);
    }

    /**
     * 대학 강의 목록을 조회한다.
     */
    public List<CollegeCourseQueryResultDto> getCollegeCourseDtos(Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        return collegeCourseLogic.getDtos(collegeId, collegeDivisionId, collegeDepartmentId);
    }
}