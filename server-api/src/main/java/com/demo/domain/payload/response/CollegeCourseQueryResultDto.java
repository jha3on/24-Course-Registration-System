package com.demo.domain.payload.response;

import com.demo.domain.entity.college.CollegeCourse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeCourseQueryResultDto implements Serializable {
    private Long id;
    private String name;
    private String number;

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeCourseQueryResultDto(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeCourseQueryResultDto(CollegeCourse collegeCourse) {
        this.id = collegeCourse.getId();
        this.name = collegeCourse.getName();
        this.number = collegeCourse.getNumber();
    }
}