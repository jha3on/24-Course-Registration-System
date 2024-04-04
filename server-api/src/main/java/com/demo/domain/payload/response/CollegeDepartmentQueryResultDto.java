package com.demo.domain.payload.response;

import com.demo.domain.entity.college.CollegeDepartment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeDepartmentQueryResultDto implements Serializable {
    private Long id;
    private String name;
    private String number;

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDepartmentQueryResultDto(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDepartmentQueryResultDto(CollegeDepartment collegeDepartment) {
        this.id = collegeDepartment.getId();
        this.name = collegeDepartment.getName();
        this.number = collegeDepartment.getNumber();
    }
}