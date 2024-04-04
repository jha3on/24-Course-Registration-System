package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CourseDto implements Serializable {
    private String courseType;
    private String courseNumber;
    private String courseTimetable;
    private String courseYear;
    private String courseSemester;
    private Integer courseCredit;
    private Integer courseRegistrationCountLimit;

    @Builder(access = AccessLevel.PUBLIC)
    public CourseDto(String courseType, String courseNumber, String courseTimetable, String courseYear, String courseSemester, Integer courseCredit, Integer courseRegistrationCountLimit) {
        this.courseType = courseType;
        this.courseNumber = courseNumber;
        this.courseTimetable = courseTimetable;
        this.courseYear = courseYear;
        this.courseSemester = courseSemester;
        this.courseCredit = courseCredit;
        this.courseRegistrationCountLimit = courseRegistrationCountLimit;
    }

    public static CourseDto of(String courseType, String courseNumber, String courseTimetable, String courseYear, String courseSemester, Integer courseCredit, Integer courseRegistrationCountLimit) {
        return CourseDto.builder()
                .courseType(courseType)
                .courseNumber(courseNumber)
                .courseTimetable(courseTimetable)
                .courseYear(courseYear)
                .courseSemester(courseSemester)
                .courseCredit(courseCredit)
                .courseRegistrationCountLimit(courseRegistrationCountLimit)
                .build();
    }
}