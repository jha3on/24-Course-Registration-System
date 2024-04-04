package com.demo.domain.entity.college;

import com.demo.global.common.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 대학 강의
 */
@Getter
@Entity
@Table(
    name = "tb_college_course",
    indexes = {
        // @Index(name = "tb_college_course_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "tb_college_course_uk_1", columnNames = {"number"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollegeCourse extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대학 강의, ID

    @Column(nullable = false)
    private String name; // 대학 강의, 이름

    @Column(nullable = false)
    private String number; // 대학 강의, 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private College college; // 대학 강의, 대학

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeDivision collegeDivision; // 대학 강의, 대학 학부

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeDepartment collegeDepartment; // 대학 강의, 대학 학과

    @Builder(access = AccessLevel.PRIVATE)
    public CollegeCourse(Long id, String name, String number, College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.college = college;
        this.collegeDivision = collegeDivision;
        this.collegeDepartment = collegeDepartment;
    }

    public static CollegeCourse createBy(College college, String collegeCourseName, String collegeCourseNumber) {
        return CollegeCourse.builder()
                .college(college)
                .name(collegeCourseName)
                .number(collegeCourseNumber)
                .build();
    }

    public static CollegeCourse createBy(College college, CollegeDepartment collegeDepartment, String collegeCourseName, String collegeCourseNumber) {
        return CollegeCourse.builder()
                .college(college)
                .collegeDepartment(collegeDepartment)
                .name(collegeCourseName)
                .number(collegeCourseNumber)
                .build();
    }

    public static CollegeCourse createBy(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, String collegeCourseName, String collegeCourseNumber) {
        return CollegeCourse.builder()
                .college(college)
                .collegeDivision(collegeDivision)
                .collegeDepartment(collegeDepartment)
                .name(collegeCourseName)
                .number(collegeCourseNumber)
                .build();
    }
}