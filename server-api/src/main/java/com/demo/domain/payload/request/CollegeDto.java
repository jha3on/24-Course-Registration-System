package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"collegeName", "collegeNumber"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeDto implements Serializable {
    private String collegeName;
    private String collegeNumber;

    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDto(String collegeName, String collegeNumber) {
        this.collegeName = collegeName;
        this.collegeNumber = collegeNumber;
    }

    public static CollegeDto of(String collegeName, String collegeNumber) {
        return CollegeDto.builder()
                .collegeName(collegeName)
                .collegeNumber(collegeNumber)
                .build();
    }
}