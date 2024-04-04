package com.demo.domain.payload.response;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.entity.course.Course;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.util.ObjectUtils;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CourseQueryResultDto implements Serializable {
    private Long courseId; // 강의, ID
    private String courseType; // 강의, 강의 구분
    private String courseNumber; // 강의, 강의 번호
    private String courseTimetable; // 강의, 강의 시간표
    private Integer courseCredit; // 강의, 강의 이수 학점
    private Integer courseRegistrationCount; // 강의, 수강 신청 인원
    private Integer courseRegistrationCountCart; // 강의, 예비 수강 신청 인원
    private Integer courseRegistrationCountLimit; // 강의, 수강 신청 제한 인원
    private String collegeName; // 대학, 이름
    private String collegeDivisionName; // 대학 학부, 이름
    private String collegeDepartmentName; // 대학 학과, 이름
    private String collegeCourseName; // 대학 강의, 이름
    private String collegeCourseNumber; // 대학 강의, 번호

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CourseQueryResultDto(Long courseId, String courseType, String courseNumber, String courseTimetable, Integer courseCredit, Integer courseRegistrationCount, Integer courseRegistrationCountCart, Integer courseRegistrationCountLimit, String collegeName, String collegeDivisionName, String collegeDepartmentName, String collegeCourseName, String collegeCourseNumber) {
        this.courseId = courseId;
        this.courseType = courseType;
        this.courseNumber = courseNumber;
        this.courseTimetable = courseTimetable;
        this.courseCredit = courseCredit;
        this.courseRegistrationCount = courseRegistrationCount;
        this.courseRegistrationCountCart = courseRegistrationCountCart;
        this.courseRegistrationCountLimit = courseRegistrationCountLimit;
        this.collegeName = collegeName;
        this.collegeDivisionName = collegeDivisionName;
        this.collegeDepartmentName = collegeDepartmentName;
        this.collegeCourseName = collegeCourseName;
        this.collegeCourseNumber = collegeCourseNumber;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CourseQueryResultDto(Course course, College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse) {
        this.courseId = course.getId();
        this.courseType = course.getType();
        this.courseNumber = course.getNumber();
        this.courseTimetable = course.getTimetable();
        this.courseCredit = course.getCredit();
        this.courseRegistrationCount = course.getRegistrationCount();
        this.courseRegistrationCountCart = course.getRegistrationCountCart();
        this.courseRegistrationCountLimit = course.getRegistrationCountLimit();
        this.collegeName = college.getName();
        this.collegeDivisionName = ObjectUtils.isEmpty(collegeDivision) ? "" : collegeDivision.getName();
        this.collegeDepartmentName = ObjectUtils.isEmpty(collegeDepartment) ? "" : collegeDepartment.getName();
        this.collegeCourseName = collegeCourse.getName();
        this.collegeCourseNumber = collegeCourse.getNumber();
    }
}