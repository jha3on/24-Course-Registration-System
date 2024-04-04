package com.demo.domain.entity.course;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.global.common.entity.AbstractEntity;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 강의
 */
@Getter
@Entity
@Table(
    name = "tb_course",
    indexes = {
        // @Index(name = "tb_course_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "tb_course_uk_1", columnNames = {"number"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 강의, ID

    @Column(nullable = false)
    private String type; // 강의, 강의 구분

    @Column(nullable = false)
    private String number; // 강의, 강의 번호

    @Column(nullable = false)
    private String timetable; // 강의, 강의 시간표

    @Column(nullable = false)
    private String year; // 강의, 강의 개설 연도

    @Column(nullable = false)
    private String semester; // 강의, 강의 개설 학기

    @Column(nullable = false)
    private Integer credit; // 강의, 강의 이수 학점

    @Column(nullable = false)
    private Integer registrationCount; // 강의, 수강 신청 인원

    @Column(nullable = false)
    private Integer registrationCountCart; // 강의, 예비 수강 신청 인원

    @Column(nullable = false)
    private Integer registrationCountLimit; // 강의, 수강 신청 제한 인원

    @ManyToOne(fetch = FetchType.LAZY)
    private College college; // 강의, 대학

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeCourse collegeCourse; // 강의, 대학 강의

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeDivision collegeDivision; // 강의, 대학 학부

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeDepartment collegeDepartment; // 강의, 대학 학과

    @Builder(access = AccessLevel.PRIVATE)
    public Course(Long id, String type, String number, String timetable, String year, String semester, Integer credit, Integer registrationCount, Integer registrationCountCart, Integer registrationCountLimit, College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse) {
        this.id = id;
        this.type = type;
        this.number = number;
        this.timetable = timetable;
        this.year = year;
        this.semester = semester;
        this.credit = credit;
        this.registrationCount = registrationCount;
        this.registrationCountCart = registrationCountCart;
        this.registrationCountLimit = registrationCountLimit;
        this.college = college;
        this.collegeCourse = collegeCourse;
        this.collegeDivision = collegeDivision;
        this.collegeDepartment = collegeDepartment;
    }

    public static Course createBy(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse, String courseType, String courseNumber, String courseTimetable, String courseYear, String courseSemester, Integer courseCredit, Integer courseRegistrationLimitCount) {
        return Course.builder()
                .type(courseType)
                .number(courseNumber)
                .timetable(courseTimetable)
                .year(courseYear)
                .semester(courseSemester)
                .credit(courseCredit)
                .registrationCount(0)
                .registrationCountCart(0)
                .registrationCountLimit(courseRegistrationLimitCount)
                .college(college)
                .collegeCourse(collegeCourse)
                .collegeDivision(collegeDivision)
                .collegeDepartment(collegeDepartment)
                .build();
    }

    public Integer getLeftCount() {
        return this.registrationCountLimit - this.registrationCount;
    }

    public Course updateByRegistrationCountPlus() {
        if (this.registrationCount > this.registrationCountLimit) {
            throw ApiException.of400(ApiExceptionType.COURSE_REGISTRATION_COUNT_NOT_VALID);
        }

        this.registrationCount = this.registrationCount + 1;
        return this;
    }

    public Course updateByRegistrationCountMinus() {
        if (this.registrationCount == 0) {
            throw ApiException.of400(ApiExceptionType.COURSE_REGISTRATION_COUNT_NOT_VALID);
        }

        this.registrationCount = this.registrationCount - 1;
        return this;
    }

    public Course updateByRegistrationCountCartPlus() {
        this.registrationCountCart = this.registrationCountCart + 1;
        return this;
    }

    public Course updateByRegistrationCountCartMinus() {
        if (this.registrationCountCart == 0) {
            throw ApiException.of400(ApiExceptionType.COURSE_REGISTRATION_CART_COUNT_NOT_VALID);
        }

        this.registrationCountCart = this.registrationCountCart - 1;
        return this;
    }
}