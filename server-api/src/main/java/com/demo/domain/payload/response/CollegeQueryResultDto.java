package com.demo.domain.payload.response;

import com.demo.domain.entity.college.College;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeQueryResultDto implements Serializable {
    private Long id;
    private String name;
    private String number;

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeQueryResultDto(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeQueryResultDto(College college) {
        this.id = college.getId();
        this.name = college.getName();
        this.number = college.getNumber();
    }
}