package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"collegeDepartmentName", "collegeDepartmentNumber"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeDepartmentDto implements Serializable {
    private String collegeDepartmentName;
    private String collegeDepartmentNumber;

    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDepartmentDto(String collegeDepartmentName, String collegeDepartmentNumber) {
        this.collegeDepartmentName = collegeDepartmentName;
        this.collegeDepartmentNumber = collegeDepartmentNumber;
    }

    public static CollegeDepartmentDto of(String collegeDepartmentName, String collegeDepartmentNumber) {
        return CollegeDepartmentDto.builder()
                .collegeDepartmentName(collegeDepartmentName)
                .collegeDepartmentNumber(collegeDepartmentNumber)
                .build();
    }
}