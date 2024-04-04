package com.demo.domain.service.course;

import com.demo.domain.service.course.logic.CourseLogic;
import com.demo.domain.service.course.logic.CourseRegistrationCartLogic;
import com.demo.domain.service.course.logic.CourseRegistrationLogic;
import com.demo.domain.service.course.logic.CourseRegistrationQueueLogic;
import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.entity.course.Course;
import com.demo.domain.payload.request.CourseDto;
import com.demo.domain.payload.response.CourseQueryResultDto;
import com.demo.domain.payload.response.CourseRegistrationCartQueryResultDto;
import com.demo.domain.payload.response.CourseRegistrationQueryResultDto;
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
public class CourseService {
    private final CourseLogic courseLogic;
    private final CourseRegistrationLogic courseRegistrationLogic;
    private final CourseRegistrationQueueLogic courseRegistrationQueueLogic;
    private final CourseRegistrationCartLogic courseRegistrationCartLogic;

    /**
     * 강의를 생성한다.
     */
    public Course createCourse(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse, CourseDto courseDto) {
        if (courseLogic.duplicated(courseDto.getCourseNumber())) {
            throw ApiException.of409(ApiExceptionType.COURSE_EXISTED);
        }

        return courseLogic.create(college, collegeDivision, collegeDepartment, collegeCourse, courseDto);
    }

    /**
     * 강의를 조회한다.
     */
    public Optional<Course> getCourse(String courseNumber) {
        return courseLogic.get(courseNumber);
    }

    /**
     * 강의 목록을 조회한다.
     */
    public List<CourseQueryResultDto> getCourseDtos(String courseYear, String courseSemester, String courseNumber) {
        return courseLogic.getDtos(courseYear, courseSemester, courseNumber);
    }

    /**
     * 강의 목록을 조회한다.
     */
    public List<CourseQueryResultDto> getCourseDtos(String courseYear, String courseSemester, String courseType, Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        return courseLogic.getDtos(courseYear, courseSemester, courseType, collegeId, collegeDivisionId, collegeDepartmentId);
    }

    /**
     * 수강 신청 목록을 조회한다.
     */
    public List<CourseRegistrationQueryResultDto> getCourseRegistrationDtos(Long userId) {
        return courseRegistrationLogic.getDtos(userId);
    }

    /**
     * 수강 신청을 생성한다.
     */
    public void createCourseRegistration(Long userId, Long courseId) {
        // 수강 신청 조건을 확인한다.
        courseRegistrationLogic.validate(userId, courseId);
        // 수강 신청 순번을 대기열에 생성한다.
        courseRegistrationQueueLogic.setOrderResult(userId, courseId);
        // 스케줄러가 대기열을 확인하고 수강 신청을 생성한다.
        // courseRegistrationQueueScheduler.setResult();
    }

    /**
     * 수강 신청을 삭제한다.
     */
    public void deleteCourseRegistration(Long userId, Long courseId) {
        // 수강 신청을 삭제한다.
        courseRegistrationLogic.delete(userId, courseId);
        // 수강 인원을 수정한다.
        courseRegistrationQueueLogic.setCourseSeatCountPlus(courseId);
    }

    /**
     * 예비 수강 신청 목록을 조회한다.
     */
    public List<CourseRegistrationCartQueryResultDto> getCourseRegistrationCartDtos(Long userId) {
        return courseRegistrationCartLogic.getDtos(userId);
    }

    /**
     * 예비 수강 신청을 생성한다.
     */
    public void createCourseRegistrationCart(Long userId, Long courseId) {
        if (courseRegistrationCartLogic.duplicated(userId, courseId)) {
            throw ApiException.of409(ApiExceptionType.COURSE_REGISTRATION_CART_DUPLICATED);
        }

        courseRegistrationCartLogic.create(userId, courseId);
    }

    /**
     * 예비 수강 신청을 삭제한다.
     */
    public void deleteCourseRegistrationCart(Long userId, Long courseId) {
        courseRegistrationCartLogic.delete(userId, courseId);
    }
}