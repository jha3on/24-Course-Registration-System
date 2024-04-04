package com.demo.domain.payload.response;

import com.demo.domain.entity.college.CollegeDivision;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeDivisionQueryResultDto implements Serializable {
    private Long id;
    private String name;
    private String number;

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDivisionQueryResultDto(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDivisionQueryResultDto(CollegeDivision collegeDivision) {
        this.id = collegeDivision.getId();
        this.name = collegeDivision.getName();
        this.number = collegeDivision.getNumber();
    }
}