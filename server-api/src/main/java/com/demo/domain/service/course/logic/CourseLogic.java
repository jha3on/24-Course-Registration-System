package com.demo.domain.service.course.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.entity.course.Course;
import com.demo.domain.payload.request.CourseDto;
import com.demo.domain.payload.response.CourseQueryResultDto;
import com.demo.domain.repository.rds.course.CourseRepository;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseLogic {
    private final CourseRepository courseRepository;

    /**
     * 강의 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String courseNumber) {
        return courseRepository.existsBy(courseNumber);
    }

    /**
     * 강의를 생성한다.
     */
    @Transactional
    public Course create(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse, CourseDto courseDto) {
        Course course = Course.createBy(college, collegeDivision, collegeDepartment, collegeCourse, courseDto.getCourseType(), courseDto.getCourseNumber(), courseDto.getCourseTimetable(), courseDto.getCourseYear(), courseDto.getCourseSemester(), courseDto.getCourseCredit(), courseDto.getCourseRegistrationCountLimit());

        return courseRepository.save(course);
    }

    /**
     * 강의를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<Course> get(String courseNumber) {
        return courseRepository.getBy(courseNumber);
    }

    /**
     * 강의 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CourseQueryResultDto> getDtos(String courseYear, String courseSemester, String courseNumber) {
        // 개설 연도 값을 확인한다.
        if (!StringUtils.hasText(courseYear)) {
            throw ApiException.of400(ApiExceptionType.COURSE_SEARCH_COURSE_YEAR_NOT_VALID);
        }
        // 개설 학기 값을 확인한다.
        if (!StringUtils.hasText(courseSemester)) {
            throw ApiException.of400(ApiExceptionType.COURSE_SEARCH_COURSE_SEMESTER_NOT_VALID);
        }

        return courseRepository.getProjectionsBy(courseYear, courseSemester, courseNumber);
    }

    /**
     * 강의 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CourseQueryResultDto> getDtos(String courseYear, String courseSemester, String courseType, Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        // 개설 연도 값을 확인한다.
        if (!StringUtils.hasText(courseYear)) {
            throw ApiException.of400(ApiExceptionType.COURSE_SEARCH_COURSE_YEAR_NOT_VALID);
        }
        // 개설 학기 값을 확인한다.
        if (!StringUtils.hasText(courseSemester)) {
            throw ApiException.of400(ApiExceptionType.COURSE_SEARCH_COURSE_SEMESTER_NOT_VALID);
        }
        // 강의 구분 값을 확인한다.
        if (!StringUtils.hasText(courseType)) {
            throw ApiException.of400(ApiExceptionType.COURSE_SEARCH_COURSE_TYPE_NOT_VALID);
        }

        return courseRepository.getProjectionsBy(courseYear, courseSemester, courseType, collegeId, collegeDivisionId, collegeDepartmentId);
    }
}