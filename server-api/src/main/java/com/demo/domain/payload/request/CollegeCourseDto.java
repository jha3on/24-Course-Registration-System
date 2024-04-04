package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"collegeCourseName", "collegeCourseNumber"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeCourseDto implements Serializable {
    private String collegeCourseName;
    private String collegeCourseNumber;

    @Builder(access = AccessLevel.PUBLIC)
    public CollegeCourseDto(String collegeCourseName, String collegeCourseNumber) {
        this.collegeCourseName = collegeCourseName;
        this.collegeCourseNumber = collegeCourseNumber;
    }

    public static CollegeCourseDto of(String collegeCourseName, String collegeCourseNumber) {
        return CollegeCourseDto.builder()
                .collegeCourseName(collegeCourseName)
                .collegeCourseNumber(collegeCourseNumber)
                .build();
    }
}